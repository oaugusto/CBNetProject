package projects.opticalNet.nodes.infrastructureImplementations;

import java.util.ArrayList;
import projects.opticalNet.nodes.nodeImplementations.NetworkNode;

public class NetworkManager {
	
	private int numberOfNodes = 0;
	private ArrayList<NetworkNode> nodes = null;
	
	private NetworkSwitch leftSwitchInput = null;
	private NetworkSwitch leftSwitchOutput = null;

	private NetworkSwitch rightSwitchInput = null;
	private NetworkSwitch rightSwitchOutput = null;
	
//	private NetworkController net
	
	public NetworkManager(int numberOfNodes) {	
		this.numberOfNodes = numberOfNodes;
		
		// the nodes must be created in this order to assure that
		// the network node ids reside in the range of 1...n 
		this.createNetworkNodes();
		this.createNetworkSwitches();
		
		this.connectNodesToInterfaces();
	}
	
	private void createNetworkNodes() {
  		this.nodes = new ArrayList<NetworkNode>();

  		for (int i = 0; i < this.numberOfNodes; ++i) {
  			NetworkNode n = new NetworkNode();
  			n.finishInitializationWithDefaultModels(true);
  			this.nodes.add(n);
  		}
	}
	
	private void createNetworkSwitches() {
		this.leftSwitchInput = new NetworkSwitch(this.numberOfNodes);
		this.leftSwitchInput.finishInitializationWithDefaultModels(true);
		this.leftSwitchInput.setIndex(1);
		
		this.leftSwitchOutput = new NetworkSwitch(this.numberOfNodes);
		this.leftSwitchOutput.finishInitializationWithDefaultModels(true);
		this.leftSwitchOutput.setIndex(2);
		
		this.rightSwitchInput = new NetworkSwitch(this.numberOfNodes);
		this.rightSwitchInput.finishInitializationWithDefaultModels(true);
		this.rightSwitchInput.setIndex(3);
		
		this.rightSwitchOutput = new NetworkSwitch(this.numberOfNodes);
		this.rightSwitchOutput.finishInitializationWithDefaultModels(true);
		this.rightSwitchOutput.setIndex(4);
	}
	
	private void connectNodeToSwitch(NetworkNode node, NetworkSwitch swtch) {
		InputNode iNode = swtch.getInputNode(node.ID - 1);
		OutputNode oNode = swtch.getOutputNode(node.ID - 1);
		node.connectToInputNode(iNode);
		iNode.connectToNode(node);
		oNode.connectToNode(node);
	}
	
	private void connectNodesToInterfaces() {
		for (int i = 0; i < this.numberOfNodes; ++i) {
			NetworkNode node = this.nodes.get(i);
			this.connectNodeToSwitch(node, this.leftSwitchInput);
			this.connectNodeToSwitch(node, this.leftSwitchOutput);
			this.connectNodeToSwitch(node, this.rightSwitchInput);
			this.connectNodeToSwitch(node, this.rightSwitchOutput);
		}
	}
	
	public ArrayList<NetworkNode> getNetworkNodes() {
		return this.nodes;
	}
	
	public void renderTopology(int width, int height) {
		// set network nodes position
		double x_space = width / 4.0;
		double y_space = height / (double) (this.numberOfNodes + 1);
		for (int i = 0; i < this.numberOfNodes; ++i) {
			NetworkNode n = this.nodes.get(i);
			n.setPosition(x_space, y_space * (i+1), 0);
		}
		
		//set network switches position
		int numberOfSwitches = 4;
		int unit = height / ((6 * numberOfSwitches) + 1);
		int switch_height = 5 * unit;
		int switch_width= switch_height / 2;
		this.leftSwitchInput.setPosition(3 * x_space, 3.5 * unit, 0);
		this.leftSwitchInput.setSwitchDimension(switch_height/3, switch_height);
		this.leftSwitchOutput.setPosition(3 * x_space, 9.5 * unit, 0);
		this.leftSwitchOutput.setSwitchDimension(switch_height/3, switch_height);
		this.rightSwitchInput.setPosition(3 * x_space, 15.5 * unit, 0);
		this.rightSwitchInput.setSwitchDimension(switch_height/3, switch_height);
		this.rightSwitchOutput.setPosition(3 * x_space, 21.5 * unit, 0);
		this.rightSwitchOutput.setSwitchDimension(switch_height/3, switch_height);
	}
}
