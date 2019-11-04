package projects.cbnet.nodes.nodeImplementations;

import projects.defaultProject.nodes.nodeImplementations.BinarySearchTreeLayer;
import projects.cbnet.nodes.tableEntry.CBInfo;

/**
 * CBTreeLayer
 */
public abstract class CBTreeLayer extends BinarySearchTreeLayer {

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


    // TODO : change the way weights are updated
    public void updateWeights(int from, int to) {
        CBTreeLayer aux = this;

        if (!aux.isLeastCommonAncestorOf(to)) {
            while (!aux.isLeastCommonAncestorOf(to)) {
                aux.incrementWeight();
                aux = (CBTreeLayer) aux.getParent();
			} 
			
			aux.incrementWeight();
        }

        while (aux.ID != to) {
			aux.incrementWeight();
			if (aux.ID > to) {
				aux = (CBTreeLayer) aux.getLeftChild();
			} else {
				aux = (CBTreeLayer) aux.getRightChild();
			}
		}
		
		aux.incrementWeight();
    }

    @Override
    public CBInfo getNodeInfo() {
        return new CBInfo(this, (CBTreeLayer) this.getParent(), (CBTreeLayer) this.getLeftChild(),
                (CBTreeLayer) this.getRightChild(), this.getMinIdInSubtree(), this.getMaxIdInSubtree(),
                this.getWeight());
    }

}