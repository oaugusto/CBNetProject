package projects.displaynet;

import java.util.ArrayList;

import projects.displaynet.nodeImplementations.BinaryTreeLayer;
import projects.displaynet.nodeImplementations.SplayNetNode;
import sinalgo.runtime.AbstractCustomGlobal;

public class CustomGlobal extends AbstractCustomGlobal {

    public int numNodes = 30;
    public ArrayList<BinaryTreeLayer> tree = null;
    public BinaryTreeLayer controlNode = null;
    public TreeConstructor treeTopology = null;
    public RequestQueue rqueue = null;

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
            SplayNetNode n = new SplayNetNode();
            n.finishInitializationWithDefaultModels(true);
            this.tree.add(n);
        }

        this.controlNode  = new SplayNetNode();
        this.controlNode.finishInitializationWithDefaultModels(true);

        this.treeTopology = new TreeConstructor(controlNode, this.tree);
        this.treeTopology.setBalancedTree();
        this.treeTopology.setPositions();
    }
    
    @Override
    public void preRound() {
        this.treeTopology.setPositions();
    }

}