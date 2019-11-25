package projects.splaynet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashMap;

import projects.defaultProject.DataCollection;
import projects.defaultProject.nodes.messages.AckApplicationMessage;
import projects.defaultProject.nodes.messages.ApplicationMessage;
import projects.defaultProject.nodes.nodeImplementations.ApplicationNode;
import projects.displaynet.nodes.nodeImplementations.HandShakeLayer;
import projects.defaultProject.nodes.tableEntry.NodeInfo;
import projects.displaynet.nodes.tableEntry.Request;
import sinalgo.gui.transformation.PositionTransformation;

/**
 * SplayNetNode
 */
public class SplayNetApp extends HandShakeLayer implements ApplicationNode {

  private boolean requestCompleted = false;
  private DataCollection data = DataCollection.getInstance();

  @Override
  public void sendMessage(ApplicationMessage msg) {
    this.myMsgBuffer.add(msg);
  }

  @Override
  public void ackMessage(AckApplicationMessage ackMsg) {
    this.data.addRotations(ackMsg.getRequest().numOfRotations);
    this.data.addRouting(1);
    this.data.addThroughput(this.getCurrentRound());
    this.data.addRoundsPerSplay(ackMsg.getRequest().finalTime - ackMsg.getRequest().initialTime);
    this.data.incrementCompletedRequests();
    requestCompleted = true;
  }

  /*-----------------Data collector-------------------*/

  @Override
  public void newSplayStarted(Request currentRequest) {
    this.data.incrementActiveSplays();
  }

  @Override
  public void communicationClusterCompleted() {
    super.communicationClusterCompleted();
    this.data.incrementActiveClusters();
  }

  @Override
  public void clusterCompleted(HashMap<String, NodeInfo> cluster) {
    super.clusterCompleted(cluster);
    this.data.incrementActiveClusters();
  }

  @Override
  public void posRound() {
    if (ID == 1) {
      this.data.addNumOfActiveSplays();
      this.data.addNumOfActiveClusters();
      this.data.resetActiveClusters();
    }

    if (requestCompleted) {
      requestCompleted = false;
      this.data.decrementActiveSplays();
    }
  }

  /*-------------------------------------------------*/

  @Override
  public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
    // String text = ID + " l:" + this.minIdInSubtree + " r:" + this.maxIdInSubtree;
    String text = "" + ID;

    // draw the node as a circle with the text inside
    super.drawNodeAsDiskWithText(g, pt, highlight, text, 12, Color.YELLOW);
  }
}