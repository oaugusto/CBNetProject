package projects.cbnet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import projects.cbnet.nodes.messages.CBNetMessage;
import projects.cbnet.nodes.messages.CompletionMessage;
import projects.cbnet.nodes.tableEntry.Request;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.messages.Message;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

/**
 * CBNetNode
 */
public class CBNetNode extends RotationLayer {
    
    // to break ties in priority
    private Random rand = new Random();

    private Queue<Request> bufferRequest;

    private enum States {
        PASSIVE, COMMUNICATING
    }

    private States state;

    private boolean first = true;

    @Override
    public void init() {
        super.init();
        
        this.bufferRequest = new LinkedList<>();
        this.state = States.PASSIVE;
    }

    @Override
    public void updateState() {

        // if (first) {
        //     first = false;

        //     if (ID == 1) {
        //         this.newMessage(30);
        //     }

        //     if (ID == 18) {
        //         this.newMessage(2);
        //     }

        //     if (ID == 18) {
        //         this.newMessage(30);
        //     }
        // }

        switch (this.state) {
        case PASSIVE:

            if (!this.bufferRequest.isEmpty()) {
                Request rq = this.bufferRequest.poll();
                this.sendCBNetMessage(rq.dstId, Global.currentTime + rand.nextDouble());

                this.state = States.COMMUNICATING;
            }

            break;

        case COMMUNICATING:
            break;

        default:
            Tools.fatalError("Invalid CBNetNode state");
            break;
        }

        super.updateState(); // TODO : change the updateState()
    }

    @Override
    public void receiveMessage(Message msg) {
        super.receiveMessage(msg);

        if (msg instanceof CompletionMessage) {
            this.state = States.PASSIVE;
            return;
        }
    }

    public void newMessage(int dst) {
        Request splay = new Request(ID, dst, 0.0);
        this.bufferRequest.add(splay);
    }

    @Override
    public void receivedCBNetMessage(CBNetMessage msg) {
        System.out.println("Node " + ID + ": message received from " + msg.getSrc());
        this.sendDirect(new CompletionMessage(), Tools.getNodeByID(msg.getSrc()));
    }

    public void communicationCompleted() {
        // System.out.println("Communication Completed node " + ID);
    }

    @Override
    public void nodeStep() {
        // useless
    }

    public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
        String text = this.getWeight() + "";
        // String text = "" + ID;

        // draw the node as a circle with the text inside
        super.drawNodeAsDiskWithText(g, pt, highlight, text, 12, Color.YELLOW);
    }

}