package linker.linkers;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import linker.data.InvertedIndex;
import linker.data.entity.*;

public abstract class Linker {
	
	protected InvertedIndex index;
	public Linker(InvertedIndex index) {
		this.index = index;
	}
	
	public abstract Set<LinkedEdge> link();
	
	protected boolean sameNode(Node n1, Node n2) { return n1.getID() == n2.getID(); }
	protected boolean sameDoc(Node n1, Node n2) { return n1.getDoc() == n2.getDoc(); }
	protected boolean longEnough(String w) { return w.length() > 3; }
	
	/**
	 * Private inner class used to identify and filter out stop-words.
	 * This is done in order to enable more meaningful links.
	 */
	protected static class StopWordFilter {
		private static final double PERCENTAGE = 0.02;
		private static final int MAX = 1000;
		private StopWordFilter() { }
		
		/**
		 * Generates a new vocabulary that is a subset of the given vocabulary,
		 * with stop words removed. 
		 */
		public static Set<String> filter(InvertedIndex ii) {
			Set<String> wordsBeingFiltered = new HashSet<String>(ii.getWords());
			Set<String> stopWords = generateStopWords(ii);
			for (String w: stopWords) {
				System.out.println("Now removing: " + w);
				wordsBeingFiltered.remove(w);
			}
			Set<String> tmpWords = new HashSet<String>(wordsBeingFiltered);
			for (String w : tmpWords) {
				if (w.length() < 3) {
					System.out.println("Also removing: " + w);
					wordsBeingFiltered.remove(w);
				}
			}
			return wordsBeingFiltered;
		}
		
		
		/**
		 * Identifies stop words for removal from the given vocabulary.
		 */
		public static Set<String> generateStopWords(InvertedIndex ii) {
			Map<String, Integer> wordCounts = new HashMap<String, Integer>();
			
			for (String w : ii.getWords()) {
				wordCounts.put(w, ii.wordFrequency(w));
			}
			List<String> sortedWords = wordsSortedByFrequency(wordCounts);
			int n = Integer.min(MAX, (int)(PERCENTAGE*ii.getWords().size()));
			System.out.println("Removing\n\n\n\n\n" + n + " \n\n\n\n words: \n\n\n\n" );
			return new HashSet<String>(sortedWords.subList(0, n));
		}
		
		
		/**
		 * Utility method used to help sort map entries by frequency and return the corresponding
		 * sorted list of only words.
		 */
		private static <K,V extends Comparable<? super V>> List<K> wordsSortedByFrequency(Map<K,V> map) {
			List<Entry<K,V>> sortedEntries = new ArrayList<Entry<K,V>>(map.entrySet());
			Collections.sort(sortedEntries, new Comparator<Entry<K,V>>() {
				@Override
				public int compare(Entry<K,V> e1, Entry<K,V> e2) {
					return e2.getValue().compareTo(e1.getValue());
				}
			});
			List<K> sortedWords = new ArrayList<K>();
			for (Entry<K,V> e : sortedEntries) {
				sortedWords.add(e.getKey());
			}
			return sortedWords;
		}
	}

}
