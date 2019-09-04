package projects.cbnet.nodes.messages.controlMessage;

import projects.displaynet.nodes.messages.controlMessages.RequestClusterMessage;

public class RequestClusterDownMessage extends RequestClusterMessage {

    public RequestClusterDownMessage(RequestClusterMessage m) {
        super(m);
    }

    public RequestClusterDownMessage(int src, int dst, int position, double priority) {
        super(src, dst, position, priority);
    }
    
}