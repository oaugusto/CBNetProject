package projects.opticalNet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;

import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;

/**
 * InputNode
 */
public class OutputNode extends Node {

	@Override
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		String text = "O: " + this.index;
		// draw the node as a circle with the text inside
	    super.drawNodeAsDiskWithText(g, pt, highlight, text, 12, Color.BLUE);
	 }

	private int index = 0;
  	private Node connectedNode = null;
  	private InputNode inputNode = null;
  	
  	public void setIndex(int index) {
  		this.index = index;
  	}
  	
  	public int getIndex() {
  		return this.index;
  	}
  
    private void addLinkTo(Node node) {
        if (node != null) {
          this.outgoingConnections.add(this, node, false);
          node.outgoingConnections.add(node, this, false);
        }
    }
  	
  	public void setConnectedNode(Node node) {
  		this.connectedNode = node;
  		this.addLinkTo(node);
  	}
  	
  	public Node getConnectedNode() {
  		return this.connectedNode;
  	}
  	
  	public void setInputNode(InputNode node) {
  		this.inputNode = node;
  	}
  	
  	public InputNode getInputNode() {
  		return this.inputNode;
  	}
  	
	protected void sendToConnectedNode(Message msg) {
		if (this.outgoingConnections.contains(this, this.connectedNode)) {
			send(msg, this.connectedNode);
		} else {
			Tools.fatalError("Trying to send message through a non-existing connection with connectedNode");
	    }
	}
  	
	@Override
	public void handleMessages(Inbox inbox) {
		while (inbox.hasNext()) {
			Message msg = inbox.next();
			this.sendToConnectedNode(msg);
		}
	}
	
	@Override
	public void init() { }
	
	@Override
	public void preStep() { }
	
	@Override
	public void neighborhoodChange() { }
	
	@Override
	public void postStep() { }
	
	@Override
	public void checkRequirements() throws WrongConfigurationException { }
}