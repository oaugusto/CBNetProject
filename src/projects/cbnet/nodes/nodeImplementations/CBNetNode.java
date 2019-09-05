package projects.cbnet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.LinkedList;
import java.util.Queue;

import projects.cbnet.nodes.messages.CompletionMessage;
import projects.cbnet.nodes.tableEntry.Request;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

/**
 * CBNetNode
 */
public class CBNetNode extends RotationLayer {

    private boolean newMessage;
    private Request activeRequest;

    private Queue<Request> bufferRequest;

    private enum States {
        PASSIVE, COMMUNICATING
    }

    private States state;

    private boolean first = true;

    @Override
    public void init() {
        super.init();

        this.newMessage = false;
        this.activeRequest = null;
        this.bufferRequest = new LinkedList<>();
    }

    @Override
    public void updateState() {

        switch (this.state) {
        case PASSIVE:

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

            return;
        }
    }

    public void newMessage(int src_splay, int dst_splay, double priority) {
        Request splay = new Request(src_splay, dst_splay, priority);
        this.activeRequest = splay;
        this.newMessage = true;
    }

    public void communicationCompleted() {
        // System.out.println("Communication Completed node " + ID);
    }

    @Override
    public void nodeStep() {

    }

    public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
        // String text = this.getWeight() + "";
        String text = "" + ID;

        // draw the node as a circle with the text inside
        super.drawNodeAsDiskWithText(g, pt, highlight, text, 12, Color.YELLOW);
    }

}