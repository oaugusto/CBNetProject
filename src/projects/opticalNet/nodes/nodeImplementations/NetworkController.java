package projects.opticalNet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import projects.opticalNet.nodes.OPTNet.Alt;
import projects.opticalNet.nodes.OPTNet.Node;
import projects.opticalNet.nodes.infrastructureImplementations.NetworkSwitch;
import projects.opticalNet.nodes.messages.ConnectNodesMessage;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.nodes.messages.Inbox;

public class NetworkController extends SynchronizerLayer {

    /* Attributes */
    private ArrayList<Node> tree;
    private ArrayList<NetworkSwitch> switches;
    private ArrayList<NetworkNode> netNodes;

    private int numNodes = 0;
    private int switchSize = 0;
    private int numSwitches = 0;
    private int numClusters = 0;
    private int numUnionClusters = 0;
    private int clusterSize;

    private static final int SIZE_CLUSTER_TYPE1 = 4;
    private static final int SIZE_CLUSTER_TYPE2 = 4;

    /* End of Attributes */

    public NetworkController(int numNodes, int switchSize, ArrayList<NetworkNode> netNodes) {
    	this.numNodes = numNodes;
    	this.switchSize = switchSize;
    	this.tree = new ArrayList<>();
    	this.switches = new ArrayList<>();
    	this.netNodes = netNodes;
    	
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
            Node newNode = new Node();
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
                    clsId * this.clusterSize + 1, (clsId + 1) * this.clusterSize, this.netNodes
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
                        clsId1 * this.clusterSize + 1, (clsId1 + 1) * this.clusterSize,
                        clsId2 * this.clusterSize + 1, (clsId2 + 1) * this.clusterSize,
                        this.netNodes
                );
		        swt.finishInitializationWithDefaultModels(true);
                swt.setIndex(this.switches.size() + 1);

                this.switches.add(swt);

                NetworkSwitch swt2 = new NetworkSwitch(
                        clsId2 * this.clusterSize + 1, (clsId2 + 1) * this.clusterSize,
                        clsId1 * this.clusterSize + 1, (clsId1 + 1) * this.clusterSize,
                        this.netNodes
                );

                swt2.finishInitializationWithDefaultModels(true);
                swt2.setIndex(this.switches.size() + 1);

                this.switches.add(swt2);

                swt2 = new NetworkSwitch(
                        clsId2 * this.clusterSize + 1, (clsId2 + 1) * this.clusterSize,
                        clsId1 * this.clusterSize + 1, (clsId1 + 1) * this.clusterSize,
                        this.netNodes
                );

                swt2.finishInitializationWithDefaultModels(true);
                swt2.setIndex(this.switches.size() + 1);

                this.switches.add(swt2);

                swt = new NetworkSwitch(
                        clsId1 * this.clusterSize + 1, (clsId1 + 1) * this.clusterSize,
                        clsId2 * this.clusterSize + 1, (clsId2 + 1) * this.clusterSize,
                        this.netNodes
                );
		        swt.finishInitializationWithDefaultModels(true);
                swt.setIndex(this.switches.size() + 1);

