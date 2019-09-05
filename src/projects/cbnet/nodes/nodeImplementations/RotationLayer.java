package projects.cbnet.nodes.nodeImplementations;

import java.util.HashMap;

import projects.cbnet.nodes.messages.CBNetMessage;
import projects.cbnet.nodes.tableEntry.CBInfo;

/**
 * RotationLayer
 */
public class RotationLayer extends ClusterLayer {

    private boolean rotating;
    
    private CBNetMessage cbnetMessage;

    @Override
    public void init() {
        super.init();

        this.rotating = false;
        this.cbnetMessage = null;
    }

    @Override
    public void updateState() {
        this.tryOperation();
    }

    public void tryOperation() {

        if (this.hasCBNetMessage()) {
            //System.out.println("Node " + ID + "has cbnet message");

            this.cbnetMessage = this.getTopCBNetMessage();

            if (this.getMinIdInSubtree() <= cbnetMessage.getDst() 
                    && cbnetMessage.getDst() <= this.getMaxIdInSubtree()) {
                this.sendRequestClusterDown(ID, 
                    cbnetMessage.getDst(), cbnetMessage.getPriority());
            } else {
                this.sendRequestClusterUp(ID, 
                    cbnetMessage.getDst(), cbnetMessage.getPriority());
            }
        }

    }

    @Override
    public void clusterCompletedBottomUp(HashMap<String, CBInfo> cluster) {
        this.rotateBottomUp(cluster);
    }

    @Override
    public void clusterCompletedTopDown(HashMap<String, CBInfo> cluster) {
        this.rotateTopDown(cluster);
    }

    private void rotateBottomUp(HashMap<String, CBInfo> cluster) {
        this.rotating = true;

        CBInfo xInfo = cluster.get("x");
        CBInfo yInfo = cluster.get("y");
        CBInfo zInfo = cluster.get("z");

        if (cluster.size() == 3) {
            this.zigBottomUp(xInfo, yInfo, zInfo);

        } else {
            CBInfo wInfo = cluster.get("w");
            CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
            CBTreeLayer y = (CBTreeLayer) yInfo.getNode();

            if ((x == yInfo.getLeftChild() && y == zInfo.getLeftChild())
                    || (x == yInfo.getRightChild() && y == zInfo.getRightChild())) {
                this.zigZigBottomUp(xInfo, yInfo, zInfo, wInfo);
            } else {
                this.zigZagBottomUp(xInfo, yInfo, zInfo, wInfo);
            }
        }

    }


    /* DONE
                  z                     z
                 /                    /
                y                   *x
              /   \               /   \
            *x     c     ->      a     y
            / \                       / \
           a   b                     b   c
	 
	 */
    private void zigBottomUp(CBInfo xInfo, CBInfo yInfo, CBInfo zInfo) {
        System.out.println("zig bottom up operation: " + ID);

        CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
        CBTreeLayer y = (CBTreeLayer) yInfo.getNode();
        CBTreeLayer z = (CBTreeLayer) zInfo.getNode();

        if (true) {

            // forward message
            this.forwardCBNetMessage(x.ID, this.cbnetMessage);

            // set the new child of node z
            if (zInfo.getLeftChild() == y) {
                this.requestRPCTo(z.ID, "changeLeftChildTo", x);
            } else {
                this.requestRPCTo(z.ID, "changeRightChildTo", x);
            }

            // left zig operetion
            if (yInfo.getLeftChild() == x) {
                // change node y
                this.requestRPCTo(y.ID, "changeLeftChildTo", (CBTreeLayer) xInfo.getRightChild());
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
                this.requestRPCTo(y.ID, "changeRightChildTo", (CBTreeLayer) xInfo.getLeftChild());
                int max = y.ID;
                if (xInfo.getLeftChild() != null) {
                    max = xInfo.getLeftChild().getMaxIdInSubtree();
                }
                this.requestRPCTo(y.ID, "setMaxIdInSubtree", max);

                // change node x
                this.requestRPCTo(x.ID, "changeLeftChildTo", y);
                this.requestRPCTo(x.ID, "setMinIdInSubtree", yInfo.getMinIdInSubtree());
            }
        } else {
            // forward here
            this.forwardCBNetMessage(y.ID, this.cbnetMessage);
        }
    }

