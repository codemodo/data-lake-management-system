package linker.linkers;

import java.util.HashSet;
import java.util.Set;

import linker.data.*;
import linker.data.entity.*;

public class SimpleLinker extends Linker {
	
	public SimpleLinker(InvertedIndex index) {
		super(index);
	}

	public Set<LinkedEdge> link() {
		// Instantiate set to hold links as they are identified
		Set<LinkedEdge> outputEdges = new HashSet<LinkedEdge>();
		
		// Identify a subset of words used for linking by eliminating stop-words	
		Set<String> filteredWords = StopWordFilter.filter(index);
		
		// Iterate over words
		for (String w : filteredWords) {
			if (index.wordFrequency(w) < 2) continue;
			// Get the nodes that word appears in
			Set<Node> wordAppearsIn = index.getNodesFromWord(w);
			
			// Create links between pairwise nodes,
			// but only if the nodes come from different document trees
			for (Node n1 : wordAppearsIn) {
				for (Node n2 : wordAppearsIn) {
					if (!sameNode(n1,n2) && !sameDoc(n1,n2)) {
						outputEdges.add(new LinkedEdge(n1.getID(),n2.getID(),"L"));
					}
				}
				//if (outputEdges.size() > 10000) break;
			}
		}
		return outputEdges;
	}
}
