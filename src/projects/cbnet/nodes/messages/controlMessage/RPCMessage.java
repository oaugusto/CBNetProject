package projects.cbnet.nodes.messages.controlMessage;

import projects.displaynet.nodes.nodeImplementations.BinaryTreeLayer;
import sinalgo.nodes.messages.Message;

/**
 * RPCMessage
 */
public class RPCMessage extends Message {

    private String command;
    private BinaryTreeLayer node;
    private long value;

    public RPCMessage(String command, BinaryTreeLayer node) {
        this.command = command;
        this.node = node;
        this.value = -1;
    }

    public RPCMessage(String command, int value) {
        this.command = command;
        this.node = null;
        this.value = value;
    }

    public String getCommand() {
        return this.command;
    }

    public BinaryTreeLayer getNode() {
        return this.node;
    }

    public long getValue() {
        return this.value;
    }

    public void setCommand(String cmd) {
        this.command = cmd;
    }

    public void setNode(BinaryTreeLayer node) {
        this.node = node;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public Message clone() {
        return this;
    }
    
}