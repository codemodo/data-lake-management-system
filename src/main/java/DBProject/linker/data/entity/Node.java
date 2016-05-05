package linker.data.entity;

/**
 * Represents a single tuple from node_table.
 *
 */
public class Node {

	private int nodeID;
	private String key, value;
	private int doc;
	
	/**
	 * Constructor.
	 * @param nodeID PK.
	 * @param key String representing extracted key.
	 * @param value String representing extracted value.
	 * @param doc FK to Document table.
	 */
	public Node(int nodeID, String key, String value, int doc) {
		this.nodeID = nodeID;
		this.key = key;
		this.value = value;
		this.doc = doc;
	}
	
	/**
	 * String representation of a key-value pair.
	 */
	public String toString() { return getKey() + ":" + getValue(); }
	
	// Getters
	public int getID() { return nodeID; }
	public String getKey() { return key; }
	public String getValue() { return value; }
	public int getDoc() { return doc; }
}
