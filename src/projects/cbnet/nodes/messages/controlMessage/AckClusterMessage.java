package projects.cbnet.nodes.messages.controlMessage;

import projects.cbnet.nodes.tableEntry.CBInfo;
import sinalgo.nodes.messages.Message;

/**
 * AckCluster
 */
public class AckClusterMessage extends Message {
    
    private int src;
    private int dst;
    private double priority;
    private CBInfo info;

    private int position;
    private boolean isFinalNode; // keep track if this node is final node in current request

    public AckClusterMessage(int src, int dst, double priority, int position, CBInfo info) {
        this.src = src;
        this.dst = dst;
        this.priority = priority;
        this.info = info;
        this.position = position;
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

    public CBInfo getInfo() {
        return info;
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

    public void setInfo(CBInfo info) {
        this.info = info;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public void setFinalNode() {
        this.isFinalNode = true;
    }

    @Override
    public Message clone() {
        return this;
    }

}