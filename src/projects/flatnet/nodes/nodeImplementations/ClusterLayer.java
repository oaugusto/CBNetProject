package projects.flatnet.nodes.nodeImplementations;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import projects.flatnet.nodes.messages.controlMessage.AckClusterMessage;
import projects.flatnet.nodes.messages.controlMessage.RequestClusterBottomUpMessage;
import projects.flatnet.nodes.messages.controlMessage.RequestClusterTopDownMessage;
import projects.flatnet.nodes.messages.controlMessage.RequestClusterMessage;
import projects.flatnet.nodes.tableEntry.FlatInfo;
import sinalgo.nodes.messages.Message;

/**
 * ClusterLayer
 */
public abstract class ClusterLayer extends FlatNetLayer {

    // this priority queue store all request cluster message received
    private PriorityQueue<RequestClusterMessage> queueClusterRequest;

    // this queue keeps all acks received due to a request cluster operation
    private Queue<AckClusterMessage> queueAckCluster;

    private boolean isClusterBottomUp; // TODO : change this
    private boolean isClusterTopDown;

    @Override
    public void init() {
        super.init();

        this.queueClusterRequest = new PriorityQueue<>();
        this.queueAckCluster = new LinkedList<>();
        this.isClusterBottomUp = false;
        this.isClusterTopDown = false;
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
    public void sendRequestClusterBottomUp(int currentNode, int src, int dst, double priority){
        this.isClusterBottomUp = true;

        RequestClusterBottomUpMessage msg = new RequestClusterBottomUpMessage(currentNode, src, dst, 0, priority);

        this.queueClusterRequest.add(msg);

        RequestClusterBottomUpMessage m = new RequestClusterBottomUpMessage(msg);
        m.shiftPosition();

        this.sendToParent(m);
    }
    public void sendRequestClusterTopDown(int currentNode, int src, int dst, double priority) {
        this.isClusterTopDown = true;

        RequestClusterTopDownMessage msg = new RequestClusterTopDownMessage(currentNode, src, dst, 0, priority);
        this.queueClusterRequest.add(msg);

        RequestClusterTopDownMessage m = new RequestClusterTopDownMessage(msg);
        m.shiftPosition();

        int direction = this.getDirection(msg.getDst());
        if (direction == 2) {
            this.sendToRightChild(m);
        } else if (direction == 1){
            this.sendToLeftChild(m);
        }
    }

    @Override
    public void receiveMessage(Message msg) {
        super.receiveMessage(msg);
        if(msg instanceof RequestClusterBottomUpMessage) {
            
            RequestClusterBottomUpMessage requestMessage = (RequestClusterBottomUpMessage) msg;
            int position = requestMessage.getPosition();

            if(!requestMessage.isFinalNode()) {
                if( position == 2 || 
                    (position == 1 && this.isLeastCommonAncestorOf(requestMessage.getDst()))) {
                    RequestClusterBottomUpMessage m = new RequestClusterBottomUpMessage(requestMessage);
                    m.setFinalNode();
                    m.shiftPosition();
                    this.sendToParent(m);
                } else {
                    RequestClusterBottomUpMessage newRequestMessage = new RequestClusterBottomUpMessage(requestMessage);
                    newRequestMessage.shiftPosition();

                    this.sendToParent(newRequestMessage);
                }
            }

            this.queueClusterRequest.add(requestMessage);
        }else if(msg instanceof RequestClusterTopDownMessage) {
            
            RequestClusterTopDownMessage requestMessage = (RequestClusterTopDownMessage) msg;

            requestMessage.setFinalNode();
            // add current request to queue
            this.queueClusterRequest.add(requestMessage);

            return;
        }else if(msg instanceof AckClusterMessage){
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

            FlatInfo info = this.getNodeInfo();
            AckClusterMessage ack = new AckClusterMessage(rq.getSrc(), rq.getDst(), rq.getPriority(), rq.getPosition(),
                    info);

            if (rq.isFinalNode()) {
                ack.setFinalNode();
            }

            this.sendForwardMessage(rq.getCurrentNode(), ack);

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

    private HashMap<String, FlatInfo> getClusterSequenceFromAckBufferBottomUp() {
        HashMap<String, FlatInfo> table = new HashMap<>();
        FlatInfo info;

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

    private HashMap<String, FlatInfo> getClusterSequenceFromAckBufferTopDown() {
        HashMap<String, FlatInfo> table = new HashMap<>();
        FlatInfo info;

        info = findAckMessageInBufferByPosition(0).getInfo();
        table.put("x", info);

        info = findAckMessageInBufferByPosition(1).getInfo();
        table.put("y", info);

        return table;
    }

    private FlatInfo findTarget() {
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

        FlatInfo target = findTarget();

        if (target != null) {

            this.targetNodeFound(target);

        } else if (!this.queueAckCluster.isEmpty() && this.isClusterGranted()) {
            // This node has sent request cluster message
            if (this.isClusterBottomUp) {

                this.clusterCompletedBottomUp(this.getClusterSequenceFromAckBufferBottomUp());

            } else if (this.isClusterTopDown) {

                this.clusterCompletedTopDown(this.getClusterSequenceFromAckBufferTopDown());

            }

        }

        // reset queue
        this.clearClusterRequestQueue();
        this.clearAckClusterQueue();
        this.isClusterTopDown = false;
        this.isClusterBottomUp = false;
    }

    public abstract void clusterCompletedBottomUp(HashMap<String, FlatInfo> cluster);

    public abstract void clusterCompletedTopDown(HashMap<String, FlatInfo> cluster);

    public abstract void targetNodeFound(FlatInfo target);

}