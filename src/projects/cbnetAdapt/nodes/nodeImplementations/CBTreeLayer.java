package projects.cbnetAdapt.nodes.nodeImplementations;

import projects.defaultProject.nodes.nodeImplementations.BinarySearchTreeLayer;
import projects.cbnetAdapt.nodes.tableEntry.CBInfo;

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
    
    public void decayWeight() {
    	this.weight /= 2;
    }

    public void updateWeights(int from, int to) {
        CBTreeLayer aux = this;
        
        if (!(aux.getParent() == null)) {
	        while (!(aux.getParent() == null)) {
	        	if (aux.isAncestorOf(from) && aux.isAncestorOf(to)) {
	        		aux.incrementWeight();
	        		aux.incrementWeight();
	        	}
	        	aux = (CBTreeLayer) aux.getParent();
			} 
	        
	        if (aux.isAncestorOf(from) && aux.isAncestorOf(to)) {
        		aux.incrementWeight();
        		aux.incrementWeight();
        	}
        }

//        if (!aux.isAncestorOf(to)) {
//            while (!aux.isAncestorOf(to)) {
//                aux.incrementWeight();
//                aux = (CBTreeLayer) aux.getParent();
//			} 
//			
//			aux.incrementWeight();
//        }

//        while (aux.ID != to) {
//			aux.incrementWeight();
//			if (aux.ID > to) {
//				aux = (CBTreeLayer) aux.getLeftChild();
//			} else {
//				aux = (CBTreeLayer) aux.getRightChild();
//			}
//		}
		
		aux.incrementWeight();
    }

    @Override
    public CBInfo getNodeInfo() {
        return new CBInfo(this, (CBTreeLayer) this.getParent(), (CBTreeLayer) this.getLeftChild(),
                (CBTreeLayer) this.getRightChild(), this.getMinIdInSubtree(), this.getMaxIdInSubtree(),
                this.getWeight());
    }

}