package projects.randomdisplaynet.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * CompletionMessage
 */
public class CompletionMessage extends Message {
 
    private int src;
    private int dst;

    private CBNetMessage cbnetMessage;

    public CompletionMessage(CBNetMessage msg) {
        this.cbnetMessage = msg;
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
     * @return the cbnetMessage
     */
    public CBNetMessage getCbnetMessage() {
        return cbnetMessage;
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
     * @param cbnetMessage the cbnetMessage to set
     */
    public void setCbnetMessage(CBNetMessage cbnetMessage) {
        this.cbnetMessage = cbnetMessage;
    }

    @Override
    public Message clone() {
        return this;
    }

    
}