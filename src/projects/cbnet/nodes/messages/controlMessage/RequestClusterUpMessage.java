package projects.cbnet.nodes.messages.controlMessage;

import projects.displaynet.nodes.messages.controlMessages.RequestClusterMessage;

/**
 * RequestCluster
 */
public class RequestClusterUpMessage extends RequestClusterMessage {

    public RequestClusterUpMessage(RequestClusterMessage m) {
        super(m);
    }

    public RequestClusterUpMessage(int src, int dst, int position, double priority) {
        super(src, dst, position, priority);
    }

}