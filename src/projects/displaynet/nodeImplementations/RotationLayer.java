package projects.displaynet.nodeImplementations;

import java.util.HashMap;

import projects.displaynet.tableEntry.NodeInfo;

/**
 * RotationLayer
 */
public abstract class RotationLayer extends ClusterLayer {

    private boolean rotating;

    @Override
    public void init() {
        super.init();

        this.rotating = false;
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

    public abstract void rotationCompleted();

}