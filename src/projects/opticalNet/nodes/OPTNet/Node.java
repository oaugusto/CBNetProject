package projects.opticalNet.nodes.OPTNet;

public class Node {

	/* Attributes */
    Node father = null;
    Node leftChild = null;
    Node rightChild = null;

    private int minId = -1;
    private int maxId = -1;

    private int weight = 0;

	private int id = 0;
	private static int ID = 0;
	/* End of Attributes */

	/* Constructors */
    public Node () {
    	this.id = ID++;
    	this.father = new Node(-1);
    	this.leftChild = new Node(-1);
    	this.rightChild = new Node(-1);
    	this.minId = this.id;
    	this.maxId = this.id;
    }

    public Node (int dummy) {
    	this.id = dummy;
    	this.minId = this.id;
    	this.maxId = this.id;
    }

    public Node (Node father, Node leftChild, Node rightChild) {
    	this.id = ID++;
    	this.father = father;
    	this.leftChild = leftChild;
    	this.rightChild = rightChild;
    	this.minId = this.leftChild.getId() == -1 ? this.getId() : this.leftChild.getId();
    	this.maxId = this.rightChild.getId() == -1 ? this.getId() : this.rightChild.getId();
    }
    /* End of Constructors */

    /* Getters */
    public int getId () {
        return this.id;
    }

    public Node getFather () {
        return this.father;
    }

    public Node getLeftChild () {
        return this.leftChild;
    }

    public Node getRightChild () {
        return this.rightChild;
    }

    public int getMinId () {
        return this.minId;
    }

    public int getMaxId () {
        return this.maxId;
    }

    public int getWeight () {
        return this.weight;
    }

    /* End of Getters */
    /* Setters */
    public void setFather (Node father) {
        if (this.getId() == -1)
            return;

        this.father = father;
    }

    public int setChild (Node child) {
        if (child.getId() == -1) {
            if (this.getLeftChild().getId() == -1)
                return this.setLeftChild(child);
            else
                return this.setRightChild(child);

        } else if (this.getId() > child.getId()) {
            return this.setLeftChild(child);
        } else {
            return this.setRightChild(child);
        }
    }

    public int setLeftChild (Node child) {
        if (this.getId() == -1)
            return -1;

        if (this.leftChild.getId() != -1)
            this.leftChild.getFather().resetNode(this);

        this.leftChild = child;
        this.leftChild.setFather(this);
        return this.updateMinMax(child, false);
    }

    public int setRightChild (Node child) {
        if (this.getId() == -1)
            return -1;

        if (this.rightChild.getId() != -1)
            this.rightChild.getFather().resetNode(this);

        this.rightChild = child;
        this.rightChild.setFather(this);

        return this.updateMinMax(child, false);
    }

    public void resetNode (Node rstNode) {
        if (this.getId() == -1)
            return;

        this.updateMinMax(rstNode, true);
        if (this.leftChild.getId() == rstNode.getId()) {
            this.leftChild = new Node(-1);
        } else if (this.rightChild.getId() == rstNode.getId()) {
            this.rightChild = new Node(-1);
        } else if (this.father.getId() == rstNode.getId()) {
            this.father = new Node(-1);
        }
    }

    public int updateMinMax (Node child, boolean remove /*= false*/) {
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

        return (this.minId == child.getMinId() ? this.minId : this.maxId);
    }

    public void setMinId (int minId) {
        this.minId = minId;
    }

    public void setMaxId (int maxId) {
        this.maxId = maxId;
    }
    /* End of Setters */
}
