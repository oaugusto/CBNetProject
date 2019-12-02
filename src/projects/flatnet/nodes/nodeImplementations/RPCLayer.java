package projects.flatnet.nodes.nodeImplementations;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import projects.flatnet.nodes.messages.controlMessage.RPCMessage;
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
            this.setParent((FlatTreeLayer) rpc.getNode());
            break;

        case "updateLeftChildTo":
            this.updateLeftChildTo((FlatTreeLayer) rpc.getNode());
            break;

        case "updateRightChildTo":
            this.updateRightChildTo((FlatTreeLayer) rpc.getNode());
            break;

        case "changeLeftChildTo":
            this.changeLeftChildTo((FlatTreeLayer) rpc.getNode());
            break;

        case "changeRightChildTo":
            this.changeRightChildTo((FlatTreeLayer) rpc.getNode());
            break;

        case "setLeftDescendants": 
            this.setLeftDescendants(rpc.getDescendants());
            break;
    
        case "setRightDescendants":
            this.setRightDescendants(rpc.getDescendants());
            break;

        default:
            Tools.fatalError("Wrong procedure called " + rpc.getCommand());
            break;
        }
    }

    public void executeAllRPC() {
        while (!this.rpcQueue.isEmpty()) {
            RPCMessage msg = this.rpcQueue.poll();
            this.execute(msg);
        }
    }

    public void requestRPCTo(int id, String command, FlatTreeLayer node) {
        RPCMessage msg = new RPCMessage(command, node);
        this.sendForwardMessage(id, msg);
    }

    public void requestRPCTo(int id, String command, long n) {
        RPCMessage msg = new RPCMessage(command, n);
        this.sendForwardMessage(id, msg);
    }

    public void requestRPCTo(int id, String command, HashSet<Integer> descendants){
        RPCMessage msg = new RPCMessage(command, descendants);
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