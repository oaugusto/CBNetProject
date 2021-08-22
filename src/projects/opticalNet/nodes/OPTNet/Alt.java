package projects.opticalNet.nodes.OPTNet;

public class Alt {
	private int switchID = -1;
    private int inNodeID = -1;
    private int outNodeID = -1;

    public Alt (int _switchID, int _inNodeID, int _outNodeID) {
      this.switchID = _switchID;
      this.inNodeID = _inNodeID;
      this.outNodeID = _outNodeID;
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
