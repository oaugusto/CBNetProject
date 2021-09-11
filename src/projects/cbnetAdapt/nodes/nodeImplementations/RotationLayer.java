package projects.cbnetAdapt.nodes.nodeImplementations;

import java.util.HashMap;

import projects.cbnetAdapt.nodes.messages.CBNetMessage;
import projects.cbnetAdapt.nodes.tableEntry.CBInfo;

/**
 * RotationLayer
 */
public abstract class RotationLayer extends ClusterLayer {

  private boolean rotating;
  private boolean routing;

  private CBNetMessage cbnetMessage;

  private double epsilon = -1.5;

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
    
    this.incrementWeight();
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

  /*
               z                 z
              /                 /
             y                *x
            / \               / \
          *x   c     -->     a   y
          / \                   / \
         a   b                 b   c
  */
  private void zigBottomUp(CBInfo xInfo, CBInfo yInfo, CBInfo zInfo) {
//     System.out.println("zig bottom up operation: " + ID);

    CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
    CBTreeLayer y = (CBTreeLayer) yInfo.getNode();
//    CBTreeLayer z = (CBTreeLayer) zInfo.getNode();
    
    // type of operation
//    boolean leftZig = x == yInfo.getLeftChild();
    
    // update weights before step ------------------------------------------
    
    //long cWeight = ()
    //long actualWeight;
    
    
//    long yNewWeight;
//    long xNewWeight;
        
    // ---------------------------------------------------------------------

//    double deltaRank = this.zigDiffRank(xInfo, yInfo);

    //if (true) { //only rotate
    //if (false) { //only forward the message
//    if (deltaRank < this.epsilon) {
//      this.rotating = true;
//
//      // DATA LOG
//      this.cbnetMessage.incrementRotations();
//      // forward message
//      this.forwardCBNetMessage(x.ID, this.cbnetMessage);
//
//      // set the new child of node z
//      if (zInfo.getLeftChild() == y) {
//        this.requestRPCTo(z.ID, "changeLeftChildTo", x);
//      } else {
//        this.requestRPCTo(z.ID, "changeRightChildTo", x);
//      }
//
//      // calculate the new rank of nodes after rotation
//      // type of operation----------------------------------------------------
//
//      RotationLayer b = (RotationLayer) ((leftZig) ? xInfo.getRightChild() : xInfo.getLeftChild());
//
//      long xOldWeight = xInfo.getWeight();
//      long yOldWeight = yInfo.getWeight();
//
//      long bWeight = (b != null) ? b.getWeight() : 0;
//
//      yNewWeight = yOldWeight - xOldWeight + bWeight;
//      xNewWeight = xOldWeight - bWeight + yNewWeight;
//      // ---------------------------------------------------------------------
//
//      // left zig operetion
//      if (yInfo.getLeftChild() == x) {
//        // change node y
//        this.requestRPCTo(y.ID, "changeLeftChildTo", (CBTreeLayer) xInfo.getRightChild());
//        int min = y.ID;
//        if (xInfo.getRightChild() != null) {
//          min = xInfo.getRightChild().getMinIdInSubtree();
//        }
//        this.requestRPCTo(y.ID, "setMinIdInSubtree", min);
//        this.requestRPCTo(y.ID, "setWeight", yNewWeight);
//
//        // change node x
//        this.requestRPCTo(x.ID, "changeRightChildTo", y);
//        this.requestRPCTo(x.ID, "setMaxIdInSubtree", yInfo.getMaxIdInSubtree());
//        this.requestRPCTo(x.ID, "setWeight", xNewWeight);
//      } else {
//        // change node y
//        this.requestRPCTo(y.ID, "changeRightChildTo", (CBTreeLayer) xInfo.getLeftChild());
//        int max = y.ID;
//        if (xInfo.getLeftChild() != null) {
//          max = xInfo.getLeftChild().getMaxIdInSubtree();
//        }
//        this.requestRPCTo(y.ID, "setMaxIdInSubtree", max);
//        this.requestRPCTo(y.ID, "setWeight", yNewWeight);
//
//        // change node x
//        this.requestRPCTo(x.ID, "changeLeftChildTo", y);
//        this.requestRPCTo(x.ID, "setMinIdInSubtree", yInfo.getMinIdInSubtree());
//        this.requestRPCTo(x.ID, "setWeight", xNewWeight);
//      }
//
//    } else {
    this.routing = true;

    // DATA LOG
    this.cbnetMessage.incrementRouting();
    // increment counters
    this.requestRPCTo(x.ID, "setWeight", xInfo.getWeight() + 1);
    if (y.ID == cbnetMessage.getDst()) {
  	  this.requestRPCTo(y.ID, "setWeight", yInfo.getWeight() + 1);
    }
    // forward here
    this.forwardCBNetMessage(y.ID, this.cbnetMessage);
//    }
  }

