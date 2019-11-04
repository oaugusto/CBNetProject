package projects.displaynet.nodes.messages.controlMessages;

import projects.defaultProject.nodes.messages.NetworkMessage;
import sinalgo.nodes.messages.Message;

/**
 * RequestCluster
 */
public class RequestClusterMessage extends NetworkMessage implements
    Comparable<RequestClusterMessage> {

  private double priority;

  private int hopCounter;
  private boolean isFinalNode; // keep track if this node is final node in current request

  public RequestClusterMessage(RequestClusterMessage msg) {
    super(msg.getSource(), msg.getDestination());
    this.priority = msg.getPriority();
    this.hopCounter = msg.getHopCounter();
    this.isFinalNode = false;
  }

  public RequestClusterMessage(int src, int dst, int hopCounter, double priority) {
    super(src, dst);
    this.priority = priority;
    this.hopCounter = hopCounter;
    this.isFinalNode = false;
  }

  public double getPriority() {
    return priority;
  }

  public int getHopCounter() {
    return hopCounter;
  }

  public boolean isFinalNode() {
    return isFinalNode;
  }

  public void setPriority(double priority) {
    this.priority = priority;
  }

  public void setHopCounter(int hopCounter) {
    this.hopCounter = hopCounter;
  }

  public void incrementHopCounter() {
    this.hopCounter++;
  }

  public void setFinalNode() {
    this.isFinalNode = true;
  }

  @Override
  public Message clone() {
    return this;
  }

  @Override
  public int compareTo(RequestClusterMessage o) {
    int value = Double.compare(this.priority, o.priority);
    if (value == 0) { // In case tie, compare the id of the source node
      return this.getDestination() - o.getDestination();
    } else {
      return value;
    }
  }

}