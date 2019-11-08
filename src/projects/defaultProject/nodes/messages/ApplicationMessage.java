package projects.defaultProject.nodes.messages;

public class ApplicationMessage extends NetworkMessage implements Comparable<ApplicationMessage> {

  private double priority;

  // collect data variable
  private long rotations = 0;
  private long routing = 0;

  public long initialTime;
  public long finalTime;

  public ApplicationMessage(int source, int destination) {
    super(source, destination);
  }

  public double getPriority() {
    return priority;
  }

  public void setPriority(double priority) {
    this.priority = priority;
  }

  public long getRotations() {
    return this.rotations;
  }

  public long getRouting() {
    return this.routing;
  }

  public void incrementRotations() {
    this.rotations++;
  }

  public void incrementRouting() {
    this.routing++;
  }

  @Override
  public int compareTo(ApplicationMessage o) {
    int value = Double.compare(this.priority, o.priority);
    if (value == 0) { // In case tie, compare the id of the source node
      return this.getDestination() - o.getDestination();
    } else {
      return value;
    }
  }
}
