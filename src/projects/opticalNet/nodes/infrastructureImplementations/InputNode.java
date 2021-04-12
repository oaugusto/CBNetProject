package projects.opticalNet.nodes.infrastructureImplementations;

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

//	@Override
//	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
//		String text = "" + this.index;
//	    super.drawNodeAsSquareWithText(g, pt, highlight, text, 12, Color.BLACK);
//	}

	private int index = -1;
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
    
  	public void connectToNode(Node node) {
  		this.connectedNode = node;
  	}
  	
  	public Node getConnectedNode() {
  		return this.connectedNode;
  	}

    private boolean isConnectedTo(Node node) {
        return this.outgoingConnections.contains(this, node);
    }
  	
    private void addLinkTo(Node node) {
        if (node != null) {
          this.outgoingConnections.add(this, node, false);
        }
    }

    /**
     * Remove a link to a node that is not null.
     *
     * @param node
     */
    private void removeLinkTo(Node node) {
    	if (node == null) {
    		return;
    	}
    	if (this.isConnectedTo(node)) {
    		this.outgoingConnections.remove(this, node);
    	} else {
    		Tools.fatalError("Trying to remove a non-existing connection to node " + node.ID);
    	}
    }
    
    /**
     * Set the link to outputNode
     *
     * @param node
     */
    public void addLinkToOutputNode(OutputNode node) {
    	// set outputNode
    	this.setOutputNode(node);
    	this.addLinkTo(node);
    }
    
    /**
     * Update the link to a new outputNode
     *
     * @param node
     */
    public void updateLinkToOutputNode(OutputNode node) {
		// remove the previous connection
    	this.removeLinkTo(this.outputNode);
    	// set the new outputNode
    	this.setOutputNode(node);
    	this.addLinkTo(node);
    }
  	
	protected void sendToOutputNode(Message msg) {
		if (this.isConnectedTo(this.outputNode)) {
			send(msg, this.outputNode);
		} else {
			Tools.fatalError("Trying to send message through a non-existing connection with outputNode");
	    }
	}
  
	@Override
	public void handleMessages(Inbox inbox) {
		while (inbox.hasNext()) {
			Message msg = inbox.next();
			this.sendToOutputNode(msg);
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