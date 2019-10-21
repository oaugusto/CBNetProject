package projects.optnet.nodes.nodeImplementations;

import java.util.LinkedList;
import java.util.Queue;

import projects.defaultProject.DataCollection;
import projects.displaynet.nodes.nodeImplementations.BinaryTreeLayer;
import projects.displaynet.nodes.tableEntry.Request;
import projects.optnet.nodes.messages.CompletionMessage;
import projects.optnet.nodes.messages.OptMessage;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

/*
 * CBNetLayer
 */
public class OptNode extends BinaryTreeLayer {

    // LOG
    DataCollection data = DataCollection.getInstance();

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
    public void postStep() {

        switch (this.state) {
        case PASSIVE:

            if (!this.bufferRequest.isEmpty()) {
                Request rq = this.bufferRequest.poll();
                OptMessage optMsg = new OptMessage(ID, rq.dstId);
                this.forwardMessage(optMsg);

                this.state = States.COMMUNICATING;
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
    public void handleMessages(Inbox inbox) {

        while (inbox.hasNext()) {
            Message msg = inbox.next();

            if (msg instanceof OptMessage) {
                OptMessage optMsg = (OptMessage) msg;

                if (optMsg.getDst() == ID) {
                    Node src = Tools.getNodeByID(optMsg.getSrc());
                    sendDirect(new CompletionMessage(), src);

                    this.data.addRotations(optMsg.getRotations());
                    this.data.addRouting(optMsg.getRouting());
                } else {
                    forwardMessage(optMsg);
                }

            } else if (msg instanceof CompletionMessage) {
                this.data.incrementCompletedRequests();
                this.state = States.PASSIVE;
            }

         }
    }

    private void forwardMessage(OptMessage msg) {
        msg.incrementRouting();

        if (ID < msg.getDst() && msg.getDst() <= this.getMaxIdInSubtree()) {
            sendToRightChild(msg);
        } else if (this.getMinIdInSubtree() <= msg.getDst() && msg.getDst() < ID) {
            sendToLeftChild(msg);
        } else {
            sendToParent(msg);
        }
    }


}