   /* DONE
	            z                  *y
     	       / \                /   \
     		  y   d              x     z
	         / \       ->       / \   / \
	       *x   c              a   b c   d
	       / \                    
	      a   b                   
	*/
    private void zigZigBottomUp(CBInfo xInfo, CBInfo yInfo, CBInfo zInfo, CBInfo wInfo) {
        System.out.println("zig zig bottom up operation: " + ID);
        
        CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
        CBTreeLayer y = (CBTreeLayer) yInfo.getNode();
        CBTreeLayer z = (CBTreeLayer) zInfo.getNode();
        CBTreeLayer w = (CBTreeLayer) wInfo.getNode();
        
        if (true) {
            // forward message
            this.forwardCBNetMessage(y.ID, this.cbnetMessage);

            // set new child of node z
            if (wInfo.getLeftChild() == z) {
                this.requestRPCTo(w.ID, "changeLeftChildTo", y);
            } else {
                this.requestRPCTo(w.ID, "changeRightChildTo", y);
            }

            // left zig operetion on node y
            if (zInfo.getLeftChild() == y) {
                // change node z
                this.requestRPCTo(z.ID, "changeLeftChildTo", (CBTreeLayer) yInfo.getRightChild());
                int min = z.ID;
                if (yInfo.getRightChild() != null) {
                    min = yInfo.getRightChild().getMinIdInSubtree();
                }
                this.requestRPCTo(z.ID, "setMinIdInSubtree", min);

                // change node y
                this.requestRPCTo(y.ID, "changeRightChildTo", z);
                this.requestRPCTo(y.ID, "setMaxIdInSubtree", zInfo.getMaxIdInSubtree());
            } else {
                // change node z
                this.requestRPCTo(z.ID, "changeRightChildTo", (CBTreeLayer) yInfo.getLeftChild());
                int max = z.ID;
                if (yInfo.getLeftChild() != null) {
                    max = yInfo.getLeftChild().getMaxIdInSubtree();
                }
                this.requestRPCTo(z.ID, "setMaxIdInSubtree", max);

                // change node y
                this.requestRPCTo(y.ID, "changeLeftChildTo", z);
                this.requestRPCTo(y.ID, "setMinIdInSubtree", zInfo.getMinIdInSubtree());
            }
        } else {
            // forward 
            this.forwardCBNetMessage(z.ID, this.cbnetMessage);
        }

    }


