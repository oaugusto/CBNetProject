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

        NodeInfo x = cluster.get("x");
        NodeInfo y = cluster.get("y");
        NodeInfo z = cluster.get("z");

        if (cluster.size() == 3) {

            this.zig(x, y, z);

        } else {

            NodeInfo w = cluster.get("w");
            int xId = x.getNode().ID;
            int yId = x.getNode().ID;

            if ((xId == y.getLeftChild().ID && yId == z.getLeftChild().ID)
                    || (xId == y.getRightChild().ID && yId == z.getRightChild().ID)) {
                this.zigZig(x, y, z, w);
            } else {
                this.zigZag(x, y, z, w);
            }
        }

    }

    //TODO set nodes range in subtree

    /*
                  z                     z
                 /                    /
                y                   x
              /   \               /   \
             x     c     ->      a     y
            / \                       / \
           a   b                     b   c
	 
	 */
    private void zig(NodeInfo x, NodeInfo y, NodeInfo z) {

        int xId = x.getNode().ID;
        int yId = y.getNode().ID;
        int zId = z.getNode().ID;

        //set the new child of node z

        if (z.getLeftChild().ID == yId) {
            this.requestRPCTo(zId, "changeLeftChildTo", x.getNode());
        } else {
            this.requestRPCTo(zId, "changeRightChildTo", x.getNode());
        }

        // left zig operetion
        if (y.getLeftChild().ID == xId) {
            this.requestRPCTo(yId, "changeLeftChildTo", x.getRightChild()); // change node y
            this.requestRPCTo(xId, "changeRightChildTo", y.getNode()); // change node x
        } else {
            this.requestRPCTo(yId, "changeRightChildTo", x.getLeftChild()); // change node y
            this.requestRPCTo(xId, "changeLeftChildTo", y.getNode()); // change node x
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
    private void zigZig(NodeInfo x, NodeInfo y, NodeInfo z, NodeInfo w) {

        int xId = x.getNode().ID;
        int yId = y.getNode().ID;
        int zId = z.getNode().ID;
        int wId = w.getNode().ID;

        // set new child of node z

        if (w.getLeftChild().ID == zId) {
            this.requestRPCTo(wId, "changeLeftChildTo", x.getNode());
        } else {
            this.requestRPCTo(wId, "changeRightChildTo", x.getNode());
        }

        // deciding between lef or right zigzig operation
        if (yId == z.getLeftChild().ID) {
            this.requestRPCTo(zId, "changeLeftChildTo", y.getRightChild()); // change node z
            this.requestRPCTo(yId, "changeLeftChildTo", x.getRightChild()); // change node y
            this.requestRPCTo(yId, "changeRightChildTo", z.getNode());
            this.requestRPCTo(xId, "changeRightChildTo", y.getNode()); // change node x
        } else {
            this.requestRPCTo(zId, "changeRightChildTo", y.getLeftChild()); // change node z
            this.requestRPCTo(yId, "changeRightChildTo", x.getLeftChild()); // change node y
            this.requestRPCTo(yId, "changeLeftChildTo", z.getNode());
            this.requestRPCTo(xId, "changeLeftChildTo", y.getNode()); // change node x
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
    private void zigZag(NodeInfo x, NodeInfo y, NodeInfo z, NodeInfo w) {

        int xId = x.getNode().ID;
        int yId = y.getNode().ID;
        int zId = z.getNode().ID;
        int wId = z.getNode().ID;

        // set new child of node z

        if (w.getLeftChild().ID == yId) {
            this.requestRPCTo(wId, "changeLeftChildTo", x.getNode());
        } else {
            this.requestRPCTo(wId, "changeRightChildTo", x.getNode());
        }

         // deciding between lef or right zigzag operation
         if (yId == z.getLeftChild().ID) {
            this.requestRPCTo(zId, "changeLeftChildTo", x.getRightChild()); // change node z
            this.requestRPCTo(yId, "changeRightChildTo", x.getLeftChild()); // change node y
            this.requestRPCTo(xId, "changeLeftChildTo", y.getNode()); // change node x
            this.requestRPCTo(xId, "changeRightChildTo", z.getNode()); // change node x
        } else {
            this.requestRPCTo(zId, "changeRightChildTo", x.getLeftChild()); // change node z
            this.requestRPCTo(yId, "changeLeftChildTo", x.getRightChild()); // change node y
            this.requestRPCTo(xId, "changeRightChildTo", y.getNode()); // change node x
            this.requestRPCTo(xId, "changeLeftChildTo", z.getNode()); // change node x
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