package projects.flatnet.nodes.nodeImplementations;

import java.util.PriorityQueue;

import projects.flatnet.nodes.messages.FlatNetMessage;
import projects.flatnet.nodes.messages.CompletionMessage;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

public abstract class FlatNetLayer extends RPCLayer {

    private PriorityQueue<FlatNetMessage> flatNetQueue;

    @Override
    public void init() {
        super.init();

        this.flatNetQueue = new PriorityQueue<>();
    }

    public boolean hasFlatNetMessage() {
        return !this.flatNetQueue.isEmpty();
    }

    public FlatNetMessage getTopFlatNetMessage() {
        return this.flatNetQueue.peek();
    }

    public void removeTopFlatNetMesssage() {
        this.flatNetQueue.poll();
    }

    public void sendFlatNetMessage(int dst, double priority) {
        FlatNetMessage msg = new FlatNetMessage(ID, dst, priority);
        msg.initialTime = this.getCurrentRound();
        this.flatNetQueue.add(msg);
    }

    public void forwardFlatNetMessage(int dst, FlatNetMessage msg) {
        this.sendForwardMessage(dst, msg);
    }

    @Override
    public void receiveMessage(Message msg) {
        super.receiveMessage(msg);

        if (msg instanceof FlatNetMessage) {
            // System.out.println("Node (" + ID + "): received flat net message");

            FlatNetMessage flatmsg = (FlatNetMessage) msg;

            if (ID == flatmsg.getDst()) {
                // System.out.println("Node (" + ID + "): received flat net message intend for itself");
                this.receivedFlatNetMessage(flatmsg);

                // ack message
                this.sendDirect(new CompletionMessage(flatmsg), Tools.getNodeByID(flatmsg.getSrc()));

            } else {
                this.flatNetQueue.add(flatmsg);
            }

            return;
        } else if (msg instanceof CompletionMessage) {
            // System.out.println("Node (" + ID + "): received completion message");

            CompletionMessage completionMessage = (CompletionMessage) msg;
            this.ackFlatNetMessageReceived(completionMessage.getFlatNetMessage());

            return;
        }
    }

    public abstract void receivedFlatNetMessage(FlatNetMessage msg);

    
    public abstract void ackFlatNetMessageReceived(FlatNetMessage msg);

}
