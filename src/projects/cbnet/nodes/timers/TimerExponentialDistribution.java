package projects.cbnet.nodes.timers;

import projects.cbnet.CustomGlobal;
import projects.cbnet.nodes.nodeImplementations.CBNetNode;
import sinalgo.nodes.timers.Timer;
import sinalgo.tools.Tools;

/**
 * TimerExponentialDistribution
 */
public class TimerExponentialDistribution extends Timer {

    public int src;
    public int dst;

    public TimerExponentialDistribution(int src, int dst) {
        this.src = src;
        this.dst = dst;
    }

    @Override
    public void fire() {

        CBNetNode srcnode = (CBNetNode) Tools.getNodeByID(src);
        srcnode.newMessage(dst);

        CustomGlobal.mustGenerate = true;
    }

}