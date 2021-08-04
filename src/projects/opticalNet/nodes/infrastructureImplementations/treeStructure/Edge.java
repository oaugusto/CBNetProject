package projects.opticalNet.nodes.infrastructureImplementations.treeStructure;

public class Edge {
	private Node node1;
	private Node node2;
	
	public Edge(Node node1, Node node2) {
		this.node1 = node1;
		this.node2 = node2;
	}
	
	public Node getNode1() {
		return this.node1;
	}
	
	public Node getNode2() {
		return this.node2;
	}
}
