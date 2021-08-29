package projects.opticalNet.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * CompletionMessage
 */
public class CompletionMessage extends Message {

    private int src;
    private int dst;

    private NetworkMessage optnetMessage;

    public CompletionMessage (NetworkMessage msg) {
        this.optnetMessage = msg;
    }

    /**
     * @return the src
     */
    public int getSrc () {
        return src;
    }

    /**
     * @return the dst
     */
    public int getDst () {
        return dst;
    }

    /**
     * @return the optnetMessage
     */
    public NetworkMessage getOptnetMessage () {
        return optnetMessage;
    }

    /**
     * @param src the src to set
     */
    public void setSrc (int src) {
        this.src = src;
    }

    /**
     * @param dst the dst to set
     */
    public void setDst (int dst) {
        this.dst = dst;
    }

    /**
     * @param optnetMessage the optnetMessage to set
     */
    public void setOptnetMessage (NetworkMessage optnetMessage) {
        this.optnetMessage = optnetMessage;
    }

    @Override
    public Message clone () {
        return this;
    }


}
