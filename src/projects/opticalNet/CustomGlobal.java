package projects.opticalNet;

import java.util.ArrayList;
import java.util.Random;

import projects.opticalNet.nodes.nodeImplementations.NetworkController;
import projects.opticalNet.nodes.nodeImplementations.NetworkNode;
import sinalgo.configuration.Configuration;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.tools.Tools;
import sinalgo.tools.Tuple;

public class CustomGlobal extends AbstractCustomGlobal {

	// simulation
	public int numberOfNodes = 4;
	public int switchSize = 8;
	public NetworkController controller = null;

  	@Override
	public void preRun() {
  		ArrayList<NetworkNode> netNodes = new ArrayList<>();
  		for (int i = 0; i < this.numberOfNodes; i++) {
  			NetworkNode newNetNode = new NetworkNode();
  			newNetNode.finishInitializationWithDefaultModels(true);
  			netNodes.add(newNetNode);
  		}
  		
  		this.controller = new NetworkController(this.numberOfNodes, this.switchSize, netNodes);
  		this.controller.renderTopology(Configuration.dimX, Configuration.dimY);
  	}

	@Override
	public void preRound() {
	
	}

	@Override
	public boolean hasTerminated() {
		return false;
	}

}