package projects.displaynet.nodes.messages;

import projects.displaynet.nodes.tableEntry.Request;
import sinalgo.nodes.messages.Message;

/**
 * CompletionMessage
 */
public class CompletionMessage extends Message {

    private Request request;

    public CompletionMessage() {

    }

    public CompletionMessage(Request request) {
        this.request = request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public Request getRequest() {
        return request;
    }

    @Override
    public Message clone() {
        return this;
    }

    
}