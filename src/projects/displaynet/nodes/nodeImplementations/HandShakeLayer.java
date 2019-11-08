package projects.displaynet.nodes.nodeImplementations;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import projects.defaultProject.nodes.messages.ApplicationMessage;
import projects.displaynet.nodes.messages.handshakeMessages.AckSplay;
import projects.displaynet.nodes.messages.handshakeMessages.RequestSplay;
import projects.displaynet.nodes.messages.handshakeMessages.StartSplay;
import projects.displaynet.nodes.tableEntry.Request;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

import projects.defaultProject.DataCollection;

/**
 * HandShakeNode
 */
public abstract class HandShakeLayer extends CommunicationLayer {

  private DataCollection data = DataCollection.getInstance();

  // Otavio -----------------------------------------------------------

  private enum HandShakeState {
    IDLE, SYNC, MASTER, SLAVE, START, SPLAY
  }

  private HandShakeState handShakeState;

  // to break ties in priority
  private Random rand = Tools.getRandomNumberGenerator();

  // current splay operation
  private Request currentSplayHandShake; // splay operation from myself or peer

  // my own message to be sent
  private ApplicationMessage myMsg;

  // in case received ack from peer
  private boolean isAckMSGReceived;

  // master peer has responded with startmsg
  private boolean isStartMSGReceived;

  // current splay operation completed
  private boolean isSplayCompleted;

  // store communication msg from this node.
  protected Queue<ApplicationMessage> myMsgBuffer = new LinkedList<>();

  // store communication request from other nodes.
  private PriorityQueue<Request> peersRequestBuffer = new PriorityQueue<>();

  @Override
  public void init() {
    super.init();

    this.handShakeState = HandShakeState.IDLE;

    this.currentSplayHandShake = null;
    this.myMsg = null;
    this.isAckMSGReceived = false;
    this.isStartMSGReceived = false;
    this.isSplayCompleted = false;
    this.myMsgBuffer.clear();
    this.peersRequestBuffer.clear();
  }

  @Override
  public void receiveMessage(Message msg) {
    super.receiveMessage(msg);

    if (msg instanceof RequestSplay) {

      RequestSplay aux = (RequestSplay) msg;
      // System.out.println("node:" + ID + " " +aux.src + " " + aux.dst);
      this.peersRequestBuffer
          .add(new Request(aux.getSource(), aux.getDestination(), aux.getPriority(), false));

    } else if (msg instanceof AckSplay) {
      // if the msg is instance of ack, my request is ready to start
      this.isAckMSGReceived = true;

    } else if (msg instanceof StartSplay) {
      // start splay once the peer has started
      this.isStartMSGReceived = true;
    }
  }

  // TODO
  @Override
  public void communicationCompleted(Request request) {
    super.communicationCompleted(request);
    this.isSplayCompleted = true;
  }

