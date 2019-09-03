package projects.cbnet.nodes.nodeImplementations;

import java.util.HashMap;

import projects.displaynet.nodeImplementations.BinaryTreeLayer;
import projects.displaynet.tableEntry.NodeInfo;

/**
 * RotationLayer
 */
public abstract class RotationLayer extends ClusterLayer {

    private long weight;

    private boolean rotating;

    @Override
    public void init() {
        super.init();

        this.rotating = false;
    }

    public void setWeight(long weight) {
        this.weight = weight;
    }

    public long getWeight() {
        return weight;
    }

    public void incrementWeight() {
        this.weight++; 
    }

    public void updateWeights(RotationLayer from, RotationLayer to) {
        RotationLayer aux = from;

        if (!aux.isLeastCommonAncestorOf(to)) {
            while (!aux.isLeastCommonAncestorOf(to)) {
				aux.incrementWeight();
				aux = (RotationLayer) aux.getParent();
			} 
			
			aux.incrementWeight();
        }

        while (aux != to) {
			aux.incrementWeight();
			if (aux.ID > to.ID) {
				aux = (RotationLayer) aux.getLeftChild();
			} else {
				aux = (RotationLayer) aux.getRightChild();
			}
		}
		
		aux.incrementWeight();
    }

    public void rotate(HashMap<String, NodeInfo> cluster) {
        this.rotating = true;

        NodeInfo xInfo = cluster.get("x");
        NodeInfo yInfo = cluster.get("y");
        NodeInfo zInfo = cluster.get("z");

        if (cluster.size() == 3) {
            this.zig(xInfo, yInfo, zInfo);

        } else {
            NodeInfo wInfo = cluster.get("w");
            BinaryTreeLayer x = xInfo.getNode();
            BinaryTreeLayer y = yInfo.getNode();

            if ((x == yInfo.getLeftChild() && y == zInfo.getLeftChild())
                    || (x == yInfo.getRightChild() && y == zInfo.getRightChild())) {
                this.zigZig(xInfo, yInfo, zInfo, wInfo);
            } else {
                this.zigZag(xInfo, yInfo, zInfo, wInfo);
            }
        }

    }


    /*
                  z                     z
                 /                    /
                y                   x
              /   \               /   \
             x     c     ->      a     y
            / \                       / \
           a   b                     b   c
	 
	 */
    private void zig(NodeInfo xInfo, NodeInfo yInfo, NodeInfo zInfo) {
        System.out.println("zig operation: " + ID);
        BinaryTreeLayer x = xInfo.getNode();
        BinaryTreeLayer y = yInfo.getNode();
        BinaryTreeLayer z = zInfo.getNode();

        //set the new child of node z
        if (zInfo.getLeftChild() == y) {
            this.requestRPCTo(z.ID, "changeLeftChildTo", x);
        } else {
            this.requestRPCTo(z.ID, "changeRightChildTo", x);
        }

        // left zig operetion
        if (yInfo.getLeftChild() == x) {
            // change node y
            this.requestRPCTo(y.ID, "changeLeftChildTo", xInfo.getRightChild());
            int min = y.ID; 
            if (xInfo.getRightChild() != null) {
                min = xInfo.getRightChild().getMinIdInSubtree();
            }
            this.requestRPCTo(y.ID, "setMinIdInSubtree", min); 

            // change node x
            this.requestRPCTo(x.ID, "changeRightChildTo", y);
            this.requestRPCTo(x.ID, "setMaxIdInSubtree", yInfo.getMaxIdInSubtree()); 
        } else {
            // change node y
            this.requestRPCTo(y.ID, "changeRightChildTo", xInfo.getLeftChild()); 
            int max = y.ID;
            if (x.getLeftChild() != null) {
                max = x.getLeftChild().getMaxIdInSubtree();
            }
            this.requestRPCTo(y.ID, "setMaxIdInSubtree", max); 

            // change node x
            this.requestRPCTo(x.ID, "changeLeftChildTo", y); 
            this.requestRPCTo(x.ID, "setMinIdInSubtree", yInfo.getMinIdInSubtree()); 
        }
    }