  /*
              z                 *y
             / \               /   \
            y   d             x     z
           / \      -->      / \   / \
         *x   c             a   b c   d
         / \
        a   b
  */
  private void zigZigBottomUp(CBInfo xInfo, CBInfo yInfo, CBInfo zInfo, CBInfo wInfo) {
//     System.out.println("zig zig bottom up operation: " + ID);

    CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
    CBTreeLayer y = (CBTreeLayer) yInfo.getNode();
    CBTreeLayer z = (CBTreeLayer) zInfo.getNode();
    CBTreeLayer w = (CBTreeLayer) wInfo.getNode();

    double deltaRank = this.zigDiffRank(yInfo, zInfo);

//     if (true) {
//     if (false) {
    if (deltaRank < this.epsilon) {
      this.rotating = true;

      // DATA LOG
      this.cbnetMessage.incrementRotations();
      this.cbnetMessage.incrementRouting();
      // forward message
      this.forwardCBNetMessage(y.ID, this.cbnetMessage);

      // set new child of node z
      if (wInfo.getLeftChild() == z) {
        this.requestRPCTo(w.ID, "changeLeftChildTo", y);
      } else {
        this.requestRPCTo(w.ID, "changeRightChildTo", y);
      }

      // calculate the new rank of nodes
      // type of operation----------------------------------------------------
      boolean leftZig = y == zInfo.getLeftChild();

      RotationLayer b = (RotationLayer) ((leftZig) ? yInfo.getRightChild() : yInfo.getLeftChild());

      long yOldWeight = yInfo.getWeight();
      long zOldWeight = zInfo.getWeight();

      long bWeight = (b != null) ? b.getWeight() : 0;

      long zNewWeight = zOldWeight - yOldWeight + bWeight;
      long yNewWeight = yOldWeight - bWeight + zNewWeight;
      // ---------------------------------------------------------------------

      //increment x counter
      this.requestRPCTo(x.ID, "setWeight", xInfo.getWeight() + 1);
      
      // left zig operetion on node y
      if (zInfo.getLeftChild() == y) {
        // change node z
        this.requestRPCTo(z.ID, "changeLeftChildTo", (CBTreeLayer) yInfo.getRightChild());
        int min = z.ID;
        if (yInfo.getRightChild() != null) {
          min = yInfo.getRightChild().getMinIdInSubtree();
        }
        this.requestRPCTo(z.ID, "setMinIdInSubtree", min);
        this.requestRPCTo(z.ID, "setWeight", zNewWeight);

        // change node y
        this.requestRPCTo(y.ID, "changeRightChildTo", z);
        this.requestRPCTo(y.ID, "setMaxIdInSubtree", zInfo.getMaxIdInSubtree());
        this.requestRPCTo(y.ID, "setWeight", yNewWeight);
      } else {
        // change node z
        this.requestRPCTo(z.ID, "changeRightChildTo", (CBTreeLayer) yInfo.getLeftChild());
        int max = z.ID;
        if (yInfo.getLeftChild() != null) {
          max = yInfo.getLeftChild().getMaxIdInSubtree();
        }
        this.requestRPCTo(z.ID, "setMaxIdInSubtree", max);
        this.requestRPCTo(z.ID, "setWeight", zNewWeight);

        // change node y
        this.requestRPCTo(y.ID, "changeLeftChildTo", z);
        this.requestRPCTo(y.ID, "setMinIdInSubtree", zInfo.getMinIdInSubtree());
        this.requestRPCTo(y.ID, "setWeight", yNewWeight);
      }
    } else {
      this.routing = true;

      // DATA LOG
      this.cbnetMessage.incrementRouting();
      this.cbnetMessage.incrementRouting();
      // increment counters
      this.requestRPCTo(x.ID, "setWeight", xInfo.getWeight() + 1);
      this.requestRPCTo(y.ID, "setWeight", yInfo.getWeight() + 1);
      if (z.ID == cbnetMessage.getDst()) {
    	  this.requestRPCTo(z.ID, "setWeight", zInfo.getWeight() + 1);
      }
      // forward
      this.forwardCBNetMessage(z.ID, this.cbnetMessage);
    }

  }

