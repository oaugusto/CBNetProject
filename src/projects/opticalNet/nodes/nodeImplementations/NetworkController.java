package projects.opticalNet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import projects.opticalNet.nodes.OPTNet.Alt;
import projects.opticalNet.nodes.OPTNet.Controller;
import projects.opticalNet.nodes.infrastructureImplementations.NetworkSwitch;
import projects.opticalNet.nodes.infrastructureImplementations.treeStructure.Tree;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.messages.Inbox;

public class NetworkController extends SynchronizerLayer {

    /* Attributes */
    private Tree treeStructure;
    private ArrayList<NetworkSwitch> switches;
    private ArrayList<NetworkNode> tree;

    private int numNodes = 0;
    private int switchSize = 0;
    private int numSwitches = 0;
    private int numClusters = 0;
    private int numUnionClusters = 0;
    private int clusterSize;

    private static final int SIZE_CLUSTER_TYPE1 = 4;
    private static final int SIZE_CLUSTER_TYPE2 = 4;

    
    /* End of Attributes */

    @Override
    public void init() {
        super.init();

        this.clusterSize = this.switchSize / 2;
        this.numClusters = (this.numNodes - this.clusterSize + 1) / this.clusterSize + 1;
        this.numUnionClusters = (
                this.numClusters > 1 ?
                this.unionPos(this.numClusters - 2, this.numClusters - 1) + 1 :
                0
        );
        this.numSwitches = (
                this.numClusters * SIZE_CLUSTER_TYPE1 +
                this.numUnionClusters * SIZE_CLUSTER_TYPE2
        );

        ArrayList<Integer> edgeList = new ArrayList<Integer>();
        for (int i = 0; i < this.numNodes; i++) {
            NetworkNode newNode = new NetworkNode();
            newNode.finishInitializationWithDefaultModels(true);

            this.tree.add(newNode);
            if (i != numNodes - 1) {
                edgeList.add(i + 1);
            } else {
                edgeList.add(-1);
            }
        }

        // Adding all Switches of Type 1
        for (int clsId = 0; clsId < this.numClusters; clsId++) {
            for (int i = 0; i < 4; i++) {
                NetworkSwitch swt = new NetworkSwitch(
                    clsId * this.clusterSize, (clsId + 1) * this.clusterSize - 1, this.tree
                );
		        swt.finishInitializationWithDefaultModels(true);
                swt.setIndex(this.switches.size() + 1);

                this.switches.add(swt);
            }
        }
        // Adding all Switches for Clusters of Type 2
        for (int clsId1 = 0; clsId1 < this.numClusters; clsId1++) {
            for (int clsId2 = clsId1 + 1; clsId2 < this.numClusters; clsId2++) {
                NetworkSwitch swt = new NetworkSwitch(
                        clsId1 * this.clusterSize, (clsId1 + 1) * this.clusterSize - 1,
                        clsId2 * this.clusterSize, (clsId2 + 1) * this.clusterSize - 1,
                        this.tree
                );
		        swt.finishInitializationWithDefaultModels(true);
                swt.setIndex(this.switches.size() + 1);

                this.switches.add(swt);

                NetworkSwitch swt2 = new NetworkSwitch(
                        clsId2 * this.clusterSize, (clsId2 + 1) * this.clusterSize - 1,
                        clsId1 * this.clusterSize, (clsId1 + 1) * this.clusterSize - 1,
                        this.tree
                );

                swt2.finishInitializationWithDefaultModels(true);
                swt2.setIndex(this.switches.size() + 1);

                this.switches.add(swt2);

                swt2 = new NetworkSwitch(
                        clsId2 * this.clusterSize, (clsId2 + 1) * this.clusterSize - 1,
                        clsId1 * this.clusterSize, (clsId1 + 1) * this.clusterSize - 1,
                        this.tree
                );

                swt2.finishInitializationWithDefaultModels(true);
                swt2.setIndex(this.switches.size() + 1);

                this.switches.add(swt2);

                swt = new NetworkSwitch(
                        clsId1 * this.clusterSize, (clsId1 + 1) * this.clusterSize - 1,
                        clsId2 * this.clusterSize, (clsId2 + 1) * this.clusterSize - 1,
                        this.tree
                );
		        swt.finishInitializationWithDefaultModels(true);
                swt.setIndex(this.switches.size() + 1);

                this.switches.add(swt);
            }
        }
    }

