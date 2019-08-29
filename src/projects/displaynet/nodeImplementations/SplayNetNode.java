package projects.displaynet.nodeImplementations;

import java.util.PriorityQueue;

import projects.displaynet.tableEntry.Request;

/**
 * SplayNetNode
 */
public class SplayNetNode extends RotationLayer {

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
    public void updateState() {
        super.updateState();

        if (ID == 7) {
            this.setMyClusterRequest(ID, 30, 1.5);
            this.sendRequestCluster();
        }

        // if (ID == 2) {
        //     this.setMyClusterRequest(ID, 30, 1.0);
        //     this.sendRequestCluster();
        // }

        // if (ID == 7) {
        //     this.setMyClusterRequest(ID, 30, 1.0);
        // }
        /*
         * Update current state in time slot 0
         */
        // switch (this.state) {
        // case PASSIVE:
        //     if (this.newRequest) {

        //         this.newRequest = false;

        //         // if checkcompletion
        //         // go to communicating
        //         // else :

        //         if (!this.isLeastCommonAncestorOf(this.activeSplay.dstId)) {
        //             this.state = States.ACTIVE;
        //             // request cluster
        //         }
        //     }
        //     break;

        // case ACTIVE:
        //     if (!this.isLeastCommonAncestorOf(this.activeSplay.dstId)) {
        //         // send cluster request
        //         // this.sendRequestCluster();
        //     }
        //     break;

        // case COMMUNICATING:

        //     break;

        // default:
        //     break;
        // }
    }


    public void splayCompleted() {
        System.out.println("Completed node " + ID);
    }

}