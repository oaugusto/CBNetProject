package projects.cbnet.nodes.nodeImplementations;

import projects.displaynet.nodes.nodeImplementations.BinaryTreeLayer;
import projects.cbnet.nodes.tableEntry.CBInfo;

/**
 * CBTreeLayer
 */
public abstract class CBTreeLayer extends BinaryTreeLayer {

    private long weight;

    @Override
    public void init() {
        super.init();

        this.weight = 0;
    }

    public long getWeight() {
        return weight;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public void incrementWeight() {
        this.weight++;
    }

    @Override
    public CBInfo getNodeInfo() {
        return new CBInfo(this, (CBTreeLayer) this.getParent(), (CBTreeLayer) this.getLeftChild(),
                (CBTreeLayer) this.getRightChild(), this.getMinIdInSubtree(), this.getMaxIdInSubtree(),
                this.getWeight());
    }

}