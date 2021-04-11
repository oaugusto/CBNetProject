package projects.opticalNet.nodes.infrastructureImplementations;

import java.util.ArrayList;
import java.util.HashMap;

import projects.opticalNet.nodes.nodeImplementations.InputNode;
import projects.opticalNet.nodes.nodeImplementations.OutputNode;
import sinalgo.runtime.Global;

public class NetworkSwitch {
	
	// the number of entries of the switch
	private int size = 0;
	
	// map the relative Id to the actual node
	private HashMap<Integer, InputNode> inputId2Node;
	private HashMap<Integer, OutputNode> outputId2Node;
	
	// keep the pair of connected nodes
	//private HashMap<InputNode, OutputNode> connections;
	
	private ArrayList<InputNode> inputNodes;
	private ArrayList<OutputNode> outputNodes;
	
	// keep the position of the switch
	private double x = 0;
	private double y = 0;
	
	// switch dimensions
	private double length = 0;
	private double width = 0;
	
	// initialize variables
	private void setup() {
		this.inputId2Node = new HashMap<>();
		this.outputId2Node = new HashMap<>();
		//this.connections = new HashMap<>();
		this.inputNodes = new ArrayList<>();
		this.outputNodes = new ArrayList<>();
		
		// initialize input nodes
	    for (int i = 1; i <= this.size; ++i) {
	        InputNode n = new InputNode();
	        n.finishInitializationWithDefaultModels(true);
	        n.setIndex(i);
	        this.inputNodes.add(n);
	        this.inputId2Node.put(i, n);
	    }
	    
	    // initialize output nodes
	    for (int i = 1; i <= this.size; ++i) {
	        OutputNode n = new OutputNode();
	        n.finishInitializationWithDefaultModels(true);
	        n.setIndex(i);
	        this.outputNodes.add(n);
	        this.outputId2Node.put(i, n);
	    }
	    
	    // initialize connections
	    for (int i = 0; i < this.size; ++i) {
	    	InputNode in = this.inputNodes.get(i);
	    	OutputNode out = this.outputNodes.get(i);
	    	in.addLinkToOutputNode(out);
	    }   
	}
	
	public NetworkSwitch(int size) {
		this.size = size;
		this.setup();
	}
	
	public void setSwitchPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	private void updatePositions() {
		if (!Global.isGuiMode) {
			return;
		}
		double y_space = this.length / this.size; 
		for (int i = 0; i < this.size; ++i) {
			this.inputNodes.get(i).setPosition(this.x, this.y + (y_space * i), 0);
			this.outputNodes.get(i).setPosition(this.x + this.width, this.y + (y_space * i), 0);
		}
	}
	
	public void setSwitchDimension(double length, double width) {
		this.length = length;
		this.width = width;
		
		updatePositions();
	}
	
	// connect the input in to the output out changing
	// old connections
	public void connectNodes(int in, int out) {
		InputNode inNode = this.inputId2Node.get(in);
		OutputNode outNode = this.outputId2Node.get(out);
		inNode.addLinkToOutputNode(outNode);
	}
	
	public InputNode getInputNode(int index) {
		return this.inputNodes.get(index);
	}
	
	public OutputNode getOutputNode(int index) {
		return this.outputNodes.get(index);
	}
}