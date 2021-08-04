package projects.opticalNet.nodes.infrastructureImplementations.treeStructure;

public class Node {

	private int ID;
	private Node parent;
	private Node leftChild;
	private Node rightChild;
	private int minIdInSubtree;
	private int maxIdInSubtree;
	
	public Node(int ID) {
		this.ID = ID;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public boolean hasParent() {
		return this.parent != null;
	}
	
	public Node getParent(Node parent) {
		return this.parent;
	}
	
	public void setLeftChild(Node leftChild) {
		this.leftChild = leftChild;
	}
	
	public boolean hasLeftChild() {
		return this.leftChild != null;
	}
	
	public Node getLeftChild() {
		return this.leftChild;
	}
	
	public void setRightChild(Node rightChild) {
		this.rightChild = rightChild;
	}
	
	public boolean hasRightChild() {
		return this.rightChild != null;
	}
	
	public Node getRightChild() {
		return this.rightChild;
	}
	
	public void setMinIdInSubtree(int min) {
		this.minIdInSubtree = min;
	}
	
	public int getMinIdInSubtree() {
		return this.minIdInSubtree;
	}
	
	public void setMaxIdInSubtree(int max) {
		this.maxIdInSubtree = max;
	}
	
	public int getMaxIdInSubtree() {
		return this.maxIdInSubtree;
	}
	
}
