package projects.displaynet.nodes.nodeImplementations;

import projects.displaynet.CustomGlobal;
import projects.displaynet.nodes.tableEntry.Request;

/**
 * SplayNetApp
 */
public class SplayNetApp extends HandShakeLayer {

    public void newSplayOperation(int dst) {
        Request rq = new Request(ID, dst);
        this.myMsgBuffer.add(rq);
    }
    
    @Override
    public void splayCompleted() {
        CustomGlobal.activeSplays--;
    }
}