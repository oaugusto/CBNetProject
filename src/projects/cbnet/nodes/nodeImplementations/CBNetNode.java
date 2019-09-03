package projects.cbnet.nodes.nodeImplementations;

import java.util.LinkedList;
import java.util.Queue;

import projects.cbnet.nodes.messages.CompletionMessage;
import projects.displaynet.tableEntry.Request;
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
        PASSIVE,
        COMMUNICATING
    }

    private States state;

    @Override
    public void init() {
        super.init();

        this.newMessage = false;
        this.activeRequest = null;
        this.bufferRequest = new LinkedList<>();
    }

    @Override
    public void updateState() {
        super.updateState();

        switch (this.state) {
            case PASSIVE:
                
                break;

            case COMMUNICATING:
                break;
        
            default:
                Tools.fatalError("Invalid CBNetNode state");
                break;
        }

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
    
}