    /*              
                   w               w
                  /               /
                 z               x
                / \             / \
	           y   d           a   y
              / \       ->        / \
             x   c               b   z
            / \                     / \
           a   b                   c   d
   */
    private void zigZig(NodeInfo xInfo, NodeInfo yInfo, NodeInfo zInfo, NodeInfo wInfo) {
        System.out.println("zig zig operation: " + ID);
        BinaryTreeLayer x = xInfo.getNode();
        BinaryTreeLayer y = yInfo.getNode();
        BinaryTreeLayer z = zInfo.getNode();
        BinaryTreeLayer w = wInfo.getNode();

        // set new child of node z

        if (wInfo.getLeftChild() == z) {
            this.requestRPCTo(w.ID, "changeLeftChildTo", x);
        } else {
            this.requestRPCTo(w.ID, "changeRightChildTo", x);
        }

        // deciding between lef or right zigzig operation
        if (y == zInfo.getLeftChild()) {
            // change node z
            this.requestRPCTo(z.ID, "changeLeftChildTo", yInfo.getRightChild()); 
            int min = z.ID;
            if (yInfo.getRightChild() != null) {
                min = yInfo.getRightChild().getMinIdInSubtree();
            }
            this.requestRPCTo(z.ID, "setMinIdInSubtree", min);

            // change node y
            this.requestRPCTo(y.ID, "changeLeftChildTo", xInfo.getRightChild()); 
            min = y.ID;
            if (xInfo.getRightChild() != null) {
                min = xInfo.getRightChild().getMinIdInSubtree();
            }
            this.requestRPCTo(y.ID, "setMinIdInSubtree", min);
            this.requestRPCTo(y.ID, "changeRightChildTo", z);
            this.requestRPCTo(y.ID, "setMaxIdInSubtree", zInfo.getMaxIdInSubtree());
            
            // change node x
            this.requestRPCTo(x.ID, "changeRightChildTo", y);
            this.requestRPCTo(x.ID, "setMaxIdInSubtree", zInfo.getMaxIdInSubtree());

        } else {

           // change node z
           this.requestRPCTo(z.ID, "changeRightChildTo", yInfo.getLeftChild()); 
           int max = z.ID;
           if (yInfo.getLeftChild() != null) {
               max = yInfo.getLeftChild().getMaxIdInSubtree();
           }
           this.requestRPCTo(z.ID, "setMaxIdInSubtree", max);

           // change node y
           this.requestRPCTo(y.ID, "changeRightChildTo", x.getLeftChild()); 
           max = y.ID;
           if (x.getLeftChild() != null) {
               max = x.getLeftChild().getMaxIdInSubtree();
           }
           this.requestRPCTo(y.ID, "setMaxIdInSubtree", max);
           this.requestRPCTo(y.ID, "changeLeftChildTo", z);
           this.requestRPCTo(y.ID, "setMinIdInSubtree", zInfo.getMinIdInSubtree());
           
           // change node x
           this.requestRPCTo(x.ID, "changeLeftChildTo", y);
           this.requestRPCTo(x.ID, "setMinIdInSubtree", zInfo.getMinIdInSubtree());
        }

    }


    /*
                   w                  w
                  /                  /
                 z					x
                / \               /   \
               y   d             y     z
              / \		  ->    / \   / \
             a   x             a   b c   d
                / \
               b   c 
	 */
    private void zigZag(NodeInfo xInfo, NodeInfo yInfo, NodeInfo zInfo, NodeInfo wInfo) {
        System.out.println("zig zag operation: " + ID);
        BinaryTreeLayer x = xInfo.getNode();
        BinaryTreeLayer y = yInfo.getNode();
        BinaryTreeLayer z = zInfo.getNode();
        BinaryTreeLayer w = wInfo.getNode();

        // set new child of node z

        if (wInfo.getLeftChild() == z) {
            this.requestRPCTo(w.ID, "changeLeftChildTo", x);
        } else {
            this.requestRPCTo(w.ID, "changeRightChildTo", x);
        }

         // deciding between lef or right zigzag operation
         if (y == zInfo.getLeftChild()) {
            // change node z
            this.requestRPCTo(z.ID, "changeLeftChildTo", xInfo.getRightChild()); 
            int min = z.ID;
            if (xInfo.getRightChild() != null) {
                min = xInfo.getRightChild().getMinIdInSubtree();
            }
            this.requestRPCTo(z.ID, "setMinIdInSubtree", min);

            // change node y
            this.requestRPCTo(y.ID, "changeRightChildTo", xInfo.getLeftChild()); 
            int max = y.ID;
            if (xInfo.getLeftChild() != null) {
                max = xInfo.getLeftChild().getMaxIdInSubtree();
            }
            this.requestRPCTo(y.ID, "setMaxIdInSubtree", max);

            // change node x
            this.requestRPCTo(x.ID, "changeLeftChildTo", y); 
            this.requestRPCTo(x.ID, "setMinIdInSubtree", yInfo.getMinIdInSubtree());
            this.requestRPCTo(x.ID, "changeRightChildTo", z);
            this.requestRPCTo(x.ID, "setMaxIdInSubtree", zInfo.getMaxIdInSubtree());

        } else {

            // change node z
            this.requestRPCTo(z.ID, "changeRightChildTo", x.getLeftChild()); 
            int max = z.ID;
            if (x.getLeftChild() != null) {
                max = x.getLeftChild().getMaxIdInSubtree();
            }
            this.requestRPCTo(z.ID, "setMaxIdInSubtree", max);

            // change node y
            this.requestRPCTo(y.ID, "changeLeftChildTo", xInfo.getRightChild()); 
            int min = y.ID;
            if (xInfo.getRightChild() != null) {
                min = xInfo.getRightChild().getMinIdInSubtree();
            }
            this.requestRPCTo(y.ID, "setMinIdInSubtree", min);

            // change node x
            this.requestRPCTo(x.ID, "changeRightChildTo", y); 
            this.requestRPCTo(x.ID, "setMaxIdInSubtree", yInfo.getMaxIdInSubtree());
            this.requestRPCTo(x.ID, "changeLeftChildTo", z);
            this.requestRPCTo(x.ID, "setMinIdInSubtree", zInfo.getMinIdInSubtree());
        }
    }

