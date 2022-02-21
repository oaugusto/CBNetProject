package projects.seqrandomdisplaynet;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import projects.defaultProject.BalancedTreeTopology;
import projects.defaultProject.DataCollection;
import projects.defaultProject.RequestQueue;
import projects.defaultProject.TreeConstructor;
import projects.defaultProject.nodes.nodeImplementations.BinarySearchTreeLayer;
import projects.randomdisplaynet.nodes.nodeImplementations.CBNetNode;
import projects.seqrandomdisplaynet.nodes.nodeImplementation.CBNetApp;
import sinalgo.configuration.Configuration;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.tools.Tools;
import sinalgo.tools.Tuple;

public class CustomGlobal extends AbstractCustomGlobal {

    // final condition
    public long MAX_REQ;

    // simulation config
    public int numNodes = 30;
    public ArrayList<BinarySearchTreeLayer> tree = null;
    public BinarySearchTreeLayer controlNode = null;
    public TreeConstructor treeTopology = null;
    public RequestQueue requestQueue;
    
    public static double p = 0.5;

    // LOG
    DataCollection data = DataCollection.getInstance();

    @Override
    public boolean hasTerminated() {
        if (this.data.getCompletedRequests() >= MAX_REQ) {
            CBNetApp node = (CBNetApp) Tools.getNodeByID(1);
            this.data.addTotalTime(node.getCurrentRound());
            this.data.printRotationData();
            this.data.printRoutingData();
            return true;
        }
        return false;
    }

    @Override
    public void preRun() {


        String input = "";
        String output = "";

        try {

            if (Configuration.hasParameter("input")) {
                input = Configuration.getStringParameter("input");
            }

            if (Configuration.hasParameter("output")) {
                output = Configuration.getStringParameter("output");
            }
            
            if (Configuration.hasParameter("p")) {
                p = (double) Configuration.getDoubleParameter("p");
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Missing configuration parameters");
        }        

        // Set Log Path
        this.data.setPath(output);

        /*
         * read input data and configure the simulation
         */
        this.requestQueue = new RequestQueue(input);
        this.numNodes = this.requestQueue.getNumberOfNodes();
        MAX_REQ = this.requestQueue.getNumberOfRequests();


        /*
         * create the nodes and constructs the tree topology
         */
        this.tree = new ArrayList<BinarySearchTreeLayer>();

        for (int i = 0; i < numNodes; i++) {
            CBNetApp n = new CBNetApp();
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

        this.treeTopology = new BalancedTreeTopology(controlNode, this.tree);
        this.treeTopology.buildTree();
        this.treeTopology.setPositions();

    }

    public void activateNextSplay(int src, int dst) {
        CBNetApp srcnode = (CBNetApp)Tools.getNodeByID(src);	
        srcnode.newMessage(dst);;

        this.data.incrementActiveSplays();
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