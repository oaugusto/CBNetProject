package projects.flatnet.nodes.timers;

import projects.flatnet.CustomGlobal;
import projects.flatnet.nodes.nodeImplementations.FlatNetNode;
import sinalgo.nodes.timers.Timer;
import sinalgo.tools.Tools;

/**
 * TimerExponentialDistribution
 */
public class TriggerNodeOperation extends Timer {

  public int src;
  public int dst;

  public TriggerNodeOperation(int src, int dst) {
    this.src = src;
    this.dst = dst;
  }

  @Override
  public void fire() {

    FlatNetNode srcnode = (FlatNetNode) Tools.getNodeByID(src);
    srcnode.newMessage(dst);

    CustomGlobal.mustGenerateSplay = true;
  }

}