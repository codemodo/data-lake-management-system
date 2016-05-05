package linker.data.entity;

/**
 * Represents a single tuple from edge_table where type=T.
 *
 */
public class TreeEdge {
	
	private int parent;
	private int child;
	private String edgeType;
	
	/**
	 * Constructor.
	 * @param n1 FK to node_table
	 * @param n2 FK to node_table
	 * @param t Type flag used to distinguish tree vs. linked edges.
	 */
	public TreeEdge(int n1, int n2, String t) {
		this.parent = n1;
		this.child = n2;
		this.edgeType = t;
	}

	// Getters
	public int getParent() { return parent; }
	public int getChild() { return child; }
	public String getEdgeType() { return edgeType; }
}
