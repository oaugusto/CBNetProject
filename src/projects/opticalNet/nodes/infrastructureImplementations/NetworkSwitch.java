package projects.opticalNet.nodes.infrastructureImplementations;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
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
	
	// switch dimensions
	private int width = 0;
	private int height = 0;
	
	// small unit
	private int unitSize = 0;
	private int internalNodeSize = 0;
	
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
	
	private void updateInternalNodesPositions() {
		double xCof = this.getPosition().xCoord - (this.width/2) + (this.internalNodeSize/2);
		double yCof = this.getPosition().yCoord - (this.height/2) + (this.internalNodeSize/2);
		for (int i = 0; i < this.size; ++i) {
			this.inputNodes.get(i).setPosition(xCof, yCof + (6 * this.unitSize * i) ,0);
		}
		
		xCof = this.getPosition().xCoord + (this.width/2.0) - (this.internalNodeSize/2.0);
		yCof = this.getPosition().yCoord - (this.height/2.0) + (this.internalNodeSize/2.0);
		for (int i = 0; i < this.size; ++i) {
			this.outputNodes.get(i).setPosition(xCof, yCof + (6 * this.unitSize * i) ,0);
		}
	}
	
	public void setSwitchDimension(int height) {
		this.height = height;
		this.unitSize = this.height / ((6 * this.size) - 1); // unit used to construct internal nodes
		this.internalNodeSize = 5 * this.unitSize;
		this.width = this.internalNodeSize * 4;
		
		for (int i = 0; i < this.size; ++i) {
			this.inputNodes.get(i).setDefaultDrawingSizeInPixels(this.internalNodeSize);
			this.outputNodes.get(i).setDefaultDrawingSizeInPixels(this.internalNodeSize);
		}
		this.updateInternalNodesPositions();
	}
	
	@Override
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		if (!Global.isGuiMode) {
			return;
		}		
		int widthInPixels = (int) (this.width * pt.getZoomFactor());
		int heightInPixels = (int) (this.height * pt.getZoomFactor());
		pt.translateToGUIPosition(this.getPosition());
		int x = pt.guiX - (widthInPixels >> 1);
		int y = pt.guiY - (heightInPixels >> 1);
		Color backupColor = g.getColor();
		Color color = getColor();
		if(highlight) {
			// a highlighted node is surrounded by a red square
			g.setColor(color == Color.RED ? Color.BLACK : Color.RED);
			g.drawRect(x-2, y-2, widthInPixels+4, heightInPixels+4);
		}
		g.setColor(Color.BLACK);
		g.drawRect(x, y, widthInPixels, heightInPixels);
		g.setColor(backupColor);

		int internalNodeSize = (int)(this.internalNodeSize * pt.getZoomFactor());
		// Set the font 
		String text_in = "In";
		int fontSize = (int) (internalNodeSize * 0.5);
		Font font = new Font(null, 0, (int) (fontSize)); 
	    g.setFont(font);
		// Determine the height and width of the text to be written
		FontMetrics fm = g.getFontMetrics(font); 
		int h = (int) Math.ceil(fm.getHeight());
		int w = (int) Math.ceil(fm.stringWidth(text_in));
		g.setColor(Color.BLACK);
		g.drawRect(x, y - internalNodeSize, internalNodeSize, internalNodeSize);
		g.setColor(Color.BLACK);
		g.drawString(text_in, x + internalNodeSize/2 - w/2, y - internalNodeSize/2 + h/2 - 2); // print the text onto the circle
		g.setColor(backupColor); // restore color
		
		String text_out = "Out";
	    g.setFont(font);
		// Determine the height and width of the text to be written
		fm = g.getFontMetrics(font); 
		h = (int) Math.ceil(fm.getHeight());
		w = (int) Math.ceil(fm.stringWidth(text_out));
		g.setColor(Color.BLACK);
		g.drawRect(x + widthInPixels - internalNodeSize, y - internalNodeSize, internalNodeSize, internalNodeSize);
		g.setColor(Color.BLACK);
		g.drawString(text_out, x - internalNodeSize/2 - w/2 + widthInPixels, y - internalNodeSize/2 + h/2 - 2); // print the text onto the circle
		g.setColor(backupColor); // restore color
	}
	
	@Override
	protected void nodePositionUpdated() {
		this.updateInternalNodesPositions();
	}
}