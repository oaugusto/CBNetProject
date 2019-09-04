package projects.cbnet;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import projects.cbnet.nodes.nodeImplementations.CBNetNode;
import projects.displaynet.RequestQueue;
import projects.displaynet.TreeConstructor;
import projects.displaynet.nodes.nodeImplementations.BinaryTreeLayer;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.runtime.AbstractCustomGlobal;

public class CustomGlobal extends AbstractCustomGlobal {
    public int numNodes = 30;
    public ArrayList<BinaryTreeLayer> tree = null;
    public BinaryTreeLayer controlNode = null;
    public TreeConstructor treeTopology = null;
    // public RequestQueue rqueue = new RequestQueue("inputs/datasetC_pairs_small.txt", " ");

    public static boolean isSequencial = true;
    public static boolean newSplay = false;
    public static long nOperations = 0;

    public boolean firstTime = true;

    @Override
    public boolean hasTerminated() {
        return false;
    }

    @Override
    public void preRun() {

        /*
            create the nodes and constructs the tree topology
        */
        this.tree = new ArrayList<BinaryTreeLayer> ();
        
        for (int i = 0; i < numNodes; i++) {
            CBNetNode n = new CBNetNode();
            n.finishInitializationWithDefaultModels(true);
            this.tree.add(n);
        }

        this.controlNode  = new CBNetNode() {
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

        */
        // if (isSequencial) {

        //     this.newSplay = true;

        // } else  {
        //     while (this.rqueue.hasNextRequest()) {
        //         Tuple<Integer, Integer> pair = this.rqueue.getNextRequest();
        //         SplayNetApp node = (SplayNetApp) Tools.getNodeByID(pair.first);
        //         node.newSplayOperation(pair.second);
        //     }
        // }
    }
    
    @Override
    public void preRound() {
        this.treeTopology.setPositions();

        // if (isSequencial && newSplay) {
        //     newSplay = false;
        //     Tuple<Integer, Integer> pair = this.rqueue.getNextRequest();
        //     SplayNetApp node = (SplayNetApp) Tools.getNodeByID(pair.first);
        //     node.newSplayOperation(pair.second);
        // }
    }

}