package projects.flatnet.nodes.nodeImplementations;

import projects.flatnet.nodes.tableEntry.FlatInfo;

/**
 * FlatTreeLayer
 */
public abstract class FlatTreeLayer extends BinaryTreeLayer {

    @Override
    public void init() {
        super.init();
    }
    @Override
    public FlatInfo getNodeInfo() {
        return new FlatInfo(this, (FlatTreeLayer) this.getParent(), (FlatTreeLayer) this.getLeftChild(),
                (FlatTreeLayer) this.getRightChild(), this.getLeftDescendants(), this.getRightDescendants());
    }

}