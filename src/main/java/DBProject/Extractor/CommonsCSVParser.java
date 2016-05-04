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
			nodeList.addToList(nextNodeID, "", "", docID);
			int rootID = nextNodeID;
			int rowNum = 0;
			for (CSVRecord record : parser) {
				rowNum++;
				nextNodeID++;
				edgeList.addToList(rootID, nextNodeID);
				nodeList.addToList(nextNodeID, "", "", docID);
				int tupleID = nextNodeID;

				for (String key : headers.keySet()) {
					nextNodeID++;
					String value = record.get(key).toLowerCase();
					key = key.toLowerCase();
					nodeList.addToList(nextNodeID, key, value, docID);
					edgeList.addToList(tupleID, nextNodeID);
					addToInvertedIndex(key, value, nextNodeID);
				}
				if (rowNum % 500 == 0){
					System.out.println("read through " + rowNum + " rows");
				}

			}
			finishAllRemainingBatches();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	static void addToInvertedIndex(String key, String value, int nodeID) {

		String[] keyParts = key.split("\\s+");
		String[] valueParts = value.split("\\s+");
		Set<String> uniqueWords = new HashSet<String>(Arrays.asList(keyParts));
		for (String s : valueParts) {
			uniqueWords.add(s);
		}

		for (String s : uniqueWords) {
			if (!NumberUtils.isNumber(s)) {
				indexList.addToList(s, nodeID);
			}
		}
	}

}
