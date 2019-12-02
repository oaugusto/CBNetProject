package projects.flatnet.nodes.messages.controlMessage;

/**
 * RequestCluster
 */
public class RequestClusterBottomUpMessage extends RequestClusterMessage {

    public RequestClusterBottomUpMessage(RequestClusterMessage m) {
        super(m);
    }

    public RequestClusterBottomUpMessage(int currentNode, int src, int dst, int position, double priority) {
        super(currentNode, src, dst, position, priority);
    }

}