    /* Rotations */
    private ArrayList<Alt> zigZigBottomUp (NetworkNode x) {
        /*
                    z                 *y
                / \               /   \
                y   d             x     z
                / \      -->      / \   / \
            *x   c             a   b c   d
            / \
            a   b
        */

        NetworkNode y = x.getFather();
        NetworkNode z = y.getFather();
        boolean leftZigZig = (y.getId() == z.getLeftChild().getId());
        NetworkNode c = (leftZigZig) ? y.getRightChild() : y.getLeftChild();

        ArrayList<Alt> ret = new ArrayList<>();
        ret.add(this.mapConn(y, z, false));
        ret.add(this.mapConn(z, c, false));

        return ret;
    }

    private ArrayList<Alt> zigZagBottomUp (NetworkNode x) {
        /*
                            w                 w
                            /                 /
                        z				 *x
                        / \               /   \
                    y   d             y     z
                    / \	   -->     / \   / \
                a   x*            a   b c   d
                        / \
                    b   c
        */
        NetworkNode y = x.getFather();
        NetworkNode z = y.getFather();
        NetworkNode w = z.getFather();
        boolean leftZigZag = (y.getId() == z.getLeftChild().getId());
        NetworkNode b = (leftZigZag) ? x.getLeftChild() : x.getRightChild();
        NetworkNode c = (leftZigZag) ? x.getRightChild() : x.getLeftChild();

        ArrayList<Alt> ret= new ArrayList<>();
        ret.add(this.mapConn(w, x, false));
        ret.add(this.mapConn(x, y, false));
        ret.add(this.mapConn(x, z, false));
        ret.add(this.mapConn(y, b, false));
        ret.add(this.mapConn(z, c, false));

        return ret;
    }

    private ArrayList<Alt> zigZigLeftTopDown (NetworkNode z) {
        /*
                            *z                   y
                            / \                 /   \
                        y   d     -->      *x     z
                        / \                 / \   / \
                    x   c               a   b c   d
                    / \
                a   b
        */
        NetworkNode y = z.getLeftChild();
        NetworkNode c = y.getRightChild();

        ArrayList<Alt> ret = new ArrayList<>();
        ret.add(this.mapConn(y, z, false));
        ret.add(this.mapConn(z, c, false));

        return ret;
    }

    private ArrayList<Alt> zigZigRightTopDown (NetworkNode z) {
        NetworkNode y = z.getRightChild();
        NetworkNode c = y.getLeftChild();

        ArrayList<Alt> ret = new ArrayList<>();
        ret.add(this.mapConn(y, z, false));
        ret.add(this.mapConn(z, c, false));

        return ret;
    }

    private ArrayList<Alt> zigZagLeftTopDown (NetworkNode z) {
        /*
                        *z                     x
                        / \        -->       /   \
                    y   d                y     z
                    / \                  / \   / \
                a   x                a  *b *c  d
                        / \
                    b   c
        */
        NetworkNode y = z.getLeftChild();
        NetworkNode x = y.getRightChild();
        NetworkNode b = x.getLeftChild();
        NetworkNode c = x.getRightChild();

        ArrayList<Alt> ret = new ArrayList<>();
        ret.add(this.mapConn(x, y, false));
        ret.add(this.mapConn(x, z, false));
        ret.add(this.mapConn(y, b, false));
        ret.add(this.mapConn(z, c, false));

        return ret;
    }

    private ArrayList<Alt> zigZagRightTopDown (NetworkNode z) {
        NetworkNode y = z.getRightChild();
        NetworkNode x = y.getLeftChild();
        NetworkNode b = x.getRightChild();
        NetworkNode c = x.getLeftChild();

        ArrayList<Alt> ret = new ArrayList<>();
        ret.add(this.mapConn(x, y, false));
        ret.add(this.mapConn(x, z, false));
        ret.add(this.mapConn(y, b, false));
        ret.add(this.mapConn(z, c, false));

        return ret;
    }
    /* End of Rotations */

