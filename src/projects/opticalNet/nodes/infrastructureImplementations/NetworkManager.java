package projects.opticalNet.nodes.infrastructureImplementations;

import java.util.ArrayList;
import projects.opticalNet.nodes.nodeImplementations.NetworkNode;

public class NetworkManager {
	
	private int numberOfnodes = 0;
	private ArrayList<NetworkNode> nodes = null;
	
	private NetworkSwitch leftSwitchInput;
	private NetworkSwitch leftSwitchOutput;

	private NetworkSwitch rightSwitchInput;
	private NetworkSwitch rightSwitchOutput;
	
	public NetworkManager(int numberOfNodes) {
		this.numberOfnodes = numberOfNodes;
	}
	
	public void connectNodesToInterfaces(ArrayList<NetworkNode> nodes) {
		this.nodes = nodes;
		
		for (int i = 0; i < this.numberOfnodes; ++i) {
			NetworkNode node = nodes.get(i);
		}
	}
}
