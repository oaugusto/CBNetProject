package projects.displaynet.nodes.nodeImplementations;

import projects.displaynet.CustomGlobal;
import projects.displaynet.nodes.tableEntry.Request;

/**
 * SplayNetApp
 */
public class SplayNetApp extends HandShakeLayer {

    private boolean first = true;

    @Override
    public void init() {
        super.init();

         if (first) {
            first = false;

            if (ID == 3) {
                this.newSplayOperation(29);;
            }
        }
    }

    public void newSplayOperation(int dst) {
        Request rq = new Request(ID, dst);
        this.myMsgBuffer.add(rq);
    }
    
    @Override
    public void splayCompleted() {
        CustomGlobal.activeSplays--;
    }
}