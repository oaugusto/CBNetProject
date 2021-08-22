package projects.opticalNet.nodes.OPTNet;

public class Alt {
    private int inNodeID = -1;
    private int outNodeID = -1;
    private int switchID = -1;

    public Alt (int _inNodeID, int _outNodeID, int _switchID) {
      this.inNodeID = _inNodeID;
      this.outNodeID = _outNodeID;
      this.switchID = _switchID;
    }

    public int getSwitchId () {
      return this.switchID;
    }

    public int getInNodeId () {
      return this.inNodeID;
    }

    public int getOutNodeId () {
      return this.outNodeID;
    }
}
