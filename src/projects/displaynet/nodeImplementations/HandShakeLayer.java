package projects.displaynet.nodeImplementations;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

import projects.displaynet.messages.handshakeMessages.AckSplay;
import projects.displaynet.messages.handshakeMessages.RequestSplay;
import projects.displaynet.messages.handshakeMessages.StartSplay;
import projects.displaynet.tableEntry.Request;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

/**
 * HandShakeNode
 */
public class HandShakeLayer extends SplayNetNode {

    // Otavio -----------------------------------------------------------

    private enum State_handshake {
        IDLE, SYNC, MASTER, SLAVE, START, SPLAY
    }

    private State_handshake state_handshake;

    // to break ties in priority
    private Random rand = new Random();

    // current splay operation
    public Request currentSplay_handshake; // splay operation from myself or peer

    // my own message to be sent
    public Request myMsg;

    // in case received ack from peer
    private boolean isAckMSGReceived;

    // master peer has responded with startmsg
    private boolean isStartMSGReceived;

    // current splay operation completed
    private boolean isSplayCompleted;

    // store communication request from this node.
    public Queue<Request> myMsgBuffer = new LinkedList<Request>();

    // store communication request from other nodes.
    public PriorityQueue<Request> peersRequestBuffer = new PriorityQueue<Request>();

    @Override
    public void init() {
        super.init();

        this.state_handshake = State_handshake.IDLE;

        this.currentSplay_handshake = null;
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
            this.peersRequestBuffer.add(new Request(aux.dst, aux.src, aux.priority));

        } else if (msg instanceof AckSplay) {
            // if the msg is instance of ack, my request is ready to start
            this.isAckMSGReceived = true;

        } else if (msg instanceof StartSplay) {
            // start splay once the peer has started
            this.isStartMSGReceived = true;
        }
    }

    // @Override
    // public void communicationCompleted() {
    //     this.isSplayCompleted = true;
    //     this.splayCompleted();
    // }

    public void splayCompleted() {

    }

    @Override
    public void nodeStep() {
        super.nodeStep();

        switch (this.state_handshake) {
        case IDLE:
            // if the node has new msg than set priority and send to peer
            // handshake_log.logln(ID +": State:" + state_handshake + " ");
            if (this.myMsg == null && !this.myMsgBuffer.isEmpty()) {
                this.myMsg = this.myMsgBuffer.poll();
                this.myMsg.priority = Global.currentTime + rand.nextDouble();

                RequestSplay msg = new RequestSplay(this.myMsg.srcId, this.myMsg.dstId, this.myMsg.priority);

                this.sendDirect(msg, Tools.getNodeByID(this.myMsg.dstId));
                // handshake_log
                // .logln("[" + Global.currentTime + "] " + ID + ": State:" + state_handshake +
                // " Request sent");
            }

            if ((this.myMsg != null) || !this.peersRequestBuffer.isEmpty()) {
                this.state_handshake = State_handshake.SYNC;
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
                this.currentSplay_handshake = this.myMsg;
                this.state_handshake = State_handshake.MASTER;
                // reset myMsg
                this.myMsg = null;

            } else if (this.myMsg == null) {
                // handshake_log
                // .logln("[" + Global.currentTime + "] " + ID + ": State:" + state_handshake +
                // " mymsg is null");
                // respond with ack and the node become slave
                this.currentSplay_handshake = this.peersRequestBuffer.poll();
                AckSplay msg = new AckSplay();
                sendDirect(msg, Tools.getNodeByID(this.currentSplay_handshake.dstId));

                this.state_handshake = State_handshake.SLAVE;

            } else {
                Request req = this.peersRequestBuffer.peek();

                if (req.priority > this.myMsg.priority) {
                    // handshake_log.logln("[" + Global.currentTime + "] " + ID + ": State:" +
                    // state_handshake
                    // + " mymsg has higher priority");
                    this.currentSplay_handshake = this.myMsg;
                    this.state_handshake = State_handshake.MASTER;
                    // reset myMsg
                    this.myMsg = null;
                } else {
                    // handshake_log.logln("[" + Global.currentTime + "] " + ID + ": State:" +
                    // state_handshake
                    // + " peermsg has higher priority");
                    // respond with ack
                    this.currentSplay_handshake = this.peersRequestBuffer.poll();
                    AckSplay msg = new AckSplay();
                    sendDirect(msg, Tools.getNodeByID(this.currentSplay_handshake.dstId));

                    this.state_handshake = State_handshake.SLAVE;
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
                sendDirect(msg, Tools.getNodeByID(this.currentSplay_handshake.dstId));

                this.state_handshake = State_handshake.START;
                // reset ack flag
                this.isAckMSGReceived = false;
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
                this.state_handshake = State_handshake.START;
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
            // this.newSplay(this.currentSplay_handshake.srcId, this.currentSplay_handshake.dstId,
            //         this.currentSplay_handshake.priority);

            this.state_handshake = State_handshake.SPLAY;
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
                // CustomGlobal.completedSplay++;
                // reset to first state
                this.currentSplay_handshake = null;
                this.state_handshake = State_handshake.IDLE;
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