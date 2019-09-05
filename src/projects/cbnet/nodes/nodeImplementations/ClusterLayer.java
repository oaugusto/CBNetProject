package projects.cbnet.nodes.nodeImplementations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import projects.cbnet.nodes.messages.controlMessage.AckClusterMessage;
import projects.cbnet.nodes.messages.controlMessage.RequestClusterUpMessage;
import projects.cbnet.nodes.messages.controlMessage.RequestClusterDownMessage;
import projects.cbnet.nodes.tableEntry.CBInfo;
import projects.displaynet.nodes.messages.controlMessages.RequestClusterMessage;
import sinalgo.nodes.messages.Message;

/**
 * ClusterLayer
 */
public abstract class ClusterLayer extends CBNetLayer {

    // this priority queue store all request cluster message received
    private PriorityQueue<RequestClusterMessage> queueClusterRequest;

    // this queue keeps all acks received due to a request cluster operation
    private Queue<AckClusterMessage> queueAckCluster;

    private boolean isClusterUp; // change this
    private boolean isClusterDown;

    @Override
    public void init() {
        super.init();

        this.queueClusterRequest = new PriorityQueue<>();
        this.queueAckCluster = new LinkedList<>();
        this.isClusterUp = false;
        this.isClusterDown = false;
    }

    public void clearClusterRequestQueue() {
        this.queueClusterRequest.clear();
    }

    public void clearAckClusterQueue() {
        this.queueAckCluster.clear();
    }

    /**
     * This method send a cluster request and put the request into the buffer of
     * current node. The positon means how many times the message should be routed.
     * 
     * @param src
     * @param dst
     * @param priority
     */
    public void sendRequestClusterUp(int src, int dst, double priority) {
        // System.out.println("Node " + ID + " sending cluster up");
        this.isClusterUp = true;

        RequestClusterUpMessage msg = new RequestClusterUpMessage(src, dst, 0, priority);

        // add cluster request to buffer
        this.queueClusterRequest.add(msg);

        // shift the position one hop
        RequestClusterUpMessage m = new RequestClusterUpMessage(msg);
        m.shiftPosition();

        this.sendToParent(m);
    }

    public void sendRequestClusterDown(int src, int dst, double priority) {
        // System.out.println("Node " + ID + " sending cluster down");
        this.isClusterDown = true;

        RequestClusterDownMessage msg = new RequestClusterDownMessage(src, dst, 1, priority);
        this.queueClusterRequest.add(msg);

        RequestClusterDownMessage toParent = new RequestClusterDownMessage(msg);
        toParent.setPosition(0);
        this.sendToParent(toParent);

        RequestClusterDownMessage downward = new RequestClusterDownMessage(msg);
        downward.shiftPosition();
        
        if (ID < msg.getDst() && msg.getDst() <= this.getMaxIdInSubtree()) {
            this.sendToRightChild(downward);
        } else if (this.getMinIdInSubtree() <= msg.getDst() && msg.getDst() < ID) {
            this.sendToLeftChild(downward);
        }
    }

