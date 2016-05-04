package DBProject.Extractor;

import java.io.File;

public class DataParser {
	
	static DatabaseConnector dbc = new DatabaseConnector();
	static int docID;
	static int maxID = dbc.getMaxNodeId();
	static InvIndexList indexList = new InvIndexList(dbc);
	static NodeList nodeList = new NodeList(dbc);
	static EdgeList edgeList = new EdgeList(dbc);
	
	static int getDocID(File file) {
		// TODO
		return file.hashCode();
	}

	static int getTupleID(int tupleNumber, int docID) {
		// TODO
		return (Integer.toString(tupleNumber) + docID).hashCode();
	}

	static int getNodeID(String key, String value, int docID, int parentNodeID) {
		// TODO
		return (key + value + docID + parentNodeID).hashCode();
	}
	
	static int getNodeID(){
		maxID += 1;
//		System.out.println("ID incremented");
		return maxID;
	}
	
	static void finishAllRemainingBatches(){
		indexList.addRemainingRecords();
		nodeList.addRemainingRecords();
		edgeList.addRemainingRecords();
	}

}
