package projects.opticalNet.nodes.OPTNet;

public class Node {

	/* Attributes */
    Node parent = null;
    Node leftChild = null;
    Node rightChild = null;

    private int minId = -1;
    private int maxId = -1;

    private long weight = 0;

	private int id = 0;
	private static int ID = 0;
	/* End of Attributes */

	/* Constructors */
    public Node () {
    	this.id = ID++;
    	this.parent = new Node(-1);
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

    public Node (Node parent, Node leftChild, Node rightChild) {
    	this.id = ID++;
    	this.parent = parent;
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

    public Node getParent () {
        return this.parent;
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

    public long getWeight () {
        return this.weight;
    }

    /* End of Getters */
    /* Setters */
    public void setParent (Node parent) {
        if (this.getId() == -1)
            return;

        this.parent = parent;
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
            this.leftChild.getParent().resetNode(this);

        this.leftChild = child;
        this.leftChild.setParent(this);
        return this.updateMinMax(child, false);
    }

    public int setRightChild (Node child) {
        if (this.getId() == -1)
            return -1;

        if (this.rightChild.getId() != -1)
            this.rightChild.getParent().resetNode(this);

        this.rightChild = child;
        this.rightChild.setParent(this);

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
        } else if (this.parent.getId() == rstNode.getId()) {
            this.parent = new Node(-1);
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

        if (this.getParent().getId() != -1) {
            if (this.getParent().getMinId() != this.minId) {
                this.getParent().updateMinMax(child, remove);
            } else if (this.getParent().getMaxId() != this.maxId) {
                this.getParent().updateMinMax(child, remove);
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

    public void setWeight (long weight) {
    	this.weight = weight;
    }

    public void incrementPathWeight (int toId, boolean rooted) {
        this.incrementWeight();

        if (!rooted && this.parent.getId() != -1) {
            this.incrementPathWeight(toId, false);
        } else if (this.getId() == toId) {
            return;
        } else if (this.getId() < to && to <= this.maxId) {
            this.rightChild.incrementPathWeight(toId, true);
        } else if (this.minId <= to && to < this.getId()) {
            this.leftChild.incrementPathWeight(toId, true);
        } else {
            this.parent.incrementPathWeight(toId, true);
        }
    }

    public void incrementWeight () {
        this.weight++;
    }
    /* End of Setters */
}