    /* Private Getters */
    private double zigDiffRank (NetworkNode x, NetworkNode y) {
        /*
                        y                   x
                    /   \               /   \
                    x     c     -->     a     y
                / \                       / \
            a   b                      b    c
        */
        // type of operation
        boolean leftZig = (y.getLeftChild() != null && x.getId() == y.getLeftChild().getId());

        NetworkNode b = (leftZig) ? x.getRightChild() : x.getLeftChild();

        long xOldWeight = x.getWeight();
        long yOldWeight = y.getWeight();

        long bWeight = (b != null) ? b.getWeight() : 0;

        long yNewWeight = yOldWeight - xOldWeight + bWeight;
        long xNewWeight = xOldWeight - bWeight + yNewWeight;

        double xOldRank = (xOldWeight == 0) ? 0 : Math.log(xOldWeight);
        double yOldRank = (yOldWeight == 0) ? 0 : Math.log(yOldWeight);
        double xNewRank = (xNewWeight == 0) ? 0 : Math.log(xNewWeight);
        double yNewRank = (yNewWeight == 0) ? 0 : Math.log(yNewWeight);

        double deltaRank = yNewRank + xNewRank - yOldRank - xOldRank;

        return deltaRank;
    }
    private double zigZagDiffRank (NetworkNode x, NetworkNode y, NetworkNode z) {
        /*
                z					   *x
            / \                   /   \
            y   d                 y     z
                / \		 -->    / \   / \
                a  *x             a   b c   d
                    / \
                    b   c
        */
        boolean lefZigZag = (z.getLeftChild() != null && y.getId() == z.getLeftChild().getId());

        NetworkNode b = lefZigZag ? x.getLeftChild() : x.getRightChild();
        NetworkNode c = lefZigZag ? x.getRightChild() : x.getLeftChild();

        long xOldWeight = x.getWeight();
        long yOldWeight = y.getWeight();
        long zOldWeight = z.getWeight();

        long bWeight = (b != null) ? b.getWeight() : 0;
        long cWeight = (c != null) ? c.getWeight() : 0;

        long yNewWeight = yOldWeight - xOldWeight + bWeight;
        long zNewWeight = zOldWeight - yOldWeight + cWeight;
        long xNewWeight = xOldWeight - bWeight - cWeight + yNewWeight + zNewWeight;

        double xOldRank = (xOldWeight == 0) ? 0 : Math.log(xOldWeight);
        double yOldRank = (yOldWeight == 0) ? 0 : Math.log(yOldWeight);
        double zOldRank = (zOldWeight == 0) ? 0 : Math.log(zOldWeight);
        double xNewRank = (xNewWeight == 0) ? 0 : Math.log(xNewWeight);
        double yNewRank = (yNewWeight == 0) ? 0 : Math.log(yNewWeight);
        double zNewRank = (zNewWeight == 0) ? 0 : Math.log(zNewWeight);

        double deltaRank = xNewRank + yNewRank + zNewRank - xOldRank - yOldRank - zOldRank;

        return deltaRank;
    }

