package projects.opticalNet.nodes.infrastructureImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.runtime.Global;

public class NetworkSwitch extends Node {
	
	// switch id
	private int index = -1;
	
	// the number of entries of the switch
	private int size = 0;
	
	// map the relative Id to the actual node
	private HashMap<Integer, InputNode> inputId2Node;
	private HashMap<Integer, OutputNode> outputId2Node;
		
	private ArrayList<InputNode> inputNodes;
	private ArrayList<OutputNode> outputNodes;
	
	// keep the position of the switch
	private double x = 0;
	private double y = 0;
	
	// switch dimensions
	private int width = 0;
	private int height = 0;
	// small unit
	private int unitSize = 0;
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public int getIndex() {
		return this.index;
	}
	
	// initialize variables
	private void setup() {
		this.inputId2Node = new HashMap<>();
		this.outputId2Node = new HashMap<>();
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
	
	// connect the input in to the output out changing old connections
	public void connectNodes(int in, int out) {
		InputNode inNode = this.inputId2Node.get(in);
		OutputNode outNode = this.outputId2Node.get(out);
		inNode.updateLinkToOutputNode(outNode);
	}
	
	public InputNode getInputNode(int index) {
		return this.inputNodes.get(index);
	}
	
	public OutputNode getOutputNode(int index) {
		return this.outputNodes.get(index);
	}

	@Override
	public void handleMessages(Inbox inbox) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void neighborhoodChange() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postStep() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void checkRequirements() throws WrongConfigurationException {
		// TODO Auto-generated method stub
		
	}
	
	public void setSwitchPosition(double x, double y) {
		this.x = x;
		this.y = y;
		this.setPosition(x, y, 0);
	}
	
	private void updateInternalNodesPositions(double x_offset, double y_offset) {
		for (int i = 0; i < this.size; ++i) {
			this.inputNodes.get(i).setPosition(this.x, this.y + (y_offset * i), 0);
			this.outputNodes.get(i).setPosition(this.x + x_offset, this.y + (y_offset * i), 0);
		}
	}
	
	public void setSwitchDimension(int length, int height) {
		this.width = length;
		this.height = height;
		this.unitSize = this.height / ((6 * this.size) - 1); // unit used to construct internal nodes
	}
	
//	@Override
//	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
//		if (!Global.isGuiMode) {
//			return;
//		}
//		Color backupColor = g.getColor();
//		drawingSizeInPixels = (int) (defaultDrawingSizeInPixels* pt.getZoomFactor()); // half the side-length in pixels of the square
//		int widthInPixels = this.width * drawingSizeInPixels;
//		int heightInPixels = this.height * drawingSizeInPixels;
//		pt.translateToGUIPosition(this.getPosition());
//		int x = pt.guiX - (this.width >> 1);
//		int y = pt.guiY - (this.height >> 1);
//		Color color = getColor();
//		if(highlight) {
//			// a highlighted node is surrounded by a red square
//			g.setColor(color == Color.RED ? Color.BLACK : Color.RED);
//			g.drawRect(x-2, y-2, widthInPixels+4, heightInPixels+4);
//		}
//		g.setColor(Color.BLACK);
//		g.fillRect(x, y, widthInPixels, heightInPixels);
//		g.setColor(backupColor);
//	}
	
	@Override
	protected void nodePositionUpdated() {
	}
}