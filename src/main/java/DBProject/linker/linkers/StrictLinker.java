package linker.linkers;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import linker.data.*;
import linker.data.entity.*;

public class StrictLinker extends Linker {
	
	
	public StrictLinker(InvertedIndex index) {
		super(index);
	}
	
	public Set<LinkedEdge> link() {
		// Instantiate set to hold links as they are identified
		Set<LinkedEdge> outputEdges = new HashSet<LinkedEdge>();
		
		// Identify a subset of words used for linking by eliminating stop-words	
		//Set<String> filteredWords = StopWordFilter.filter(index);

		Map<String, HashSet<Node>> phrasesToNodeSet = new HashMap<String, HashSet<Node>>();
		
		for (Node n : index.getNodes()) {
			
//			String key = n.getKey().replaceAll("[^A-Za-z0-9]", "");
//			System.out.println(key);
//			if (longEnough(key)) {
//				if (!phrasesToNodeSet.containsKey(key)) phrasesToNodeSet.put(key, new HashSet<Node>());
//				phrasesToNodeSet.get(key).add(n);
//			}
			
			String val = n.getValue().replaceAll("[^A-Za-z0-9]", "");
			if (longEnough(val)) {
				if (!phrasesToNodeSet.containsKey(val)) phrasesToNodeSet.put(val, new HashSet<Node>());
				phrasesToNodeSet.get(val).add(n);
			}
		}
		
		Set<String> phraseSet = phrasesToNodeSet.keySet();
		
		for (String s : phraseSet) {
			for (Node n1 : phrasesToNodeSet.get(s)) {
				for (Node n2 : phrasesToNodeSet.get(s)) {
					if (!sameNode(n1, n2) && !sameDoc(n1, n2)) {
						System.out.println(n1.toString() + "   " + n2.toString());
						outputEdges.add(new LinkedEdge(n1.getID(), n2.getID(), "L"));
					}
				}
			}
		}
		
		return outputEdges;
	}
}
