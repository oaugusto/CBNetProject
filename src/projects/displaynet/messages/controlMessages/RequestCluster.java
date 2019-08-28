package projects.displaynet.messages.controlMessages;

import sinalgo.nodes.messages.Message;

/**
 * RequestCluster
 */
public class RequestCluster extends Message {

    public int timeout;
    public int src;
    public int dst;
    public double priority;

    public RequestCluster(int src, int dst, int timeout, double priority) {
        this.src = src;
        this.dst = dst;
        this.timeout = timeout;
        this.priority = priority;
    }

    @Override
    public Message clone() {
        return this;
    }
    
}