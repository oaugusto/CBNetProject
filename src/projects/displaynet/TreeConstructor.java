package projects.displaynet;

import java.util.ArrayList;
import java.util.Collections;

import projects.displaynet.nodeImplementations.BinaryTreeLayer;
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
		BinaryTreeLayer root = buildTree(1, this.tree.size());

		// configure the control node
		this.controlNode.setParent(null);
		this.controlNode.setRightChild(null);
		this.controlNode.addLinkToLeftChild(root);
		this.controlNode.setMinIdInSubtree(1);
		this.controlNode.setMaxIdInSubtree(this.tree.size());
	}

	/**
	 * This function build a balance binary tree network and return root node
	 * @param start
	 * @param end
	 * @return
	 */
	private BinaryTreeLayer buildTree(int start, int end) {

		int parentId = Integer.MIN_VALUE;
		int leftChildId = Integer.MIN_VALUE;
		int rightChildId = Integer.MIN_VALUE;

		BinaryTreeLayer parent = null;
		BinaryTreeLayer leftChild = null;
		BinaryTreeLayer rightChild = null;

		parentId = (start + end) / 2;
		parent = tree.get(parentId - 1);
		parent.setMinIdInSubtree(start);
		parent.setMaxIdInSubtree(end);

		// case there is left subtree
		if (parentId != start) {
			leftChildId = (start + parentId - 1) / 2; // find left child
			leftChild = tree.get(leftChildId - 1);
			parent.addLinkToLeftChild(leftChild);
			leftChild.setMinIdInSubtree(start);
			leftChild.setMaxIdInSubtree(parentId - 1);

			buildTree(start, parentId - 1);
		}

		// case there is right subtree
		if (parentId != end) {
			rightChildId = (parentId + 1 + end) / 2;
			rightChild = tree.get(rightChildId - 1);
			parent.addLinkToRightChild(rightChild);
			rightChild.setMinIdInSubtree(parentId + 1);
			rightChild.setMaxIdInSubtree(end);

			buildTree(parentId + 1, end);
		}

		return parent;
	}

	public void linearTree() {
		BinaryTreeLayer node = this.tree.get(0);
		BinaryTreeLayer previous = node;

		node.setLeftChild(null);
		node.setRightChild(null);
		node.setMaxIdInSubtree(node.ID);
		node.setMinIdInSubtree(node.ID);

		for (int i = 1; i < this.tree.size(); i++) {
			node = this.tree.get(i);
			node.setRightChild(null);
			node.setMaxIdInSubtree(node.ID);
			node.setMinIdInSubtree(previous.getMinIdInSubtree());
			node.setLeftChild(previous);
			previous.setParent(node);

			previous = node;
		}
	}

	public void randomTree() {
		ArrayList<Integer> indexs = new ArrayList<Integer>();
		for (int i = 0; i < this.tree.size(); i++) {
			indexs.add(i);
		}

		Collections.shuffle(indexs);

		BinaryTreeLayer root = this.tree.get(indexs.remove(0));
		root.setParent(null);
		root.setLeftChild(null);
		root.setRightChild(null);
		root.setMaxIdInSubtree(root.ID);
		root.setMinIdInSubtree(root.ID);

		BinaryTreeLayer n;
		BinaryTreeLayer prev;
		BinaryTreeLayer next;

		while (!indexs.isEmpty()) {
			n = this.tree.get(indexs.remove(0));
			prev = null;
			next = root;

			while (next != null) {
				prev = next;

				if (next.ID > n.ID) {
					if (n.ID < next.getMinIdInSubtree()) {
						next.setMinIdInSubtree(n.ID);
					}
					next = next.getLeftChild();
				} else {
					if (n.ID > next.getMaxIdInSubtree()) {
						next.setMaxIdInSubtree(n.ID);
					}
					next = next.getRightChild();
				}
			}

			n.setParent(prev);
			n.setLeftChild(null);
			n.setRightChild(null);
			n.setMaxIdInSubtree(n.ID);
			n.setMinIdInSubtree(n.ID);

			if (prev.ID > n.ID) {
				prev.setLeftChild(n);
			} else {
				prev.setRightChild(n);
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