                this.switches.add(swt);
            }
        }

        this.setup(edgeList);
    }
    
    @Override
    public void init() {
        super.init();
    }

    private void setup (ArrayList<Integer> edgeList) {
        for (int i = 0; i < this.numNodes; i++) {
            if (edgeList.get(i) != -1) {
            	Alt tmp = this.mapConn(this.tree.get(edgeList.get(i)), this.tree.get(i));
//            	System.out.println(tmp.getSwitchId() + ", " + tmp.getInNodeId() + ": " + tmp.getOutNodeId());
            }
        }
    }

    /* Rotations */
    private ArrayList<Alt> zigZigBottomUp (Node x) {
        /*
                 z                 *y
                / \               /   \
               y   d             x     z
              / \      -->      / \   / \
            *x   c             a   b c   d
            / \
           a   b
        */

        Node y = x.getFather();
        Node z = y.getFather();
        boolean leftZigZig = (y.getId() == z.getLeftChild().getId());
        Node c = (leftZigZig) ? y.getRightChild() : y.getLeftChild();

        ArrayList<Alt> ret = new ArrayList<>();
        ret.add(this.mapConn(y, z));
        ret.add(this.mapConn(z, c));

        return ret;
    }

    private ArrayList<Alt> zigZagBottomUp (Node x) {
        /*
                  w               w
                 /               /
                z		      *x
               / \            /   \
              y   d          y     z
             / \	   -->  / \   / \
            a   x*         a   b c   d
               / \
              b   c
        */
        Node y = x.getFather();
        Node z = y.getFather();
        Node w = z.getFather();
        boolean leftZigZag = (y.getId() == z.getLeftChild().getId());
        Node b = (leftZigZag) ? x.getLeftChild() : x.getRightChild();
        Node c = (leftZigZag) ? x.getRightChild() : x.getLeftChild();

        ArrayList<Alt> ret= new ArrayList<>();
        ret.add(this.mapConn(w, x));
        ret.add(this.mapConn(x, y));
        ret.add(this.mapConn(x, z));
        ret.add(this.mapConn(y, b));
        ret.add(this.mapConn(z, c));

        return ret;
    }

    private ArrayList<Alt> zigZigLeftTopDown (Node z) {
        /*
                 *z                    y
                 / \                 /   \
                y   d     -->      *x     z
               / \                 / \   / \
              x   c               a   b c   d
             / \
            a   b
        */
        Node y = z.getLeftChild();
        Node c = y.getRightChild();

        ArrayList<Alt> ret = new ArrayList<>();
        ret.add(this.mapConn(y, z));
        ret.add(this.mapConn(z, c));

        return ret;
    }

    private ArrayList<Alt> zigZigRightTopDown (Node z) {
        Node y = z.getRightChild();
        Node c = y.getLeftChild();

        ArrayList<Alt> ret = new ArrayList<>();
        ret.add(this.mapConn(y, z));
        ret.add(this.mapConn(z, c));

        return ret;
    }

    private ArrayList<Alt> zigZagLeftTopDown (Node z) {
        /*
                     *z                     x
                     / \        -->       /   \
                    y   d                y     z
                   / \                  / \   / \
                  a   x                a  *b *c  d
                     / \
                    b   c
        */
        Node y = z.getLeftChild();
        Node x = y.getRightChild();
        Node b = x.getLeftChild();
        Node c = x.getRightChild();

        ArrayList<Alt> ret = new ArrayList<>();
        ret.add(this.mapConn(x, y));
        ret.add(this.mapConn(x, z));
        ret.add(this.mapConn(y, b));
        ret.add(this.mapConn(z, c));

        return ret;
    }

    private ArrayList<Alt> zigZagRightTopDown (Node z) {
        Node y = z.getRightChild();
        Node x = y.getLeftChild();
        Node b = x.getRightChild();
        Node c = x.getLeftChild();

        ArrayList<Alt> ret = new ArrayList<>();
        ret.add(this.mapConn(x, y));
        ret.add(this.mapConn(x, z));
        ret.add(this.mapConn(y, b));
        ret.add(this.mapConn(z, c));

        return ret;
    }
    /* End of Rotations */

    /* Private Getters */
    private double zigDiffRank (Node x, Node y) {
        /*
                     y                   x
                   /   \               /   \
                  x     c     -->     a     y
                 / \                       / \
                a   b                     b    c
        */
        // type of operation
        boolean leftZig = (y.getLeftChild() != null && x.getId() == y.getLeftChild().getId());

        Node b = (leftZig) ? x.getRightChild() : x.getLeftChild();

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
    private double zigZagDiffRank (Node x, Node y, Node z) {
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

        Node b = lefZigZag ? x.getLeftChild() : x.getRightChild();
        Node c = lefZigZag ? x.getRightChild() : x.getLeftChild();

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

    private int getRotationToPerforme (Node x) {
        double maxDelta = 0;
        int operation = 0;

        /*bottom-up - BEGIN*/
        if (x.getFather() != null && x.getFather().getFather() != null)
        {
            Node y = x.getFather();
            Node z = y.getFather();
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
                Node y = x.getLeftChild();

                // zigzig left top-down
                if (y.getLeftChild() != null) {
                        Node z = y.getLeftChild();
                        double aux = zigDiffRank(y, z);
                        if (aux > maxDelta) {
                                maxDelta = aux;
                                operation = 5;
                        }
                }

                // zigzag left top-down
                if (y.getRightChild() != null) {
                        Node z = y.getRightChild();
                        double aux = zigDiffRank(y, z);
                        if (aux > maxDelta) {
                                maxDelta = aux;
                                operation = 6;
                        }
                }
        }

        if (x.getRightChild() != null) {
                Node y = x.getRightChild();

                // zigzig right top-down
                if (y.getRightChild() != null) {
                        Node z = y.getRightChild();
                        double aux = zigDiffRank(y, z);
                        if (aux > maxDelta) {
                                maxDelta = aux;
                                operation = 7;
                        }
                }

                // zigzag right top-down
                if (y.getLeftChild() != null) {
                        Node z = y.getLeftChild();
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
            Node node = this.tree.get(i);
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

    Node getNode (int nodeId) {
        return this.tree.get(nodeId);
    }

    int getClusterId (Node fromNode, Node toNode) {
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
    int getClusterId (Node node) {
        /*
                The clusterId of a given node is calculated by the floor of the
                division between the Node Id and the size of the clusters of the
                system.
        */
        return node.getId() / this.clusterSize;
    }

    int getSwitchId (Node fromNode, Node toNode) {
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

    NetworkSwitch getSwitch (Node fromNode, Node toNode) {
        return this.switches.get(this.getSwitchId(fromNode, toNode));
    }
    NetworkSwitch getSwitch (int switchId) {
        return this.switches.get(switchId);
    }

    boolean areSameCluster (Node node1, Node node2) {
        return this.getClusterId(node1) == this.getClusterId(node2);
    }
    /* End of Getters */

    /* Setters */
    Alt mapConn (Node fromNode, Node toNode) {
        int swtId = this.getSwitchId(fromNode, toNode);

        fromNode.setChild(toNode);

//        this.getSwitch(swtId).connectNodes(fromNode.getId() + 1, toNode.getId() + 1);
//        this.getSwitch(swtId + 1).connectNodes(toNode.getId() + 1, fromNode.getId() + 1);
        
        this.sendConnectNodesMessage(swtId, fromNode.getId() + 1, toNode.getId() + 1);
        this.sendConnectNodesMessage(swtId + 1, toNode.getId() + 1, fromNode.getId() + 1);

        return new Alt(swtId, fromNode.getId() + 1, toNode.getId() + 1);
    }
    
    private void sendConnectNodesMessage(int switchId, int from, int to) {
    	ConnectNodesMessage msg = new ConnectNodesMessage(from, to);
    	this.send(msg, this.getSwitch(switchId));
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

    ArrayList<Alt> updateConn () {
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
    
    public void renderTopology(int width, int height) {
		// set network nodes position
		double x_space = width / 4.0;
		double y_space = height / (double) (this.numNodes + 1);
		for (int i = 0; i < this.numNodes; ++i) {
			NetworkNode n = this.netNodes.get(i);
			n.setPosition(x_space, y_space * (i+1), 0);
		}
		
		//set network switches position
		int unit = height / ((6 * this.numSwitches) + 1);
		int switch_height = 5 * unit;
		int switch_width= switch_height / 2;
//		this.leftSwitchInput.setPosition(3 * x_space, 3.5 * unit, 0);
//		this.leftSwitchInput.setSwitchDimension(switch_height/3, switch_height);
//		this.leftSwitchOutput.setPosition(3 * x_space, 9.5 * unit, 0);
//		this.leftSwitchOutput.setSwitchDimension(switch_height/3, switch_height);
//		this.rightSwitchInput.setPosition(3 * x_space, 15.5 * unit, 0);
//		this.rightSwitchInput.setSwitchDimension(switch_height/3, switch_height);
//		this.rightSwitchOutput.setPosition(3 * x_space, 21.5 * unit, 0);
//		this.rightSwitchOutput.setSwitchDimension(switch_height/3, switch_height);
		for (int i = 0; i < this.numSwitches; ++i) {
			NetworkSwitch n = this.switches.get(i);
			n.setPosition(3 * x_space, (i + 1) * 5 * unit, 0);
			n.setSwitchDimension(switch_height/3, switch_height);
		}
		
		// set controller node position
		this.setPosition(4 * x_space, height / 2, 0);
	}
}
