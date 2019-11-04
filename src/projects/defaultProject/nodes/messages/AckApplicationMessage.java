package projects.defaultProject.nodes.messages;

public class AckApplicationMessage extends NetworkMessage {

  public AckApplicationMessage(int src, int dst) {
    super(src, dst);
  }
}
