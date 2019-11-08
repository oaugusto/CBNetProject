package projects.displaynet.nodes.tableEntry;

/**
 * Request
 */
public class Request implements Comparable<Request> {

  // requester of communication id
  private int srcId;
  // target node to send message id
  private int dstId;
  // priority of this request
  private double priority = Double.MAX_VALUE;
  // current request belongs to master node
  private boolean isMaster = false;

  // log information
  public long numOfRotations = 0;
  public long numOfRouting = 0;

  public long initialTime = 0;
  public long finalTime = 0;

  public Request(int srcId, int dstId, boolean master) {
    this.srcId = srcId;
    this.dstId = dstId;
    this.isMaster = master;
  }

  public Request(int srcId, int dstId, double priority, boolean master) {
    this.srcId = srcId;
    this.dstId = dstId;
    this.priority = priority;
    this.isMaster = master;
  }

  public Request(Request o) {
    this.srcId = o.getSrcId();
    this.dstId = o.getDstId();
    this.priority = o.getPriority();
    this.isMaster = o.isMaster();
  }

  public int getSrcId() {
    return srcId;
  }

  public int getDstId() {
    return dstId;
  }

  public boolean isMaster() {
    return isMaster;
  }

  public void setPriority(double priority) {
    this.priority = priority;
  }

  public double getPriority() {
    return priority;
  }

  /**
   * the target node corresponds to the node to connect.
   * @return
   */
  public int getTargetNode() {
    if (isMaster) {
      return dstId;
    } else {
      return srcId;
    }
  }

  @Override
  public int compareTo(Request o) {
    int cmpPriority = Double.compare(this.priority, o.priority); // compare the priorities
    int cmpIDs = this.srcId - o.srcId; // in tie, use the id of the requester

    if (cmpPriority == 0) {
      if (cmpIDs == 0) {
        // the requester node has higher priority
        return Boolean.compare(this.isMaster(), o.isMaster());
      } else {
        return cmpIDs;
      }
    } else {
      return cmpPriority;
    }
  }
}