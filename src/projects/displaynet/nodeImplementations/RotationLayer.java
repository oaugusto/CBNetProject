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

    private void zig(NodeInfo x, NodeInfo y, NodeInfo z) {

        if (z.getLeftChild().ID == y.getNode().ID) {
            this.requestRPCTo(z.getNode().ID, "changeLeftChildTo", x.getNode());
        } else {
            this.requestRPCTo(z.getNode().ID, "changeRightChildTo", x.getNode());
        }
    }

    private void zigZig(NodeInfo x, NodeInfo y, NodeInfo z, NodeInfo w) {

        if (z.getLeftChild().ID == y.getNode().ID) {
            this.requestRPCTo(z.getNode().ID, "changeLeftChildTo", x.getNode());
        } else {
            this.requestRPCTo(z.getNode().ID, "changeRightChildTo", x.getNode());
        }
    }

    private void zigZag(NodeInfo x, NodeInfo y, NodeInfo z, NodeInfo w) {

        if (z.getLeftChild().ID == y.getNode().ID) {
            this.requestRPCTo(z.getNode().ID, "changeLeftChildTo", x.getNode());
        } else {
            this.requestRPCTo(z.getNode().ID, "changeRightChildTo", x.getNode());
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