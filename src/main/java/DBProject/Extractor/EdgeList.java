package DBProject.Extractor;

import java.util.ArrayList;

public class EdgeList {
	private ArrayList<Edge> entries;
	DatabaseConnector dbc;
	
	public EdgeList(DatabaseConnector dbc){
		entries = new ArrayList<Edge>();
		this.dbc = dbc;
	}
	
	public void addToList(Edge entry){
		entries.add(entry);
		if (entries.size() > 999){
			dbc.addToEdgeTable(entries);
		}
		entries = new ArrayList<Edge>();
		
	}
	
	public void addRemainingRecords(){
		dbc.addToEdgeTable(entries);
	}

}
