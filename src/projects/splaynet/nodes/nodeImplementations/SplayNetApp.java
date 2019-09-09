package projects.splaynet.nodes.nodeImplementations;

import java.util.HashMap;

import projects.displaynet.nodes.nodeImplementations.HandShakeLayer;
import projects.displaynet.nodes.tableEntry.NodeInfo;
import projects.displaynet.nodes.tableEntry.Request;
import projects.splaynet.DataCollection;

/**
 * SplayNetNode
 */
public class SplayNetApp extends HandShakeLayer {

    private int numOfRotations = 0;
    private DataCollection data  = DataCollection.getInstance();

    public void newSplayOperation(int dst) {
        Request rq = new Request(ID, dst);
        this.myMsgBuffer.add(rq);

        this.data.incrementActiveSplays();
    }
        
    @Override
    public void newSplayStarted(Request request) {
        super.newSplayStarted(request);

        this.numOfRotations = 0;
    }

    @Override
    public void clusterCompleted(HashMap<String, NodeInfo> cluster) {
        super.clusterCompleted(cluster);

        this.data.incrementActiveClusters();
    }

    @Override
    public void rotationCompleted() {
        super.rotationCompleted();

        this.numOfRotations++;
    }
    
    @Override
    public void communicationClusterFormed(Request request) {
        super.communicationClusterFormed(request);
        if (request.srcId < ID) {
            this.data.incrementActiveClusters();
        }
    }

    @Override
    public void communicationCompleted(Request request) {
        super.communicationCompleted(request);

        if (request.srcId < ID) {
            this.data.decrementActiveSplays();
            this.data.addRotations(this.numOfRotations + request.numOfRotations);
            this.data.addRouting(1);
        }
    }

    @Override
    public void posRound() {
        super.posRound();

        if (ID == 1) {
            this.data.addNumOfActiveSplays();
            this.data.addNumOfActiveClusters();
            this.data.resetActiveClusters();
        }
    }


    
}