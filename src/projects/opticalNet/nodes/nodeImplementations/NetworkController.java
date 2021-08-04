package projects.opticalNet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import projects.opticalNet.nodes.infrastructureImplementations.NetworkSwitch;
import projects.opticalNet.nodes.infrastructureImplementations.treeStructure.Tree;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.messages.Inbox;

public class NetworkController extends SynchronizerLayer {

	private Tree treeStructure;
	private ArrayList<NetworkSwitch> switches;
    
    private ArrayList<NetworkSwitch> _switches;
    private ArrayList<NetworkNode> tree;
    /* End of Attributes */
	
	@Override
	public void init() {
		super.init();
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