    private int getRotationToPerforme (NetworkNode x) {
        double maxDelta = 0;
        int operation = 0;

        /*bottom-up - BEGIN*/
        if (x.getFather() != null && x.getFather().getFather() != null)
        {
                NetworkNode y = x.getFather();
                NetworkNode z = y.getFather();
                if (y.getLeftChild() != null && x.getId() == y.getLeftChild().getId() &&
                        z.getLeftChild() != null && y.getId() == z.getLeftChild().getId()) {
                        // zigzigLeft
                        double aux = zigDiffRank(y, z);
                        if (aux > maxDelta) {
                                maxDelta = aux;
                                operation = 1;
                        }
                } else
                if (y.getRightChild() != null && x.getId() == y.getRightChild().getId() &&
                        z.getRightChild() != null && y.getId() == z.getRightChild().getId()) {
                        // zigzigRight
                        double aux = zigDiffRank(y, z);
                        if (aux > maxDelta) {
                                maxDelta = aux;
                                operation = 2;
                        }
                } else
                if (y.getRightChild() != null && x.getId() == y.getRightChild().getId() &&
                        z.getLeftChild() != null && y.getId() == z.getLeftChild().getId()) {
                        // zigzagLeft
                        double aux = zigZagDiffRank(x, y, z);
                        if (aux > maxDelta) {
                                maxDelta = aux;
                                operation = 3;
                        }
                } else
                if (y.getLeftChild() != null && x.getId() == y.getLeftChild().getId() &&
                        z.getRightChild() != null && y.getId() == z.getRightChild().getId()) {
                        // zigzagRight
                        double aux = zigZagDiffRank(x, y, z);
                        if (aux > maxDelta) {
                                maxDelta = aux;
                                operation = 4;
                        }
                }
        }

        /*top-down - BEGIN*/
        if (x.getLeftChild() != null) {
                NetworkNode y = x.getLeftChild();

                // zigzig left top-down
                if (y.getLeftChild() != null) {
                        NetworkNode z = y.getLeftChild();
                        double aux = zigDiffRank(y, z);
                        if (aux > maxDelta) {
                                maxDelta = aux;
                                operation = 5;
                        }
                }

                // zigzag left top-down
                if (y.getRightChild() != null) {
                        NetworkNode z = y.getRightChild();
                        double aux = zigDiffRank(y, z);
                        if (aux > maxDelta) {
                                maxDelta = aux;
                                operation = 6;
                        }
                }
        }

        if (x.getRightChild() != null) {
                NetworkNode y = x.getRightChild();

                // zigzig right top-down
                if (y.getRightChild() != null) {
                        NetworkNode z = y.getRightChild();
                        double aux = zigDiffRank(y, z);
                        if (aux > maxDelta) {
                                maxDelta = aux;
                                operation = 7;
                        }
                }

                // zigzag right top-down
                if (y.getLeftChild() != null) {
                        NetworkNode z = y.getLeftChild();
                        double aux = zigDiffRank(y, z);
                        if (aux > maxDelta) {
                                maxDelta = aux;
                                operation = 8;
                        }
                }
        }

        return operation;
    }
    /* End of Private Getters */

    /* Getters */
    int getNumNodes () {
        return this.numNodes;
    }
    int getNumSwitches () {
        return this.numSwitches;
    }
    int getNumClusters () {
        return this.numClusters;
    }
    int getNumUnionClusters () {
        return this.numUnionClusters;
    }
    int getClusterSize () {
        return this.clusterSize;
    }
    int getSwitchSize () {
        return this.switchSize;
    }

    ArrayList<Alt> getTreeConfiguration () {
        ArrayList<Alt> ret = new ArrayList<>();

        for (int i = 0; i < this.numNodes; i++) {
            NetworkNode node = this.tree.get(i);
            if (node.getLeftChild() != null) {
                Node child = node.getLeftChild();
                ret.add(new Alt(
                    this.getSwitchId(
                        this.tree.get(node.getId()), this.tree.get(child.getId())
                    ), node.getId(), child.getId()
                ));
            }
            if (node.getRightChild() != null) {
                Node child = node.getRightChild();
                ret.add(new Alt(
                    this.getSwitchId(
                        this.tree.get(node.getId()), this.tree.get(child.getId())
                    ), node.getId(), child.getId()
                ));
            }
        }

        return ret;
    }

    NetworkNode getNode (int nodeId) {
        return this.tree.get(nodeId);
    }

    NetworkSwitch getSwitch (int switchId) {
        return this.switches.get(switchId);
    }

    int getClusterId (NetworkNode fromNode, NetworkNode toNode) {
        /*
                The clusterId of two nodes in the same cluster is calculated
                by the floor of the division between any of the nodes ids and the
                size of the clusters of the system.

                The clusterId of two nodes in different clusters is calculated
                by adding the number of clusters and the position of the UnionCluster
                from unionPos.
        */
        if (this.areSameCluster(fromNode, toNode)) {
                return this.getClusterId(fromNode);
        } else {
                return this.numClusters + this.unionPos(
                        this.getClusterId(fromNode), this.getClusterId(toNode)
                );
        }
    }
    int getClusterId (NetworkNode node) {
        /*
                The clusterId of a given node is calculated by the floor of the
                division between the Node Id and the size of the clusters of the
                system.
        */
        return node.getId() / this.clusterSize;
    }

