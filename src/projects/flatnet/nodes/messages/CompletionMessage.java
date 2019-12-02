package projects.flatnet.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * CompletionMessage
 */
public class CompletionMessage extends Message {
 
    private int src;
    private int dst;

    private FlatNetMessage flatNetMessage;

    public CompletionMessage(FlatNetMessage msg) {
        this.flatNetMessage = msg;
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
     * @return the flatNetMessage
     */
    public FlatNetMessage getFlatNetMessage() {
        return flatNetMessage;
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
     * @param flatNetMessage the flatNetMessage to set
     */
    public void setFlatNetMessage(FlatNetMessage flatNetMessage) {
        this.flatNetMessage = flatNetMessage;
    }

    @Override
    public Message clone() {
        return this;
    }

    
}