  /*
                 w                  w
                /                  /
               z				 *x
              / \               /   \
             y   d             y     z
            / \		  -->     / \   / \
           a   x*            a   b c   d
              / \
             b   c
 */
  private void zigZagBottomUp(CBInfo xInfo, CBInfo yInfo, CBInfo zInfo, CBInfo wInfo) {
//     System.out.println("zig zag bottom up operation: " + ID);

    CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
    CBTreeLayer y = (CBTreeLayer) yInfo.getNode();
    CBTreeLayer z = (CBTreeLayer) zInfo.getNode();
    CBTreeLayer w = (CBTreeLayer) wInfo.getNode();

    double deltaRank = this.zigZagDiffRank(xInfo, yInfo, zInfo);

//    if (true) {
//     if (false) {
    if (deltaRank < this.epsilon) {
      this.rotating = true;

      // DATA LOG
      this.cbnetMessage.incrementRotations();
      this.cbnetMessage.incrementRotations();
      // forward message
      this.forwardCBNetMessage(x.ID, this.cbnetMessage);

      // set new child of node z
      if (wInfo.getLeftChild() == z) {
        this.requestRPCTo(w.ID, "changeLeftChildTo", x);
      } else {
        this.requestRPCTo(w.ID, "changeRightChildTo", x);
      }

      // new weights------------------------------------------------------
      boolean lefZigZag = y == zInfo.getLeftChild();

      RotationLayer b = (RotationLayer) (lefZigZag ? xInfo.getLeftChild() : xInfo.getRightChild());
      RotationLayer c = (RotationLayer) (lefZigZag ? xInfo.getRightChild() : xInfo.getLeftChild());

      long xOldWeight = xInfo.getWeight();
      long yOldWeight = yInfo.getWeight();
      long zOldWeight = zInfo.getWeight();

      long bWeight = (b != null) ? b.getWeight() : 0;
      long cWeight = (c != null) ? c.getWeight() : 0;

      long yNewWeight = yOldWeight - xOldWeight + bWeight;
      long zNewWeight = zOldWeight - yOldWeight + cWeight;
      long xNewWeight = xOldWeight - bWeight - cWeight + yNewWeight + zNewWeight;
      // ---------------------------------------------------------------

      // deciding between left or right zigzag operation
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
        this.requestRPCTo(x.ID, "setWeight", xNewWeight);
        
        //increment counters based on nodes position
        if (x.ID == cbnetMessage.getSrc()) {
        	this.requestRPCTo(z.ID, "setWeight", zNewWeight);
        	this.requestRPCTo(y.ID, "setWeight", yNewWeight);
        } else if (x.ID < cbnetMessage.getSrc()
                && cbnetMessage.getSrc() <= this.getMaxIdInSubtree()) {
        	this.requestRPCTo(z.ID, "setWeight", zNewWeight + 1);
        } else {
        	this.requestRPCTo(y.ID, "setWeight", yNewWeight + 1);
        }

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
        this.requestRPCTo(x.ID, "setWeight", xNewWeight);
        
        //increment counters based on nodes position
        if (x.ID == cbnetMessage.getSrc()) {
        	this.requestRPCTo(z.ID, "setWeight", zNewWeight);
        	this.requestRPCTo(y.ID, "setWeight", yNewWeight);
        } else if (x.ID < cbnetMessage.getSrc()
                && cbnetMessage.getSrc() <= this.getMaxIdInSubtree()) {
        	this.requestRPCTo(y.ID, "setWeight", yNewWeight + 1);
        } else {
        	this.requestRPCTo(z.ID, "setWeight", zNewWeight + 1);
        }
        
      }
    } else {
      this.routing = true;

      // DATA LOG
      this.cbnetMessage.incrementRouting();
      this.cbnetMessage.incrementRouting();
      // increment counters
      this.requestRPCTo(x.ID, "setWeight", xInfo.getWeight() + 1);
      this.requestRPCTo(y.ID, "setWeight", yInfo.getWeight() + 1);
      if (z.ID == cbnetMessage.getDst()) {
    	  this.requestRPCTo(z.ID, "setWeight", zInfo.getWeight() + 1);
      }
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

  /*
             *z                   y
            / \                 /   \
           y   d     -->      *x     z
          / \                 / \   / \
         x   c               a   b c   d
        / \
       a   b
  */
  private void zigZigTopDown(CBInfo xInfo, CBInfo yInfo, CBInfo zInfo, CBInfo wInfo) {
//     System.out.println("zig zig top down operation: " + ID);

    CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
    CBTreeLayer y = (CBTreeLayer) yInfo.getNode();
    CBTreeLayer z = (CBTreeLayer) zInfo.getNode();
    CBTreeLayer w = (CBTreeLayer) wInfo.getNode();

    double deltaRank = this.zigDiffRank(yInfo, zInfo);

//    if (true) {
//     if (false) {
    if (deltaRank < this.epsilon) {
      this.rotating = true;

      // DATA LOG
      this.cbnetMessage.incrementRotations();
      this.cbnetMessage.incrementRouting();
      // forward message
      this.forwardCBNetMessage(x.ID, this.cbnetMessage);

      // set new child of node z
      if (wInfo.getLeftChild() == z) {
        this.requestRPCTo(w.ID, "changeLeftChildTo", y);
      } else {
        this.requestRPCTo(w.ID, "changeRightChildTo", y);
      }

      // calculate the new rank of nodes
      // type of operation----------------------------------------------------
      boolean leftZig = y == zInfo.getLeftChild();

      RotationLayer b = (RotationLayer) ((leftZig) ? yInfo.getRightChild() : yInfo.getLeftChild());

      long yOldWeight = yInfo.getWeight();
      long zOldWeight = zInfo.getWeight();

      long bWeight = (b != null) ? b.getWeight() : 0;

      long zNewWeight = zOldWeight - yOldWeight + bWeight;
      long yNewWeight = yOldWeight - bWeight + zNewWeight;
      // ---------------------------------------------------------------------

      // left zig operetion on node y
      if (zInfo.getLeftChild() == y) {
        // change node z
        this.requestRPCTo(z.ID, "changeLeftChildTo", (CBTreeLayer) yInfo.getRightChild());
        int min = z.ID;
        if (yInfo.getRightChild() != null) {
          min = yInfo.getRightChild().getMinIdInSubtree();
        }
        this.requestRPCTo(z.ID, "setMinIdInSubtree", min);
        // increment z counter
        if (z.ID <= cbnetMessage.getSrc()
                && cbnetMessage.getSrc() <= this.getMaxIdInSubtree()) {
        	this.requestRPCTo(z.ID, "setWeight", zNewWeight + 1);
        } else {
        	this.requestRPCTo(z.ID, "setWeight", zNewWeight);
        }

        // change node y
        this.requestRPCTo(y.ID, "changeRightChildTo", z);
        this.requestRPCTo(y.ID, "setMaxIdInSubtree", zInfo.getMaxIdInSubtree());
        // increment y counter
        this.requestRPCTo(y.ID, "setWeight", yNewWeight + 1);
        if (x.ID == cbnetMessage.getDst()) {
      	  this.requestRPCTo(x.ID, "setWeight", xInfo.getWeight() + 1);
        }
        
      } else {
        // change node z
        this.requestRPCTo(z.ID, "changeRightChildTo", (CBTreeLayer) yInfo.getLeftChild());
        int max = z.ID;
        if (yInfo.getLeftChild() != null) {
          max = yInfo.getLeftChild().getMaxIdInSubtree();
        }
        this.requestRPCTo(z.ID, "setMaxIdInSubtree", max);
        // increment z counter
        if (this.getMinIdInSubtree() <= cbnetMessage.getSrc()
                && cbnetMessage.getSrc() <= z.ID) {
        	this.requestRPCTo(z.ID, "setWeight", zNewWeight + 1);
        } else {
        	this.requestRPCTo(z.ID, "setWeight", zNewWeight);
        }

        // change node y
        this.requestRPCTo(y.ID, "changeLeftChildTo", z);
        this.requestRPCTo(y.ID, "setMinIdInSubtree", zInfo.getMinIdInSubtree());
        // increment y counter
        this.requestRPCTo(y.ID, "setWeight", yNewWeight + 1);
        if (x.ID == cbnetMessage.getDst()) {
      	  this.requestRPCTo(x.ID, "setWeight", xInfo.getWeight() + 1);
        }
        
      }

    } else {
      this.routing = true;

      // DATA LOG
      this.cbnetMessage.incrementRouting();
      this.cbnetMessage.incrementRouting();
      // increment counters
      this.requestRPCTo(z.ID, "setWeight", zInfo.getWeight() + 1);
      this.requestRPCTo(y.ID, "setWeight", yInfo.getWeight() + 1);
      if (x.ID == cbnetMessage.getDst()) {
    	  this.requestRPCTo(x.ID, "setWeight", xInfo.getWeight() + 1);
      }
      // forward
      this.forwardCBNetMessage(x.ID, this.cbnetMessage);
    }
  }

  /*
         *z                     x
         / \        -->       /   \
        y   d                y     z
       / \                  / \   / \
      a   x                a  *b *c  d
         / \
        b   c
  */
  private void zigZagTopDown(CBInfo xInfo, CBInfo yInfo, CBInfo zInfo, CBInfo wInfo) {
//     System.out.println("zig zag top down operation: " + ID);

    CBTreeLayer x = (CBTreeLayer) xInfo.getNode();
    CBTreeLayer y = (CBTreeLayer) yInfo.getNode();
    CBTreeLayer z = (CBTreeLayer) zInfo.getNode();
    CBTreeLayer w = (CBTreeLayer) wInfo.getNode();

    double deltaRank = this.zigZagDiffRank(xInfo, yInfo, zInfo);

//    if (true) {
//     if (false) {
    if (deltaRank < this.epsilon) {
      this.rotating = true;

      // DATA LOG
      this.cbnetMessage.incrementRotations();
      this.cbnetMessage.incrementRotations();
      // forward message
      if (x.ID == this.cbnetMessage.getDst()) {
    	  this.forwardCBNetMessage(xInfo.getNode().ID, this.cbnetMessage);
  	 } else if (xInfo.getMinIdInSubtree() <= this.cbnetMessage.getDst()
          && this.cbnetMessage.getDst() < x.ID) {
        this.forwardCBNetMessage(xInfo.getLeftChild().ID, this.cbnetMessage);
      } else {
        this.forwardCBNetMessage(xInfo.getRightChild().ID, this.cbnetMessage);
      }

      // set new child of node z
      if (wInfo.getLeftChild() == z) {
        this.requestRPCTo(w.ID, "changeLeftChildTo", x);
      } else {
        this.requestRPCTo(w.ID, "changeRightChildTo", x);
      }

      // new weights------------------------------------------------------
      boolean lefZigZag = y == zInfo.getLeftChild();

      RotationLayer b = (RotationLayer) (lefZigZag ? xInfo.getLeftChild() : xInfo.getRightChild());
      RotationLayer c = (RotationLayer) (lefZigZag ? xInfo.getRightChild() : xInfo.getLeftChild());

      long xOldWeight = xInfo.getWeight();
      long yOldWeight = yInfo.getWeight();
      long zOldWeight = zInfo.getWeight();

      long bWeight = (b != null) ? b.getWeight() : 0;
      long cWeight = (c != null) ? c.getWeight() : 0;

      long yNewWeight = yOldWeight - xOldWeight + bWeight;
      long zNewWeight = zOldWeight - yOldWeight + cWeight;
      long xNewWeight = xOldWeight - bWeight - cWeight + yNewWeight + zNewWeight;
      // ---------------------------------------------------------------

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
        
        // increment counters
        if (z.ID <= cbnetMessage.getSrc()
                && cbnetMessage.getSrc() <= this.getMaxIdInSubtree()) {
        	if (x.ID == this.cbnetMessage.getDst()) {
        		this.requestRPCTo(x.ID, "setWeight", xNewWeight + 1);
        		this.requestRPCTo(y.ID, "setWeight", yNewWeight);
        		this.requestRPCTo(z.ID, "setWeight", zNewWeight + 1);
        	} else if (xInfo.getMinIdInSubtree() <= this.cbnetMessage.getDst()
        	          && this.cbnetMessage.getDst() < x.ID) {
        		//y+1, x+1, z+1
        		this.requestRPCTo(x.ID, "setWeight", xNewWeight + 1);
        		this.requestRPCTo(y.ID, "setWeight", yNewWeight + 1);
        		this.requestRPCTo(z.ID, "setWeight", zNewWeight + 1);
        	} else {
        		//z+1
        		this.requestRPCTo(x.ID, "setWeight", xNewWeight);
        		this.requestRPCTo(y.ID, "setWeight", yNewWeight);
        		this.requestRPCTo(z.ID, "setWeight", zNewWeight + 1);
        	}
        } else {
        	if (x.ID == this.cbnetMessage.getDst()) {
        		this.requestRPCTo(x.ID, "setWeight", xNewWeight + 1);
        		this.requestRPCTo(y.ID, "setWeight", yNewWeight);
        		this.requestRPCTo(z.ID, "setWeight", zNewWeight);
        	} else if (xInfo.getMinIdInSubtree() <= this.cbnetMessage.getDst()
      	          && this.cbnetMessage.getDst() < x.ID) {
        		//y+1, x+1
        		this.requestRPCTo(x.ID, "setWeight", xNewWeight + 1);
        		this.requestRPCTo(y.ID, "setWeight", yNewWeight + 1);
        		this.requestRPCTo(z.ID, "setWeight", zNewWeight);
        	} else {
        		//z+1, x+1
        		this.requestRPCTo(x.ID, "setWeight", xNewWeight + 1);
        		this.requestRPCTo(y.ID, "setWeight", yNewWeight);
        		this.requestRPCTo(z.ID, "setWeight", zNewWeight + 1);
        	}
        }

      } else {

        // change node z
        this.requestRPCTo(z.ID, "changeRightChildTo", (CBTreeLayer) x.getLeftChild());
        int max = z.ID;
        if (x.getLeftChild() != null) {
          max = x.getLeftChild().getMaxIdInSubtree();
        }
        this.requestRPCTo(z.ID, "setMaxIdInSubtree", max);
        this.requestRPCTo(z.ID, "setWeight", zNewWeight);

        // change node y
        this.requestRPCTo(y.ID, "changeLeftChildTo", (CBTreeLayer) xInfo.getRightChild());
        int min = y.ID;
        if (xInfo.getRightChild() != null) {
          min = xInfo.getRightChild().getMinIdInSubtree();
        }
        this.requestRPCTo(y.ID, "setMinIdInSubtree", min);
        this.requestRPCTo(y.ID, "setWeight", yNewWeight);

        // change node x
        this.requestRPCTo(x.ID, "changeRightChildTo", y);
        this.requestRPCTo(x.ID, "setMaxIdInSubtree", yInfo.getMaxIdInSubtree());
        this.requestRPCTo(x.ID, "changeLeftChildTo", z);
        this.requestRPCTo(x.ID, "setMinIdInSubtree", zInfo.getMinIdInSubtree());
        this.requestRPCTo(x.ID, "setWeight", xNewWeight);
        
        // increment counters
        if (this.getMaxIdInSubtree() <= cbnetMessage.getSrc()
                && cbnetMessage.getSrc() <= z.ID) {
        	if (x.ID == this.cbnetMessage.getDst()) {
        		this.requestRPCTo(x.ID, "setWeight", xNewWeight + 1);
        		this.requestRPCTo(y.ID, "setWeight", yNewWeight);
        		this.requestRPCTo(z.ID, "setWeight", zNewWeight + 1);
        	} else if (xInfo.getMinIdInSubtree() <= this.cbnetMessage.getDst()
        	          && this.cbnetMessage.getDst() < x.ID) {
        		//z+1
        		this.requestRPCTo(x.ID, "setWeight", xNewWeight);
        		this.requestRPCTo(y.ID, "setWeight", yNewWeight);
        		this.requestRPCTo(z.ID, "setWeight", zNewWeight + 1);
        	} else {
        		//y+1, x+1, z+1
        		this.requestRPCTo(x.ID, "setWeight", xNewWeight + 1);
        		this.requestRPCTo(y.ID, "setWeight", yNewWeight + 1);
        		this.requestRPCTo(z.ID, "setWeight", zNewWeight + 1);
        	}
        } else {
        	if (x.ID == this.cbnetMessage.getDst()) { //TODO
        		this.requestRPCTo(x.ID, "setWeight", xNewWeight + 1);
        		this.requestRPCTo(y.ID, "setWeight", yNewWeight);
        		this.requestRPCTo(z.ID, "setWeight", zNewWeight);
        	} else if (xInfo.getMinIdInSubtree() <= this.cbnetMessage.getDst()
      	          && this.cbnetMessage.getDst() < x.ID) {
        		//x+1, z+1
        		this.requestRPCTo(x.ID, "setWeight", xNewWeight + 1);
        		this.requestRPCTo(y.ID, "setWeight", yNewWeight);
        		this.requestRPCTo(z.ID, "setWeight", zNewWeight + 1);
        	} else {
        		//y+1, x+1
        		this.requestRPCTo(x.ID, "setWeight", xNewWeight + 1);
        		this.requestRPCTo(y.ID, "setWeight", yNewWeight + 1);
        		this.requestRPCTo(z.ID, "setWeight", zNewWeight);
        	}
        }
      }
    } else {
      this.routing = true;

      // DATA LOG
      this.cbnetMessage.incrementRouting();
      this.cbnetMessage.incrementRouting();
      // increment counters
      this.requestRPCTo(z.ID, "setWeight", zInfo.getWeight() + 1);
      this.requestRPCTo(y.ID, "setWeight", yInfo.getWeight() + 1);
      if (x.ID == cbnetMessage.getDst()) {
    	  this.requestRPCTo(x.ID, "setWeight", xInfo.getWeight() + 1);
      }
      // forward
      this.forwardCBNetMessage(x.ID, this.cbnetMessage);
    }
  }