    /* DONE
                   w                  w
                  /                  /
                 z				   *x
                / \               /   \
               y   d             y     z
              / \		  ->    / \   / \
             a   x*            a   b c   d
                / \
               b   c 
	 */
    private void zigZagBottomUp(CBInfo xInfo, CBInfo yInfo, CBInfo zInfo, CBInfo wInfo) {
        System.out.println("zig zag bottom up operation: " + ID);
        
        CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
        CBTreeLayer y = (CBTreeLayer) yInfo.getNode();
        CBTreeLayer z = (CBTreeLayer) zInfo.getNode();
        CBTreeLayer w = (CBTreeLayer) wInfo.getNode();

        if (true) {
            // forward message
            this.forwardCBNetMessage(x.ID, this.cbnetMessage);

            // set new child of node z
            if (wInfo.getLeftChild() == z) {
                this.requestRPCTo(w.ID, "changeLeftChildTo", x);
            } else {
                this.requestRPCTo(w.ID, "changeRightChildTo", x);
            }

            // deciding between lef or right zigzag operation
            if (y == zInfo.getLeftChild()) {
                // change node z
                this.requestRPCTo(z.ID, "changeLeftChildTo", (CBTreeLayer) xInfo.getRightChild());
                int min = z.ID;
                if (xInfo.getRightChild() != null) {
                    min = xInfo.getRightChild().getMinIdInSubtree();
                }
                this.requestRPCTo(z.ID, "setMinIdInSubtree", min);

                // change node y
                this.requestRPCTo(y.ID, "changeRightChildTo", (CBTreeLayer) xInfo.getLeftChild());
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
                this.requestRPCTo(z.ID, "changeRightChildTo", (CBTreeLayer) x.getLeftChild());
                int max = z.ID;
                if (x.getLeftChild() != null) {
                    max = x.getLeftChild().getMaxIdInSubtree();
                }
                this.requestRPCTo(z.ID, "setMaxIdInSubtree", max);

                // change node y
                this.requestRPCTo(y.ID, "changeLeftChildTo", (CBTreeLayer) xInfo.getRightChild());
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
        } else {
            // forward
            this.forwardCBNetMessage(z.ID, this.cbnetMessage);
        }
    }

    private void rotateTopDown(HashMap<String, CBInfo> cluster) {
        this.rotating = true;

        CBInfo xInfo = cluster.get("x");
        CBInfo yInfo = cluster.get("y");
        CBInfo zInfo = cluster.get("z");
        CBInfo wInfo = cluster.get("w");

        CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
        CBTreeLayer y = (CBTreeLayer) yInfo.getNode();

        if ((x == yInfo.getLeftChild() && y == zInfo.getLeftChild())
                || (x == yInfo.getRightChild() && y == zInfo.getRightChild())) {
            this.zigZigTopDown(xInfo, yInfo, zInfo, wInfo);
        } else {
            this.zigZagTopDown(xInfo, yInfo, zInfo, wInfo);
        }
    }

    /* DONE
	      	    *z                    y
		        / \                 /   \
		       y   d      ->      *x     z
		      / \                 / \   / \
		     x   c               a   b c   d
		    / \
		   a   b    
	*/
    private void zigZigTopDown(CBInfo xInfo, CBInfo yInfo, CBInfo zInfo, CBInfo wInfo) {
        System.out.println("zig zig top down operation: " + ID);
      
        CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
        CBTreeLayer y = (CBTreeLayer) yInfo.getNode();
        CBTreeLayer z = (CBTreeLayer) zInfo.getNode();
        CBTreeLayer w = (CBTreeLayer) wInfo.getNode();

        if (true) {

            // forward message
            this.forwardCBNetMessage(x.ID, this.cbnetMessage);

            // set new child of node z
            if (wInfo.getLeftChild() == z) {
                this.requestRPCTo(w.ID, "changeLeftChildTo", y);
            } else {
                this.requestRPCTo(w.ID, "changeRightChildTo", y);
            }

            // left zig operetion on node y
            if (zInfo.getLeftChild() == y) {
                // change node z
                this.requestRPCTo(z.ID, "changeLeftChildTo", (CBTreeLayer) yInfo.getRightChild());
                int min = z.ID;
                if (yInfo.getRightChild() != null) {
                    min = yInfo.getRightChild().getMinIdInSubtree();
                }
                this.requestRPCTo(z.ID, "setMinIdInSubtree", min);

                // change node y
                this.requestRPCTo(y.ID, "changeRightChildTo", z);
                this.requestRPCTo(y.ID, "setMaxIdInSubtree", zInfo.getMaxIdInSubtree());
            } else {
                // change node z
                this.requestRPCTo(z.ID, "changeRightChildTo", (CBTreeLayer) yInfo.getLeftChild());
                int max = z.ID;
                if (yInfo.getLeftChild() != null) {
                    max = yInfo.getLeftChild().getMaxIdInSubtree();
                }
                this.requestRPCTo(z.ID, "setMaxIdInSubtree", max);

                // change node y
                this.requestRPCTo(y.ID, "changeLeftChildTo", z);
                this.requestRPCTo(y.ID, "setMinIdInSubtree", zInfo.getMinIdInSubtree());
            }

        } else {
            // forward
            this.forwardCBNetMessage(x.ID, this.cbnetMessage);
        }
    }

    /* DONE
	         *z                     x
	         / \        ->        /   \
	        y   d                y     z
	       / \                  / \   / \
	      a   x                a  *b *c  d
	         / \
	        b   c
	*/
    private void zigZagTopDown(CBInfo xInfo, CBInfo yInfo, CBInfo zInfo, CBInfo wInfo) {
        System.out.println("zig zag top down operation: " + ID);
        
        CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
        CBTreeLayer y = (CBTreeLayer) yInfo.getNode();
        CBTreeLayer z = (CBTreeLayer) zInfo.getNode();
        CBTreeLayer w = (CBTreeLayer) wInfo.getNode();

        if (true) {
            // forward message
            if (xInfo.getMinIdInSubtree() <= this.cbnetMessage.getDst() && this.cbnetMessage.getDst() < x.ID) {
                this.forwardCBNetMessage(xInfo.getLeftChild().ID, this.cbnetMessage);
            }
            {
                this.forwardCBNetMessage(xInfo.getRightChild().ID, this.cbnetMessage);
            }

            // set new child of node z
            if (wInfo.getLeftChild() == z) {
                this.requestRPCTo(w.ID, "changeLeftChildTo", x);
            } else {
                this.requestRPCTo(w.ID, "changeRightChildTo", x);
            }

            // deciding between lef or right zigzag operation
            if (y == zInfo.getLeftChild()) {
                // change node z
                this.requestRPCTo(z.ID, "changeLeftChildTo", (CBTreeLayer) xInfo.getRightChild());
                int min = z.ID;
                if (xInfo.getRightChild() != null) {
                    min = xInfo.getRightChild().getMinIdInSubtree();
                }
                this.requestRPCTo(z.ID, "setMinIdInSubtree", min);

                // change node y
                this.requestRPCTo(y.ID, "changeRightChildTo", (CBTreeLayer) xInfo.getLeftChild());
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
                this.requestRPCTo(z.ID, "changeRightChildTo", (CBTreeLayer) x.getLeftChild());
                int max = z.ID;
                if (x.getLeftChild() != null) {
                    max = x.getLeftChild().getMaxIdInSubtree();
                }
                this.requestRPCTo(z.ID, "setMaxIdInSubtree", max);

                // change node y
                this.requestRPCTo(y.ID, "changeLeftChildTo", (CBTreeLayer) xInfo.getRightChild());
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
        } else {
            // forward
            this.forwardCBNetMessage(z.ID, this.cbnetMessage);
        }
    }

    private double log2(long value) {
		return Math.log(value) / Math.log(2);
	}

    /*
              y                   x
            /   \               /   \
           x     c     ->      a     y
          / \                       / \
         a   b                     b   c
    */
    public double zigDiffRank(CBInfo xInfo, CBInfo yInfo) {
		RotationLayer x = (RotationLayer) xInfo.getNode();
		RotationLayer y = (RotationLayer) yInfo.getNode();
		
		//type of operation
		boolean leftZig = (x == yInfo.getLeftChild())? true: false;
		
		RotationLayer b = (RotationLayer) ((leftZig) ? xInfo.getRightChild(): xInfo.getLeftChild());
		
		long xOldWeight = xInfo.getWeight();
		long yOldWeight = yInfo.getWeight();
		
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

    /*
	            z                  *y
     	       / \                /   \
     		  y   d              x     z
	         / \       ->       / \   / \
	       *x   c              a   b c   d
	       / \                    
	      a   b                   
	*/
    private double zigZagDiffRank(CBInfo xInfo, CBInfo yInfo, CBInfo zInfo) {
		RotationLayer x = (RotationLayer) xInfo.getNode();
		RotationLayer y = (RotationLayer) yInfo.getNode();
		RotationLayer z = (RotationLayer) zInfo.getNode(); 
		
		boolean lefZigZag = (y == zInfo.getLeftChild())? true: false;
		
		RotationLayer b =  (RotationLayer) (lefZigZag ? xInfo.getLeftChild(): xInfo.getRightChild()); 
		RotationLayer c =  (RotationLayer) (lefZigZag ? xInfo.getRightChild(): xInfo.getLeftChild()); 
		
		long xOldWeight = xInfo.getWeight();
		long yOldWeight = yInfo.getWeight();
		long zOldWeight = zInfo.getWeight();
		
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
        this.cbnetMessage = null;

        if (this.rotating) {
            this.rotationCompleted();
            this.rotating = false;
        }
    }

    public void rotationCompleted() {

    }
    
}