package linker.data.entity;

/**
 * Represents a single tuple from the Document table.
 *
 */
public class Document {

	private int docID;
	private String docName;
	
	/**
	 * Constructor.
	 * @param id id column (PK)
	 * @param name name column
	 */
	public Document(int id, String name) {
		this.docID = id;
		this.docName = name;
	}
	
	// Getters
	public int getDocID() { return docID; }
	public String getDocName() { return docName; }
	
}