    @Override
    public void receiveMessage(Message msg) {
        super.receiveMessage(msg);

        if (msg instanceof RequestClusterUpMessage) {

            RequestClusterUpMessage requestMessage = (RequestClusterUpMessage) msg;
            int position = requestMessage.getPosition();

            /**
             * It is not possible to parent to be the dst because completion is check first
             * and the grandparent being LCA has effect on the type of rotation
             */
            if (position == 3 || (position == 2 && ID == requestMessage.getDst())) {

                requestMessage.setFinalNode();

            } else if (!requestMessage.isFinalNode()) {

                RequestClusterUpMessage newRequestMessage = new RequestClusterUpMessage(requestMessage);
                newRequestMessage.shiftPosition();

                if (position == 1 && this.isLeastCommonAncestorOf(requestMessage.getDst())) {
                    newRequestMessage.setFinalNode();
                }

                this.sendToParent(newRequestMessage);

            }

            // add current request to queue
            this.queueClusterRequest.add(requestMessage);

            return;

        } else if (msg instanceof RequestClusterDownMessage) {

            RequestClusterDownMessage requestMessage = (RequestClusterDownMessage) msg;
            int position = requestMessage.getPosition();

            /**
             * It is not possible to parent to be the dst because completion is check first
             * and the grandparent being LCA has effect on the type of rotation
             */
            if (position == 3) {

                requestMessage.setFinalNode();

            } else if (position != 0) {

                RequestClusterDownMessage newRequestMessage = new RequestClusterDownMessage(requestMessage);
                newRequestMessage.shiftPosition();

                if (ID == requestMessage.getDst()) {

                    requestMessage.setFinalNode();

                } else if (ID < newRequestMessage.getDst() && newRequestMessage.getDst() <= this.getMaxIdInSubtree()) {
                    if (this.hasRightChild()) {
                        // System.out.println("node " + ID + " forwarding cluster msg down left");
                        this.sendToRightChild(newRequestMessage);
                    } else {
                        requestMessage.setFinalNode();
                    }
                } else if (this.getMinIdInSubtree() <= newRequestMessage.getDst() && newRequestMessage.getDst() < ID) {
                    if (this.hasLeftChild()) {
                        // System.out.println("node " + ID + " forwarding cluster msg down right");
                        this.sendToLeftChild(newRequestMessage);
                    } else {
                        requestMessage.setFinalNode();
                    }
                }

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

            CBInfo info = this.getNodeInfo();
            AckClusterMessage ack = new AckClusterMessage(rq.getSrc(), rq.getDst(), rq.getPriority(), rq.getPosition(),
                    info);

            if (rq.isFinalNode()) {
                ack.setFinalNode();
            }

            this.sendForwardMessage(rq.getSrc(), ack);

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

    private HashMap<String, CBInfo> getClusterSequenceFromAckBufferBottomUp() {
        HashMap<String, CBInfo> table = new HashMap<>();
        CBInfo info;

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

    private HashMap<String, CBInfo> getClusterSequenceFromAckBufferTopDown() {
        HashMap<String, CBInfo> table = new HashMap<>();
        CBInfo info;

        info = findAckMessageInBufferByPosition(0).getInfo();
        table.put("w", info);

        info = findAckMessageInBufferByPosition(1).getInfo();
        table.put("z", info);

        info = findAckMessageInBufferByPosition(2).getInfo();
        table.put("y", info);

        if (this.queueAckCluster.size() == 4) {
            info = findAckMessageInBufferByPosition(3).getInfo();
            table.put("x", info);
        }

        return table;
    }

    private CBInfo findTarget() {
        for (AckClusterMessage m : this.queueAckCluster) {
            if (m.getDst() == m.getInfo().getNode().ID) {
                return m.getInfo();
            }
        }

        return null;
    }

    /**
     * In this time slot all ack message will have arrived If the node has sent
     * message requesting cluster formation verify if the permission was granted.
     */
    @Override
    public void timeslot6() {
        super.timeslot6();

        CBInfo target = findTarget();

        if (target != null) {

            this.targetNodeFound(target);

        } else if (!this.queueAckCluster.isEmpty() && this.isClusterGranted()) {
            // This node has sent request cluster message
            if (this.isClusterUp) {

                this.clusterCompletedBottomUp(this.getClusterSequenceFromAckBufferBottomUp());

            } else if (this.isClusterDown) {

                this.clusterCompletedTopDown(this.getClusterSequenceFromAckBufferTopDown());

            }

        }

        // reset queue
        this.clearClusterRequestQueue();
        this.clearAckClusterQueue();
        this.isClusterDown = false;
        this.isClusterUp = false;
    }

    public abstract void clusterCompletedBottomUp(HashMap<String, CBInfo> cluster);

    public abstract void clusterCompletedTopDown(HashMap<String, CBInfo> cluster);

    public abstract void targetNodeFound(CBInfo target);

}
