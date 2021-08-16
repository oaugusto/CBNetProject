package projects.opticalNet.nodes.OPTNet;

import java.util.ArrayList;

public class Controller {

    /* Attributes */
    private int numNodes = 0;
    private int numSwitches = 0;
    private int numClusters = 0;
    private int numUnionClusters = 0;
    private int clusterSize;
    private int switchSize;
    private ArrayList<Switch> switches;
    private ArrayList<Node> tree;
    /* End of Attributes */

    private static final int SIZE_CLUSTER_TYPE1 = 4;
    private static final int SIZE_CLUSTER_TYPE2 = 4;

    /* Setters */
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
      ret.add(this.mapConn(y, z, false));
      ret.add(this.mapConn(z, c, false));

      return ret;
    }

    private ArrayList<Alt> zigZagBottomUp (Node x) {
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
      Node y = x.getFather();
      Node z = y.getFather();
      Node w = z.getFather();
      boolean leftZigZag = (y.getId() == z.getLeftChild().getId());
      Node b = (leftZigZag) ? x.getLeftChild() : x.getRightChild();
      Node c = (leftZigZag) ? x.getRightChild() : x.getLeftChild();

      ArrayList<Alt> ret= new ArrayList<>();
      ret.add(this.mapConn(w, x, false));
      ret.add(this.mapConn(x, y, false));
      ret.add(this.mapConn(x, z, false));
      ret.add(this.mapConn(y, b, false));
      ret.add(this.mapConn(z, c, false));

      return ret;
    }

    private ArrayList<Alt> zigZigLeftTopDown (Node z) {
      /*
                *z                   y
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
      ret.add(this.mapConn(y, z, false));
      ret.add(this.mapConn(z, c, false));

      return ret;
    }

    private ArrayList<Alt> zigZigRightTopDown (Node z) {
      Node y = z.getRightChild();
      Node c = y.getLeftChild();

      ArrayList<Alt> ret = new ArrayList<>();
      ret.add(this.mapConn(y, z, false));
      ret.add(this.mapConn(z, c, false));

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
      ret.add(this.mapConn(x, y, false));
      ret.add(this.mapConn(x, z, false));
      ret.add(this.mapConn(y, b, false));
      ret.add(this.mapConn(z, c, false));

      return ret;
    }

    private ArrayList<Alt> zigZagRightTopDown (Node z) {
      Node y = z.getRightChild();
      Node x = y.getLeftChild();
      Node b = x.getRightChild();
      Node c = x.getLeftChild();

      ArrayList<Alt> ret = new ArrayList<>();
      ret.add(this.mapConn(x, y, false));
      ret.add(this.mapConn(x, z, false));
      ret.add(this.mapConn(y, b, false));
      ret.add(this.mapConn(z, c, false));

      return ret;
    }
    /* End of Setters */

    /* Private Getters */
    private double zigDiffRank (Node x, Node y) {
      /*
              y                   x
            /   \               /   \
           x     c     -->     a     y
          / \                       / \
        a   b                      b    c
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

    /* Constructors */
    public Controller (
        int _numNodes, int _switchSize, ArrayList<Integer> edgeList
    ) {
      /*
          Clusters of Type 1 can represent up to half of the switch size nodes
      */
      this.numNodes = _numNodes;
      this.switchSize = _switchSize;
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

      for (int nodeId = 0; nodeId < this.numNodes; nodeId++) {
          this.tree.add(new Node());
      }

      // Adding all Switches of Type 1
      for (int clsId = 0; clsId < this.numClusters; clsId++) {
          Switch swt = new Switch(clsId * this.clusterSize, (clsId + 1) * this.clusterSize - 1);
          this.switches.add(swt);
          this.switches.add(swt);
          this.switches.add(swt);
          this.switches.add(swt);
      }
      // Adding all Switches of Type 2
      for (int clsId1 = 0; clsId1 < this.numClusters; clsId1++) {
          for (int clsId2 = clsId1 + 1; clsId2 < this.numClusters; clsId2++) {
              Switch swt = new Switch(
                  clsId1 * this.clusterSize, (clsId1 + 1) * this.clusterSize - 1,
                  clsId2 * this.clusterSize, (clsId2 + 1) * this.clusterSize - 1
              );
              Switch swt2 = new Switch(
                  clsId2 * this.clusterSize, (clsId2 + 1) * this.clusterSize - 1,
                  clsId1 * this.clusterSize, (clsId1 + 1) * this.clusterSize - 1
              );

              this.switches.add(swt);
              this.switches.add(swt2);
              this.switches.add(swt2);
              this.switches.add(swt);
          }
      }

      for (int i = 0; i < this.numNodes; i++) {
          if (edgeList.get(i) != -1)
            this.mapConn(this.tree.get(edgeList.get(i)), this.tree.get(i), false);
      }
    };
    /* End of Constructors */

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
				ret.add(new Alt(this.getSwitchId(this.tree.get(node.getId()), this.tree.get(child.getId())), node.getId(), child.getId()));
			}
			if (node.getRightChild() != null) {
				Node child = node.getRightChild();
				ret.add(new Alt(this.getSwitchId(this.tree.get(node.getId()), this.tree.get(child.getId())), node.getId(), child.getId()));
			}
		}

		return ret;
	}

    Node getNode (int nodeId) {
      return this.tree.get(nodeId);
    }
    Conn getConnection (Node fromNode, Node toNode) {
        return this.getSwitch(fromNode, toNode).getConnection(fromNode.getId());
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

    Switch getSwitch (Node fromNode, Node toNode) {
      return this.switches.get(this.getSwitchId(fromNode, toNode));
    }
    Switch getSwitch (int switchId) {
      return this.switches.get(switchId);
    }

    boolean areSameCluster (Node node1, Node node2) {
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
    /* End of Setters */

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
    // void debug (void) //

    /* End of Auxiliary Functions */
}
