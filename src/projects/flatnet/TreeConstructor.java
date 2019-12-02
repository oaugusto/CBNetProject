package projects.flatnet;

import java.util.ArrayList;

import projects.flatnet.nodes.nodeImplementations.BinaryTreeLayer;
import sinalgo.configuration.Configuration;
import sinalgo.runtime.Global;
import sinalgo.tools.Tools;

public class TreeConstructor {

	private BinaryTreeLayer controlNode;
	private ArrayList<BinaryTreeLayer> tree;

	public TreeConstructor(BinaryTreeLayer controlNode, ArrayList<BinaryTreeLayer> tree) {
		this.controlNode = controlNode;
		this.tree = tree;
	}

	public void setBalancedTree() {

		if (tree.isEmpty()) {
			Tools.fatalError("Empty network passed to TreeConstructor");
		}

		// build binary tree topology
		buildTree(0, this.tree.size());
		BinaryTreeLayer root = tree.get(0);

		// configure the control node
		this.controlNode.setParent(null);
		this.controlNode.setRightChild(null);
		this.controlNode.addLinkToLeftChild(root);
		this.controlNode.setLeftDescendants(root);
	}

	/**
	 * This function build a balance binary tree network and return root node
	 * @param start
	 * @param end
	 * @return
	 */
	private void buildTree(int start, int end) {

		BinaryTreeLayer parent = null;
		BinaryTreeLayer node = null;

		for(int i = end-1; i > start; i--){ 
			node = tree.get(i);
			parent = tree.get((i-1)/2);
			if((i-1)%2 == 1){
				parent.addLinkToRightChild(node);
				parent.setRightDescendants(node);
			}else{
				parent.addLinkToLeftChild(node);
				parent.setLeftDescendants(node);
			}
		}

	}


	public BinaryTreeLayer getRootNode() {
		return this.controlNode.getLeftChild();
	}

	private int getTreeHeight(BinaryTreeLayer root) {

		int left = (root.hasLeftChild()) ? getTreeHeight(root.getLeftChild()) : 0;
		int right = (root.hasRightChild()) ? getTreeHeight(root.getRightChild()) : 0;

		return Math.max(left, right) + 1;
	}

	public void setPositions() {
		if (!Global.isGuiMode) {
			return;
		}

		BinaryTreeLayer root = getRootNode();
		int height = getTreeHeight(root);

		double x = Configuration.dimX / 2;
		double y_space = Configuration.dimY / (height + 2);

		// null node
		this.controlNode.setPosition(x, Configuration.dimY/20, 0);

		setPositionHelper(root, x, y_space, 1);

		Tools.repaintGUI();
	}

	private void setPositionHelper(BinaryTreeLayer root, double x, double y_space, int level) {

		root.setPosition(x, y_space * (level + 1), 0);

		double x_space = Configuration.dimX / Math.pow(2, level + 1);

		if (root.hasLeftChild()) {
			setPositionHelper(root.getLeftChild(), x - x_space, y_space, level + 1);
		}

		if (root.hasRightChild()) {
			setPositionHelper(root.getRightChild(), x + x_space, y_space, level + 1);
		}
	}
}