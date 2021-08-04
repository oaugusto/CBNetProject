package projects.opticalNet.nodes.OPTNet;

public class Conn {

	/* Constructors */
	Conn (int toNode) {
		this.toNode = toNode;
	}
	
	Conn (int toNode, boolean dummy) {
		this.toNode = toNode;
		this.dummy = dummy;
	}
	/* End of Constructors */
	
	/* Getters */
	int getToNode () {
	    return this.toNode;
	}
	
	boolean getDummy () {
	    return this.dummy;
	}
	/* End of Getters */
	
	/* Setters */
	void setConnection (int toNode, boolean dummy /*=true*/) {
	    this.toNode = toNode;
	    this.dummy = dummy;
	}
	
	void removeConnection () {
	    this.dummy = false;
	}
	/* End of Setters */
	
	/* Attributes */
    private int toNode;
    private boolean dummy;
    /* End of Attributes */
}
