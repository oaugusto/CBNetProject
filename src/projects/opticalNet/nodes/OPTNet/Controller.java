package projects.opticalNet.nodes.OPTNet;

public class Controller {

    private static final int SIZE_CLUSTER_TYPE1 = 4;
    private static final int SIZE_CLUSTER_TYPE2 = 4;

    /* Setters */
    private void zigBottomUp (Node x) {
      /*
                z                 z
                /                 /
              y                *x
              / \               / \
            *x   c     -->     a   y
            / \                   / \
          a   b                 b   c
      */
      Node y = x->getFather();
      Node z = y->getFather();
      boolean leftZig = (x->getId() == y->getLeftChild()->getId());
      Node b = (leftZig) ? x->getRightChild() : x->getLeftChild();
      this->mapConn(z, y);
      this->mapConn(x, y);
      this->mapConn(x, b);
    }
    private void zigZigBottomUp (Node x) {
      /*
                z                 *y
              / \               /   \
              y   d             x     z
            / \      -->      / \   / \
          *x   c             a   b c   d
          / \
          a   b
      */
      Node y = x->getFather();
      Node z = y->getFather();
      boolean leftZigZig = (y->getId() == z->getLeftChild()->getId());
      Node c = (leftZigZig) ? y->getRightChild() : y->getLeftChild();
      this->mapConn(y, z);
      this->mapConn(z, c);
    }
    private void zigZagBottomUp (Node x) {
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
      Node y = x->getFather();
      Node z = y->getFather();
      Node w = z->getFather();
      boolean leftZigZag = (y->getId() == z->getLeftChild()->getId());
      Node b = (leftZigZag) ? x->getLeftChild() : x->getRightChild();
      Node c = (leftZigZag) ? x->getRightChild() : x->getLeftChild();
      this->mapConn(w, x);
      this->mapConn(x, y);
      this->mapConn(x, z);
      this->mapConn(y, b);
      this->mapConn(z, c);
    }
    private void zigZigTopDown (Node z) {
      /*
                *z                   y
                / \                 /   \
              y   d     -->      *x     z
              / \                 / \   / \
            x   c               a   b c   d
            / \
          a   b
      */
      boolean leftZigZig = (z->getLeftChild() && y->getId() == z->getLeftChild()->getId());
      Node y = (leftZigZig) ? z->getLeftChild() : z->getRightChild();
      Node c = (leftZigZig) ? y->getRightChild() : y->getLeftChild();
      this->mapConn(y, z);
      this->mapConn(z, c);
    }
    private void zigZagTopDown (Node z) {
      /*
              *z                     x
              / \        -->       /   \
            y   d                y     z
            / \                  / \   / \
          a   x                a  *b *c  d
              / \
            b   c
      */
      boolean leftZigZag = (z->getLeftChild() && y->getId() == z->getLeftChild()->getId());
      Node y = (leftZigZag) ? z->getLeftChild() : z->getRightChild();
      Node x = (leftZigZag) ? y->getRightChild() : y->getLeftChild();
      Node b = (leftZigZag) ? x->getLeftChild() : x->getRightChild();
      Node c = (leftZigZag) ? x->getRightChild() : x->getLeftChild();
      this->mapConn(x, y);
      this->mapConn(x, z);
      this->mapConn(y, b);
      this->mapConn(z, c);
    }
    /* End of Setters */

