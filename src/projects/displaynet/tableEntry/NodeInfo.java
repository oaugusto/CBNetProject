package projects.displaynet.tableEntry;

import projects.displaynet.nodeImplementations.BinaryTreeNode;

public class NodeInfo {

    public BinaryTreeNode id;
    public BinaryTreeNode parent;
    public BinaryTreeNode leftChild;
    public BinaryTreeNode rightChild;
    public int minIdInSubtree;
    public int maxIdInSubtree;

    public NodeInfo() {
        this.id = null;
        this.parent = null;
        this.leftChild = null;
        this.rightChild = null;
        this.minIdInSubtree = Integer.MIN_VALUE;
        this.maxIdInSubtree = Integer.MAX_VALUE;
    }

    public NodeInfo(BinaryTreeNode id, BinaryTreeNode parent, BinaryTreeNode left, BinaryTreeNode right, int smallId,
            int largeId) {
        this.id = id;
        this.parent = parent;
        this.leftChild = left;
        this.rightChild = right;
        this.minIdInSubtree = smallId;
        this.maxIdInSubtree = largeId;
    }
}