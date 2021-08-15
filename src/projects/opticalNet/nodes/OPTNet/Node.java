package projects.opticalNet.nodes.OPTNet;

import java.util.Collections;

public class Node {

	/* Constructors */
    Node () {
    	this.id = ID++;
    	this.father = null;
    	this.leftChild = null;
    	this.rightChild = null;
    	this.minId = this.id;
    	this.maxId = this.id;
    }

    Node (int dummy) {
    	this.id = dummy;
    	this.minId = this.id;
    	this.maxId = this.id;
    }

    Node (Node father, Node leftChild, Node rightChild) {
    	this.id = ID++;
    	this.father = father;
    	this.leftChild = leftChild;
    	this.rightChild = rightChild;
    	this.minId = this.leftChild.getId() == -1 ? this.getId() : this.leftChild.getId();
    	this.maxId = this.rightChild.getId() == -1 ? this.getId() : this.rightChild.getId();
    }
    /* End of Constructors */

    /* Getters */
    int getId () {
        return this.id;
    }

    Node getFather () {
        return this.father;
    }

    Node getLeftChild () {
        return this.leftChild;
    }

    Node getRightChild () {
        return this.rightChild;
    }

    int getLeftSwitch () {
        return this.leftSwitch;
    }

    int getRightSwitch () {
        return this.rightSwitch;
    }

    int getMinId () {
        return this.minId;
    }

    int getMaxId () {
        return this.maxId;
    }

    int getWeight () {
        return this.weight;
    }

    /* End of Getters */
    /* Setters */
    void setFather (Node father) {
        if (this.getId() == -1)
            return;

        this.father = father;
    }

    int setLeftChild (Node child, int switchId /*= -1*/) {
        if (this.getId() == -1)
            return -1;
        //Collections.swap(this.leftSwitch, switchId);

        if (this.leftChild.getId() != -1)
            this.leftChild.getFather().resetNode(this);

        this.leftChild = child;
        this.leftChild.setFather(this);
        this.updateMinMax(child, false);

        return switchId;
    }

    int setRightChild (Node child, int switchId /*= -1*/) {
        if (this.getId() == -1)
            return -1;
        //std::swap(this->rightSwitch, switchId);

        if (this.rightChild.getId() != -1)
            this.rightChild.getFather().resetNode(this);

        this.rightChild = child;
        this.rightChild.setFather(this);
        this.updateMinMax(child, false);

        return switchId;
    }

    void resetNode (Node rstNode) {
        if (this.getId() == -1)
            return;

        this.updateMinMax(rstNode, true);
        if (this.leftChild.getId() == rstNode.getId()) {
            this.leftChild = null;
        } else if (this.rightChild.getId() == rstNode.getId()) {
            this.rightChild = null;
        } else if (this.father.getId() == rstNode.getId()) {
            this.father = null;
        }
    }

    void updateMinMax (Node child, boolean remove /*= false*/) {
        if (remove) {
            if (child.getId() > this.getId())
                this.maxId = this.getId();
            else
                this.minId = this.getId();
        } else {
            this.minId = Math.min(this.minId, child.getMinId());
            this.maxId = Math.max(this.maxId, child.getMaxId());
        }

        if (this.getFather().getId() != -1) {
            if (this.getFather().getMinId() != this.minId) {
                this.getFather().updateMinMax(child, remove);
            } else if (this.getFather().getMaxId() != this.maxId) {
                this.getFather().updateMinMax(child, remove);
            }
        }
    }

    void setMinId (int minId) {
        this.minId = minId;
    }

    void setMaxId (int maxId) {
        this.maxId = maxId;
    }
    /* End of Setters */

	/* Attributes */
    Node father;
    Node leftChild;
    Node rightChild;

    private int leftSwitch = -1;
    private int rightSwitch = -1;

    private int minId = -1;
    private int maxId = -1;

    private int weight = 0;

	private int id = 0;
	private static int ID = 0;
	/* End of Attributes */

}
