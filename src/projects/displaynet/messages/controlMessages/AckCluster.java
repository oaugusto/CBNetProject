package projects.displaynet.messages.controlMessages;

import sinalgo.nodes.messages.Message;

/**
 * AckCluster
 */
public class AckCluster extends Message {

    public int src;
    public int dst;
    public double priority;

    public AckCluster(int src, int dst, double priority) {
        this.src = src;
        this.dst = dst;
        this.priority = priority;
    }

    @Override
    public Message clone() {
        return this;
    }

    
}