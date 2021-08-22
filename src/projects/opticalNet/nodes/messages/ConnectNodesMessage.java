package projects.opticalNet.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * CompletionMessage
 */
public class ConnectNodesMessage extends Message {
 
	private int from;
    private int to;


    public ConnectNodesMessage(int to, int from) {
    }

    public int getFrom() {
    	return this.from;
    }
    
    public int getTo() {
        return this.to;
    }
    
    public void setFrom(int from) {
    	this.from = from;
    }

    public void setTo(int to) {
        this.to = to;
    }

    @Override
    public Message clone() {
        return this;
    }

    
}