package projects.displaynet.tableEntry;

import sinalgo.nodes.Node;

public class NodeInfo {

    public Node id;
    public Node parent;
    public Node leftChild;
    public Node rightChild;
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

    public NodeInfo(Node id, Node parent, Node left, Node right, int smallId, int largeId) {
        this.id = id;
        this.parent = parent;
        this.leftChild = left;
        this.rightChild = right;
        this.minIdInSubtree = smallId;
        this.maxIdInSubtree = largeId;
    }
}