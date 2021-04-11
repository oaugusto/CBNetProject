package projects.opticalNet;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import projects.opticalNet.nodes.infrastructureImplementations.NetworkManager;
import projects.opticalNet.nodes.nodeImplementations.NetworkNode;
import sinalgo.configuration.Configuration;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.tools.Tools;
import sinalgo.tools.Tuple;

public class CustomGlobal extends AbstractCustomGlobal {

	// simulation
	public int numberOfNodes = 30;
	public ArrayList<NetworkNode> nodes = null;
	public NetworkManager networkManager = null;

  	@Override
	public void preRun() {
  		/*
  		 * create the nodes and constructs the topology
  		 */
  		this.nodes = new ArrayList<NetworkNode>();

  		for (int i = 0; i < numberOfNodes; ++i) {
  			NetworkNode n = new NetworkNode();
  			n.finishInitializationWithDefaultModels(true);
  			this.nodes.add(n);
  		}
  		
  		this.networkManager = new NetworkManager(this.numberOfNodes);
  		this.networkManager.connectNodesToInterface(nodes);
  	}

	@Override
	public void preRound() {
	
	}

	@Override
	public boolean hasTerminated() {
		return false;
	}

}