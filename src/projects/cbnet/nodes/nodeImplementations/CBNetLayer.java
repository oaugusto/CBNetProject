package projects.cbnet.nodes.nodeImplementations;

import java.util.PriorityQueue;

import projects.cbnet.nodes.messages.CBNetMessage;
import projects.cbnet.nodes.messages.CompletionMessage;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

/**
 * CBNetLayer
 */
public abstract class CBNetLayer extends RPCLayer {

    private PriorityQueue<CBNetMessage> cbnetQueue;

    @Override
    public void init() {
        super.init();

        this.cbnetQueue = new PriorityQueue<>();
    }

    public boolean hasCBNetMessage() {
        return !this.cbnetQueue.isEmpty();
    }

    public CBNetMessage getTopCBNetMessage() {
        return this.cbnetQueue.peek();
    }

    public void removeTopCBNetMesssage() {
        this.cbnetQueue.poll();
    }

    public void sendCBNetMessage(int dst, double priority) {
        CBNetMessage msg = new CBNetMessage(ID, dst, priority);
        msg.initialTime = this.getCurrentRound();
        this.cbnetQueue.add(msg);
    }

    public void forwardCBNetMessage(int dst, CBNetMessage msg) {
        this.sendForwardMessage(dst, msg);
    }

    @Override
    public void receiveMessage(Message msg) {
        super.receiveMessage(msg);

        if (msg instanceof CBNetMessage) {
            CBNetMessage cbmsg = (CBNetMessage) msg;

            if (ID == cbmsg.getDst()) {
                this.receivedCBNetMessage(cbmsg);
//                this.updateWeights(ID, cbmsg.getSrc()); // dangerous

                // ack message
                this.sendDirect(new CompletionMessage(cbmsg), Tools.getNodeByID(cbmsg.getSrc()));

            } else {
                this.cbnetQueue.add(cbmsg);
            }

            return;
        } else if (msg instanceof CompletionMessage) {
            CompletionMessage completionMessage = (CompletionMessage) msg;
            this.ackCBNetMessageReceived(completionMessage.getCbnetMessage());

            return;
        }
    }

    public abstract void receivedCBNetMessage(CBNetMessage msg);

    
    public abstract void ackCBNetMessageReceived(CBNetMessage msg);
    
}