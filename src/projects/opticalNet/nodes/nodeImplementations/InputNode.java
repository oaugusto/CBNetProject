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
public class InputNode extends Node {

	@Override
	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
		String text = "I: " + this.index;	
	    // draw the node as a circle with the text inside
	    super.drawNodeAsDiskWithText(g, pt, highlight, text, 12, Color.BLACK);
	}

	private int index = 0;
  	private Node connectedNode = null;
  	private OutputNode outputNode = null;
  	  	
  	public void setIndex(int index) {
  		this.index = index;
  	}
  	
  	public int getIndex() {
  		return this.index;
  	}
  	
  	public void setOutputNode(OutputNode node) {
  		this.outputNode = node;
  	}
  	
  	public OutputNode getOutputNode() {
  		return this.outputNode;
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
    
    /**
     * Set the link to outputNode
     *
     * @param node
     */
    public void addLinkToOutputNode(OutputNode node) {
		this.addLinkTo(node);
		this.setOutputNode(node);
		
		if (node != null) {
			node.setInputNode(this);
		}
    }
  	
	protected void sendToOutput(Message msg) {
		if (this.outgoingConnections.contains(this, this.outputNode)) {
			send(msg, this.outputNode);
		} else {
			Tools.fatalError("Trying to send message through a non-existing connection with outputNode");
	    }
	}
  
	@Override
	public void handleMessages(Inbox inbox) {
		while (inbox.hasNext()) {
			Message msg = inbox.next();
			this.sendToOutput(msg);
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