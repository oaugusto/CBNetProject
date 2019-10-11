package projects.seqsimplenet;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import projects.displaynet.DataCollection;
import projects.displaynet.RequestQueue;
import projects.displaynet.TreeConstructor;
import projects.displaynet.nodes.nodeImplementations.BinaryTreeLayer;
import projects.simplenet.nodes.nodeImplementations.CBNetNode;
import projects.seqsimplenet.nodes.nodeImplementation.CBNetApp;
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
    public ArrayList<BinaryTreeLayer> tree = null;
    public BinaryTreeLayer controlNode = null;
    public TreeConstructor treeTopology = null;
    public RequestQueue requestQueue;

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

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Missing configuration parameters");
        }        

        // Set Log Path
        this.data.setPath(output);

        /*
         * read input data and configure the simulation
         */
        this.requestQueue = new RequestQueue(input, " ");
        this.numNodes = this.requestQueue.getNumberOfNodes();
        MAX_REQ = this.requestQueue.getNumberOfRequests();


        /*
         * create the nodes and constructs the tree topology
         */
        this.tree = new ArrayList<BinaryTreeLayer>();

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

        this.treeTopology = new TreeConstructor(controlNode, this.tree);
        this.treeTopology.setBalancedTree();
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