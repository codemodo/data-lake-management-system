package linker.data;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import linker.data.entity.*;

/**
 * This class stores a subset of the dataset and facilitates
 * lookups and utility operations used during linking.
 */
public class InvertedIndex {

	private Set<String> words;
	private Set<Node> nodes;
	private Set<IndexEntry> indexEntries;
	private Map<String,HashSet<Node>> index;
	private Map<Integer, Node> idToNode;
	private Set<Document> docs;
	private Map<Integer, Document> idToDoc;
	private Map<String, Document> docNameToDoc;
	//private Dataset ds;
	
	
	/**
	 * Constructor.
	 * Initializes local collections and builds data structures.
	 * @param ds
	 */
	public InvertedIndex(Dataset ds) {
		//this.ds = ds;
		this.nodes = ds.getNodes();
		this.indexEntries = ds.getIndexEntries();
		this.docs = ds.getDocs();
		extractWords();
		buildNodeLookup();
		buildDocLookup();
		buildIndex();
		//pruneData();
	}
	
	
	/**
	 * Extracts words from inverted index table into String set.
	 */
	private void extractWords() {
		this.words = new HashSet<String>();
		for (IndexEntry ie : this.indexEntries) { words.add(ie.getWord()); }
	}
	
	
	/**
	 * Creates hashmap to quickly find Node from node_id.
	 */
	private void buildNodeLookup() {
		this.idToNode = new HashMap<Integer, Node>();
		for (Node n : nodes) idToNode.put(n.getID(), n);
	}
	
	
	/**
	 * Creates hashmap to quickly find Document from doc_id.
	 */
	private void buildDocLookup() {
		this.idToDoc = new HashMap<Integer, Document>();
		this.docNameToDoc = new HashMap<String, Document>();
		for (Document d : docs) {
			idToDoc.put(d.getDocID(), d);
			docNameToDoc.put(d.getDocName().toLowerCase(), d);
		}
	}
	
	
	/**
	 * Creates hashmap to quickly find all Nodes from word String
	 */
	private void buildIndex() {
		Map<String,HashSet<Node>> ii = new HashMap<String,HashSet<Node>>();
		for (IndexEntry ie : indexEntries) {
			String w = ie.getWord();
			Node n = idToNode.get(ie.getNodeID());
			if (n != null) {
				if (!ii.containsKey(w)) ii.put(w, new HashSet<Node>());
				ii.get(w).add(n);
			}
		}
		this.index = ii;
	}
	
	
	/**
	 * Removes words in the case where WORD:NODE appears in inverted index table,
	 * but referenced node is not found and therefore does not show up in the index hashmap.
	 * 
	 * This is primarily used for testing.  While testing certain functionality, 
	 * we sometimes pull only a small subset from each table, which naturally leads
	 * to mismatches between the nodes referenced and nodes pulled.
	 */
	@SuppressWarnings("unused")
	private void pruneData() {
		Set<String> tmp = new HashSet<String>(this.words);
		for (String w : tmp) {
			if (!this.index.containsKey(w)) {
				this.words.remove(w);
			}
		}
	}
	
	
	/**
	 * Fetches all the nodes where a word appears using the word's String.
	 * @param word
	 * @return Set containing the nodes
	 */
	public Set<Node> getNodesFromWord(String word) {
		return index.get(word);
	}
	
	/**
	 * Fetches all nodes that are contained within the given document tree
	 * @param docID
	 * @return
	 */
	public Set<Node> getNodesFromDocID(int docID) {
		Set<Node> nodes = new HashSet<Node>();
		for (Node n : this.nodes) {
			if (n.getDoc() == docID) nodes.add(n);
		}
		return nodes;
	}
	
	
	/**
	 * Given a document name, returns the document id
	 * @param docName
	 * @return
	 */
	public int getDocIdFromName(String docName) {
		return docNameToDoc.get(docName).getDocID();
	}
	
	
	/**
	 * Returns the number of nodes in which a word is found.
	 * Primarily used for identifying stop-words.
	 * @param w The word for lookup.
	 * @return The word's frequency.
	 */
	public int wordFrequency(String w) {
		return index.get(w).size();
	}
	
	
	// Getters
	public Set<String> getWords() { return this.words; }
	public Set<Node> getNodes() { return this.nodes; }
	public Set<Document> getDocs() { return this.docs; }
	
}
