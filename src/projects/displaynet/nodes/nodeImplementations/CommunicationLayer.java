package projects.displaynet.nodes.nodeImplementations;

import projects.defaultProject.nodes.messages.ApplicationMessage;

public abstract class CommunicationLayer extends ControlLayer {

  private ApplicationMessage applicationMessage;

  public void setNewApplicationMessage(ApplicationMessage msg) {
    this.applicationMessage = msg;
  }

}
