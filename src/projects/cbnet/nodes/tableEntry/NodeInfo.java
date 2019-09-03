package projects.cbnet.nodes.tableEntry;

import projects.displaynet.nodeImplementations.BinaryTreeLayer;

/**
 * NodeInfo
 */
public class NodeInfo {

    private BinaryTreeLayer node;
    private BinaryTreeLayer parent;
    private BinaryTreeLayer leftChild;
    private BinaryTreeLayer rightChild;
    private int minIdInSubtree;
    private int maxIdInSubtree;
    private long weight;

    public NodeInfo() {
        this.node = null;
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
        this.minIdInSubtree = Integer.MIN_VALUE;
        this.maxIdInSubtree = Integer.MAX_VALUE;
        this.weight = Long.MIN_VALUE;
    }

    public NodeInfo(BinaryTreeLayer node, BinaryTreeLayer parent, BinaryTreeLayer left, BinaryTreeLayer right,
            int smallId, int largeId, long weight) {
        this.node = node;
        this.parent = parent;
        this.leftChild = left;
        this.rightChild = right;
        this.minIdInSubtree = smallId;
        this.maxIdInSubtree = largeId;
        this.weight = weight;
    }

    /**
     * @return the node
     */
    public BinaryTreeLayer getNode() {
        return node;
    }

    /**
     * @return the parent
     */
    public BinaryTreeLayer getParent() {
        return parent;
    }

    /**
     * @return the leftChild
     */
    public BinaryTreeLayer getLeftChild() {
        return leftChild;
    }

    /**
     * @return the rightChild
     */
    public BinaryTreeLayer getRightChild() {
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
     * @return the weight
     */
    public long getWeight() {
        return weight;
    }

    /**
     * @param node the node to set
     */
    public void setNode(BinaryTreeLayer node) {
        this.node = node;
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

    /**
     * @param weight the weight to set
     */
    public void setWeight(long weight) {
        this.weight = weight;
    }
}