package linker.data;
import java.util.HashSet;
import java.util.Set;

import linker.data.entity.*;

/**
 * This class stores the dataset used for linking operations
 */
public class Dataset {
	
	// Initial data resulting from extraction
	private Set<Node> nodes = new HashSet<Node>();
	private Set<Document> docs = new HashSet<Document>();
	private Set<TreeEdge> tEdges = new HashSet<TreeEdge>();
	private Set<IndexEntry> indexEntries = new HashSet<IndexEntry>();
	
	// Newly discovered links from linking
	private Set<LinkedEdge> linkedEdges = null;
	
	/**
	 * Constructor
	 * @param words Set of Word objects
	 * @param docs Set of Document objects
	 * @param nodes Set of Node objects
	 * @param dEdges Set of TreeEdge objects
	 * @param indexEntries Set of IndexEntry objects
	 */
	public Dataset(Set<Document> docs, Set<Node> nodes, Set<TreeEdge> dEdges, Set<IndexEntry> indexEntries) {
		this.docs = docs;
		this.nodes = nodes;
		this.tEdges = dEdges;
		this.indexEntries = indexEntries;
	}
	
	public Dataset(Set<Document> docs, Set<Node> nodes, Set<IndexEntry> indexEntries) {
		this.docs = docs;
		this.nodes = nodes;
		this.indexEntries = indexEntries;
	}
	
	public Dataset(Set<Node> nodes, Set<IndexEntry> indexEntries) {
		this.nodes = nodes;
		this.indexEntries = indexEntries;
	}
	
	// Initial dataset
	public Set<Node> getNodes() { return new HashSet<Node>(nodes); }
	public Set<Document> getDocs() { return new HashSet<Document>(docs); }
	public Set<TreeEdge> getTEdges() { return new HashSet<TreeEdge>(tEdges); }
	public Set<IndexEntry> getIndexEntries() { return new HashSet<IndexEntry>(indexEntries); }
	
	// Newly discovered links
	public void addLinks(Set<LinkedEdge> links) { this.linkedEdges = links; }
	public Set<LinkedEdge> getLinks() { return linkedEdges; }
}
