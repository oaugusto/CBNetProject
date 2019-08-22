package projects.displaynet.messages.controlMessages;

import sinalgo.nodes.messages.Message;

/**
 * RequestCluster
 */
public class RequestCluster extends Message {

    public int timeout;

    @Override
    public Message clone() {
        return this;
    }

    
}