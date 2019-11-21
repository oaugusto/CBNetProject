package projects.displaynet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import projects.defaultProject.DataCollection;
import projects.defaultProject.nodes.messages.AckApplicationMessage;
import projects.defaultProject.nodes.messages.ApplicationMessage;
import projects.defaultProject.nodes.nodeImplementations.ApplicationNode;
import projects.displaynet.nodes.tableEntry.Request;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.tools.Tools;

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
  public void ackMessage(AckApplicationMessage ackMsg) {

  }

  /*-----------------Data collector-------------------*/

  @Override
  public void posRound() {

  }

  @Override
  public void newSplayStarted(Request currentRequest) {

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