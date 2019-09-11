package projects.displaynet;

import projects.displaynet.nodes.nodeImplementations.DiSplayNetApp;
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
   private Logging rotations_per_splay;
   private Logging routing_per_splay;
   private Logging rounds_per_splay;
   private Logging total_time_log;
   private Logging concurrency_log;
   private Logging num_of_cluster;
   private Logging throughput_log;

   private DataCollection() {

   }

   public void setPath(String path) {
       rotations_per_splay = Logging.getLogger(path + "/rotations_per_splay.txt");
       routing_per_splay = Logging.getLogger(path + "/routing_per_splay.txt");
       rounds_per_splay = Logging.getLogger(path + "/rounds_per_splay.txt");
       total_time_log = Logging.getLogger(path + "/total_time.txt");
       concurrency_log = Logging.getLogger(path + "/concurrent_req.txt");
       num_of_cluster = Logging.getLogger(path + "/clusters.txt");
       throughput_log = Logging.getLogger(path + "/throughput.txt");
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

    public void addTotalTime(long num) {
        this.total_time_log.logln("" + num);
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