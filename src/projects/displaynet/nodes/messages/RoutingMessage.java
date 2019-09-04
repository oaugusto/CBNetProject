package projects.displaynet.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * RoutingMessage
 */
public class RoutingMessage extends Message {

    private int source;
    private int destination;
    private Message payload;

    public RoutingMessage() {
        this.source = Integer.MIN_VALUE;
        this.destination = Integer.MIN_VALUE;
        this.payload = null;
    }

    public RoutingMessage(int source, int destination) {
        this.source = source;
        this.destination = destination;
        this.payload = null;
    }

    public RoutingMessage(int source, int destination, Message payload) {
        this.source = source;
        this.destination = destination;
        this.payload = payload;
    }

    public int getSource() {
        return this.source;
    }

    public int getDestination() {
        return this.destination;
    }

    public Message getPayLoad() {
        return this.payload;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public void setDestination(int destination) {
        this.destination = destination;
    }

    public void setPayLoad(Message payload) {
        this.payload = payload;
    }

    @Override
    public Message clone() {
        return this;
    }

}