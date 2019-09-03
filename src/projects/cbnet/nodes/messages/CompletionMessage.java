package projects.cbnet.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * CompletionMessage
 */
public class CompletionMessage extends Message {
 
    private int src;
    private int dst;

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

    @Override
    public Message clone() {
        return this;
    }

    
}