package projects.cbnet.nodes.nodeImplementations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import projects.cbnet.nodes.messages.CBNetMessage;
import projects.displaynet.messages.controlMessages.AckClusterMessage;
import projects.displaynet.messages.controlMessages.RequestClusterMessage;
import projects.displaynet.nodeImplementations.BinaryTreeLayer;
import projects.displaynet.nodeImplementations.RPCLayer;
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

    public RequestClusterMessage getClusterRequest() {
        return clusterRequest;
    }

    public void setClusterRequest(int src, int dst, double priority) {
        // position is set to 0, first node in the sequence
        this.clusterRequest = new RequestClusterMessage(src, dst, 0, priority);
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

    private void forwardRequestCluster(RequestClusterMessage msg) {
        this.sendToParent(msg);
    }

    /**
     * This method send a cluster request and put the request into the buffer of
     * current node. The positon means how many times the message should be routed.
     * 
     * @param src
     * @param dst
     * @param priority
     */
    public void sendRequestCluster() {
        // add cluster request to buffer
        this.queueClusterRequest.add(this.clusterRequest);

        // shift the position one hop
        RequestClusterMessage msg = new RequestClusterMessage(this.clusterRequest);
        msg.shiftPosition();

        this.forwardRequestCluster(msg);
    }

    @Override
    public void receiveMessage(Message msg) {
        super.receiveMessage(msg);

        if (msg instanceof RequestClusterMessage) {

            RequestClusterMessage requestMessage = (RequestClusterMessage) msg;
            int position = requestMessage.getPosition();

            /**
             * It is not possible to parent to be the dst because completion is check first
             * and the grandparent being LCA has effect on the type of rotation
             */
            if (position == 3 || (position == 2 && ID == requestMessage.getDst())) {

                requestMessage.setFinalNode();

            } else if (!requestMessage.isFinalNode()) {

                RequestClusterMessage newRequestMessage = new RequestClusterMessage(requestMessage);
                newRequestMessage.shiftPosition();

                if (position == 1 && this.isLeastCommonAncestorOf(requestMessage.getDst())) {
                    newRequestMessage.setFinalNode();
                }

                this.forwardRequestCluster(newRequestMessage);

            }

            // add current request to queue
            this.queueClusterRequest.add(requestMessage);

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

        if (!this.queueClusterRequest.isEmpty()) {
            RequestClusterMessage rq = this.queueClusterRequest.poll();

            // check if my current request is greater than request received
            // if my current msg is greater than the request received send ack message.
            // The node can send message to itself
            if (this.clusterRequest == null || this.clusterRequest.compareTo(rq) >= 0
                    || (this.isLeastCommonAncestorOf(rq.getSrc()) && this.clusterRequest.getDst() == rq.getSrc())) {

                AckClusterMessage ack = new AckClusterMessage(rq.getSrc(), rq.getDst(), rq.getPriority(),
                        rq.getPosition(), this.getNodeInfo());

                if (rq.isFinalNode()) {
                    ack.setFinalNode();
                }

                this.sendForwardMessage(rq.getSrc(), ack);
            }
        }
    }

    private AckClusterMessage findAckMessageInBufferByPosition(int pos) {
        for (AckClusterMessage m : this.queueAckCluster) {
            if (m.getPosition() == pos) {
                return m;
            }
        }

        return null;
    }

    /**
     * This function checks ack buffer to see if ack message form a linear sequence
     * to final node. In case missing one ack message the function return false.
     * 
     * @return
     */
    private boolean isClusterGranted() {
        AckClusterMessage m;

        for (int pos = 0; pos <= 3; pos++) {
            m = findAckMessageInBufferByPosition(pos);

            if (m == null) {

                return false;

            } else if (m.isFinalNode()) {

                return true;

            }
        }

        return false;
    }

    private HashMap<String, NodeInfo> getClusterSequenceFromAckBuffer() {
        HashMap<String, NodeInfo> table = new HashMap<>();
        NodeInfo info;

        info = findAckMessageInBufferByPosition(0).getInfo();
        table.put("x", info);

        info = findAckMessageInBufferByPosition(1).getInfo();
        table.put("y", info);

        info = findAckMessageInBufferByPosition(2).getInfo();
        table.put("z", info);

        if (this.queueAckCluster.size() == 4) {
            info = findAckMessageInBufferByPosition(3).getInfo();
            table.put("w", info);
        }

        return table;
    }

    /**
     * In this time slot all ack message will have arrived If the node has sent
     * message requesting cluster formation verify if the permission was granted.
     */
    @Override
    public void timeslot6() {
        super.timeslot6();

        // This node has sent request cluster message
        if (!this.queueAckCluster.isEmpty()) {
            if (this.isClusterGranted()) {
                this.clusterCompleted(this.getClusterSequenceFromAckBuffer());
            }
        }

        // reset queue
        this.clearClusterRequestQueue();
        this.clearAckClusterQueue();
    }

    public abstract void clusterCompleted(HashMap<String, NodeInfo> cluster);
}
