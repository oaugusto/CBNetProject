package projects.cbnet.nodes.nodeImplementations;

import java.util.PriorityQueue;

import projects.cbnet.nodes.messages.CBNetMessage;
import sinalgo.nodes.messages.Message;

/**
 * CBNetLayer
 */
public class CBNetLayer extends RPCLayer {

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
                this.updateWeights(ID, cbmsg.getSrc());
            } else {
                this.cbnetQueue.add(cbmsg);
            }

            return;
        }
    }

    public void receivedCBNetMessage(CBNetMessage msg) {
        System.out.println("Node " + ID + ": Message arrived from " + msg.getSrc());
    }

    @Override
    public void nodeStep() {
        // TODO Auto-generated method stub

    }

    
}