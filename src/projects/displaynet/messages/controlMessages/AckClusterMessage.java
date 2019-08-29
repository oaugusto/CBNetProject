package projects.displaynet.messages.controlMessages;

import projects.displaynet.tableEntry.NodeInfo;
import sinalgo.nodes.messages.Message;

/**
 * AckCluster
 */
public class AckClusterMessage extends Message {

    private int src;
    private int dst;
    private double priority;
    private NodeInfo info;

    private boolean finalNode;

    public AckClusterMessage(int src, int dst, double priority, NodeInfo info) {
        this.src = src;
        this.dst = dst;
        this.priority = priority;
        this.info = info;
    }

    /**
     * @param finalNode the finalNode to set
     */
    public void setFinalNode() {
        this.finalNode = true;
    }

    /**
     * @return the finalNode
     */
    public boolean isFinalNode() {
        return finalNode;
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
     * @return the info
     */
    public NodeInfo getInfo() {
        return info;
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

    /**
     * @param info the info to set
     */
    public void setInfo(NodeInfo info) {
        this.info = info;
    }

    @Override
    public Message clone() {
        return this;
    }

}