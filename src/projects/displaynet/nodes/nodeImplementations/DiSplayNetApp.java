package projects.displaynet.nodes.nodeImplementations;

import java.util.HashMap;

import projects.defaultProject.DataCollection;
import projects.defaultProject.nodes.tableEntry.NodeInfo;
import projects.displaynet.nodes.tableEntry.Request;

/**
 * DiSplayNetApp
 */
public class DiSplayNetApp extends HandShakeLayer {

  private boolean completed = false;
  private DataCollection data = DataCollection.getInstance();

  public void newSplayOperation(int dst) {
    Request rq = new Request(ID, dst);
    this.myMsgBuffer.add(rq);

  }

  @Override
  public void newSplayStarted(Request currentRequest) {
    super.newSplayStarted(currentRequest);

    if (currentRequest.getDstId() < ID) {
      this.data.incrementActiveSplays();
    }
  }

  @Override
  public void clusterCompleted(HashMap<String, NodeInfo> cluster) {
    super.clusterCompleted(cluster);

    this.data.incrementActiveClusters();
  }

  @Override
  public void communicationClusterFormed(Request currentRequest) {
    super.communicationClusterFormed(currentRequest);
    if (currentRequest.getDstId() < ID) {
      this.data.incrementActiveClusters();
    }
  }

  @Override
  public void communicationCompleted(Request peerRequest) {
    super.communicationCompleted(peerRequest);
    Request activeRequest = this.getActiveSplay();

    if (activeRequest.getDstId() < ID) {
      completed = true;
      this.data.addRotations(activeRequest.numOfRotations + peerRequest.numOfRotations);
      this.data.addRouting(1);
      this.data.addThroughput(this.getCurrentRound());
      this.data.addRoundsPerSplay(peerRequest.finalTime - peerRequest.initialTime);
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

    if (completed) {
      completed = false;
      this.data.decrementActiveSplays();
    }
  }
}