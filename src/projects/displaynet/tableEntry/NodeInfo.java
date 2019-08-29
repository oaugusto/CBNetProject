package projects.displaynet.tableEntry;

import sinalgo.nodes.Node;

public class NodeInfo {

    private Node node;
    private Node parent;
    private Node leftChild;
    private Node rightChild;
    private int minIdInSubtree;
    private int maxIdInSubtree;

    public NodeInfo() {
        this.node = null;
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
        this.minIdInSubtree = Integer.MIN_VALUE;
        this.maxIdInSubtree = Integer.MAX_VALUE;
    }

    public NodeInfo(Node id, Node parent, Node left, Node right, int smallId, int largeId) {
        this.node = id;
        this.parent = parent;
        this.leftChild = left;
        this.rightChild = right;
        this.minIdInSubtree = smallId;
        this.maxIdInSubtree = largeId;
    }

    /**
     * @return the node
     */
    public Node getNode() {
        return node;
    }

    /**
     * @return the parent
     */
    public Node getParent() {
        return parent;
    }

    /**
     * @return the leftChild
     */
    public Node getLeftChild() {
        return leftChild;
    }

    /**
     * @return the rightChild
     */
    public Node getRightChild() {
        return rightChild;
    }

    /**
     * @return the minIdInSubtree
     */
    public int getMinIdInSubtree() {
        return minIdInSubtree;
    }

    /**
     * @return the maxIdInSubtree
     */
    public int getMaxIdInSubtree() {
        return maxIdInSubtree;
    }

    /**
     * @param node the node to set
     */
    public void setNode(Node node) {
        this.node = node;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(Node parent) {
        this.parent = parent;
    }

    /**
     * @param leftChild the leftChild to set
     */
    public void setLeftChild(Node leftChild) {
        this.leftChild = leftChild;
    }

    /**
     * @param rightChild the rightChild to set
     */
    public void setRightChild(Node rightChild) {
        this.rightChild = rightChild;
    }

    /**
     * @param minIdInSubtree the minIdInSubtree to set
     */
    public void setMinIdInSubtree(int minIdInSubtree) {
        this.minIdInSubtree = minIdInSubtree;
    }

    /**
     * @param maxIdInSubtree the maxIdInSubtree to set
     */
    public void setMaxIdInSubtree(int maxIdInSubtree) {
        this.maxIdInSubtree = maxIdInSubtree;
    }

}