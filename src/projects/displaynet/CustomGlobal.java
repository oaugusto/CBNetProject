package projects.displaynet;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import projects.displaynet.nodes.nodeImplementations.BinaryTreeLayer;
import projects.displaynet.nodes.nodeImplementations.DiSplayNetApp;
import projects.displaynet.nodes.nodeImplementations.SplayNetNode;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.tools.Tools;
import sinalgo.tools.Tuple;

public class CustomGlobal extends AbstractCustomGlobal {

    // final condition
    public long MAX_REQ;

    // simulation
    public int numNodes = 30;
    public ArrayList<BinaryTreeLayer> tree = null;
    public BinaryTreeLayer controlNode = null;
    public TreeConstructor treeTopology = null;
    public RequestQueue requestQueue = new RequestQueue("inputs/tor_512_flow.txt", " ");
    // public RequestQueue requestQueue = new RequestQueue("inputs/datasetC_pairs_small.txt", " ");

    // control execution
    public static boolean isSequencial = false;
    public static boolean mustGenerate = true;

    public Random random = Tools.getRandomNumberGenerator();
    public double lambda = 0.15;

    // LOG
    DataCollection data = DataCollection.getInstance();

    @Override
    public boolean hasTerminated() {
        if (this.data.getCompletedRequests() >= MAX_REQ) {
            this.data.addTotalTime();
            this.data.printRotationData();
            this.data.printRoutingData();
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
        MAX_REQ = this.requestQueue.getNumberOfRequests();

        /*
         * create the nodes and constructs the tree topology
         */
        this.tree = new ArrayList<BinaryTreeLayer>();

        for (int i = 0; i < numNodes; i++) {
            DiSplayNetApp n = new DiSplayNetApp();
            n.finishInitializationWithDefaultModels(true);
            this.tree.add(n);
        }

        this.controlNode = new SplayNetNode() {
            public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
                String text = "ControlNode";
                super.drawNodeAsDiskWithText(g, pt, highlight, text, 10, Color.YELLOW);
            }
        };
        this.controlNode.finishInitializationWithDefaultModels(true);

        this.treeTopology = new TreeConstructor(controlNode, this.tree);
        this.treeTopology.setBalancedTree();
        this.treeTopology.setPositions();


        /*
         *  initiate sigma buffers with message 
         */
        while (this.requestQueue.hasNextRequest()) {
            Tuple<Integer, Integer> r = requestQueue.getNextRequest();
            DiSplayNetApp node = (DiSplayNetApp) Tools.getNodeByID(r.first);
            node.newSplayOperation(r.second);
        }

    }

    @Override
    public void preRound() {
        this.treeTopology.setPositions();
    }
}