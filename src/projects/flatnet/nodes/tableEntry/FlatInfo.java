package projects.flatnet.nodes.tableEntry;

import java.util.HashSet;

import projects.flatnet.nodes.tableEntry.NodeInfo;
import projects.flatnet.nodes.nodeImplementations.FlatTreeLayer;

/**
 * NodeInfo
 */
public class FlatInfo extends NodeInfo {

    public FlatInfo() {
        super();
    }

    public FlatInfo(FlatTreeLayer node, FlatTreeLayer parent, FlatTreeLayer left, FlatTreeLayer right,
            HashSet<Integer> leftDescendants, HashSet<Integer> rightDescendants) {
        super(node, parent, left, right, leftDescendants, rightDescendants);
    }
    
}