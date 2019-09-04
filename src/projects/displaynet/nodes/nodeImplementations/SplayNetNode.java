package projects.displaynet.nodes.nodeImplementations;

import projects.displaynet.nodes.tableEntry.Request;
import sinalgo.tools.Tools;

/**
 * SplayNetNode
 */
public class SplayNetNode extends RotationLayer {

    public enum States {
        PASSIVE, ACTIVE, COMMUNICATING
    }

    // keep current state of the node
    public States state;

    // keeps track of every splay a node participate as source or destination. once
    // a splay is completed, it is assigned null value
    public boolean newRequest;
    public Request activeSplay;


    boolean first = true;

    @Override
    public void init() {
        super.init();

        this.state = States.PASSIVE;
        this.newRequest = false;
        this.activeSplay = null;
    }

    @Override
    public void updateState() {
        super.updateState();

        /*
         * Update current state in time slot 0
         */
        switch (this.state) {

        case COMMUNICATING:
            this.state = States.PASSIVE;
            this.unblockRotations();
            this.clearClusterRequest();
            this.communicationCompleted();

        case PASSIVE:
            if (this.newRequest) {

                this.newRequest = false;

                if (this.checkCompletion() == true) {

                    this.blockRotations();
                    this.state = States.COMMUNICATING;
                    // communicate for one round

                } else if (!this.isLeastCommonAncestorOf(this.activeSplay.dstId)) {

                    this.state = States.ACTIVE;
                    this.setOperation(this.activeSplay.srcId, this.activeSplay.dstId, this.activeSplay.priority);
                    this.tryRotation();

                }
            }
            break;

        case ACTIVE:
            if (this.checkCompletion() == true) {

                this.blockRotations();
                this.state = States.COMMUNICATING;
                // communicate for one round

            } else if (!this.isLeastCommonAncestorOf(this.activeSplay.dstId)) {
                this.tryRotation();
            }

            break;

        default:
            Tools.fatalError("Non-existing splay state");
            break;
        }
    }

    public void newSplay(int src_splay, int dst_splay, double priority) {
        this.newRequest = true;
        Request splay = new Request(src_splay, dst_splay, priority);
        this.activeSplay = splay;
    }

    private boolean checkCompletion() {
        if (this.isNeighbor(this.activeSplay.dstId)) {
            return true;
        }
        return false;
    }

    private void blockRotations() {
        this.setOperation(Integer.MIN_VALUE, Integer.MIN_VALUE, Double.MIN_VALUE);
    }

    private void unblockRotations() {
        this.clearClusterRequest();
    }

    @Override
    public void rotationCompleted() {
        // collect data here
    }

    public void communicationCompleted() {
        System.out.println("Node " + ID + ": Comunication completed");
    }

    @Override
    public void nodeStep() {

    }

}