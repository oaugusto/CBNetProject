package projects.displaynet.nodeImplementations;

import projects.displaynet.messages.RoutingMessage;
import projects.displaynet.messages.controlMessages.AckCluster;
import projects.displaynet.messages.controlMessages.RequestCluster;
import projects.displaynet.tableEntry.Request;
import sinalgo.nodes.messages.Message;

/**
 * SplayNetNode
 */
public class SplayNetNode extends SynchronizerNode {

    public enum States {
        PASSIVE, 
        WAITING, 
        CLIMBING
    }

    // keep current state of the node
    public States state;
    
	// keeps track of every splay a node participate as source or destination. once
	// a splay is completed, it is assigned null value
    public Request activeSplay;

    private Request currentCluster;

    private boolean clusterRequestSent;
    private boolean clusterAckReceived;
    private boolean clusterRequestReceived;

    @Override
    public void init() {
        super.init();

        this.state = States.PASSIVE;
        this.activeSplay = null;
        this.clusterRequestSent = false;
        this.clusterAckReceived = false;
        this.clusterRequestReceived = false;
        this.currentCluster = null;
    }

    public void newSplay(int src_splay, int dst_splay, double priority) {
		Request splay = new Request(src_splay, dst_splay, priority);
		this.activeSplay = splay;
    }
    
    private void sendRequestCluster() {
        RequestCluster rcluster = new RequestCluster(this.activeSplay.srcId, 
                            this.activeSplay.dstId, 2, this.activeSplay.priority);
        this.currentCluster = new Request(this.activeSplay.srcId, 
                            this.activeSplay.dstId, this.activeSplay.priority);
        this.sendToParent(rcluster); 
    }

    @Override
    public boolean snoopingMessage(Message msg) {
                
        return true;
    }

    @Override
    public void receiveMessage(Message msg) {
        
    }

    @Override
    public void timeslotZero() {

        // switch (this.state) {
        //     case PASSIVE:
        //         if (this.activeSplay != null) {
        //             if (this.isLeastCommonAncestorOf(this.activeSplay.dstId)) {
        //                 this.state = States.WAITING;
        //             } else {
        //                 this.state = States.CLIMBING;
        //                 //send cluster request
        //                 this.sendRequestCluster();
        //                 this.clusterRequestSent = true;
        //             }
        //         }
        //         break;
                
        //     case WAITING:
        //         if (!this.isLeastCommonAncestorOf(this.activeSplay.dstId)) {
        //             this.state = States.CLIMBING;
        //             //send cluster request
        //             this.sendRequestCluster();
        //             this.clusterRequestSent = true;
        //         }
        //         break;
                
        //     case CLIMBING:
        //         if (this.isLeastCommonAncestorOf(this.activeSplay.dstId)) {
        //             this.state = States.WAITING;
        //         } else {
        //             //send cluster request
        //             this.sendRequestCluster();
        //             this.clusterRequestSent = true;
        //         }
        //         break;
                
        //     default:
        //         break;
        // }
    }

    @Override
    public void timeslotThree() {
       
    }

    @Override
    public void timeslotSix() {
      
    }

    @Override
    public void timeslotSeven() {
        
    }

    public void splayCompleted() {
        System.out.println("Completed node " + ID);
    }

}