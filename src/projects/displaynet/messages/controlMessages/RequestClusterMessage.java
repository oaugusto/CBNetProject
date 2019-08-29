package projects.displaynet.messages.controlMessages;

import sinalgo.nodes.messages.Message;

/**
 * RequestCluster
 */
public class RequestClusterMessage extends Message implements Comparable<RequestClusterMessage> {

    private int src;
    private int dst;
    private double priority;
    
    public int timeout;

    public RequestClusterMessage(RequestClusterMessage msg) {
        this.src = msg.getSrc();
        this.dst = msg.getDst();
        this.priority = msg.getPriority();
        this.timeout = msg.getTimeout();
    }

    public RequestClusterMessage(int src, int dst, int timeout, double priority) {
        this.src = src;
        this.dst = dst;
        this.timeout = timeout;
        this.priority = priority;
    }

    /**
     * @return the src
     */
    public int getSrc() {
        return src;
    }

    /**
     * @return the dst
     */
    public int getDst() {
        return dst;
    }

    /**
     * @return the priority
     */
    public double getPriority() {
        return priority;
    }

    /**
     * @param src the src to set
     */
    public void setSrc(int src) {
        this.src = src;
    }

    /**
     * @param dst the dst to set
     */
    public void setDst(int dst) {
        this.dst = dst;
    }

    /**
     * @param priority the priority to set
     */
    public void setPriority(double priority) {
        this.priority = priority;
    }

    public int getTimeout() {
        return this.timeout;
    }

    public void setTimeout(int t) {
        this.timeout = t;
    }

    @Override
    public Message clone() {
        return this;
    }

    @Override
	public int compareTo(RequestClusterMessage o) {
		int value = Double.compare(this.priority, o.priority);
		if (value == 0) { // In case tie, compare the id of the source node
			return this.src - o.src;
		} else {
			return value;
		}
	}
    
}