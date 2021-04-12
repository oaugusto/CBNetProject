package projects.opticalNet.nodes.infrastructureImplementations;

import java.util.ArrayList;
import projects.opticalNet.nodes.nodeImplementations.NetworkNode;

public class NetworkManager {
	
	private int numberOfnodes = 0;
	private ArrayList<NetworkNode> nodes = null;
	
	private NetworkSwitch leftSwitchInput = null;
	private NetworkSwitch leftSwitchOutput = null;

	private NetworkSwitch rightSwitchInput = null;
	private NetworkSwitch rightSwitchOutput = null;
	
	public NetworkManager(int numberOfNodes) {	
		this.numberOfnodes = numberOfNodes;
		this.leftSwitchInput = new NetworkSwitch(numberOfNodes);
		this.leftSwitchInput.setIndex(1);
		this.leftSwitchInput.setSwitchPosition(500, 500);
		this.leftSwitchInput.setSwitchDimension(200, 200);
		
		this.leftSwitchOutput = new NetworkSwitch(numberOfNodes);
		this.leftSwitchOutput.setIndex(2);
		
		this.rightSwitchInput = new NetworkSwitch(numberOfNodes);
		this.rightSwitchInput.setIndex(3);
		
		this.rightSwitchOutput = new NetworkSwitch(numberOfNodes);
		this.rightSwitchOutput.setIndex(4);
	}
	
	public void setSwitchesPosition() {
		
	}
	
	// TODO check if node ids start at 1
	private void connectNodeToSwitch(NetworkNode node, NetworkSwitch swtch) {
		InputNode iNode = swtch.getInputNode(node.ID - 1);
		OutputNode oNode = swtch.getOutputNode(node.ID - 1);
		node.connectToInputNode(iNode);
		iNode.connectToNode(node);
		oNode.connectToNode(node);
	}
	
	public void connectNodesToInterfaces(ArrayList<NetworkNode> nodes) {
		this.nodes = nodes;
		
		for (int i = 0; i < this.numberOfnodes; ++i) {
			NetworkNode node = nodes.get(i);
			this.connectNodeToSwitch(node, this.leftSwitchInput);
			this.connectNodeToSwitch(node, this.leftSwitchOutput);
			this.connectNodeToSwitch(node, this.rightSwitchInput);
			this.connectNodeToSwitch(node, this.rightSwitchOutput);
		}
	}
}
