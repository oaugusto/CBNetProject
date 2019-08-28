package projects.displaynet.nodeImplementations;

import java.util.LinkedList;
import java.util.Queue;

import projects.displaynet.messages.controlMessages.RPCMessage;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

/**
 * RPCLayer
 */
public abstract class RPCLayer extends BinaryTreeLayer {

    private Queue<RPCMessage> rpcQueue;

    @Override
    public void init() {
        super.init();
        this.rpcQueue = new LinkedList<>();
    }

    public void cleanBuffer() {
        this.rpcQueue.clear();
    }

    public void execute(RPCMessage rpc) {
        switch (rpc.getCommand()) {
        case "setParent":
            this.setParent((BinaryTreeLayer) rpc.getArg());
            break;

        case "setLeftChild":
            this.setLeftChild((BinaryTreeLayer) rpc.getArg());
            break;

        case "setRightChild":
            this.setRightChild((BinaryTreeLayer) rpc.getArg());
            break;

        case "changeLeftChildTo":
            this.changeLeftChildTo((BinaryTreeLayer) rpc.getArg());
            break;

        case "changeRightChildTo":
            this.changeRightChildTo((BinaryTreeLayer) rpc.getArg());

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

    public void requestRPCTo(int id, String command, BinaryTreeLayer node ) {
        RPCMessage msg = new RPCMessage(command, node);
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