    int getSwitchId (NetworkNode fromNode, NetworkNode toNode) {
        /*
                To find the switchId between two nodes from the same cluster
                we need to multiply by 4 the numbers of clusters of type 1
                prior to our. Then adding 2 to the result if it is a right edge.

                To find the switchId between two nodes from different clusters
                we neet to multiply by 4 the numbers of clusters of type 1.
                Then we add 4 times the number clusters of type 2 prior to our.
        */
        int previousSwitches = (
                this.areSameCluster(fromNode, toNode) ?
                this.getClusterId(fromNode, toNode) * SIZE_CLUSTER_TYPE1 :
                this.numClusters * SIZE_CLUSTER_TYPE1 + this.unionPos(
                        this.getClusterId(fromNode), this.getClusterId(toNode)
                ) * SIZE_CLUSTER_TYPE2
        );

        return previousSwitches + 2 * (fromNode.getId() > toNode.getId() ? 1 : 0);
    }

    NetworkSwitch getSwitch (NetworkNode fromNode, NetworkNode toNode) {
        return this.switches.get(this.getSwitchId(fromNode, toNode));
    }
    NetworkSwitch getSwitch (int switchId) {
        return this.switches.get(switchId);
    }

    boolean areSameCluster (NetworkNode node1, NetworkNode node2) {
        return this.getClusterId(node1) == this.getClusterId(node2);
    }
    /* End of Getters */

    /* Setters */
    Alt mapConn (Node fromNode, Node toNode, boolean dummy /*=false*/) {
        int swtId = this.getSwitchId(fromNode, toNode);
        int oldSwt = -1;

        if (fromNode.getId() > toNode.getId()) {
            oldSwt = fromNode.setLeftChild(toNode, swtId);
        } else{
            oldSwt = fromNode.setRightChild(toNode, swtId);
        }

        if (oldSwt != -1 && swtId != oldSwt) {
            this.getSwitch(swtId).removeConn(fromNode.getId());
            this.getSwitch(swtId + 1).removeConn(toNode.getId());
        }

        this.getSwitch(swtId).updateConn(fromNode.getId(), toNode.getId(), false);
        this.getSwitch(swtId + 1).updateConn(toNode.getId(), fromNode.getId(), false);

        return new Alt(swtId, fromNode.getId(), toNode.getId());
    }
    /* End of Setters

    /* Auxiliary Functions */
    int unionPos (int clsId1, int clsId2) {
        /*
                To calculate the postion of a UnionCluster we compute the
                summation from (NUM_CLUSTERS - 1) to (NUM_CLUSTER - minimum(clsId1, clsId2))
                and add to the result the distance between clsId1 to clsId2.
        */

        if (clsId1 > clsId2) {
            int aux = clsId1;
            clsId1 = clsId2;
            clsId2 = aux;
        }
        // AP(n) = ((a0 + an) * n) / 2
        // a0 = NUM_CLUSTERS - 1
        // an = NUM_CLUSTER - 1 - clsId1 + 1
        // n = clsId1
        int apSum = (
            clsId1 != 0 ?
            ((2 * this.numClusters - 1 - clsId1) * (clsId1)) / 2 :
            0
        );
        return apSum + clsId2 - clsId1 - 1;
    }

    ArrayList<Alt> updateConnections () {
        ArrayList<Alt> ret = new ArrayList<>();

        for (int i = 0; i < this.numNodes; i++) {
        Node node = this.tree.get(i);
            switch (getRotationToPerforme(node)) {
                case 1:
                case 2:
                        ret = this.zigZigBottomUp(node);
                        break;
                case 3:
                case 4:
                        ret = this.zigZagBottomUp(node);
                        break;
                case 5:
                        ret = this.zigZigLeftTopDown(node);
                        break;
                case 6:
                        ret = this.zigZigRightTopDown(node);
                        break;
                case 7:
                        ret = this.zigZagLeftTopDown(node);
                        break;
                case 8:
                        ret = this.zigZagRightTopDown(node);
                        break;
                default:
                        break;
            }
        }

        return ret;
    }

    /* End of Auxiliary Functions */

    @Override
    public void nodeStep() {

    }

    @Override
    public void handleMessages(Inbox inbox) {


    }

    @Override
    public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
        String text = "" + ID;
        // draw the node as a circle with the text inside
        super.drawNodeAsDiskWithText(g, pt, highlight, text, 12, Color.YELLOW);
    }
}
