package projects.flatnet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;

import projects.defaultProject.nodes.messages.RoutingMessage;
import projects.flatnet.nodes.tableEntry.NodeInfo;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;
import java.util.HashSet;

/**
 * BinaryTreeLayer This class implements the binary tree node functions like
 * keeping track of neighboors and changing links. Only parent nodes can change
 * link to its children
 */
public abstract class BinaryTreeLayer extends Node implements Comparable<BinaryTreeLayer> {

    private BinaryTreeLayer parent;
    private BinaryTreeLayer leftChild;
    private BinaryTreeLayer rightChild;
    private HashSet<Integer> leftDescendants;
    private HashSet<Integer> rightDescendants;
    private int deferredChild;
    private boolean isRoot;

    public BinaryTreeLayer getParent() {
        return this.parent;
    }

    public BinaryTreeLayer getLeftChild() {
        return this.leftChild;
    }

    public BinaryTreeLayer getRightChild() {
        return this.rightChild;
    }

    public BinaryTreeLayer getDeferredChild(){
        if(this.deferredChild == 0)
            return this.leftChild;
        else
            return this.rightChild;
    }
    public BinaryTreeLayer getPreferredChild(){
        if(this.deferredChild == 1)
            return this.leftChild;
        else
            return this.rightChild;
    }

    public void shiftDefChild(){
        if(this.leftChild == null)
            this.deferredChild = 0;
        else if(this.rightChild == null)
            this.deferredChild = 1;
        else
            this.deferredChild = (this.deferredChild + 1)%2;
    }

    public BinaryTreeLayer getNextNode(int id) {
        int direction = this.getDirection(id);
        if(direction == 1)
            return this.leftChild;
        else if(direction == 2)
            return this.rightChild;
        else
            return null;
    }

    public HashSet<Integer> getLeftDescendants() {
        HashSet<Integer> leftDescendants = new HashSet<Integer>();
        leftDescendants.addAll(this.leftDescendants);
        return leftDescendants;
    }

    public HashSet<Integer> getRightDescendants() {
        HashSet<Integer> rightDescendants = new HashSet<Integer>();
        rightDescendants.addAll(this.rightDescendants);
        return rightDescendants;
    }

    public boolean isRoot() {
        return this.isRoot;
    }

    public void setRoot() {
        this.isRoot = true;
    }

    public void unsetRoot() {
        this.isRoot = false;
    }

    public boolean isLeastCommonAncestorOf(BinaryTreeLayer node) {
        return this.isLeastCommonAncestorOf(node.ID);
    }

