package projects.cbnetAdapt.nodes.messages.controlMessage;

public class RequestClusterDownMessage extends RequestClusterMessage {

    public RequestClusterDownMessage(RequestClusterMessage m) {
        super(m);
    }

    public RequestClusterDownMessage(int currentNode, int src, int dst, int position, double priority) {
        super(currentNode, src, dst, position, priority);
    }
    
}