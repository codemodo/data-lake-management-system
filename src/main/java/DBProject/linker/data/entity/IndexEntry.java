package linker.data.entity;

/**
 * Represents a single tuple from ii_table.
 *
 */
public class IndexEntry {
	private String word;
	private int nodeID;
	
	/**
	 * Constructor.
	 * @param wordID FK to word_table
	 * @param nodeID FK to node_table
	 */
	public IndexEntry(String word, int nodeID) {
		this.nodeID = nodeID;
		this.word = word;
	}
	
	// Getters
	public String getWord() { return word; }
	public int getNodeID() { return nodeID; }
	
}
