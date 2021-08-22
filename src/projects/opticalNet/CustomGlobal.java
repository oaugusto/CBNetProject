package projects.opticalNet;

import java.util.ArrayList;
import java.util.Random;

import projects.opticalNet.nodes.nodeImplementations.NetworkNode;
import sinalgo.configuration.Configuration;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.tools.Tools;
import sinalgo.tools.Tuple;

public class CustomGlobal extends AbstractCustomGlobal {

	// simulation
	public int numberOfNodes = 6;
	public ArrayList<NetworkNode> nodes = null;

  	@Override
	public void preRun() {
  	}

	@Override
	public void preRound() {
	
	}

	@Override
	public boolean hasTerminated() {
		return false;
	}

}