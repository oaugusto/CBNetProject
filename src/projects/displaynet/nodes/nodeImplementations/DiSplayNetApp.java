package projects.displaynet.nodes.nodeImplementations;

import projects.defaultProject.DataCollection;
import projects.defaultProject.nodes.messages.ApplicationMessage;
import projects.defaultProject.nodes.nodeImplementations.ApplicationNode;

/**
 * DiSplayNetApp
 */
public class DiSplayNetApp extends HandShakeLayer implements ApplicationNode {

  private DataCollection data = DataCollection.getInstance();

  @Override
  public void sendMessage(ApplicationMessage msg) {
    this.myMsgBuffer.add(msg);
  }

  @Override
  public void posRound() {

  }

}