    /* Private Getters */
    private double zigDiffRank (Node x, Node y) {
      /*
              y                   x
            /   \               /   \
          x     c     -->     a     y
          / \                       / \
        a   b                     b   c
      */
      // type of operation
      boolean leftZig = (y->getLeftChild() && x->getId() == y->getLeftChild()->getId());

      Node b = (leftZig) ? x->getRightChild() : x->getLeftChild();

      long xOldWeight = x->getWeight();
      long yOldWeight = y->getWeight();

      long bWeight = (b != null) ? b->getWeight() : 0;

      long yNewWeight = yOldWeight - xOldWeight + bWeight;
      long xNewWeight = xOldWeight - bWeight + yNewWeight;

      double xOldRank = (xOldWeight == 0) ? 0 : log2(xOldWeight);
      double yOldRank = (yOldWeight == 0) ? 0 : log2(yOldWeight);
      double xNewRank = (xNewWeight == 0) ? 0 : log2(xNewWeight);
      double yNewRank = (yNewWeight == 0) ? 0 : log2(yNewWeight);

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
      boolean lefZigZag = (z->getLeftChild() && y->getId() == z->getLeftChild()->getId());

      Node b = lefZigZag ? x->getLeftChild() : x->getRightChild();
      Node c = lefZigZag ? x->getRightChild() : x->getLeftChild();

      long xOldWeight = x->getWeight();
      long yOldWeight = y->getWeight();
      long zOldWeight = z->getWeight();

      long bWeight = (b != null) ? b->getWeight() : 0;
      long cWeight = (c != null) ? c->getWeight() : 0;

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
    private int getRotationToPerforme (Node x) {
      double maxDelta = 0;
      int operation = 0;

      /*bottom-up - BEGIN*/
      if (x->getFather() != null && x->getFather()->getFather() != null)
      {
          Node y = x->getFather();
          Node z = y->getFather();
          if (y->getLeftChild() && x->getId() == y->getLeftChild()->getId() &&
              z->getLeftChild() && y->getId() == z->getLeftChild()->getId()) {
              // zigzigLeft
              double aux = zigDiffRank(y, z);
              if (aux > maxDelta) {
                  maxDelta = aux;
                  operation = 1;
              }
          } else
          if (y->getRightChild() && x->getId() == y->getRightChild()->getId() &&
              z->getRightChild() && y->getId() == z->getRightChild()->getId()) {
              // zigzigRight
              double aux = zigDiffRank(y, z);
              if (aux > maxDelta) {
                  maxDelta = aux;
                  operation = 2;
              }
          } else
          if (y->getRightChild() && x->getId() == y->getRightChild()->getId() &&
              z->getLeftChild() && y->getId() == z->getLeftChild()->getId()) {
              // zigzagLeft
              double aux = zigZagDiffRank(x, y, z);
              if (aux > maxDelta) {
                  maxDelta = aux;
                  operation = 3;
              }
          } else
          if (y->getLeftChild() && x->getId() == y->getLeftChild()->getId() &&
              z->getRightChild() && y->getId() == z->getRightChild()->getId()) {
              // zigzagRight
              double aux = zigZagDiffRank(x, y, z);
              if (aux > maxDelta) {
                  maxDelta = aux;
                  operation = 4;
              }
          }
      }

      /*top-down - BEGIN*/
      if (x->getFather() != null && x->getFather()->getFather() != null) {
          Node y = x->getFather();
          Node z = y->getFather();
          if (x->getId() == y->getLeftChild()->getId() && y->getId() == z->getLeftChild()->getId()) {
          }
      }

      if (x->getLeftChild()) {
          Node y = x->getLeftChild();

          // zigzig left top-down
          if (y->getLeftChild()) {
              Node z = y->getLeftChild();
              double aux = zigDiffRank(y, z);
              if (aux > maxDelta) {
                  maxDelta = aux;
                  operation = 5;
              }
          }

          // zigzag left top-down
          if (y->getRightChild()) {
              Node z = y->getRightChild();
              double aux = zigDiffRank(y, z);
              if (aux > maxDelta) {
                  maxDelta = aux;
                  operation = 6;
              }
          }
      }

      if (x->getRightChild()) {
          Node y = x->getRightChild();

          // zigzig right top-down
          if (y->getRightChild()) {
              Node z = y->getRightChild();
              double aux = zigDiffRank(y, z);
              if (aux > maxDelta) {
                  maxDelta = aux;
                  operation = 7;
              }
          }

          // zigzag right top-down
          if (y->getleftChild()) {
              Node z = y->getlefttChild();
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
    ControllerNode (
        int _numNodes, int _switchSize, std::vector<std::pair<int, int>> edgeList = DEFAULT_VECTOR
    ) {
      /*
          Clusters of Type 1 can represent up to half of the switch size nodes
      */
      this->numNodes = _numNodes;
      this->switchSize = _switchSize;
      this->clusterSize = this->switchSize / 2;
      this->numClusters = (this->numNodes - this->clusterSize + 1) / this->clusterSize + 1;
      this->numUnionClusters = (
          this->numClusters > 1 ?
          this->unionPos(this->numClusters - 2, this->numClusters - 1) + 1 :
          0
      );
      this->numSwitches = (
          this->numClusters * SIZE_CLUSTER_TYPE1 +
          this->numUnionClusters * SIZE_CLUSTER_TYPE2
      );

      for (int nodeId = 0; nodeId < this->numNodes; nodeId++) {
          this->tree.push_back(new Node());
      }

      // Adding all Switches of Type 1
      for (int clsId = 0; clsId < this->numClusters; clsId++) {
          Switch swt(clsId * this->clusterSize, (clsId + 1) * this->clusterSize - 1);
          this->switches.push_back(swt);
          this->switches.push_back(swt);
          this->switches.push_back(swt);
          this->switches.push_back(swt);
      }
      // Adding all Switches of Type 2
      for (int clsId1 = 0; clsId1 < this->numClusters; clsId1++) {
          for (int clsId2 = clsId1 + 1; clsId2 < this->numClusters; clsId2++) {
              Switch swt(
                  clsId1 * this->clusterSize, (clsId1 + 1) * this->clusterSize - 1,
                  clsId2 * this->clusterSize, (clsId2 + 1) * this->clusterSize - 1
              );
              Switch swt2 = Switch(
                  clsId2 * this->clusterSize, (clsId2 + 1) * this->clusterSize - 1,
                  clsId1 * this->clusterSize, (clsId1 + 1) * this->clusterSize - 1
              );

              this->switches.push_back(swt);
              this->switches.push_back(swt2);
              this->switches.push_back(swt2);
              this->switches.push_back(swt);
          }
      }

      for (std::pair<int, int> edge: edgeList) {
          this->mapConn(this->tree[edge.first], this->tree[edge.second]);
      }
    };
    /* End of Constructors */

    /* Getters */
    int getNumNodes (void) {
      return this->numNodes;
    }
    int getNumSwitches (void) {
      return this->numSwitches;
    }
    int getNumClusters (void) {
      return this->numClusters;
    }
    int getNumUnionClusters (void) {
      return this->numUnionClusters;
    }
    int getClusterSize (void) {
      return this->clusterSize;
    }
    int getSwitchSize (void) {
      return this->switchSize;
    }

    Node getNode (int nodeId) {
      return this->tree[nodeId];
    }
    Conn getConnection (Node fromNode, Node toNode) {
        return this->getSwitch(fromNode, toNode).getConnection(fromNode->getId());
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
      if (this->areSameCluster(fromNode, toNode)) {
          return this->getClusterId(fromNode);
      } else {
          return this->numClusters + this->unionPos(
              this->getClusterId(fromNode), this->getClusterId(toNode)
          );
      }
    }
    int getClusterId (Node node) {
      /*
          The clusterId of a given node is calculated by the floor of the
          division between the Node Id and the size of the clusters of the
          system.
      */
      return node->getId() / this->clusterSize;
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
          this->areSameCluster(fromNode, toNode) ?
          this->getClusterId(fromNode, toNode) * SIZE_CLUSTER_TYPE1 :
          this->numClusters * SIZE_CLUSTER_TYPE1 + this->unionPos(
              this->getClusterId(fromNode), this->getClusterId(toNode)
          ) * SIZE_CLUSTER_TYPE2
      );

      return previousSwitches + 2 * (fromNode->getId() > toNode->getId());
    }

    Switch getSwitch (Node fromNode, Node toNode) {
      return this->switches[this->getSwitchId(fromNode, toNode)];
    }
    Switch getSwitch (int switchId) {
      return this->switches[switchId];
    }

    boolean areSameCluster (Node node1, Node node2) {
      return this->getClusterId(node1) == this->getClusterId(node2);
    }
    /* End of Getters */
    /* Setters */
    void mapConn (Node fromNode, Node toNode, boolean dummy /*=false*/) {
      int swtId = this->getSwitchId(fromNode, toNode);
      int oldSwt = -1;

      if (fromNode->getId() > toNode->getId()) {
          oldSwt = fromNode->setLeftChild(toNode, swtId);
      } else{
          oldSwt = fromNode->setRightChild(toNode, swtId);
      }

      if (oldSwt != -1 && swtId != oldSwt) {
          this->getSwitch(swtId).removeConn(fromNode->getId());
          this->getSwitch(swtId + 1).removeConn(toNode->getId());
      }

      this->getSwitch(swtId).updateConn(fromNode->getId(), toNode->getId());
      this->getSwitch(swtId + 1).updateConn(toNode->getId(), fromNode->getId());
    }
    /* End of Setters */
    /* Auxiliary Functions */
    int unionPos (int clsId1, int clsId2) {
      /*
          To calculate the postion of a UnionCluster we compute the
          summation from (NUM_CLUSTERS - 1) to (NUM_CLUSTER - minimum(clsId1, clsId2))
          and add to the result the distance between clsId1 to clsId2.
      */

      if (clsId1 > clsId2)
          std::swap(clsId1, clsId2);
      // AP(n) = ((a0 + an) * n) / 2
      // a0 = NUM_CLUSTERS - 1
      // an = NUM_CLUSTER - 1 - clsId1 + 1
      // n = clsId1
      int apSum = (
          clsId1 != 0 ?
          ((2 * this->numClusters - 1 - clsId1) * (clsId1)) / 2 :
          0
      );
      return apSum + clsId2 - clsId1 - 1;
    }
    void updateConnections (void) {
      for (const auto& node : this->tree) {
        switch (getRotationToPerforme(node)) {
          case 1:
              this->zigZigBottomUp(node);
              break;
          case 2:
              this->zigZagBottomUp(node);
              break;
          case 3:
              this->zigZagTopDown(node);
              break;
          case 4:
              this->zigZagTopDown(node);
              break;
          default:
              break;
        }
      }
    }
    // void debug (void) //

    /* End of Auxiliary Functions */

    /* Attributes */
    private int numNodes = 0;
    private int numSwitches = 0;
    private int numClusters = 0;
    private int numUnionClusters = 0;
    private int clusterSize;
    private int switchSize;
    private std::vector<Switch> switches;
    private std::vector<Node*> tree;
    /* End of Attributes */
}
