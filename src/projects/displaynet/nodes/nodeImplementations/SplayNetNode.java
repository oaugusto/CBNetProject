package projects.displaynet.nodes.nodeImplementations;

import java.util.HashMap;

import projects.displaynet.DataCollection;
import projects.displaynet.nodes.messages.CompletionMessage;
import projects.displaynet.nodes.tableEntry.NodeInfo;
import projects.displaynet.nodes.tableEntry.Request;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

/**
 * SplayNetNode
 */
public abstract class SplayNetNode extends RotationLayer {

    // LOG
    private DataCollection data = DataCollection.getInstance();

    public enum States {
        PASSIVE, ACTIVE, COMMUNICATING
    }

    // keep current state of the node
    public States state;

    // keeps track of every splay a node participate as source or destination. once
    // a splay is completed, it is assigned null value
    public boolean newRequest;
    public Request activeSplay;


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
            this.activeSplay = null;

        case PASSIVE:
            if (this.newRequest) {

                this.newRequest = false;

                if (this.checkCompletion() == true) {

                    this.blockRotations();
                    this.state = States.COMMUNICATING;
                    // communicate for one round
                    this.data.incrementActiveClusters();
                    CompletionMessage cmpMessage = new CompletionMessage(this.activeSplay);
                    this.sendForwardMessage(this.activeSplay.dstId, cmpMessage);

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
                this.data.incrementActiveClusters();
                CompletionMessage cmpMessage = new CompletionMessage(this.activeSplay);
                this.sendForwardMessage(this.activeSplay.dstId, cmpMessage);

            } else if (!this.isLeastCommonAncestorOf(this.activeSplay.dstId)) {
                this.tryRotation();
            }

            break;

        default:
            Tools.fatalError("Non-existing splay state");
            break;
        }
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

    public void newSplay(int src_splay, int dst_splay, double priority) {
        this.newRequest = true;
        Request splay = new Request(src_splay, dst_splay, priority);
        this.activeSplay = splay;

        // LOG
        this.data.incrementActiveSplays();
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


    // LOG ------------------------------------------------------

    @Override
    public void clusterCompleted(HashMap<String, NodeInfo> cluster) {
        super.clusterCompleted(cluster);
        this.data.incrementActiveClusters();
    }

    @Override
    public void rotationCompleted() {
        this.activeSplay.numOfRotations++;
    }

    public void communicationCompleted(Request request) {
        // System.out.println("Node " + ID + ": Comunication completed");
        
        if (ID <request.srcId) {
            this.data.decrementActiveSplays();
            this.data.addRotations(request.numOfRotations + this.activeSplay.numOfRotations);
            this.data.addRouting(1);
        }

    }

    @Override
    public void posRound() {
        if (ID == 1) {
            this.data.addNumOfActiveSplays();
            this.data.addNumOfActiveClusters();
            this.data.resetActiveClusters();
        }
    }

   
}