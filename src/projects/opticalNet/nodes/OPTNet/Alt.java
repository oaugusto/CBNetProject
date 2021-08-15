package projects.opticalNet.nodes.OPTNet;

public class Alt {
    private int inNodeID = -1;
    private int outNodeID = -1;
    private int switchID = -1;

    Alt (int _inNodeID, int _outNodeID, int _switchID) {
      this.inNodeID = _inNodeID;
      this.outNodeID = _outNodeID;
      this.switchID = _switchID;
    }

    int getSwitchId () {
      return this.switchID;
    }
    int getInNodeId () {
      return this.inNodeID;
    }
    int getOutNodeId () {
      return this.outNodeID;
    }
}
