package projects.splaynet.nodes.nodeImplementations;

import java.util.HashMap;

import projects.defaultProject.DataCollection;
import projects.displaynet.nodes.nodeImplementations.HandShakeLayer;
import projects.defaultProject.nodes.tableEntry.NodeInfo;
import projects.displaynet.nodes.tableEntry.Request;

/**
 * SplayNetNode
 */
public class SplayNetApp extends HandShakeLayer {

    private DataCollection data  = DataCollection.getInstance();

    public void newSplayOperation(int dst) {
        Request rq = new Request(ID, dst);
        this.myMsgBuffer.add(rq);

        this.data.incrementActiveSplays();
    }
    
    @Override
    public void newSplayStarted(Request request) {
        super.newSplayStarted(request);
    }

    @Override
    public void clusterCompleted(HashMap<String, NodeInfo> cluster) {
        super.clusterCompleted(cluster);

        this.data.incrementActiveClusters();
    }

    @Override
    public void rotationCompleted() {
        super.rotationCompleted();
    }
    
    @Override
    public void communicationClusterFormed(Request request) {
        super.communicationClusterFormed(request);
        if (request.getDstId() < ID) {
            this.data.incrementActiveClusters();
        }
    }

    @Override
    public void communicationCompleted(Request request) {
        super.communicationCompleted(request);
        Request activeRequest = this.getActiveSplay();

        if (activeRequest.getDstId() < ID) {
            this.data.decrementActiveSplays();
            this.data.addRotations(activeRequest.numOfRotations + request.numOfRotations);
            this.data.addRouting(1);
            this.data.addThroughput(this.getCurrentRound());
            this.data.addRoundsPerSplay(request.finalTime - request.initialTime);;
            this.data.incrementCompletedRequests();
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