package projects.opticalNet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import projects.opticalNet.nodes.messages.NetworkMessage;
import projects.opticalNet.nodes.infrastructureImplementations.InputNode;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.gui.transformation.PositionTransformation;

/**
 * InputNode
 */
public class NetworkNode extends SynchronizerLayer {
	private ArrayList<InputNode> interfaces = new ArrayList<>();
	
	private InputNode parent = null;
	private InputNode leftChild = null;
	private InputNode rightChild = null;
	
	private int minIdInSubtree = 0;
	private int maxIdInSubtree = 0;
	
	public void connectToInputNode(InputNode node) {
		this.interfaces.add(node);
		this.addConnectionTo(node);
	}
	
	public void setParent(InputNode node) {
		this.parent = node;
	}
	
	public InputNode getParent() {
		return this.parent;
	}
	
	public void setLeftChild(InputNode node) {
		this.leftChild = node;
	}
	
	public InputNode getLeftChild() {
		return this.leftChild;
	}
	
	public void setRightChild(InputNode node) {
		this.rightChild = node;
	}
	
	public void setMinIdInSubtree(int value) {
		this.minIdInSubtree = value;
	}
	
	public int getMinIdInSubtree() {
		return this.minIdInSubtree;
	}

	public void setMaxIdInSubtree(int value) {
		this.maxIdInSubtree = value;
	}
	
	public int getMaxIdInSubtree() {
		return this.maxIdInSubtree;
	}
	
	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public void handleMessages(Inbox inbox) {
		while (inbox.hasNext()) {
			Message msg = inbox.next();
	        if (!(msg instanceof NetworkMessage)) {
	        	continue;
	        }
	        NetworkMessage cbmsg = (NetworkMessage) msg;
	        if (cbmsg.getDst() == this.ID) {
	        	System.out.println("Message received from node " + cbmsg.getSrc());
	        	continue;
	        }
	        // forward message in the network
	        if (this.minIdInSubtree <= cbmsg.getDst() && cbmsg.getDst() < this.ID) {
	        	this.send(cbmsg, this.leftChild);
	        } else if (this.ID < cbmsg.getDst() && cbmsg.getDst() <= this.maxIdInSubtree) {
	        	this.send(cbmsg, this.rightChild);
	        } else {
	        	this.send(cbmsg, this.parent);
	        }
		}
	}
		
	@Override
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		String text = "" + ID;
	    // draw the node as a circle with the text inside
	    super.drawNodeAsDiskWithText(g, pt, highlight, text, 12, Color.YELLOW);
	}
}