package projects.opticalNet;

import java.util.ArrayList;
import java.util.Random;

import projects.defaultProject.DataCollection;
import projects.defaultProject.RequestQueue;
import projects.opticalNet.nodes.nodeImplementations.NetworkController;
import projects.opticalNet.nodes.nodeImplementations.NetworkNode;
import projects.opticalNet.nodes.timers.TriggerNodeOperation;
import sinalgo.configuration.Configuration;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.tools.Tools;
import sinalgo.tools.Tuple;

public class CustomGlobal extends AbstractCustomGlobal {

	// final condition
	public long MAX_REQ;
	
	// simulation
	public int numberOfNodes = 4;
	public int switchSize = 8;
	public NetworkController controller = null;
	public ArrayList<NetworkNode> netNodes = new ArrayList<>();
	
	public RequestQueue requestQueue;
	
	// control execution
	public static boolean isSequencial = true;
	public static boolean mustGenerateSplay = true;
	
	public Random random = Tools.getRandomNumberGenerator();
	public double lambda = 0.05;

	  // LOG
	DataCollection data = DataCollection.getInstance();

  	@Override
	public void preRun() {
  		String input = "";
  	    String output = "";

  	    try {

  	      if (Configuration.hasParameter("input")) {
  	        input = Configuration.getStringParameter("input");
  	      }

  	      if (Configuration.hasParameter("output")) {
  	        output = Configuration.getStringParameter("output");
  	      }

  	      if (Configuration.hasParameter("mu")) {
  	        double mu = (double) Configuration.getIntegerParameter("mu");
  	        lambda = (double) (1 / mu);
  	      }

  	    } catch (Exception e) {
  	      e.printStackTrace();
  	      System.out.println("Missing configuration parameters");
  	    }

  	    // Set Log Path
  	    this.data.setPath(output);

  	    /*
  	     * read input data and configure the simulation
  	     */
  	    this.requestQueue = new RequestQueue(input);
  	    this.numberOfNodes = this.requestQueue.getNumberOfNodes();
  	    MAX_REQ = this.requestQueue.getNumberOfRequests();
  		
  		
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
  		 if (mustGenerateSplay && this.requestQueue.hasNextRequest()) {
  			 mustGenerateSplay = false;

  			 double u = random.nextDouble();
  			 double x = Math.log(1 - u) / (-lambda);
  			 x = (int) x;
  			 if (x <= 0) {
  				 x = 1;
  			 }

  			Tuple<Integer, Integer> r = this.requestQueue.getNextRequest();
  	      	TriggerNodeOperation ted = new TriggerNodeOperation(r.first, r.second);
  	      	ted.startGlobalTimer(x);

  		 }
  	 }

	@Override
	public boolean hasTerminated() {
		if (this.data.getCompletedRequests() >= MAX_REQ) {
//			CBNetApp node = (CBNetApp) Tools.getNodeByID(1);
//			this.data.addTotalTime(node.getCurrentRound());
//			this.data.printRotationData();
//			this.data.printRoutingData();
			return true;
	    }
	    return false;
	}

}