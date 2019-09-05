package projects.cbnet.nodes.nodeImplementations;

import java.util.LinkedList;
import java.util.Queue;

import projects.cbnet.nodes.messages.CompletionMessage;
import projects.cbnet.nodes.tableEntry.Request;
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

        if (first) {
            first = false;

            if (ID == 2) {
                this.sendCBNetMessage(26, 1.0);
            }

            // if (ID == 5) {
            // this.sendRequestClusterDown(5, 6, 2.0);
            // }

            // if (ID == 11) {
            // this.sendRequestClusterUp(11, 30, 1.0);
            // }
        }

        super.updateState();

        // switch (this.state) {
        // case PASSIVE:

        // break;

        // case COMMUNICATING:
        // break;

        // default:
        // Tools.fatalError("Invalid CBNetNode state");
        // break;
        // }

    }

    // @Override
    // public void receiveMessage(Message msg) {
    // super.receiveMessage(msg);

    // if (msg instanceof CompletionMessage) {

    // return;
    // }
    // }

    // public void newMessage(int src_splay, int dst_splay, double priority) {
    // Request splay = new Request(src_splay, dst_splay, priority);
    // this.activeRequest = splay;
    // this.newMessage = true;
    // }

    // public void communicationCompleted() {
    // // System.out.println("Communication Completed node " + ID);
    // }

    @Override
    public void nodeStep() {

    }

}