package projects.opticalNet.nodes.infrastructureImplementations.treeStructure;

import java.util.ArrayList;

import projects.defaultProject.nodes.nodeImplementations.BinarySearchTreeLayer;
import sinalgo.tools.Tools;


public class Tree {

	private int size;
	private Node controlNode;
	private ArrayList<Node> nodeList = new ArrayList<Node>();
	
	private long MIN = Long.MAX_VALUE;
	private long MAX = Long.MIN_VALUE;
	
	public Tree(int size) {
		this.size = size;
		for (int i = 1; i <= this.size; ++i) {
			Node node = new Node(i);
			this.nodeList.add(node);
		}
	}
	
	public Node getRootNode() {
	    return this.controlNode.getLeftChild();
	  }
	
	public void buildTree() {

	    if (nodeList.isEmpty()) {
	      Tools.fatalError("Empty network passed to TreeConstructor");
	    }

	    // build binary tree topology
	    Node root = buildBalancedTree(1, this.nodeList.size());

	    // configure the control node
	    this.controlNode.setParent(null);
	    this.controlNode.setRightChild(null);
	    this.controlNode.setMinIdInSubtree(1);
	    this.controlNode.setMaxIdInSubtree(this.nodeList.size());
	}
	
	private Node buildBalancedTree(int start, int end) {

	    int parentId = Integer.MIN_VALUE;
	    int leftChildId = Integer.MIN_VALUE;
	    int rightChildId = Integer.MIN_VALUE;

	    Node parent = null;
	    Node leftChild = null;
	    Node rightChild = null;

	    parentId = (start + end) / 2;
	    parent = this.nodeList.get(parentId - 1);
	    parent.setMinIdInSubtree(start);
	    parent.setMaxIdInSubtree(end);

	    // case there is left subtree
	    if (parentId != start) {
	      leftChildId = (start + parentId - 1) / 2; // find left child
	      leftChild = this.nodeList.get(leftChildId - 1);
//	      parent.addLinkToLeftChild(leftChild);
	      leftChild.setMinIdInSubtree(start);
	      leftChild.setMaxIdInSubtree(parentId - 1);

	      buildBalancedTree(start, parentId - 1);
	    }

	    // case there is right subtree
	    if (parentId != end) {
	      rightChildId = (parentId + 1 + end) / 2;
	      rightChild = this.nodeList.get(rightChildId - 1);
//	      parent.addLinkToRightChild(rightChild);
	      rightChild.setMinIdInSubtree(parentId + 1);
	      rightChild.setMaxIdInSubtree(end);

	      buildBalancedTree(parentId + 1, end);
	    }

	    return parent;
	  }
	
	private int getTreeHeight(Node root) {
		int left = (root.hasLeftChild()) ? getTreeHeight(root.getLeftChild()) : 0;
	    int right = (root.hasRightChild()) ? getTreeHeight(root.getRightChild()) : 0;

	    return Math.max(left, right) + 1;
	}
	
	private int getTreeSize(Node root) {
		int left = (root.hasLeftChild()) ? getTreeSize(root.getLeftChild()) : 0;
		int right = (root.hasRightChild()) ? getTreeSize(root.getRightChild()) : 0;

		return left + right + 1;
	}
	
	
}
