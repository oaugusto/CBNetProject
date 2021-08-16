package projects.opticalNet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import projects.opticalNet.nodes.OPTNet.Controller;
import projects.opticalNet.nodes.infrastructureImplementations.NetworkSwitch;
import projects.opticalNet.nodes.infrastructureImplementations.treeStructure.Tree;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.messages.Inbox;

public class NetworkController extends SynchronizerLayer {

	private Tree treeStructure;
	private ArrayList<NetworkSwitch> switches;
    
    private ArrayList<NetworkSwitch> _switches;
    private ArrayList<NetworkNode> tree;
    
    private Controller controller;
    /* End of Attributes */
	
	@Override
	public void init() {
		super.init();
		
		ArrayList<Integer> edgeList = new ArrayList<Integer>();
		for (int i = 1; i < 12; i++)
		{
			edgeList.add(i);
		}
		edgeList.add(-1);
		this.controller = new Controller(12, 12, edgeList);
	}
	
	@Override
	public void nodeStep() {
		
	}
	
	@Override
	public void handleMessages(Inbox inbox) {
		
		
	}

	@Override
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		String text = "" + ID;
	    // draw the node as a circle with the text inside
	    super.drawNodeAsDiskWithText(g, pt, highlight, text, 12, Color.YELLOW);
	}
}