    private double log2(long value) {
		return Math.log(value) / Math.log(2);
	}

    public double zigDiffRank() {
		RotationLayer x = this;
		RotationLayer y = (RotationLayer) x.getParent();
		
		//type of operation
		boolean leftZig = (x == y.getLeftChild())? true: false;
		
		RotationLayer b = (RotationLayer) ((leftZig) ? x.getRightChild(): x.getLeftChild());
		
		long xOldWeight = x.getWeight();
		long yOldWeight = y.getWeight();
		
		long bWeight = (b != null) ? b.getWeight(): 0;
		
		long yNewWeight = yOldWeight - xOldWeight + bWeight;
		long xNewWeight = xOldWeight - bWeight + yNewWeight;
		
		double xOldRank = (xOldWeight == 0) ? 0 : log2(xOldWeight);
		double yOldRank = (yOldWeight == 0) ? 0 : log2(yOldWeight);
		double xNewRank = (xNewWeight == 0) ? 0 : log2(xNewWeight);
		double yNewRank = (yNewWeight == 0) ? 0 : log2(yNewWeight);
		
		double deltaRank = yNewRank + xNewRank - yOldRank - xOldRank;
		
		return deltaRank;
	}

    private double zigZagDiffRank() {
		RotationLayer x = this;
		RotationLayer y = (RotationLayer) x.getParent();
		RotationLayer z = (RotationLayer) y.getParent(); 
		
		boolean lefZigZag = (y == z.getLeftChild())? true: false;
		
		RotationLayer b = (CBNetNode) (lefZigZag ? x.getLeftChild(): x.getRightChild()); 
		RotationLayer c = (CBNetNode) (lefZigZag ? x.getRightChild(): x.getLeftChild()); 
		
		long xOldWeight = x.getWeight();
		long yOldWeight = y.getWeight();
		long zOldWeight = z.getWeight();
		
		long bWeight = (b != null) ? b.getWeight(): 0;
		long cWeight = (c != null) ? c.getWeight(): 0;
		
		long yNewWeight = yOldWeight - xOldWeight + bWeight;
		long zNewWeight = zOldWeight - yOldWeight + cWeight;
		long xNewWeight = xOldWeight - bWeight - cWeight + yNewWeight + zNewWeight;
		
		double xOldRank = (xOldWeight == 0) ? 0 : log2(xOldWeight);
		double yOldRank = (yOldWeight == 0) ? 0 : log2(yOldWeight);
		double zOldRank = (zOldWeight == 0) ? 0 : log2(zOldWeight);
		double xNewRank = (xNewWeight == 0) ? 0 : log2(xNewWeight);
		double yNewRank = (yNewWeight == 0) ? 0 : log2(yNewWeight);
		double zNewRank = (zNewWeight == 0) ? 0 : log2(zNewWeight);
		
		double deltaRank = xNewRank + yNewRank + zNewRank - xOldRank - yOldRank - zOldRank;
		
		return deltaRank;
	}

    @Override
    public void timeslot9() {
        super.timeslot9();

        this.executeAllRPC();
        this.clearRPCQueue();

        if (this.rotating) {
            this.rotationCompleted();
            this.rotating = false;
        }
    }

    @Override
    public void clusterCompleted(HashMap<String, NodeInfo> cluster) {
        System.out.println("Node " + ID + ": cluster completed");
        this.rotate(cluster);
    }

    public void rotationCompleted() {

    }
    
}