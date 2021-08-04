package projects.opticalNet.nodes.OPTNet;

import java.util.ArrayList;
import java.util.HashMap;

public class Switch {

	/* Private Getters */
	ArrayList<Conn> getRConnections () {
	    return this.rConnections;
	}
	
	Conn getRConnection (int toNode) {
	    return this.rConnections.get(this.rNodesIds.get(toNode));
	}
	/* End of Private Getters */

	/* Constructors */
	Switch (int minId, int maxId) {
		this.id = ID++;
	    int size = maxId - minId + 1;
	    for (int i = 0; i < size; i++) {
	        this.nodesIds.put(minId + i, i);
	        this.rNodesIds.put(minId + i, i);

	        this.connections.add(
	        new Conn((i + 1 == size ? 0 : i + 1) + minId, true)
	        );
	        this.rConnections.add(
	        new Conn((i == 0 ? maxId : minId + i - 1), true)
	        );
	    }
	}
	
	Switch (int minId1, int maxId1, int minId2, int maxId2) {
		this.id = ID++;
	    int halfSize = maxId1 - minId1 + 1;
	    for (int i = 0; i < halfSize; i++) {
	        this.nodesIds.put(minId1 + i, i);
	        this.rNodesIds.put(minId2 + i, i);

	        this.connections.add(
	        new Conn(minId2 + i, true)
	        );
	        this.rConnections.add(
	        new Conn(minId1 + i, true)
	        );
	    }
	}
	/* End of Constructors */
	/* Getters */
	int getId () {
	    return this.id;
	}
	
	ArrayList<Conn> getConnections () {
	    return this.connections;
	}
	
	Conn getConnection (int fromNode) {
	    return this.connections.get(this.nodesIds.get(fromNode));
	}
	
	int getSize () {
	    return this.nodesIds.size() * 2;
	}
	/* End of Getters */
	
	/* Setters */
	void updateConn (int fromNode, int toNode, boolean dummy  /*=false*/) {
	    int oldTo = this.getConnection(fromNode).getToNode();
	    int oldFrom = this.getRConnection(toNode).getToNode();

	    if (oldFrom != fromNode && oldTo != toNode) {
	        this.getConnection(oldFrom).setConnection(oldTo, true);
	        this.getRConnection(oldTo).setConnection(oldFrom, true);
	    }

	    this.getConnection(fromNode).setConnection(toNode, dummy);
	    this.getRConnection(toNode).setConnection(fromNode, dummy);
	}
	
	void removeConn (int fromNode){
	    int toNode = this.getConnection(fromNode).getToNode();

	    this.getConnection(fromNode).removeConnection();
	    this.getRConnection(toNode).removeConnection();
	}
	/* End of Setters */
	
	/* Attributes */
    private static int ID = 0;
    private int id;

    private HashMap<Integer, Integer> nodesIds = new HashMap<>();
    private HashMap<Integer, Integer> rNodesIds = new HashMap<>();

    private ArrayList<Conn> connections = new ArrayList<>();
    private ArrayList<Conn> rConnections = new ArrayList<>();
    /* End of Attributes */
}
