package linker;

import linker.data.Database;
import linker.data.Dataset;
import linker.data.InvertedIndex;
import linker.linkers.Linker;
import linker.linkers.LinkerFactory;
import linker.linkers.LinkerType;

/**
 * Main class of Datalake's Linker component.
 */
public class Driver {

	/**
	 * Stores data used for linking.
	 */
	private Dataset dataset;
	
	/**
	 * Utility methods for efficient linking.
	 */
	private InvertedIndex index;
	
	/**
	 * Contains linking logic.
	 */
	private Linker linker;
	
	/**
	 * Executes all the steps in the Linker program.
	 */
	public void drive() {
		
		// Load data
		dataset = Database.pullDataset();
		
		// Build inverted index using object representation of the data
		index = new InvertedIndex(dataset);
		
		// Create and linker object and pass it the index
		LinkerFactory linkerFactory = new LinkerFactory();
		linker = linkerFactory.createLinker(LinkerType.STRICT, index);
		//linker = new DataLinker(index);
		
		// Link and update data set
		dataset.addLinks(linker.link());
		
		// Push links to database
		Database.pushLinks(dataset.getLinks());
	}
}