    public boolean isLeastCommonAncestorOf(int id) {

        if(this.ID == id)
            return true;

        if (this.getDirection(id) != 0)
            return true;

        return false;
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

    public boolean isNeighbor(int id) {
        if ((this.parent != null && this.parent.ID == id) || (this.leftChild != null && this.leftChild.ID == id)
                || (this.rightChild != null && this.rightChild.ID == id)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isNeighbor(BinaryTreeLayer node) {
        return this.isNeighbor(node.ID);
    }

    public BinaryTreeLayer getNeighbor(int id) {
        if (id == this.parent.ID) {
            return this.parent;
        } else if (id == this.leftChild.ID) {
            return this.leftChild;
        } else if (id == this.rightChild.ID) {
            return this.rightChild;
        } else {
            return null;
        }
    }

    public String getRelationship(BinaryTreeLayer node) {
        return getRelationship(node.ID);
    }

    public String getRelationship(int id) {
        if (this.parent != null && id == this.parent.ID) {
            return "Parent";
        } else if (this.leftChild != null && id == this.leftChild.ID) {
            return "LeftChild";
        } else if (this.rightChild != null && id == this.rightChild.ID) {
            return "RightChild";
        } else {
            return "None";
        }
    }

    public NodeInfo getNodeInfo() {
        return new NodeInfo(this, this.parent, this.leftChild, this.rightChild, this.leftDescendants,
                            this.rightDescendants);
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(BinaryTreeLayer parent) {
        this.parent = parent;
    }

    /**
     * @param leftChild the leftChild to set
     */
    public void setLeftChild(BinaryTreeLayer leftChild) {
        this.leftChild = leftChild;
    }

    /**
     * @param rightChild the rightChild to set
     */
    public void setRightChild(BinaryTreeLayer rightChild) {
        this.rightChild = rightChild;
    }

    private boolean isConnectedTo(BinaryTreeLayer node) {
        if (this.outgoingConnections.contains(this, node) && node.outgoingConnections.contains(node, this)) {
            return true;
        } else {
            return false;
        }
    }

    private void addLinkTo(BinaryTreeLayer node) {
        if (node != null) {
            this.outgoingConnections.add(this, node, false);
            node.outgoingConnections.add(node, this, false);
        }
    }

    /**
     * Remove a link with the node is not null.
     * 
     * @param node
     */
    private void removeLinkTo(BinaryTreeLayer node) {

        if (node == null || this.getRelationship(node) == "Parent") {
            return;
        }

        if (this.isConnectedTo(node)) {
            this.outgoingConnections.remove(this, node);
            node.outgoingConnections.remove(node, this);
        } else {
            Tools.fatalError("Trying to remove a non-existing conenction on node " + ID);
        }
    }

    /**
     * Set the link to node and update reference to left child
     * 
     * @param node
     */
    public void addLinkToLeftChild(BinaryTreeLayer node) {
        // update current left child and create edge

        this.addLinkTo(node);
        this.setLeftChild(node);

        if (node != null) {
            node.setParent(this);
            this.leftDescendants.add(node.ID);
        }
    }

    /**
     * Set the link to node and update reference to right child
     * 
     * @param node
     */
    public void addLinkToRightChild(BinaryTreeLayer node) {

        this.addLinkTo(node);
        this.setRightChild(node);

        if (node != null) {
            node.setParent(this);
            this.rightDescendants.add(node.ID);
        }
    }

    public void setLeftDescendants(BinaryTreeLayer node) {
        this.leftDescendants = new HashSet<Integer>();
        if(node == null) return;

        this.leftDescendants.addAll(node.getLeftDescendants());
        this.leftDescendants.addAll(node.getRightDescendants());
        this.leftDescendants.add(node.ID);
    }
    public void setLeftDescendants(HashSet<Integer> newTable) {
        this.leftDescendants = new HashSet<Integer>();

        this.leftDescendants.addAll(newTable);
    }

    public void setRightDescendants(BinaryTreeLayer node) {
        this.rightDescendants = new HashSet<Integer>();
        if(node == null) return;

        this.rightDescendants.addAll(node.getLeftDescendants());
        this.rightDescendants.addAll(node.getRightDescendants());
        this.rightDescendants.add(node.ID);
    }


    public void setRightDescendants(HashSet<Integer> newTable) {
        this.rightDescendants = new HashSet<Integer>();
        this.rightDescendants.addAll(newTable);
    }

    /**
     * Change link to left child, update reference to left child but do not update
     * old left child parent reference. The next node to set old left child as child
     * will update its parent.
     */
    public void changeLeftChildTo(BinaryTreeLayer node) {
        if(node != null)
            this.leftDescendants.add(node.ID);
        if(this.leftChild != null)
            this.leftDescendants.remove(this.leftChild.ID);
        
        // remove the previous connection
        this.removeLinkTo(this.leftChild);
        // update current left child and create edge

        this.addLinkToLeftChild(node);
    }

    /**
     * Change link to right child, update reference to right child but do not update
     * old right child parent reference. The next node to set old left child as
     * child will update its parent.
     */
    public void changeRightChildTo(BinaryTreeLayer node) {
        if(node != null)
            this.rightDescendants.add(node.ID);
        if(this.rightChild != null)
            this.rightDescendants.remove(this.rightChild.ID);

        this.removeLinkTo(this.rightChild);
        this.addLinkToRightChild(node);
    }

    public void updateLeftChildTo(BinaryTreeLayer node){
        this.removeLinkTo(this.leftChild);
        this.addLinkToLeftChild(node);
    }

    public void updateRightChildTo(BinaryTreeLayer node){
        this.removeLinkTo(this.rightChild);
        this.addLinkToRightChild(node);
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

    public int getDirection(int id){
        if(this.leftDescendants.contains(id))
            return 1;
        else if(this.rightDescendants.contains(id))
            return 2;
        else
            return 0;
    }

    private void forwardMessage(RoutingMessage msg) {
        //// myLog.logln(Global.currentTime +":: "+"Forwarding Message" + msg.receiver +
        //// "\n");
        int direction = this.getDirection(msg.getDestination());
        if (direction == 2) {
            sendToRightChild(msg);
        } else if (direction == 1) {
            sendToLeftChild(msg);
        } else {
            sendToParent(msg);
        }
    }

    public void sendForwardMessage(int dst, Message msg) {
        // myLog.logln(Global.currentTime +":: "+"sendForwarding Message request splay"
        // + msg.receiver + "\n");
        RoutingMessage rt = new RoutingMessage(ID, dst, msg);

        if (dst == ID) {
            this.receiveMessage(msg);
            return;
        }

        forwardMessage(rt);
    }

    @Override
    public void init() {
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
        this.leftDescendants = new HashSet<Integer>();
        this.rightDescendants = new HashSet<Integer>();
    }

    @Override
    public void preStep() {
        // nothing to do
    }

    /**
     * The snoopingMessage method allow to intercept one forward message and decide
     * if the message should be routed or not.
     */
    @Override
    public void handleMessages(Inbox inbox) {
        while (inbox.hasNext()) {
            Message msg = inbox.next();

            if (msg instanceof RoutingMessage) {
                RoutingMessage rt = (RoutingMessage) msg;
                Message payload = rt.getPayLoad();

                if (this.snoopingMessage(payload)) {
                    if (rt.getDestination() == ID) {
                        this.receiveMessage(payload);
                    } else {
                        this.forwardMessage(rt);
                    }
                }

            } else {
                this.receiveMessage(msg);
            }
        }
    }

    /**
     * This function is called for each message received
     * 
     * @param msg
     */
    public void receiveMessage(Message msg) {

    }

    /**
     * if the function return false the message will not be forward to next node
     */
    public boolean snoopingMessage(Message msg) {
        return true;
    }

    @Override
    public void neighborhoodChange() {
        // nothing to do
    }

    @Override
    public void checkRequirements() throws WrongConfigurationException {
        // nothing to do
    }

    @Override
    public int compareTo(BinaryTreeLayer o) {
        return ID - o.ID;
    }

    public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
        // String text = ID + " l:" + this.minIdInSubtree + " r:" + this.maxIdInSubtree;
        String text = "" + ID;

        // draw the node as a circle with the text inside
        super.drawNodeAsDiskWithText(g, pt, highlight, text, 12, Color.YELLOW);
    }
}