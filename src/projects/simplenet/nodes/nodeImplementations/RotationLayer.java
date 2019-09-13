package projects.simplenet.nodes.nodeImplementations;

import java.util.HashMap;

import projects.cbnet.nodes.messages.CBNetMessage;
import projects.cbnet.nodes.nodeImplementations.CBTreeLayer;
import projects.cbnet.nodes.nodeImplementations.ClusterLayer;
import projects.cbnet.nodes.tableEntry.CBInfo;

/**
 * RotationLayer
 */
public abstract class RotationLayer extends ClusterLayer {

    private boolean rotating;
    private boolean routing;

    private CBNetMessage cbnetMessage;

    // private double epsilon = -2;

    @Override
    public void init() {
        super.init();

        this.rotating = false;
        this.routing = false;
        this.cbnetMessage = null;
    }

    @Override
    public void timeslot0() {
        this.tryOperation();
    }

    public void tryOperation() {

        if (this.hasCBNetMessage()) {
            // System.out.println("Node " + ID + "has cbnet message");

            this.cbnetMessage = this.getTopCBNetMessage();

            if (this.getMinIdInSubtree() <= cbnetMessage.getDst()
                    && cbnetMessage.getDst() <= this.getMaxIdInSubtree()) {
                this.sendRequestClusterDown(ID, cbnetMessage.getSrc(), cbnetMessage.getDst(),
                        cbnetMessage.getPriority());
            } else {
                this.sendRequestClusterUp(ID, cbnetMessage.getSrc(), cbnetMessage.getDst(), 
                        cbnetMessage.getPriority());
            }
        }

    }

    @Override
    public void clusterCompletedBottomUp(HashMap<String, CBInfo> cluster) {

        this.removeTopCBNetMesssage();
        this.rotateBottomUp(cluster);
        // System.out.println("Cluster formed bottom up at node " + ID);
    }

    @Override
    public void clusterCompletedTopDown(HashMap<String, CBInfo> cluster) {

        this.removeTopCBNetMesssage();
        this.rotateTopDown(cluster);
        // System.out.println("Cluster formed top down at node " + ID);
    }

    @Override
    public void targetNodeFound(CBInfo target) {

        this.removeTopCBNetMesssage();
        this.cbnetMessage.incrementRouting(); // DATA LOG
        this.forwardCBNetMessage(target.getNode().ID, this.cbnetMessage);
        // System.out.println("Cluster target found at node " + ID);
    }

