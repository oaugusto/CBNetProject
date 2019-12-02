package projects.flatnet.nodes.messages.controlMessage;

public class RequestClusterTopDownMessage extends RequestClusterMessage {

    public RequestClusterTopDownMessage(RequestClusterMessage m) {
        super(m);
    }

    public RequestClusterTopDownMessage(int currentNode, int src, int dst, int position, double priority) {
        super(currentNode, src, dst, position, priority);
    }
    
}