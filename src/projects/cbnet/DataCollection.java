package projects.cbnet;

import projects.cbnet.nodes.nodeImplementations.CBNetApp;
import sinalgo.tools.Tools;
import sinalgo.tools.logging.Logging;
import sinalgo.tools.statistics.DataSeries;

public class DataCollection {

    private static DataCollection single_instance = null;

    private DataSeries rotationData = new DataSeries();
    private DataSeries routingData = new DataSeries();

    private long activeSplays = 0;
    private long activeClusters = 0;

    private long completedRequests = 0;

    // LOGS
    private Logging rotations_per_splay = Logging.getLogger("cbnet/rotations_per_splay.txt");
    private Logging routing_per_splay = Logging.getLogger("cbnet/routing_per_splay.txt");
    private Logging rounds_per_splay = Logging.getLogger("cbnet/rounds_per_splay.txt");
    private Logging total_time_log = Logging.getLogger("cbnet/total_time.txt");
    private Logging concurrency_log = Logging.getLogger("cbnet/concurrent_req.txt");
    private Logging num_of_cluster = Logging.getLogger("cbnet/clusters.txt");
    private Logging throughput_log = Logging.getLogger("cbnet/throughput.txt");

    private DataCollection() {

    }

    public static DataCollection getInstance() {
        if (single_instance == null) {
            single_instance = new DataCollection();
        }

        return single_instance;
    }

    public void initCollection() {
        this.activeSplays = 0;
        this.activeClusters = 0;
    }

    public void addRotations(long num) {
        this.rotationData.addSample(num);
        this.rotations_per_splay.logln("" + num);
    }

    public void addRouting(long num) {
        this.routingData.addSample(num);
        this.routing_per_splay.logln("" + num);
    }

    public void resetCollection() {
        this.rotationData.reset();
        this.routingData.reset();
    }

    public void addNumOfActiveSplays() {
        this.concurrency_log.logln("" + this.activeSplays);
    }

    public void addNumOfActiveClusters() {
        this.num_of_cluster.logln("" + this.activeClusters);
    }

    public void addTotalTime() {
        CBNetApp node = (CBNetApp) Tools.getNodeByID(1);
        this.total_time_log.logln("" + node.getCurrentRound());
    }

    public void addThroughput(long num) {
        this.throughput_log.logln("" + num);
    }

    public void addRoundsPerSplay(long num) {
        this.rounds_per_splay.logln("" + num);
    }

    public void incrementActiveSplays() {
        this.activeSplays++;
    }

    public void decrementActiveSplays() {
        this.activeSplays--;
    }

    public long getNumbugerOfActiveSplays() {
        return activeSplays;
    }

    public void incrementActiveClusters() {
        this.activeClusters++;
    }

    public void decrementActiveClusters() {
        this.activeClusters--;
    }

    public void resetActiveClusters() {
        this.activeClusters = 0;
    }

    public long getActiveClusters() {
        return activeClusters;
    }

    public void incrementCompletedRequests() {
        this.completedRequests++;
    }

    public long getCompletedRequests() {
        return completedRequests;
    }

    public void printRotationData() {
        System.out.println("Rotations:");
        System.out.println("Number of request: " + this.rotationData.getNumberOfSamples());
        System.out.println("Mean: " + this.rotationData.getMean());
        System.out.println("Standard Deviation: " + this.rotationData.getStandardDeviation());
        System.out.println("Min: " + this.rotationData.getMinimum());
        System.out.println("Max: " + this.rotationData.getMaximum());
    }

    public void printRoutingData() {
        System.out.println("Routing:");
        System.out.println("Number of request " + this.routingData.getNumberOfSamples());
        System.out.println("Mean: " + this.routingData.getMean());
        System.out.println("Standard Deviation: " + this.routingData.getStandardDeviation());
        System.out.println("Min: " + this.routingData.getMinimum());
        System.out.println("Max: " + this.routingData.getMaximum());
    }
}