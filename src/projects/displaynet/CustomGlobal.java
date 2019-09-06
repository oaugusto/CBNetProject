package projects.displaynet;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import projects.displaynet.nodes.nodeImplementations.BinaryTreeLayer;
import projects.displaynet.nodes.nodeImplementations.SplayNetApp;
import projects.displaynet.nodes.timers.TriggerNodeOperation;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;
import sinalgo.tools.Tuple;

public class CustomGlobal extends AbstractCustomGlobal {

    // LOG
    public static long activeSplays = 0;
    public static long numberClusters = 0;

    // final condition
    public static long MAX_REQ;
    public static long completedRequests = 0;

    // simulation config
    public int numNodes = 30;
    public ArrayList<BinaryTreeLayer> tree = null;
    public BinaryTreeLayer controlNode = null;
    public TreeConstructor treeTopology = null;
    public static RequestQueue rqueue = new RequestQueue("inputs/datasetC_pairs_small.txt", " ");

    // control execution
    public static boolean isSequencial = true;
    public static boolean mustGenerate = true;

    public Random random = new Random();
    public double lambda = 0.15;

    @Override
    public boolean hasTerminated() {
        return completedRequests >= MAX_REQ;
    }

    @Override
    public void preRun() {

        /*
         * create the nodes and constructs the tree topology
         */
        this.tree = new ArrayList<BinaryTreeLayer>();

        for (int i = 0; i < numNodes; i++) {
            SplayNetApp n = new SplayNetApp();
            n.finishInitializationWithDefaultModels(true);
            this.tree.add(n);
        }

        this.controlNode = new SplayNetApp() {
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
         * read input data and configure the simulation
         */
        this.numNodes = this.rqueue.getNumberOfNodes();
        MAX_REQ = this.rqueue.getNumberOfRequests();

    }

    public static void activateNextSplay(int src, int dst) {
						
        SplayNetApp srcnode = (SplayNetApp)Tools.getNodeByID(src);	
        srcnode.newSplayOperation(dst);
            
    }

    public static void generateNextSplayExponential(int src, int dst, double x) {
        TriggerNodeOperation ted = new TriggerNodeOperation(src,dst);
        ted.startGlobalTimer(x);
    }

    public static void generateNextSplay(double x){
        
        if(rqueue.hasNextRequest()){
            Tuple<Integer, Integer> r = rqueue.getNextRequest();
                    
            generateNextSplayExponential(r.first, r.second, x);
        }
    }
    
    @Override
    public void preRound() {
        this.treeTopology.setPositions();
        // LOG
        numberClusters = 0;

        if(isSequencial == true){			
            if(activeSplays < 1){
                if(rqueue.hasNextRequest()){
                    Tuple<Integer, Integer> r = rqueue.getNextRequest();
                    activateNextSplay(r.first, r.second);
                }
            }
        } else if (mustGenerate == true) { // CHANGE BATCH HERE!
            double u = random.nextDouble();
            double x = Math.log(1 - u) / (-lambda);
            x = (int) x;
            if (x <= 0) {
                x = 1;
            }
            mustGenerate = false;

            generateNextSplay(x);

        }

    }

}