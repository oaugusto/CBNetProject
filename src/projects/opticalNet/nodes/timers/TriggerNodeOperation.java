package projects.opticalNet.nodes.timers;

import projects.opticalNet.CustomGlobal;
import projects.opticalNet.nodes.nodeImplementations.NetworkNode;
//import projects.opticalNet.nodes.nodeImplementations.CBNetNode;
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

        NetworkNode srcnode = (NetworkNode) Tools.getNodeByID(src);
        srcnode.sendMsg(dst);

        CustomGlobal.mustGenerateSplay = true;
    }

}