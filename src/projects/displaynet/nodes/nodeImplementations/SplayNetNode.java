package projects.displaynet.nodes.nodeImplementations;

import projects.displaynet.nodes.messages.CompletionMessage;
import projects.displaynet.nodes.tableEntry.Request;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

/**
 * SplayNetNode
 */
public abstract class SplayNetNode extends RotationLayer {

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

    public void newSplay(int src, int dst, double priority) {
        this.activeSplay = new Request(src, dst, priority);
    }

    @Override
    public void updateState() {

        /*
         * Update current state in time slot 0
         */
        switch (this.state) {

        case COMMUNICATING:
            this.state = States.PASSIVE;
            this.unblockRotations();
            this.clearClusterRequest();
            this.activeSplay = null;

        case PASSIVE:
            if (this.activeSplay != null) {

                if (this.checkCompletion() == true) {

                    this.blockRotations();
                    this.state = States.COMMUNICATING;
                    this.sendCompletionMessage(this.activeSplay.dstId, this.activeSplay);

                    // event
                    this.communicationClusterFormed(this.activeSplay);

                } else if (!this.isLeastCommonAncestorOf(this.activeSplay.dstId)) {

                    this.state = States.ACTIVE;
                    this.setOperation(this.activeSplay.srcId, this.activeSplay.dstId, this.activeSplay.priority);
                    this.tryRotation();

                    // event
                    this.newSplayStarted(this.activeSplay);
                }
            }
            break;

        case ACTIVE:
            if (this.checkCompletion() == true) {

                this.blockRotations();
                this.state = States.COMMUNICATING;
                this.sendCompletionMessage(this.activeSplay.dstId, this.activeSplay);

                // event
                this.communicationClusterFormed(this.activeSplay);

            } else if (!this.isLeastCommonAncestorOf(this.activeSplay.dstId)) {
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
        super.rotationCompleted();
        this.activeSplay.numOfRotations++;
    }

    public void newSplayStarted(Request request) {

    }

    public void communicationClusterFormed(Request request) {

    }

    public void communicationCompleted(Request request) {

    }


   
}