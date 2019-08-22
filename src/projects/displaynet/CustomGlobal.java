package projects.displaynet;

import java.util.ArrayList;

import projects.displaynet.nodeImplementations.BinaryTreeNode;
import projects.displaynet.nodeImplementations.SplayNetNode;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

public class CustomGlobal extends AbstractCustomGlobal {

    public int numNodes = 30;
    public ArrayList<BinaryTreeNode> tree = null;
    public TreeConnections treeTopology = null;

    @Override
    public boolean hasTerminated() {
        return false;
    }

    @Override
    public void preRun() {

        this.tree = new ArrayList<BinaryTreeNode> ();
        
        for (int i = 0; i < numNodes; i++) {
            SplayNetNode n = new SplayNetNode();
            n.finishInitializationWithDefaultModels(true);
            this.tree.add(n);
        }

        this.treeTopology = new TreeConnections(this.tree);
        this.treeTopology.setBalancedTree();
        if (Global.isGuiMode) {
            this.treeTopology.setPositions();
            Tools.repaintGUI();
        }
    }
    
    @Override
    public void preRound() {
        if (Global.isGuiMode) {
            this.treeTopology.setPositions();
            Tools.repaintGUI();
        }
    }

}