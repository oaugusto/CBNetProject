package projects.displaynet.nodes.nodeImplementations;

import projects.defaultProject.nodes.messages.AckApplicationMessage;
import projects.defaultProject.nodes.messages.ApplicationMessage;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

public class CommunicationLayer extends ControlLayer {

  private ApplicationMessage applicationMessage;

  public void setNewApplicationMessage(ApplicationMessage msg) {
    this.applicationMessage = msg;
  }

  public void connectionEstablished() {
    if (this.applicationMessage == null) {
      Tools.fatalError("Null aplication message");
    }
    this.sendForwardMessage(this.applicationMessage.getDestination(), this.applicationMessage);
    //System.out.println("connection established at " + ID);
  }

  @Override
  public void receiveMessage(Message msg) {
    super.receiveMessage(msg);

    if (msg instanceof ApplicationMessage) {
      ApplicationMessage appMsg = (ApplicationMessage) msg;

      this.sendForwardMessage(appMsg.getSource(), new AckApplicationMessage(appMsg));

      this.resetSplay();
      this.communicationCompleted();
      //System.out.println("communication completed at node " + ID);

    } else if (msg instanceof AckApplicationMessage) {
      AckApplicationMessage ackAppMsg = (AckApplicationMessage) msg;

      // clear current application msg
      this.applicationMessage = null;

      this.resetSplay();
      this.communicationCompleted();
      this.ackMessage(ackAppMsg);
      //System.out.println("communication completed at node " + ID);
    }
  }

  public void communicationCompleted() {}

  public void ackMessage(AckApplicationMessage ackMsg) {}

}
