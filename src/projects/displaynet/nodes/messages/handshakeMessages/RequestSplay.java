package projects.displaynet.nodes.messages.handshakeMessages;

import sinalgo.nodes.messages.Message;

/**
 * RequestSplay
 */
public class RequestSplay extends Message {

    public int src;
	public int dst;
	public double priority;
	
	public RequestSplay(int src, int dst, double priority) {
		this.src = src;
		this.dst = dst;
		this.priority = priority;
	}
	
	@Override
	public Message clone() {
		return this;
	}
	
}