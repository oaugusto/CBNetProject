package projects.cbnet.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * CBNetMessage
 */
public class CBNetMessage extends Message {

    private int src;
    private int dst;
    private double priority;

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

    
}