  @Override
  public void handShakeStep() {

    switch (this.handShakeState) {
      case IDLE:
        // if the node has new msg than set priority and send to peer
        // handshake_log.logln(ID +": State:" + state_handshake + " ");
        if (this.myMsg == null && !this.myMsgBuffer.isEmpty()) {
          this.myMsg = this.myMsgBuffer.poll();
          this.myMsg.setPriority(Global.currentTime + rand.nextDouble());

          RequestSplay msg = new RequestSplay(this.myMsg.getSource(), this.myMsg.getDestination(),
              this.myMsg.getPriority());

          this.sendDirect(msg, Tools.getNodeByID(this.myMsg.getDestination()));
          // handshake_log
          // .logln("[" + Global.currentTime + "] " + ID + ": State:" + state_handshake +
          // " Request sent");
        }

        if ((this.myMsg != null) || !this.peersRequestBuffer.isEmpty()) {
          this.handShakeState = HandShakeState.SYNC;
          // handshake_log
          // .logln("[" + Global.currentTime + "] " + ID + ": State:" + state_handshake +
          // " is next state");
        }

        break;

      // set current splay operation
      case SYNC:
        if (this.peersRequestBuffer.isEmpty()) {
          // handshake_log.logln(
          // "[" + Global.currentTime + "] " + ID + ": State:" + state_handshake + "
          // peerRequest is empty");
          // current node become master
          this.currentSplayHandShake = new Request(this.myMsg.getSource(),
              this.myMsg.getDestination(), this.myMsg.getPriority(), true);
          this.handShakeState = HandShakeState.MASTER;
          // reset myMsg
          this.setNewApplicationMessage(this.myMsg);
          this.myMsg = null;

        } else if (this.myMsg == null) {
          // handshake_log
          // .logln("[" + Global.currentTime + "] " + ID + ": State:" + state_handshake +
          // " mymsg is null");
          // respond with ack and the node become slave
          this.currentSplayHandShake = this.peersRequestBuffer.poll();
          AckSplay msg = new AckSplay();
          sendDirect(msg, Tools.getNodeByID(this.currentSplayHandShake.getSrcId()));

          this.handShakeState = HandShakeState.SLAVE;

        } else {
          Request req = this.peersRequestBuffer.peek();

          if (req.getPriority() > this.myMsg.getPriority()) {
            // handshake_log.logln("[" + Global.currentTime + "] " + ID + ": State:" +
            // state_handshake
            // + " mymsg has higher priority");
            this.currentSplayHandShake = new Request(this.myMsg.getSource(),
                this.myMsg.getDestination(), this.myMsg.getPriority(), true);
            this.handShakeState = HandShakeState.MASTER;
            // reset myMsg
            this.myMsg = null;
          } else {
            // handshake_log.logln("[" + Global.currentTime + "] " + ID + ": State:" +
            // state_handshake
            // + " peermsg has higher priority");
            // respond with ack
            this.currentSplayHandShake = this.peersRequestBuffer.poll();
            if (this.currentSplayHandShake == null) { // test only
              Tools.fatalError("currentSplayHandshake is null");
            }
            AckSplay msg = new AckSplay();
            sendDirect(msg, Tools.getNodeByID(this.currentSplayHandShake.getSrcId()));

            this.handShakeState = HandShakeState.SLAVE;
          }
        }

        // handshake_log.logln("[" + Global.currentTime + "] " + ID + ": State:" +
        // state_handshake + " is next state");
        break;

      case MASTER:
        // init splay sending start msg
        if (this.isAckMSGReceived) {
          // handshake_log
          // .logln("[" + Global.currentTime + "] " + ID + ": State:" + state_handshake +
          // " ack received");
          StartSplay msg = new StartSplay();
          sendDirect(msg, Tools.getNodeByID(this.currentSplayHandShake.getDstId()));

          this.handShakeState = HandShakeState.START;
          // reset ack flag
          this.isAckMSGReceived = false;

          //Log
          this.data.addSequence(this.currentSplayHandShake.getSrcId() - 1,
              this.currentSplayHandShake.getDstId() - 1);
        }
        // handshake_log.logln("[" + Global.currentTime + "] " + ID + ": State:" +
        // state_handshake + " is next state");
        break;

      case SLAVE:
        // wait the StartMsg
        if (this.isStartMSGReceived) {
          // handshake_log
          // .logln("[" + Global.currentTime + "] " + ID + ": State:" + state_handshake +
          // " start received");
          this.handShakeState = HandShakeState.START;
          // reset startmsg flag
          this.isStartMSGReceived = false;
          // go to splay state
        } else {
          // handshake_log
          // .logln("[" + Global.currentTime + "] " + ID + ": State:" + state_handshake +
          // " is next state");
          break;
        }
        // System.out.println("[" + Global.currentTime + "] " + ID + ": State:" +
        // state_handshake + " is next state");

      case START:
        // insert currentsplay_hands'hake operation on active_splay
        // handshake_log.logln("[" + Global.currentTime + "] " + ID + ": State:" +
        // state_handshake
        // + " generate next splay src:" + this.currentSplay_handshake.srcId + " dst"
        // + this.currentSplay_handshake.dstId + "priority: " +
        // this.currentSplay_handshake.priority);
        // TODO
        this.newSplay(this.currentSplayHandShake);

        this.handShakeState = HandShakeState.SPLAY;
        // handshake_log.logln("[" + Global.currentTime + "] " + ID + ": State:" +
        // state_handshake + " is next state");
        // System.out.println(ID + " Init splay:(" + this.currentSplay_handshake.srcId +
        // ", "
        // + this.currentSplay_handshake.dstId + ") time: " + Global.currentTime);

      case SPLAY:
        // splay is completed when the src and dst are neighbor of each other
        if (this.isSplayCompleted) {
          // handshake_log.logln(
          // "[" + Global.currentTime + "] " + ID + ": State:" + state_handshake + " splay
          // completec");
          // TODO
          // reset to first state
          this.currentSplayHandShake = null;
          this.handShakeState = HandShakeState.IDLE;
          // reset isSplayCompleted
          this.isSplayCompleted = false;
        }
        // handshake_log.logln("[" + Global.currentTime + "] " + ID + ": State:" +
        // state_handshake + " is next state");
        break;

      default:
        Tools.fatalError("Find default state in module handshake");
        break;
    }

  }
}