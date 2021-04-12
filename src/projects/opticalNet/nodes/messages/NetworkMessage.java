package projects.opticalNet.nodes.messages;

import sinalgo.nodes.messages.Message;

/**
 * CBNetMessage
 */
public class NetworkMessage extends Message implements Comparable<NetworkMessage> {

  private int src;
  private int dst;
  private double priority;

  // collect data variable
  private long rotations;
  private long routing;

  public long initialTime;
  public long finalTime;

  public NetworkMessage(int src, int dst, double priority) {
    this.src = src;
    this.dst = dst;
    this.priority = priority;

    // collect
    this.rotations = 0;
    this.routing = 0;
  }

  /**
   * @return the src
   */
  public int getSrc() {
    return src;
  }

  /**
   * @return the dst
   */
  public int getDst() {
    return dst;
  }

  /**
   * @return the priority
   */
  public double getPriority() {
    return priority;
  }

  /**
   * @param src the src to set
   */
  public void setSrc(int src) {
    this.src = src;
  }

  /**
   * @param dst the dst to set
   */
  public void setDst(int dst) {
    this.dst = dst;
  }

  /**
   * @param priority the priority to set
   */
  public void setPriority(double priority) {
    this.priority = priority;
  }

  @Override
  public Message clone() {
    return this;
  }

  /**
   * @return the rotations
   */
  public long getRotations() {
    return rotations;
  }

  /**
   * @return the routing
   */
  public long getRouting() {
    return routing;
  }

  public void incrementRotations() {
    this.rotations++;
  }

  public void incrementRouting() {
    this.routing++;
  }

  @Override
  public int compareTo(NetworkMessage o) {
    int value = Double.compare(this.priority, o.priority);
    if (value == 0) { // In case tie, compare the id of the source node
      return this.dst - o.dst;
    } else {
      return value;
    }
  }


}