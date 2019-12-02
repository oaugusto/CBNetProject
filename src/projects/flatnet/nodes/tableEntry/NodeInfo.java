package projects.flatnet.nodes.tableEntry;

import projects.flatnet.nodes.nodeImplementations.BinaryTreeLayer;
import java.util.HashSet;

public class NodeInfo {

    private BinaryTreeLayer node;
    private BinaryTreeLayer parent;
    private BinaryTreeLayer leftChild;
    private BinaryTreeLayer rightChild;
    private HashSet<Integer> leftDescendants;
    private HashSet<Integer> rightDescendants;

    public NodeInfo() {
        this.node = null;
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
        this.leftDescendants = new HashSet<Integer>();
        this.rightDescendants = new HashSet<Integer>();
    }

    public NodeInfo(BinaryTreeLayer node, BinaryTreeLayer parent, BinaryTreeLayer left, BinaryTreeLayer right,
                    HashSet<Integer> leftDescendants, HashSet<Integer> rightDescendants) {
        this.node = node;
        this.parent = parent;
        this.leftChild = left;
        this.rightChild = right;
        this.leftDescendants = leftDescendants;
        this.rightDescendants = rightDescendants;
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
     * @return the leftDescendants
     */
    public HashSet<Integer> getLeftDescendants() {
        HashSet<Integer> leftDescendants = new HashSet<Integer>();
        leftDescendants.addAll(this.leftDescendants);
        return leftDescendants;
    }

    /**
     * @return the rightDescendants
     */
    public HashSet<Integer> getRightDescendants() {
        HashSet<Integer> rightDescendants = new HashSet<Integer>();
        rightDescendants.addAll(this.rightDescendants);
        return rightDescendants;
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
     * @param leftDescendants the leftDescendants to set
     */
    public void setLeftDescendants(HashSet<Integer> leftDescendants) {
        this.leftDescendants = new HashSet<Integer>();
        this.leftDescendants.addAll(this.leftDescendants);
    }

    /**
     * @param rightDescendants the rightDescendants to set
     */
    public void setRightDescendants(int rightDescendants) {
        this.rightDescendants = new HashSet<Integer>();
        this.rightDescendants.addAll(this.rightDescendants);
    }

}