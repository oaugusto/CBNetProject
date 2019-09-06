package projects.displaynet.nodes.timers;

import projects.displaynet.CustomGlobal;
import projects.displaynet.nodes.nodeImplementations.SplayNetApp;
import sinalgo.nodes.timers.Timer;
import sinalgo.tools.Tools;

/**
 * TimerExponential
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

        SplayNetApp srcnode = (SplayNetApp) Tools.getNodeByID(src);
        srcnode.newSplayOperation(dst);

        CustomGlobal.mustGenerate = true;
    }
    
}