package projects.displaynet.nodes.nodeImplementations;

import projects.displaynet.nodes.tableEntry.Request;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

/**
 * SplayNetNode
 */
public abstract class ControlLayer extends RotationLayer {

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

  public void newSplay(Request rq) {
    this.activeSplay = rq;
    this.activeSplay.initialTime = this.getCurrentRound();
  }

  @Override
  public void controlStep() {

    /*
     * Update current state in time slot 0
     */
    switch (this.state) {

      case PASSIVE:
        if (this.activeSplay != null) {

          // event
          this.newSplayStarted(this.activeSplay);

          if (this.isNeighbor(this.activeSplay.getTargetNode())) {

            this.state = States.ACTIVE;
            // request communication cluster from cluster layer

          } else {
            this.state = States.ACTIVE;
            this.setRequest(this.activeSplay.getSrcId(), this.activeSplay.getDstId(),
                this.activeSplay.getPriority());
            if (!this.isLeastCommonAncestorOf(this.activeSplay.getTargetNode())) {
              this.tryRotation();
            }
          }
        }
        break;

      case ACTIVE:
        if (this.isNeighbor(this.activeSplay.getTargetNode())) {

          this.state = States.ACTIVE;
          // request communication cluster from cluster layer

        } else if (!this.isLeastCommonAncestorOf(this.activeSplay.getTargetNode())) {
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

  public void newSplayStarted(Request currentRequest) {

  }

  public void communicationClusterFormed(Request currentRequest) {

  }

  public void communicationCompleted(Request peerRequest) {
    this.activeSplay.finalTime = this.getCurrentRound();
  }

}