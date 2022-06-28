package projects.randomdisplaynetAdapt.nodes.messages.controlMessage;

/**
 * RequestCluster
 */
public class RequestClusterUpMessage extends RequestClusterMessage {

    public RequestClusterUpMessage(RequestClusterMessage m) {
        super(m);
    }

    public RequestClusterUpMessage(int currentNode, int src, int dst, int position, double priority) {
        super(currentNode, src, dst, position, priority);
    }

}