package DBProject.Extractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

public class CommonsCSVParser extends DataParser {
	
	public static boolean parseWithCommonsCSV(File file) {
		try {
			CSVParser parser = new CSVParser(new FileReader(file),
					CSVFormat.DEFAULT.withHeader());
			Map<String, Integer> headers = parser.getHeaderMap();
			int docID = getDocID(file);
			dbc.addToNodeTable(docID, "", "", docID);
			int tupleNumber = 0;
			for (CSVRecord record : parser) {
				tupleNumber++;
				int tupleID = getTupleID(tupleNumber, docID);
				dbc.addToEdgeTable(docID, tupleID, docID);
				dbc.addToNodeTable(tupleID, "", "", docID);

				for (String key : headers.keySet()) {
					String value = record.get(key);
					int leafID = getNodeID(key, value, docID, tupleID);
					dbc.addToNodeTable(leafID, key, value, docID);
					dbc.addToEdgeTable(tupleID, leafID, docID);
				}
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
	
	

}
