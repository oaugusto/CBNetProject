package projects.flatnet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import projects.flatnet.nodes.nodeImplementations.RotationLayer;
import projects.flatnet.nodes.messages.FlatNetMessage;
import projects.flatnet.nodes.tableEntry.Request;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

/**
 * FlatNetNode
 */
 public class FlatNetNode extends RotationLayer {
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
                // System.out.println("Node (" + ID + "): has sent new flatnet message");
                Request rq = this.bufferRequest.poll();
                this.sendFlatNetMessage(rq.dstId, Global.currentTime + rand.nextDouble());
                this.state = States.COMMUNICATING;
                this.newMessageSent();
            }

            break;

        case COMMUNICATING:
            break;

        default:
            Tools.fatalError("Invalid FlatNetNode state");
            break;
        }

    }

    public void newMessage(int dst) {
        Request splay = new Request(ID, dst);
        this.bufferRequest.add(splay);
    }

    @Override
    public void ackFlatNetMessageReceived(FlatNetMessage msg) {
        this.state = States.PASSIVE;
        msg.finalTime = this.getCurrentRound();
        this.communicationCompleted(msg);
    }

    public void newMessageSent() {
        
    }

    public void communicationCompleted(FlatNetMessage msg) {

    }

    @Override
    public void receivedFlatNetMessage(FlatNetMessage msg) {
        // System.out.println("Node " + ID + ": message received from " + msg.getSrc());
    }

    // GUI --------------------------------------------------------------

    public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
        // String text = this.getWeight() + "";
        String text = "" + ID;

        // draw the node as a circle with the text inside
        super.drawNodeAsDiskWithText(g, pt, highlight, text, 12, Color.YELLOW);
    }
};