package projects.semisplaynet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import projects.cbnet.nodes.messages.CBNetMessage;
import projects.cbnet.nodes.tableEntry.Request;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

/**
 * CBNetNode
 */
public class CBNetNode extends RotationLayer {

    // to break ties in priority
    private Random rand = Tools.getRandomNumberGenerator();

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
        Request splay = new Request(ID, dst);
        this.bufferRequest.add(splay);
    }

    @Override
    public void ackCBNetMessageReceived(CBNetMessage msg) {
        this.state = States.PASSIVE;
        msg.finalTime = this.getCurrentRound();
        this.communicationCompleted(msg);
    }

    public void newMessageSent() {
        
    }

    public void communicationCompleted(CBNetMessage msg) {

    }

    @Override
    public void receivedCBNetMessage(CBNetMessage msg) {
        // System.out.println("Node " + ID + ": message received from " + msg.getSrc());
    }

    // GUI --------------------------------------------------------------

    public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
        // String text = this.getWeight() + "";
        String text = "" + ID;

        // draw the node as a circle with the text inside
        super.drawNodeAsDiskWithText(g, pt, highlight, text, 12, Color.YELLOW);
    }

}