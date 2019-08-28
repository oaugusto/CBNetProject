package projects.displaynet.nodeImplementations;

import java.util.PriorityQueue;

import projects.displaynet.tableEntry.Request;
import sinalgo.nodes.messages.Message;

/**
 * SplayNetNode
 */
public class SplayNetNode extends SynchronizerLayer {

    public enum States {
        PASSIVE, ACTIVE, COMMUNICATING
    }

    // keep current state of the node
    public States state;

    // keeps track of every splay a node participate as source or destination. once
    // a splay is completed, it is assigned null value
    public boolean newRequest;
    public Request activeSplay;

    public PriorityQueue<Request> requestClusterBuffer;

    @Override
    public void init() {
        super.init();

        this.state = States.PASSIVE;
        this.newRequest = false;
        this.activeSplay = null;
    }

    public void newSplay(int src_splay, int dst_splay, double priority) {
        this.newRequest = true;
        Request splay = new Request(src_splay, dst_splay, priority);
        this.activeSplay = splay;
    }

    @Override
    public boolean snoopingMessage(Message msg) {
        return true;
    }

    @Override
    public void receiveMessage(Message msg) {
        super.receiveMessage(msg);
    }

    @Override
    public void timeslot0() {
        /*
         * Update current state in time slot 0
         */
        switch (this.state) {
        case PASSIVE:
            if (this.newRequest) {

                this.newRequest = false;

                // if checkcompletion
                // go to communicating
                // else :

                if (!this.isLeastCommonAncestorOf(this.activeSplay.dstId)) {
                    this.state = States.ACTIVE;
                    // request cluster
                }
            }
            break;

        case ACTIVE:
            if (!this.isLeastCommonAncestorOf(this.activeSplay.dstId)) {
                // send cluster request
                // this.sendRequestCluster();
            }
            break;

        case COMMUNICATING:

            break;

        default:
            break;
        }
    }

    @Override
    public void timeslot3() {

    }

    @Override
    public void timeslot6() {

    }

    @Override
    public void timeslot7() {

    }

    public void splayCompleted() {
        System.out.println("Completed node " + ID);
    }

}