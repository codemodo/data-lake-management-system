package DBProject.Extractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

public class CommonsCSVParser extends DataParser {
	static int nextNodeID;

	public static boolean parseWithCommonsCSV(File file) {
		try {
			CSVParser parser = new CSVParser(new FileReader(file),
					CSVFormat.DEFAULT.withHeader());
			Map<String, Integer> headers = parser.getHeaderMap();
			int docID = getDocID(file);
			nextNodeID = dbc.getMaxNodeId() + 1;
			dbc.addToNodeTable(nextNodeID, "", "", docID);
			int rootID = nextNodeID;
			int rowsAdded = 0;

			for (CSVRecord record : parser) {

				nextNodeID++;
				dbc.addToEdgeTable(rootID, nextNodeID);
				dbc.addToNodeTable(nextNodeID, "", "", docID);
				int tupleID = nextNodeID;

				for (String key : headers.keySet()) {
					nextNodeID++;
					String value = record.get(key);
					dbc.addToNodeTable(nextNodeID, key, value, docID);
					dbc.addToEdgeTable(tupleID, nextNodeID);
					addToInvertedIndex(key, value, nextNodeID);
				}

				if (rowsAdded % 500 == 0) {
					System.out.println("Added " + rowsAdded + " rows at time "
							+ System.nanoTime());
				}
				rowsAdded++;

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

	static void addToInvertedIndex(String key, String value,
			int nodeID) {

		String[] keyParts = key.split("\\s+");
		String[] valueParts = value.split("\\s+");
		Set<String> uniqueWords = new HashSet<String>(Arrays.asList(keyParts));
		for (String s : valueParts) {
			uniqueWords.add(s);
		}

		for (String s : uniqueWords) {
			if (!NumberUtils.isNumber(s)) {
				dbc.addToIITable(s, nodeID);
			}
		}
	}

}
