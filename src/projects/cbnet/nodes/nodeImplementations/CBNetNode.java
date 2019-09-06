package projects.cbnet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import projects.cbnet.DataCollection;
import projects.cbnet.nodes.messages.CBNetMessage;
import projects.cbnet.nodes.tableEntry.CBInfo;
import projects.cbnet.nodes.tableEntry.Request;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

/**
 * CBNetNode
 */
public class CBNetNode extends RotationLayer {

    // LOG
    private DataCollection data = DataCollection.getInstance();

    // to break ties in priority
    private Random rand = new Random();

    private Queue<Request> bufferRequest;

    private enum States {
        PASSIVE, COMMUNICATING
    }

    private States state;

    @Override
    public void init() {
        super.init();

        this.bufferRequest = new LinkedList<>();
        this.state = States.PASSIVE;
    }

    @Override
    public void updateState() {
        super.updateState();

        switch (this.state) {
        case PASSIVE:

            if (!this.bufferRequest.isEmpty()) {
                Request rq = this.bufferRequest.poll();
                this.sendCBNetMessage(rq.dstId, Global.currentTime + rand.nextDouble());

                this.state = States.COMMUNICATING;
                this.newMessageSent();
            }

            break;

        case COMMUNICATING:
            break;

        default:
            Tools.fatalError("Invalid CBNetNode state");
            break;
        }

    }

    public void newMessage(int dst) {
        Request splay = new Request(ID, dst, 0.0);
        this.bufferRequest.add(splay);
    }

    @Override
    public void ackCBNetMessageReceived() {
        this.state = States.PASSIVE;
        this.communicationCompleted();
    }

    public void communicationCompleted() {

    }

    // LOG --------------------------------------------------------------
    public void newMessageSent() {
        this.data.incrementActiveSplays();
    }

    @Override
    public void receivedCBNetMessage(CBNetMessage msg) {
        // System.out.println("Node " + ID + ": message received from " + msg.getSrc());
        this.data.incrementCompletedRequests();
        this.data.addRotations(msg.getRotations());
        this.data.addRouting(msg.getRouting());
        this.data.decrementActiveSplays();
    }

    @Override
    public void clusterCompletedBottomUp(HashMap<String, CBInfo> cluster) {
        super.clusterCompletedBottomUp(cluster);
        this.data.incrementActiveClusters();
    }

    @Override
    public void clusterCompletedTopDown(HashMap<String, CBInfo> cluster) {
        super.clusterCompletedTopDown(cluster);
        this.data.incrementActiveClusters();
    }

    @Override
    public void targetNodeFound(CBInfo target) {
        super.targetNodeFound(target);
        this.data.incrementActiveClusters();
    }

    @Override
    public void round() {
        super.round();
        if (ID == 1) {
            this.data.addNumOfActiveSplays();
            this.data.addNumOfActiveClusters();
            this.data.resetActiveClusters();
        }
    }

    // GUI --------------------------------------------------------------

    public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
        // String text = this.getWeight() + "";
        String text = "" + ID;

        // draw the node as a circle with the text inside
        super.drawNodeAsDiskWithText(g, pt, highlight, text, 12, Color.YELLOW);
    }

}