package projects.displaynet.nodes.nodeImplementations;

import projects.displaynet.nodes.tableEntry.Request;
import sinalgo.tools.Tools;

/**
 * SplayNetNode
 */
public abstract class ControlLayer extends RotationLayer {

  private enum States {
    PASSIVE, ACTIVE
  }

  // keep current state of the node
  private States state;

  // keeps track of every splay a node participate as source or destination. once
  // a splay is completed, it is assigned null value
  private boolean newSplay;
  private Request activeSplay;

  @Override
  public void init() {
    super.init();

    this.state = States.PASSIVE;
    this.newSplay = false;
    this.activeSplay = null;
  }

  protected Request getActiveSplay() {
    return activeSplay;
  }

  public void startNewSplay(Request rq) {
    this.newSplay = true;
    this.activeSplay = rq;
    this.activeSplay.initialTime = this.getCurrentRound();
  }

  public void resetSplay() {
    this.newSplay = false;
    this.activeSplay = null;
    this.state = States.PASSIVE;
  }

  @Override
  public void controlStep() {

    /*
     * Update current state in time slot 0
     */
    switch (this.state) {

      case PASSIVE:
        if (this.newSplay) {

          // event
          this.newSplayStarted(this.activeSplay);

          this.state = States.ACTIVE;
          this.setRequest(this.activeSplay.getSrcId(), this.activeSplay.getDstId(),
              this.activeSplay.getPriority(), this.activeSplay.isMaster());
        } else {
          break;
        }

      case ACTIVE:

        if (this.isNeighbor(this.activeSplay.getDstId())) {

          // request communication cluster from cluster layer
          if (this.activeSplay.isMaster()){
            this.sendCommunicationRequestCluster();
          }

        } else if (!this.isAncestorOf(this.activeSplay.getDstId())) {
          System.out.println("is neighbor node" + ID);
          this.tryRotation();
        }

        break;

      default:
        Tools.fatalError("Non-existing splay state");
        break;
    }
  }

  @Override
  public void zigCompleted() { // single rotation
    this.activeSplay.numOfRotations++;
  }

  @Override
  public void rotationCompleted() { // double rotation
    this.activeSplay.numOfRotations++;
    this.activeSplay.numOfRotations++;
  }

  @Override
  public void communicationClusterCompleted() {
    this.resetSplay();
    this.connectionEstablished();
  }

  public abstract void newSplayStarted(Request currentRequest);

//  this.activeSplay.finalTime = this.getCurrentRound();
  public abstract void connectionEstablished();

}