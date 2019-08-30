package projects.displaynet.nodeImplementations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import projects.displaynet.messages.controlMessages.AckClusterMessage;
import projects.displaynet.messages.controlMessages.RequestClusterMessage;
import projects.displaynet.tableEntry.NodeInfo;
import sinalgo.nodes.messages.Message;

/**
 * ClusterLayer
 */
public abstract class ClusterLayer extends RPCLayer {

    // set with current splay request of this node
    private RequestClusterMessage clusterRequest;

    // this priority queue store all request cluster message received
    private PriorityQueue<RequestClusterMessage> queueClusterRequest;

    // this queue keeps all acks received due to a request cluster operation
    private Queue<AckClusterMessage> queueAckCluster;

    @Override
    public void init() {
        super.init();

        this.clusterRequest = null;
        this.queueClusterRequest = new PriorityQueue<>();
        this.queueAckCluster = new LinkedList<>();
    }

    /**
     * @return the myClusterRquest
     */
    public RequestClusterMessage getClusterRequest() {
        return clusterRequest;
    }

    /**
     * set the current request operation of this node
     */
    public void setClusterRequest(int src, int dst, double priority) {
        this.clusterRequest = new RequestClusterMessage(src, dst, 3, priority);
    }

    public void clearClusterRequest() {
        this.clusterRequest = null;
    }

    public void clearClusterRequestQueue() {
        this.queueClusterRequest.clear();
    }

    public void clearAckClusterQueue() {
        this.queueAckCluster.clear();
    }

    /**
     * This method send a cluster request and put the request into the buffer of
     * current node. The timout means how many times the message should be routed.
     * 
     * @param src
     * @param dst
     * @param priority
     */
    public void sendRequestCluster() {
        this.queueClusterRequest.add(this.clusterRequest);
        RequestClusterMessage msg = new RequestClusterMessage(this.clusterRequest);
        msg.setTimeout(2);
        this.sendToParent(msg);
    }

    @Override
    public void receiveMessage(Message msg) {
        super.receiveMessage(msg);

        if (msg instanceof RequestClusterMessage) {

            RequestClusterMessage rqMessage = (RequestClusterMessage) msg;
            int timeout = rqMessage.getTimeout();

            /**
             * 0  --------- x
             *    -------- /
             * 1  ------- x [dst]
             *    ------ /  
             * 2  ----  x [LCA]
             *    ---  / 
             * 3  --  x dst 
             * 
             * It is not possible to parent to be the dst because completion is check first
             * and the grandparent being LCA has effect on the type of rotation
             */
            if (timeout == 2 && this.isLeastCommonAncestorOf(rqMessage.getDst())) {

                RequestClusterMessage newRqMessage = new RequestClusterMessage(rqMessage);
                newRqMessage.setTimeout(0);
                this.sendToParent(newRqMessage);

            } else if (timeout == 1 && ID == rqMessage.getDst()) {

                rqMessage.setTimeout(0);

            } else if (timeout != 0) {

                RequestClusterMessage newRqMessage = new RequestClusterMessage(rqMessage);
                newRqMessage.setTimeout(timeout - 1);
                this.sendToParent(newRqMessage);

            }

            //add current request to queue
            this.queueClusterRequest.add(rqMessage);

            return;

        } else if (msg instanceof AckClusterMessage) {

            AckClusterMessage ackMessage = (AckClusterMessage) msg;
            this.queueAckCluster.add(ackMessage);

            return;
        }
    }

    /**
     * In this time slot all request message will have arrived
     */
    @Override
    public void timeslot3() {
        super.timeslot3();

        if (!this.queueClusterRequest.isEmpty()) {
            RequestClusterMessage rq = this.queueClusterRequest.poll();

            // check if my current request is greater than request received
            // if my current msg is greater than the request received send ack message.
            // The node can send message to itself
            if (this.clusterRequest == null || (this.clusterRequest.compareTo(rq) >= 0)) {

                AckClusterMessage ack = new AckClusterMessage(rq.getSrc(), rq.getDst(), rq.getPriority(),
                        this.getNodeInfo());

                if (rq.getTimeout() == 0) {
                    ack.setFinalNode();
                }

                this.sendForwardMessage(rq.getSrc(), ack);
            }
        }
    }

    private AckClusterMessage findAckMessageInBufferById(int id) {
        for (AckClusterMessage m : this.queueAckCluster) {
            if (m.getInfo().getNode().ID == id) {
                return m;
            }
        }

        return null;
    }

    /**
     * This function check ack buffer to see if ack message
     * form a linear sequence to final node. In case missing one
     * ack message the function return false.
     * @return
    */
    public boolean isClusterGranted() {
        boolean clusterGranted = false;
        BinaryTreeLayer node = this;
        AckClusterMessage m;

        while((m = findAckMessageInBufferById(node.ID)) != null) {
            if (m.isFinalNode()) {
                clusterGranted = true;
                break;
            }
            node = (BinaryTreeLayer) m.getInfo().getParent();
        }
         

        return clusterGranted;
    }

    private HashMap<String, NodeInfo> getClusterSequenceFromAckBuffer() {
        HashMap<String, NodeInfo> table = new HashMap<>();
        NodeInfo info;

        info = findAckMessageInBufferById(ID).getInfo();
        table.put("x", info);

        info = findAckMessageInBufferById(info.getParent().ID).getInfo();
        table.put("y", info);

        info = findAckMessageInBufferById(info.getParent().ID).getInfo();
        table.put("z", info);

        if (this.queueAckCluster.size() == 4) {
            info = findAckMessageInBufferById(info.getParent().ID).getInfo();
            table.put("w", info);
        }

        return table;
    }

    /**
     * In this time slot all ack message will have arrived
     * If the node has sent message requesting cluster formation
     * verify if the permission was granted.
     */
    @Override
    public void timeslot6() {
        super.timeslot6();

        if (!this.queueAckCluster.isEmpty()) { // This node has sent request cluster message
           if (this.isClusterGranted()) {
                this.clusterCompleted(this.getClusterSequenceFromAckBuffer());
           }
        }

        //reset queue
        this.clearClusterRequestQueue();
        this.clearAckClusterQueue();
    }

    public abstract void clusterCompleted(HashMap<String, NodeInfo> cluster);
}