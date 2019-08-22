package projects.displaynet.nodeImplementations;

import java.util.PriorityQueue;

import projects.displaynet.messages.RoutingMessage;
import projects.displaynet.tableEntry.Request;
import sinalgo.nodes.messages.Message;

/**
 * SplayNetNode
 */
public class SplayNetNode extends SynchronizerNode {

    public enum States {
        S0, // Passive
        S1, // Waiting
        S2, // Climbing
        S3, // Communicating
    }

    // keep current state of the node
    public States state;
    
    
	// keeps track of every splay a node participate as source or destination. once
	// a splay is completed, it is assigned null value
    public Request activeSplay;

    PriorityQueue<Request> buffer = new PriorityQueue<>();

    @Override
    public void init() {
        super.init();

        this.state = States.S0;
        this.activeSplay = null;
    }

    public void generateSplay(int src_splay, int dst_splay, double priority) {
		Request splay = new Request(src_splay, dst_splay, priority);
		this.activeSplay = splay;
	}

    @Override
    public void receiveMsg(Message msg) {

    }

    @Override
    public boolean snoopingMessage(RoutingMessage msg) {
        return true;
	}

    // public abstract void splayCompleted();

}