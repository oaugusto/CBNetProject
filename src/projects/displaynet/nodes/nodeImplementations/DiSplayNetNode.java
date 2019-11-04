package projects.displaynet.nodes.nodeImplementations;

import projects.displaynet.nodes.messages.CompletionMessage;
import projects.displaynet.nodes.tableEntry.Request;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

/**
 * SplayNetNode
 */
public abstract class DiSplayNetNode extends RotationLayer {

  private enum States {
    PASSIVE, ACTIVE, COMMUNICATING
  }

  // keep current state of the node
  private States state;

  // keeps track of every splay a node participate as source or destination. once
  // a splay is completed, it is assigned null value
  private Request activeSplay;

  @Override
  public void init() {
    super.init();

    this.state = States.PASSIVE;
    this.activeSplay = null;
  }

  protected Request getActiveSplay() {
    return activeSplay;
  }

  public void newSplay(int src, int dst, double priority) {
    this.activeSplay = new Request(src, dst, priority);
    this.activeSplay.initialTime = this.getCurrentRound();
  }

  @Override
  public void updateState() {

    /*
     * Update current state in time slot 0
     */
    switch (this.state) {

      case COMMUNICATING:
        this.state = States.PASSIVE;
        this.unlockRotations();
        this.clearClusterRequest();
        this.activeSplay = null;

      case PASSIVE:
        if (this.activeSplay != null) {

          // event
          this.newSplayStarted(this.activeSplay);

          if (this.checkCompletion() == true) {

            this.lockRotations();
            this.state = States.COMMUNICATING;
            this.sendCompletionMessage(this.activeSplay.getDstId(), this.activeSplay);

            // event
            this.communicationClusterFormed(this.activeSplay);

          } else {
            this.state = States.ACTIVE;
            this.setOperation(this.activeSplay.getSrcId(), this.activeSplay.getDstId(),
                this.activeSplay.priority);
            if (!this.isLeastCommonAncestorOf(this.activeSplay.getDstId())) {
              this.tryRotation();
            }
          }
        }
        break;

      case ACTIVE:
        if (this.checkCompletion() == true) {

          this.lockRotations();
          this.state = States.COMMUNICATING;
          this.sendCompletionMessage(this.activeSplay.getDstId(), this.activeSplay);

          // event
          this.communicationClusterFormed(this.activeSplay);

        } else if (!this.isLeastCommonAncestorOf(this.activeSplay.getDstId())) {
          this.tryRotation();
        }

        break;

      default:
        Tools.fatalError("Non-existing splay state");
        break;
    }
  }

  public void sendCompletionMessage(int dst, Request rq) {
    CompletionMessage cmpMessage = new CompletionMessage(rq);
    this.sendForwardMessage(dst, cmpMessage);
  }

  @Override
  public void receiveMessage(Message msg) {
    super.receiveMessage(msg);

    if (msg instanceof CompletionMessage) {
      CompletionMessage cmpMessage = (CompletionMessage) msg;

      this.communicationCompleted(cmpMessage.getRequest());

      return;
    }
  }

  private boolean checkCompletion() {
    if (this.isNeighbor(this.activeSplay.getDstId())) {
      return true;
    }
    return false;
  }

  private void lockRotations() {
    this.setOperation(Integer.MIN_VALUE, Integer.MIN_VALUE, Double.MIN_VALUE);
  }

  private void unlockRotations() {
    this.clearClusterRequest();
  }

  @Override
  public void zigCompleted() { // single rotation
    super.zigCompleted();
    this.activeSplay.numOfRotations++;
  }

  @Override
  public void rotationCompleted() { // double rotation
    super.rotationCompleted();
    this.activeSplay.numOfRotations++;
    this.activeSplay.numOfRotations++;
  }

  public void newSplayStarted(Request currentRequest) {

  }

  public void communicationClusterFormed(Request currentRequest) {

  }

  public void communicationCompleted(Request peerRequest) {
    this.activeSplay.finalTime = this.getCurrentRound();
  }

}