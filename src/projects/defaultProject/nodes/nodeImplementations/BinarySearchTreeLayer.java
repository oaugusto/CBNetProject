package projects.defaultProject.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import projects.defaultProject.nodes.messages.RoutingMessage;
import projects.defaultProject.nodes.tableEntry.NodeInfo;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

/**
 * BinaryTreeLayer This class implements the binary tree node functions like keeping track of
 * neighboors and changing links. **Only parent nodes can change link to its children
 */
public abstract class BinarySearchTreeLayer extends Node implements
    Comparable<BinarySearchTreeLayer> {

  private BinarySearchTreeLayer parent;
  private BinarySearchTreeLayer leftChild;
  private BinarySearchTreeLayer rightChild;
  private int minIdInSubtree;
  private int maxIdInSubtree;
  private boolean isRoot;

  public BinarySearchTreeLayer getParent() {
    return this.parent;
  }

  public BinarySearchTreeLayer getLeftChild() {
    return this.leftChild;
  }

  public BinarySearchTreeLayer getRightChild() {
    return this.rightChild;
  }

  public int getMinIdInSubtree() {
    return this.minIdInSubtree;
  }

  public int getMaxIdInSubtree() {
    return this.maxIdInSubtree;
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

  public boolean isLeastCommonAncestorOf(BinarySearchTreeLayer node) {

    return this.minIdInSubtree <= node.ID && node.ID <= this.maxIdInSubtree;
  }

  public boolean isLeastCommonAncestorOf(int id) {

    return this.minIdInSubtree <= id && id <= this.maxIdInSubtree;
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

  protected boolean isNeighbor(int id) {
    return (this.parent != null && this.parent.ID == id) || (this.leftChild != null
        && this.leftChild.ID == id)
        || (this.rightChild != null && this.rightChild.ID == id);
  }

  public boolean isNeighbor(BinarySearchTreeLayer node) {
    return this.parent == node || this.leftChild == node || this.rightChild == node;
  }

  public BinarySearchTreeLayer getNeighbor(int id) {
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

  public String getRelationship(BinarySearchTreeLayer node) {
    if (node == this.parent) {
      return "Parent";
    } else if (node == this.leftChild) {
      return "LeftChild";
    } else if (node == this.rightChild) {
      return "RightChild";
    } else {
      return "None";
    }
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
    return new NodeInfo(this, this.parent, this.leftChild, this.rightChild, this.minIdInSubtree,
        this.maxIdInSubtree);
  }

  /**
   * @param parent the parent to set
   */
  public void setParent(BinarySearchTreeLayer parent) {
    this.parent = parent;
  }

  /**
   * @param leftChild the leftChild to set
   */
  public void setLeftChild(BinarySearchTreeLayer leftChild) {
    this.leftChild = leftChild;
  }

  /**
   * @param rightChild the rightChild to set
   */
  public void setRightChild(BinarySearchTreeLayer rightChild) {
    this.rightChild = rightChild;
  }

  private boolean isConnectedTo(BinarySearchTreeLayer node) {
    return this.outgoingConnections.contains(this, node) && node.outgoingConnections
        .contains(node, this);
  }

  private void addLinkTo(BinarySearchTreeLayer node) {
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
  private void removeLinkTo(BinarySearchTreeLayer node) {

    if ((node == null) || this.getRelationship(node).equals("Parent")) {
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
  public void addLinkToLeftChild(BinarySearchTreeLayer node) {
    // update current left child and create edge
    this.addLinkTo(node);
    this.setLeftChild(node);

    if (node != null) {
      node.setParent(this);
    }
  }

  /**
   * Set the link to node and update reference to right child
   *
   * @param node
   */
  public void addLinkToRightChild(BinarySearchTreeLayer node) {
    this.addLinkTo(node);
    this.setRightChild(node);

    if (node != null) {
      node.setParent(this);
    }
  }

  public void setMinIdInSubtree(int min) {
    this.minIdInSubtree = min;
  }

  public void setMaxIdInSubtree(int max) {
    this.maxIdInSubtree = max;
  }

  /**
   * Change link to left child, update reference to left child but do not update old left child
   * parent reference. The next node to set old left child as child will update its parent.
   */
  public void changeLeftChildTo(BinarySearchTreeLayer node) {
    // remove the previous connection
    this.removeLinkTo(this.leftChild);
    // update current left child and create edge
    this.addLinkToLeftChild(node);
  }

  /**
   * Change link to right child, update reference to right child but do not update old right child
   * parent reference. The next node to set old left child as child will update its parent.
   */
  protected void changeRightChildTo(BinarySearchTreeLayer node) {
    this.removeLinkTo(this.rightChild);
    this.addLinkToRightChild(node);
  }

  protected void sendToParent(Message msg) {
    if (this.outgoingConnections.contains(this, this.parent)) {
      send(msg, this.parent);
    } else {
      Tools.fatalError("Trying to send message through a non-existing connection with parent node");
    }
  }

  protected void sendToLeftChild(Message msg) {
    if (this.outgoingConnections.contains(this, this.leftChild)) {
      send(msg, this.leftChild);
    } else {
      Tools.fatalError("Trying to send message through a non-existing connection with left child");
    }
  }

  protected void sendToRightChild(Message msg) {
    if (this.outgoingConnections.contains(this, this.rightChild)) {
      send(msg, this.rightChild);
    } else {
      Tools.fatalError("Trying to send message through a non-existing connection with right child");
    }
  }

  private void forwardMessage(RoutingMessage msg) {
    if (ID < msg.getDestination() && msg.getDestination() <= this.maxIdInSubtree) {
      sendToRightChild(msg);
    } else if (this.minIdInSubtree <= msg.getDestination() && msg.getDestination() < ID) {
      sendToLeftChild(msg);
    } else {
      sendToParent(msg);
    }
  }

  protected void sendForwardMessage(int dst, Message msg) {
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
    this.minIdInSubtree = Integer.MIN_VALUE;
    this.maxIdInSubtree = Integer.MIN_VALUE;
  }

  @Override
  public void preStep() {
    // nothing to do
  }

  /**
   * The snoopingMessage method allow to intercept one forward message and decide if the message
   * should be routed or not.
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
  public int compareTo(BinarySearchTreeLayer o) {
    return ID - o.ID;
  }

  public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
    // String text = ID + " l:" + this.minIdInSubtree + " r:" + this.maxIdInSubtree;
    String text = "" + ID;

    // draw the node as a circle with the text inside
    super.drawNodeAsDiskWithText(g, pt, highlight, text, 12, Color.YELLOW);
  }
}