package projects.flatnet.nodes.nodeImplementations;

import java.util.HashMap;

import projects.flatnet.nodes.messages.FlatNetMessage;
import projects.flatnet.nodes.tableEntry.FlatInfo;
import projects.defaultProject.DataCollection;

/**
 * CBNetApp
 */
public class FlatNetApp extends FlatNetNode {

    private boolean msgSent = false;
    private DataCollection data = DataCollection.getInstance();

    @Override
    public void newMessageSent() {
        super.newMessageSent();

        this.data.incrementActiveSplays();
    }

    @Override
    public void clusterCompletedBottomUp(HashMap<String, FlatInfo> cluster) {
        super.clusterCompletedBottomUp(cluster);

        this.data.incrementActiveClusters();
    }
    
    @Override
    public void clusterCompletedTopDown(HashMap<String, FlatInfo> cluster) {
        super.clusterCompletedTopDown(cluster);

        this.data.incrementActiveClusters();
    }
    
    @Override
    public void targetNodeFound(FlatInfo target) {
        super.targetNodeFound(target);

        this.data.incrementActiveClusters();
    }

    @Override
    public void communicationCompleted(FlatNetMessage msg) {
        super.communicationCompleted(msg);

        this.msgSent = true;
        this.data.addRotations(msg.getRotations());
        this.data.addRouting(msg.getRouting());
        this.data.addThroughput(this.getCurrentRound());
        this.data.addRoundsPerSplay(msg.finalTime - msg.initialTime);
        this.data.incrementCompletedRequests();
    }

    @Override
    public void posRound() {
        super.posRound();

        if (ID == 1) {
            this.data.addNumOfActiveSplays();
            this.data.addNumOfActiveClusters();
            this.data.resetActiveClusters();
        }

        if (this.msgSent) {
            this.msgSent = false;
            this.data.decrementActiveSplays();
        }
    }
}