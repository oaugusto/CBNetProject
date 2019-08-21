package projects.displaynet.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;

import projects.displaynet.messages.RoutingMessage;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

/**
 * BinaryTreeNode This class implements the binary tree node functions like
 * keeping track of neighboors and changing links
 */
public class BinaryTreeNode extends Node implements Comparable<BinaryTreeNode> {

    private BinaryTreeNode parent;
    private BinaryTreeNode leftChild;
    private BinaryTreeNode rightChild;
    private int minIdInSubtree;
    private int maxIdInSubtree;

    // private int time = 0;
    // private boolean firstTime = false;

    public BinaryTreeNode getParent() {
        return this.parent;
    }

    public BinaryTreeNode getLeftChild() {
        return this.leftChild;
    }

    public BinaryTreeNode getRightChild() {
        return this.rightChild;
    }

    public int getMinIdInSubtree() {
        return this.minIdInSubtree;
    }

    public int getMaxIdInSubtree() {
        return this.maxIdInSubtree;
    }

    public boolean isRoot() {
        return this.parent == null;
    }

    public boolean isLeaf() {
        return this.leftChild == null && this.rightChild == null;
    }

    public boolean hasLeftChild() {
        return this.leftChild != null;
    }

    public boolean hasRightChild() {
        return this.rightChild != null;
    }

    public void setParent(BinaryTreeNode node) {
        this.parent = node;
    }

    public void setLeftChild(BinaryTreeNode node) {
        this.leftChild = node;
    }

    public void setRightChild(BinaryTreeNode node) {
        this.rightChild = node;
    }

    private void addLinkTo(BinaryTreeNode node) {
        if (node != null) {
            this.outgoingConnections.add(this, node, false);
            node.outgoingConnections.add(node, this, false);
        } else {
            Tools.fatalError("Trying to add a connection with non-existing node");
        }
    }

    private boolean removeLinkTo(BinaryTreeNode node) {
        if (!this.outgoingConnections.contains(this, node) || !node.outgoingConnections.contains(node, this)) {
            return false;
        }

        this.outgoingConnections.remove(this, node);
        node.outgoingConnections.remove(node, this);
        return true;
    }

    public void changeParentTo(BinaryTreeNode node) {
        this.removeLinkTo(this.parent);
        this.addLinkTo(node);
        this.parent = node;
        // change_log.logln(Global.currentTime + " [CHANGE LINK] node " + ID + " change
        // parent to " + "node " + node);
    }

    public void changeRightChildTo(BinaryTreeNode node) {
        this.removeLinkTo(this.rightChild);
        this.addLinkTo(node);
        this.rightChild = node;
        // change_log.logln(Global.currentTime + " [CHANGE LINK] node " + ID + " change
        // right child to " + "node " + node);
    }

    public void changeLeftChildTo(BinaryTreeNode node) {
        this.removeLinkTo(this.leftChild);
        this.addLinkTo(node);
        this.leftChild = node;
        // change_log.logln(Global.currentTime + " [CHANGE LINK] node " + ID + " change
        // left child to " + "node " + node);
    }

    public void setMinIdInSubtree(int min) {
        this.minIdInSubtree = min;
        // change_log.logln(Global.currentTime + " [CHANGE min] node " + ID + " change
        // min to " + min);
    }

    public void setMaxIdInSubtree(int max) {
        this.maxIdInSubtree = max;
        // change_log.logln(Global.currentTime + " [CHANGE max] node " + ID + " change
        // MAX to " + max);

    }

    public boolean sendToParent(Message msg) {
        if (this.outgoingConnections.contains(this, this.parent)) {
            send(msg, this.parent);
            return true;
        } else {
            Tools.fatalError("Trying to send message through a non-existing connection with parent node");
            return false;
        }
    }

    public boolean sendToLeftChild(Message msg) {
        if (this.outgoingConnections.contains(this, this.leftChild)) {
            send(msg, this.leftChild);
            return true;
        } else {
            Tools.fatalError("Trying to send message through a non-existing connection with left child");
            return false;
        }
    }

    public boolean sendToRightChild(Message msg) {
        if (this.outgoingConnections.contains(this, this.rightChild)) {
            send(msg, this.rightChild);
            return true;
        } else {
            Tools.fatalError("Trying to send message through a non-existing connection with right child");
            return false;
        }
    }

    private void forwardMessage(RoutingMessage msg) {
        //// myLog.logln(Global.currentTime +":: "+"Forwarding Message" + msg.receiver +
        //// "\n");
        if (ID < msg.getDestination() && msg.getDestination() <= this.maxIdInSubtree) {
            sendToRightChild(msg);
        } else if (this.minIdInSubtree <= msg.getDestination() && msg.getDestination() < ID) {
            sendToLeftChild(msg);
        } else {
            sendToParent(msg);
        }
    }

    public void sendForwardMessge(RoutingMessage msg) {
        // myLog.logln(Global.currentTime +":: "+"sendForwarding Message request splay"
        // + msg.receiver + "\n");
        if (msg.getDestination() == ID) {
            Tools.fatalError("Trying to send message to itself");
        }

        forwardMessage(msg);
    }

    @Override
    public void init() {
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
        this.minIdInSubtree = Integer.MIN_VALUE;
        this.maxIdInSubtree = Integer.MIN_VALUE;
    }

    @Override
    public void preStep() {

    }

    @Override
    public void handleMessages(Inbox inbox) {
        while (inbox.hasNext()) {
            Message msg = inbox.next();

            if (msg instanceof RoutingMessage) {
                RoutingMessage rt = (RoutingMessage) msg;

                if (rt.getDestination() == ID) {
                    this.receiveMsg();
                } else {
                    this.forwardMessage(rt);
                }
            }
        }
    }

    public void receiveMsg() {
        System.out.println("MessageReceive");
    }

    @Override
    public void postStep() {
        // if (time == 4) {
        //     if (this.firstTime == false) {
        //         if (ID == 1) {
        //             RoutingMessage rt = new RoutingMessage(ID, 30);
        //             sendForwardMessge(rt);
        //         }
        //         this.firstTime = true;
        //     }
        // }
        // time++;
    }

    @Override
    public void neighborhoodChange() {
        //nothing to do
    }

    @Override
    public void checkRequirements() throws WrongConfigurationException {
        //nothing to do
    }

    @Override
    public int compareTo(BinaryTreeNode o) {
        return ID - o.ID;
    }

    public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
        String text = Integer.toString(ID);
        // draw the node as a circle with the text inside
        super.drawNodeAsDiskWithText(g, pt, highlight, text, 16, Color.YELLOW);
    }

}