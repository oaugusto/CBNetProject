package projects.seqsemisplaynet.nodes.nodeImplementation;

import java.util.HashMap;

import projects.cbnet.nodes.messages.CBNetMessage;
import projects.cbnet.nodes.tableEntry.CBInfo;
import projects.displaynet.DataCollection;
import projects.semisplaynet.nodes.nodeImplementations.CBNetNode;

/**
 * CBNetApp
 */
public class CBNetApp extends CBNetNode {

    private boolean msgSent = false;
    private DataCollection data = DataCollection.getInstance();

    @Override
    public void newMessageSent() {
        super.newMessageSent();
    }

    @Override
    public void clusterCompletedBottomUp(HashMap<String, CBInfo> cluster) {
        super.clusterCompletedBottomUp(cluster);

        this.data.incrementActiveClusters();
    }
    
    @Override
    public void clusterCompletedTopDown(HashMap<String, CBInfo> cluster) {
        super.clusterCompletedTopDown(cluster);

        this.data.incrementActiveClusters();
    }
    
    @Override
    public void targetNodeFound(CBInfo target) {
        super.targetNodeFound(target);

        this.data.incrementActiveClusters();
    }

    @Override
    public void communicationCompleted(CBNetMessage msg) {
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