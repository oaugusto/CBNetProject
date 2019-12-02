package projects.flatnet.nodes.messages.controlMessage;

import java.util.HashSet;

import projects.flatnet.nodes.nodeImplementations.BinaryTreeLayer;
import sinalgo.nodes.messages.Message;

/**
 * RPCMessage
 */
public class RPCMessage extends Message {

    private String command;
    private BinaryTreeLayer node;
    private HashSet<Integer> descendants;
    private long value;

    public RPCMessage(String command, BinaryTreeLayer node) {
        this.command = command;
        this.node = node;
        this.value = -1;
        this.descendants = new HashSet<Integer>();
    }

    public RPCMessage(String command, long value) {
        this.command = command;
        this.node = null;
        this.value = value;
        this.descendants = new HashSet<Integer>();
    }

    public RPCMessage(String command, HashSet<Integer> descendants){
        this.command = command;
        this.node = null;
        this.value = -1;
        this.descendants = descendants;
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

    public HashSet<Integer> getDescendants() {
        return this.descendants;
    }

    public void setDescendants(HashSet<Integer> newSet){
        this.descendants = newSet;
    }

    public void setCommand(String cmd) {
        this.command = cmd;
    }

    public void setNode(BinaryTreeLayer node) {
        this.node = node;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public Message clone() {
        return this;
    }
    
}