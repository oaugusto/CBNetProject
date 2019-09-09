package projects.splaynet;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import projects.displaynet.RequestQueue;
import projects.displaynet.TreeConstructor;
import projects.displaynet.nodes.nodeImplementations.BinaryTreeLayer;
import projects.displaynet.nodes.nodeImplementations.SplayNetNode;
import projects.splaynet.nodes.nodeImplementations.SplayNetApp;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.tools.Tools;
import sinalgo.tools.Tuple;

public class CustomGlobal extends AbstractCustomGlobal {

    // final condition
    public long MAX_REQ;

    // simulation config
    public int numNodes = 30;
    public ArrayList<BinaryTreeLayer> tree = null;
    public BinaryTreeLayer controlNode = null;
    public TreeConstructor treeTopology = null;
    public RequestQueue requestQueue = new RequestQueue("inputs/tor_256_flow.txt", " ");

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
            SplayNetApp n = new SplayNetApp();
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

    }

    public static void activateNextSplay(int src, int dst) {
        SplayNetApp srcnode = (SplayNetApp)Tools.getNodeByID(src);	
        srcnode.newSplayOperation(dst);
    }
    
    @Override
    public void preRound() {
        this.treeTopology.setPositions();
    
        // System.out.println(this.data.getNumbugerOfActiveSplays());
        if(this.data.getNumbugerOfActiveSplays() < 1){
            if(requestQueue.hasNextRequest()){
                Tuple<Integer, Integer> r = requestQueue.getNextRequest();
                activateNextSplay(r.first, r.second);
            }
        }

    }

}