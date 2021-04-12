package projects.opticalNet.nodes.infrastructureImplementations;

import java.awt.Color;
import java.awt.Graphics;

import sinalgo.nodes.Node;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.configuration.WrongConfigurationException;
import sinalgo.gui.transformation.PositionTransformation;

/**
 * InputNode
 */
public class OutputNode extends Node {

//	@Override
//	public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
//		String text = "" + this.index;
//	    super.drawNodeAsSquareWithText(g, pt, highlight, text, 12, Color.BLACK);
//	}

	private int index = -1;
  	private Node connectedNode = null;
  	
  	public void setIndex(int index) {
  		this.index = index;
  	}
  	
  	public int getIndex() {
  		return this.index;
  	}
  	
  	public void connectToNode(Node node) {
  		this.connectedNode = node;
  	}
  	
  	public Node getConnectedNode() {
  		return this.connectedNode;
  	}
  	
	protected void sendToConnectedNode(Message msg) {
		sendDirect(msg, this.connectedNode);
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