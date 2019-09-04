package projects.cbnet.nodes.tableEntry;

import projects.cbnet.nodes.nodeImplementations.CBTreeLayer;
import projects.displaynet.nodes.tableEntry.NodeInfo;

/**
 * NodeInfo
 */
public class CBInfo extends NodeInfo {

    private long weight;

    public CBInfo() {
        super();
        this.weight = Long.MIN_VALUE;
    }

    public CBInfo(CBTreeLayer node, CBTreeLayer parent, CBTreeLayer left, CBTreeLayer right,
            int smallId, int largeId, long weight) {
        super(node, parent, left, right, smallId, largeId);
        this.weight = weight;
    }

    /**
     * @return the weight
     */
    public long getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(long weight) {
        this.weight = weight;
    }
}