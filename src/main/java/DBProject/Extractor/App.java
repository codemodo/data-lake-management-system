package DBProject.Extractor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {
	
	static DatabaseConnector dbc = new DatabaseConnector();

	

	public static void main(String[] args) {
		// File input = new File(args[0]);
		// File input = new File(
		// "/Users/joshkessler/Documents/workspace/Extractor/CustomerSvcCalls.json");
		// File input = new File(
		// "/Users/joshkessler/Documents/workspace/Extractor/philaHistoricSites.xml");
		File input = new File(
				"/Users/joshkessler/Documents/workspace/Extractor/Employee_Salaries_-_March_2016_test.csv");

		dbc.createConnection();
		dbc.deleteTable("edge_table");
		dbc.deleteTable("node_table");
		
		dbc.createNodeTable();
		dbc.createEdgeTable();
		
		parseFile(input);
		dbc.printTable("node_table");
		dbc.printTable("edge_table");
		dbc.closeConnection();
	}

	public static boolean parseWithTika(File file) {
		// Instantiating Tika facade class
		Tika tika = new Tika();
		try {
			String fileContent;
			try {
				fileContent = tika.parseToString(file);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

		} catch (TikaException e) {
			return false;
		}
		return true;
	}

	public static boolean parseFile(File file) {

		// if (!parseWithJackson(file)) {
		// if (!parseWithJaxp(file)) {
		if (!parseWithCommonsCSV(file)) {
			if (!parseWithTika(file)) {
				return false;
			}
		}
		// }
		// }
		return true;
	}

	private static boolean parseWithCommonsCSV(File file) {
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
				dbc.addToEdgeTable(docID, tupleID,docID);
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

	static boolean parseWithJaxp(File file) {
		// TODO Auto-generated method stub

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// dbf.setValidating(true);
		dbf.setNamespaceAware(true);

		try {
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document document = builder.parse(file);
			Node root = document.getDocumentElement();
			parseWithJaxp(root);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	static void parseWithJaxp(Node node) {
		System.out.println("value: " + node.getNodeValue());
		System.out.println("type: " + node.getNodeType());
		NodeList children = null;
		if (node.hasChildNodes()) {
			children = node.getChildNodes();
		}

		System.out.println("nodename: " + ((Element) node).getNodeName());

		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				parseWithJaxp(children.item(i));
			}
		}
	}

	private static boolean parseWithJackson(File file) {
		// TODO Auto-generated method stub
		ObjectMapper m = new ObjectMapper();
		// JsonParserFactor jpf = Json.createParserFactory();
		JsonNode rootNode;
		try {
			rootNode = m.readTree(file);
			parseJsonTree2(rootNode);
			// // lets see what type the node is
			// System.out.println(rootNode.getNodeType()); // prints OBJECT
			// // is it a container
			// System.out.println(rootNode.isContainerNode()); // prints true
			// System.out.println(rootNode.textValue());
			// Iterator<String> fieldNames = rootNode.fieldNames();
			// Iterator<JsonNode> childNodes = rootNode.elements();
			// while (fieldNames.hasNext()){
			// String fieldName = fieldNames.next();
			// System.out.println(fieldName);
			// }
			// while(childNodes.hasNext()){
			// JsonNode child = childNodes.next();
			// System.out.println(child.textValue());
			// }
			//
			//
			// parseJsonTree(rootNode);
			return true;
		} catch (JsonProcessingException e) {
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	public static void parseJsonTree2(JsonNode node) {
		Iterator<JsonNode> childNodes = node.elements();
		while (childNodes.hasNext()) {
			JsonNode child = childNodes.next();
			System.out.println(child.textValue());
			parseJsonTree2(child);
		}
	}

	public static void parseJsonTree(JsonNode node) {
		if (node == null) {
			return;
		} else {
			storeJsonNode(node);
			for (int i = 0; i < node.size(); i++) {
				storeJsonLink(node, node.get(i));
				parseJsonTree(node.get(i));
			}
		}
	}

	public static void storeJsonNode(JsonNode node) {
		// TODO store key and value
	}

	public static void storeJsonLink(JsonNode parent, JsonNode child) {
		// TODO store key and value
	}

	public void parseXmlTree(Node node) {
		if (node == null) {
			return;
		} else {
			storeXmlNode(node);
			NodeList children = node.getChildNodes();
			for (int i = 0; i < children.getLength(); i++) {
				storeXmlLink(node, children.item(i));
				parseXmlTree(children.item(i));
			}
		}
	}

	private void storeXmlLink(Node node, Node item) {
		// TODO Auto-generated method stub

	}

	public void storeXmlNode(Node node) {

	}
	
	

}