    private void rotateBottomUp(HashMap<String, CBInfo> cluster) {
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
        // System.out.println("zig bottom up operation: " + ID);

        CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
        CBTreeLayer y = (CBTreeLayer) yInfo.getNode();
        CBTreeLayer z = (CBTreeLayer) zInfo.getNode();

        this.routing = true;

        // DATA LOG
        this.cbnetMessage.incrementRouting();
        // forward here
        this.forwardCBNetMessage(y.ID, this.cbnetMessage);
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
        // System.out.println("zig zig bottom up operation: " + ID);

        CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
        CBTreeLayer y = (CBTreeLayer) yInfo.getNode();
        CBTreeLayer z = (CBTreeLayer) zInfo.getNode();
        CBTreeLayer w = (CBTreeLayer) wInfo.getNode();

        this.routing = true;

        // DATA LOG
        this.cbnetMessage.incrementRouting();
        this.cbnetMessage.incrementRouting();
        // forward
        this.forwardCBNetMessage(z.ID, this.cbnetMessage);

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
        // System.out.println("zig zag bottom up operation: " + ID);

        CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
        CBTreeLayer y = (CBTreeLayer) yInfo.getNode();
        CBTreeLayer z = (CBTreeLayer) zInfo.getNode();
        CBTreeLayer w = (CBTreeLayer) wInfo.getNode();

        this.routing = true;

        // DATA LOG
        this.cbnetMessage.incrementRouting();
        this.cbnetMessage.incrementRouting();
        // forward
        this.forwardCBNetMessage(z.ID, this.cbnetMessage);

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
        // System.out.println("zig zig top down operation: " + ID);

        CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
        CBTreeLayer y = (CBTreeLayer) yInfo.getNode();
        CBTreeLayer z = (CBTreeLayer) zInfo.getNode();
        CBTreeLayer w = (CBTreeLayer) wInfo.getNode();

        this.routing = true;

        // DATA LOG
        this.cbnetMessage.incrementRouting();
        this.cbnetMessage.incrementRouting();
        // forward
        this.forwardCBNetMessage(x.ID, this.cbnetMessage);

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
        // System.out.println("zig zag top down operation: " + ID);

        CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
        CBTreeLayer y = (CBTreeLayer) yInfo.getNode();
        CBTreeLayer z = (CBTreeLayer) zInfo.getNode();
        CBTreeLayer w = (CBTreeLayer) wInfo.getNode();

        this.routing = true;

        // DATA LOG
        this.cbnetMessage.incrementRouting();
        this.cbnetMessage.incrementRouting();
        // forward
        this.forwardCBNetMessage(x.ID, this.cbnetMessage);
        
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

        // type of operation
        boolean leftZig = (x == yInfo.getLeftChild()) ? true : false;

        RotationLayer b = (RotationLayer) ((leftZig) ? xInfo.getRightChild() : xInfo.getLeftChild());

        long xOldWeight = xInfo.getWeight();
        long yOldWeight = yInfo.getWeight();

        long bWeight = (b != null) ? b.getWeight() : 0;

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
			  z					*x
			 / \               /   \
			y   d             y     z
		   / \		   ->    / \   / \
		  a  *x             a   b c   d
		     / \
		    b	c 
	*/
    // private double zigZagDiffRank(CBInfo xInfo, CBInfo yInfo, CBInfo zInfo) {
    //     RotationLayer x = (RotationLayer) xInfo.getNode();
    //     RotationLayer y = (RotationLayer) yInfo.getNode();
    //     RotationLayer z = (RotationLayer) zInfo.getNode();

    //     boolean lefZigZag = (y == zInfo.getLeftChild()) ? true : false;

    //     RotationLayer b = (RotationLayer) (lefZigZag ? xInfo.getLeftChild() : xInfo.getRightChild());
    //     RotationLayer c = (RotationLayer) (lefZigZag ? xInfo.getRightChild() : xInfo.getLeftChild());

    //     long xOldWeight = xInfo.getWeight();
    //     long yOldWeight = yInfo.getWeight();
    //     long zOldWeight = zInfo.getWeight();

    //     long bWeight = (b != null) ? b.getWeight() : 0;
    //     long cWeight = (c != null) ? c.getWeight() : 0;

    //     long yNewWeight = yOldWeight - xOldWeight + bWeight;
    //     long zNewWeight = zOldWeight - yOldWeight + cWeight;
    //     long xNewWeight = xOldWeight - bWeight - cWeight + yNewWeight + zNewWeight;

    //     double xOldRank = (xOldWeight == 0) ? 0 : log2(xOldWeight);
    //     double yOldRank = (yOldWeight == 0) ? 0 : log2(yOldWeight);
    //     double zOldRank = (zOldWeight == 0) ? 0 : log2(zOldWeight);
    //     double xNewRank = (xNewWeight == 0) ? 0 : log2(xNewWeight);
    //     double yNewRank = (yNewWeight == 0) ? 0 : log2(yNewWeight);
    //     double zNewRank = (zNewWeight == 0) ? 0 : log2(zNewWeight);

    //     double deltaRank = xNewRank + yNewRank + zNewRank - xOldRank - yOldRank - zOldRank;

    //     return deltaRank;
    // }

    @Override
    public void timeslot10() {

        this.executeAllRPC();
        this.clearRPCQueue();
        this.cbnetMessage = null;

        if (this.rotating) {

            this.rotationCompleted();
            this.rotating = false;

        } else if (this.routing) {

            this.forwardCompleted();
            this.routing = false;

        }
    }

    public void rotationCompleted() {

    }

    public void forwardCompleted() {

    }

}