  private double log2(long value) {
    return Math.log(value) / Math.log(2);
  }

  /*
            y                   x
          /   \               /   \
         x     c     -->     a     y
        / \                       / \
       a   b                     b   c
  */
  public double zigDiffRank(CBInfo xInfo, CBInfo yInfo) {
    RotationLayer x = (RotationLayer) xInfo.getNode();
    // RotationLayer y = (RotationLayer) yInfo.getNode();

    // type of operation
    boolean leftZig = x == yInfo.getLeftChild();

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
         z					   *x
        / \                   /   \
       y   d                 y     z
          / \		 -->    / \   / \
         a  *x             a   b c   d
            / \
           b   c
  */
  private double zigZagDiffRank(CBInfo xInfo, CBInfo yInfo, CBInfo zInfo) {
    // RotationLayer x = (RotationLayer) xInfo.getNode();
    RotationLayer y = (RotationLayer) yInfo.getNode();
    // RotationLayer z = (RotationLayer) zInfo.getNode();

    boolean lefZigZag = y == zInfo.getLeftChild();

    RotationLayer b = (RotationLayer) (lefZigZag ? xInfo.getLeftChild() : xInfo.getRightChild());
    RotationLayer c = (RotationLayer) (lefZigZag ? xInfo.getRightChild() : xInfo.getLeftChild());

    long xOldWeight = xInfo.getWeight();
    long yOldWeight = yInfo.getWeight();
    long zOldWeight = zInfo.getWeight();

    long bWeight = (b != null) ? b.getWeight() : 0;
    long cWeight = (c != null) ? c.getWeight() : 0;

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
    
    super.timeslot10();
  }

  public void rotationCompleted() {
	  
  }

  public void forwardCompleted() {
	  
  }

}