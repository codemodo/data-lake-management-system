package linker.data.entity;

/**
 * Used to store newly discovered links.
 * Represents a single tuple to be uploaded to edge table.
 */
public class LinkedEdge {

	private int node1;
	private int node2;
	private String edgeType;
	
	/**
	 * Constructor.
	 * @param n1 FK to node_table.
	 * @param n2 FK to node_table.
	 * @param t Type flag used to distinguish tree vs. linked edges.
	 */
	public LinkedEdge(int n1, int n2, String t) {
		node1 = n1;
		node2 = n2;
		edgeType = t;
	}
	
	// Getters
	public int getNode1() { return node1; }
	public int getNode2() { return node2; }
	public String getEdgeType() { return edgeType; }
}
