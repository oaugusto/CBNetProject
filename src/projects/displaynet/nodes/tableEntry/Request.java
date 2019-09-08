package projects.displaynet.nodes.tableEntry;

/**
 * Request
 */
public class Request implements Comparable<Request> {
	public int srcId;
	public int dstId;
	public double priority;

	public int numOfRotations;
	public int numOfRouting;
	
	public Request() {
		this.srcId = 0;
		this.dstId = 0;
		this.priority = 0.0;
		this.numOfRotations = 0;
		this.numOfRouting = 0;
	}
	
	public Request(int src, int dst) {
		this.srcId = src;
		this.dstId = dst;
		this.priority = 0.0;
		this.numOfRotations = 0;
		this.numOfRouting = 0;
	}
	
	public Request(int src, int dst, double pri) {
		this.srcId = src;
		this.dstId = dst;
		this.priority = pri;
		this.numOfRotations = 0;
		this.numOfRouting = 0;
	}

	@Override
	public int compareTo(Request o) {
		int value = Double.compare(this.priority, o.priority);
		if (value == 0) {
			return this.srcId - o.srcId;
		} else {
			return value;
		}
	}
}