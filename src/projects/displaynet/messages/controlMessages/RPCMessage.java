package projects.displaynet.messages.controlMessages;

import projects.displaynet.nodeImplementations.BinaryTreeLayer;
import sinalgo.nodes.messages.Message;

/**
 * RPCMessage
 */
public class RPCMessage extends Message {

    private String command;
    private BinaryTreeLayer arg;

    public RPCMessage(String command, BinaryTreeLayer node) {
        this.command = command;
        this.arg = node;
    }

    public String getCommand() {
        return this.command;
    }

    public BinaryTreeLayer getArg() {
        return this.arg;
    }

    public void setCommand(String cmd) {
        this.command = cmd;
    }

    public void setArgs(BinaryTreeLayer node) {
        this.arg = node;
    }

    @Override
    public Message clone() {
        return this;
    }
    
}