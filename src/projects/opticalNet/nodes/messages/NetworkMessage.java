package projects.opticalNet.nodes.messages;

import sinalgo.nodes.messages.Message;

public class NetworkMessage extends Message {

  private int src;
  private int dst;

  // collect data variable
  private long rotations;
  private long routing;

  public long initialTime;
  public long finalTime;

  public NetworkMessage (int src, int dst) {
    this.src = src;
    this.dst = dst;

    // collect
    this.rotations = 0;
    this.routing = 0;
  }

  /**
   * @return the src
   */
  public int getSrc () {
    return src;
  }

  /**
   * @return the dst
   */
  public int getDst () {
    return dst;
  }


  /**
   * @param src the src to set
   */
  public void setSrc (int src) {
    this.src = src;
  }

  /**
   * @param dst the dst to set
   */
  public void setDst (int dst) {
    this.dst = dst;
  }


  @Override
  public Message clone () {
    return this;
  }

  /**
   * @return the rotations
   */
  public long getRotations () {
    return rotations;
  }

  /**
   * @return the routing
   */
  public long getRouting () {
    return routing;
  }

  public void incrementRotations () {
    this.rotations++;
  }

  public void incrementRouting () {
    this.routing++;
  }
}
