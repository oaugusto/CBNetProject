package projects.cbnet;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import projects.cbnet.nodes.nodeImplementations.CBNetNode;
import projects.cbnet.nodes.timers.TriggerNodeOperation;
import projects.displaynet.RequestQueue;
import projects.displaynet.TreeConstructor;
import projects.displaynet.nodes.nodeImplementations.BinaryTreeLayer;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.tools.Tools;
import sinalgo.tools.Tuple;

public class CustomGlobal extends AbstractCustomGlobal {

    // final condition
    public long MAX_REQ;

    // simulation
    public int numNodes;
    public ArrayList<BinaryTreeLayer> tree = null;
    public BinaryTreeLayer controlNode = null;
    public TreeConstructor treeTopology = null;
    // public RequestQueue requestQueue = new RequestQueue("inputs/tor_512_flow.txt", " ");
    public RequestQueue requestQueue = new RequestQueue("inputs/datasetC_pairs_small.txt", " ");

    // control execution
    public static boolean isSequencial = false;
    public static boolean mustGenerate = true;

    public Random random = new Random();
    public double lambda = 0.15;

    // LOG
    DataCollection data = DataCollection.getInstance();

    @Override
    public boolean hasTerminated() {
        if (this.data.getCompletedRequests() >= MAX_REQ) {
            data.printRotationData();
            data.printRoutingData();
            return true;
        }
        return false;
    }

    @Override
    public void preRun() {

        /*
         * read input data and configure the simulation
         */
        this.numNodes = this.requestQueue.getNumberOfNodes();
        // MAX_REQ = this.rqueue.getNumberOfRequests();
        MAX_REQ = 1000;

        /*
         * create the nodes and constructs the tree topology
         */
        this.tree = new ArrayList<BinaryTreeLayer>();

        for (int i = 0; i < numNodes; i++) {
            CBNetNode n = new CBNetNode();
            n.finishInitializationWithDefaultModels(true);
            this.tree.add(n);
        }

        this.controlNode = new CBNetNode() {
            public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
                String text = "ControlNode";
                super.drawNodeAsDiskWithText(g, pt, highlight, text, 10, Color.YELLOW);
            }
        };
        this.controlNode.finishInitializationWithDefaultModels(true);

        this.treeTopology = new TreeConstructor(controlNode, this.tree);
        this.treeTopology.setBalancedTree();
        this.treeTopology.setPositions();
    }

    public static void activateNextSplay(int src, int dst) {
        CBNetNode srcnode = (CBNetNode) Tools.getNodeByID(src);
        srcnode.newMessage(dst);
    }

    public void generateNextSplayExponential(int src, int dst) {
        double u = random.nextDouble();
        double x = Math.log(1 - u) / (-lambda);

        x = (int) x;
        if (x <= 0) {
            x = 1;
        }

        TriggerNodeOperation ted = new TriggerNodeOperation(src, dst);
        ted.startGlobalTimer(x);
    }

    public void generateNextSplay() {
        // initialize only one splay
        if (requestQueue.hasNextRequest()) {
            Tuple<Integer, Integer> r = requestQueue.getNextRequest();

            generateNextSplayExponential(r.first, r.second);
        }
    }

    @Override
    public void preRound() {
        this.treeTopology.setPositions();

        if (isSequencial == true) {
            if (data.getNumbugerOfActiveSplays() < 1) {
                if (requestQueue.hasNextRequest()) {
                    Tuple<Integer, Integer> r = requestQueue.getNextRequest();
                    activateNextSplay(r.first, r.second);
                    // System.out.println("src: " + r.first + " dst: " + r.second);

                    this.data.incrementActiveSplays();
                }
            }
        } else if (mustGenerate == true) { // CHANGE BATCH HERE!
            mustGenerate = false;
            generateNextSplay();
        }

    }

}