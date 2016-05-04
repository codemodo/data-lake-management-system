package DBProject.Extractor;

import java.util.ArrayList;

public class InvIndexList {
	private ArrayList<InvertedIndexEntry> entries;
	DatabaseConnector dbc;
	
	public InvIndexList(DatabaseConnector dbc){
		entries = new ArrayList<InvertedIndexEntry>();
		this.dbc = dbc;
	}
	
	public void addToList(InvertedIndexEntry entry){
		entries.add(entry);
		if (entries.size() > 999){
			dbc.addToIITable(entries);
		}
		entries = new ArrayList<InvertedIndexEntry>();
		
	}
	
	public void addRemainingRecords(){
		dbc.addToIITable(entries);
	}

}
