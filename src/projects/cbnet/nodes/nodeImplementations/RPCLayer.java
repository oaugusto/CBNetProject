package projects.cbnet.nodes.nodeImplementations;

import java.util.LinkedList;
import java.util.Queue;

import projects.cbnet.nodes.messages.controlMessage.RPCMessage;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

/**
 * RPCLayer
 */
public abstract class RPCLayer extends SynchronizerLayer {

    private Queue<RPCMessage> rpcQueue;

    @Override
    public void init() {
        super.init();
        this.rpcQueue = new LinkedList<>();
    }

    public void clearRPCQueue() {
        this.rpcQueue.clear();
    }

    public void execute(RPCMessage rpc) {
        switch (rpc.getCommand()) {
        case "setParent":
            this.setParent((CBTreeLayer) rpc.getNode());
            break;

        case "setLeftChild":
            this.setLeftChild((CBTreeLayer) rpc.getNode());
            break;

        case "setRightChild":
            this.setRightChild((CBTreeLayer) rpc.getNode());
            break;

        case "setMinIdInSubtree":
            this.setMinIdInSubtree((int) rpc.getValue());
            break;

        case "setMaxIdInSubtree":
            this.setMaxIdInSubtree((int) rpc.getValue());
            break;

        case "changeLeftChildTo":
            this.changeLeftChildTo((CBTreeLayer) rpc.getNode());
            break;

        case "changeRightChildTo":
            this.changeRightChildTo((CBTreeLayer) rpc.getNode());
            break;

        case "setWeight":
            this.setWeight(rpc.getValue());

        case "incrementWeight":
            this.incrementWeight();

        default:
            Tools.fatalError("Wrong procedure called");
            break;
        }
    }

    public void executeAllRPC() {
        while (!this.rpcQueue.isEmpty()) {
            RPCMessage msg = this.rpcQueue.poll();
            this.execute(msg);
        }
    }

    public void requestRPCTo(int id, String command, CBTreeLayer node) {
        RPCMessage msg = new RPCMessage(command, node);
        this.sendForwardMessage(id, msg);
    }

    public void requestRPCTo(int id, String command, int n) {
        RPCMessage msg = new RPCMessage(command, n);
        this.sendForwardMessage(id, msg);
    }

    @Override
    public void receiveMessage(Message msg) {
        if (msg instanceof RPCMessage) {
            this.rpcQueue.add((RPCMessage) msg);
            return;
        }
    }

}