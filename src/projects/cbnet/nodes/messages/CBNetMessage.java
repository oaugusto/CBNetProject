package projects.cbnet.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * CBNetMessage
 */
public class CBNetMessage extends Message implements Comparable<CBNetMessage> {

    private int src;
    private int dst;
    private double priority;

    public CBNetMessage(int src, int dst, double priority) {
        this.src = src;
        this.dst = dst;
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

    @Override
    public Message clone() {
        return this;
    }

    @Override
    public int compareTo(CBNetMessage o) {
            int value = Double.compare(this.priority, o.priority);
            if (value == 0) { // In case tie, compare the id of the source node
                return this.dst - o.dst;
            } else {
                return value;
            }
        }

    
}