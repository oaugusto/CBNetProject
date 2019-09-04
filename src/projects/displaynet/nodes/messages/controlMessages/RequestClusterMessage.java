package projects.displaynet.nodes.messages.controlMessages;

import sinalgo.nodes.messages.Message;

/**
 * RequestCluster
 */
public class RequestClusterMessage extends Message implements Comparable<RequestClusterMessage> {

    private int src;
    private int dst;
    private double priority;

    private int position;
    private boolean isFinalNode; // keep track if this node is final node in current request
    
    public RequestClusterMessage(RequestClusterMessage msg) {
        this.src = msg.getSrc();
        this.dst = msg.getDst();
        this.priority = msg.getPriority();
        this.position = msg.getPosition();
        this.isFinalNode = false;
    }

    public RequestClusterMessage(int src, int dst, int position, double priority) {
        this.src = src;
        this.dst = dst;
        this.priority = priority;
        this.position = position;
        this.isFinalNode = false;
    }

    public int getSrc() {
        return src;
    }

    public int getDst() {
        return dst;
    }

    public double getPriority() {
        return priority;
    }

    public int getPosition() {
        return position;
    }

    public boolean isFinalNode() {
        return isFinalNode;
    }

    public void setSrc(int src) {
        this.src = src;
    }

    public void setDst(int dst) {
        this.dst = dst;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void shiftPosition() {
        this.position++;
    }

    public void setFinalNode() {
        this.isFinalNode = true;
    }

    @Override
    public Message clone() {
        return this;
    }

    @Override
	public int compareTo(RequestClusterMessage o) {
		int value = Double.compare(this.priority, o.priority);
		if (value == 0) { // In case tie, compare the id of the source node
			return this.dst - o.dst;
		} else {
			return value;
		}
	}
    
}