package linker.linkers;

import java.util.HashSet;
import java.util.Set;

import linker.data.*;
import linker.data.entity.*;
import linker.data.entity.Node;

/**
 * This implementation focuses on creating links between nodes where a
 * particular document is mentioned and all the nodes in that document's
 * tree.
 *
 */
public class DocumentNameLinker extends Linker {
	
	/**
	 * Constructor.  Calls super's constructor.
	 * @param index
	 */
	public DocumentNameLinker(InvertedIndex index) {
		super(index);
	}

	/**
	 * Creates links (as a set of LinkedEdge objects) between
	 * nodes where a document name appears in the key or value
	 * and nodes contained within that document's tree.
	 */
	public Set<LinkedEdge> link() {
		// Instantiate set to hold links as they are identified
		Set<LinkedEdge> outputEdges = new HashSet<LinkedEdge>();
		
		// Identify a subset of words used for linking by eliminating stop-words	
		Set<String> filteredWords = index.getWords();
		Set<Document> docs = index.getDocs();
		Set<String> docNames = new HashSet<String>();
		for (Document d : docs) {
			//System.out.println(d.getDocID());
			//System.out.println(d.getDocName());
			//System.out.println(d.getDocName().toLowerCase());
			docNames.add(d.getDocName().toLowerCase());
		}
		
		// Iterate over words
		for (String w : filteredWords) {
			//System.out.println(w);
			//System.out.println(docNames);
			if (docNames.contains(w)) {
				//System.out.println("___"+w);
				// then nodes containing this document name should be linked to all nodes in that document
				Set<Node> nodesWithDocName = index.getNodesFromWord(w);
				Set<Node> tmp = new HashSet<Node>(nodesWithDocName);
				for (Node n : tmp) {
					if (n.getDoc()==index.getDocIdFromName(w)) {
						//System.out.println(n.getDoc());
						//System.out.println(w);
						nodesWithDocName.remove(n);
					}
				}
				
				Set<Node> nodesInMatchingDoc = index.getNodesFromDocID(index.getDocIdFromName(w));
				
				for (Node n1 : nodesWithDocName) {
					for (Node n2 : nodesInMatchingDoc) {
						if (!sameNode(n1, n2) && !sameDoc(n1, n2)) {
							//System.out.println(n1.toString() + " " + n2.toString());
							outputEdges.add(new LinkedEdge(n1.getID(), n2.getID(), "L"));
						}
					}
				}
				
			}
		}
		return outputEdges;
	}
}