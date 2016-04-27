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
	public static void main(String[] args) {
		// File input = new File(args[0]);
//		File input = new File(
//				"/Users/joshkessler/Documents/workspace/Extractor/CustomerSvcCalls.json");
//		File input = new File(
//				"/Users/joshkessler/Documents/workspace/Extractor/philaHistoricSites.xml");
		File input = new File("/Users/joshkessler/Documents/workspace/Extractor/Employee_Salaries_-_March_2016.csv");
		// createConnAndCreateTable("");
		// parseWithJaxp(input);
		parseFile(input);

		System.out.println("Hello World!");
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

//		if (!parseWithJackson(file)) {
//			if (!parseWithJaxp(file)) {
				if (!parseWithCommonsCSV(file)) {
					if (!parseWithTika(file)) {
						return false;
					}
				}
//			}
//		}
		return true;
	}

	private static boolean parseWithCommonsCSV(File file) {
		try {
			CSVParser parser = new CSVParser(new FileReader(file), CSVFormat.DEFAULT.withHeader());
			Map<String,Integer> headers = parser.getHeaderMap();
			int docID = getDocID(file);
			addToNodeTable(docID, "", "", docID);
			int tupleNumber = 0;
			for (CSVRecord record : parser){
				tupleNumber++;
				int tupleID = getTupleID(tupleNumber, docID);
				addToLinkTable(docID, tupleID);

				for (String key : headers.keySet()){
					String value = record.get(key);
					int leafID = getNodeID(key, value, docID, tupleID);
					addToNodeTable(leafID, key, value, docID);
					addToLinkTable(tupleID, leafID);
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
	
	static int getDocID(File file){
		//TODO
		return file.hashCode();
	}
	
	static int getTupleID(int tupleNumber, int docID){
		//TODO
		return (Integer.toString(tupleNumber) + docID).hashCode();
	}
	
	static int getNodeID(String key, String value, int docID, int parentNodeID){
		//TODO
		return (key + value + docID + parentNodeID).hashCode();
	}

	static boolean parseWithJaxp(File file) {
		// TODO Auto-generated method stub

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		dbf.setValidating(true);
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
	
	static void parseWithJaxp(Node node){
		System.out.println("value: " + node.getNodeValue());
		System.out.println("type: " + node.getNodeType());
		NodeList children = null;
		if (node.hasChildNodes()){
			children = node.getChildNodes();
		}

		System.out.println("nodename: " + ((Element) node).getNodeName());	

		if (children != null){
			for(int i = 0; i < children.getLength(); i++){
				parseWithJaxp(children.item(i));
			}
		}
	}

	private static boolean parseWithJackson(File file) {
		// TODO Auto-generated method stub
		ObjectMapper m = new ObjectMapper();
//		JsonParserFactor jpf = Json.createParserFactory();
		JsonNode rootNode;
		try {
			rootNode = m.readTree(file);
			parseJsonTree2(rootNode);
//			// lets see what type the node is
//	        System.out.println(rootNode.getNodeType()); // prints OBJECT
//	        // is it a container
//	        System.out.println(rootNode.isContainerNode()); // prints true
//	        System.out.println(rootNode.textValue());
//	        Iterator<String> fieldNames = rootNode.fieldNames();
//	        Iterator<JsonNode> childNodes = rootNode.elements();
//	        while (fieldNames.hasNext()){
//	        	String fieldName = fieldNames.next();
//	        	System.out.println(fieldName);
//	        }
//	        while(childNodes.hasNext()){
//	        	JsonNode child = childNodes.next();
//	        	System.out.println(child.textValue());
//	        }
//	        
//	        
//			parseJsonTree(rootNode);
			return true;
		} catch (JsonProcessingException e) {
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public static void parseJsonTree2(JsonNode node){
		Iterator<JsonNode> childNodes = node.elements();
        while(childNodes.hasNext()){
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

	public static void createConnAndUpdateTable(String statement) {
		Connection conn = null;
		Statement stmt = null;
		try {
			String databaseURL = "ec2-50-19-202-216.compute-1.amazonaws.com";
			String databaseName = "EMP";
			String user = "ryan";
			String password = "mysqladmin";

			conn = DriverManager.getConnection("jdbc:mysql://" + databaseURL
					+ "/" + databaseName, user, password);
			// ABOVE can also be used w/ query string syntax:
			// "jdbc:mysql://ec2-50-19-202-216.compute-1.amazonaws.com/EMP?user=ryan&password=mysqladmin"

			// create a statement and query
			stmt = conn.createStatement();
			stmt.executeUpdate(statement);

		} catch (SQLException se) {
			// Handle errors for JDBC
			se.printStackTrace();
		} catch (Exception e) {
			// Handle errors for Class.forName
			e.printStackTrace();
		} finally {
			// finally block used to close resources
			try {
				if (stmt != null)
					conn.close();
			} catch (SQLException se) {
			}// do nothing
			try {
				if (conn != null)
					conn.close();
			} catch (SQLException se) {
				se.printStackTrace();
			}// end finally try
		}// end try
	}

	public static void createConnAndCreateTable(String createTableStatement) {
		createConnAndUpdateTable(createTableStatement);
	}

	public static void createConnAndAddToTable(String updateStatement) {
		createConnAndUpdateTable(updateStatement);
	}
	
	public static void addToNodeTable(int nodeID, String key, String value, int docID){
		String statement = "INSERT INTO Node_Table VALUES (" + nodeID + ", " + key + ", " + value + ", " + docID + ")";
		System.out.println(statement);
//		createConnAndAddToTable(statement);
	}
	
	public static void addToLinkTable(int parentNode, int childNode){
		String statement = "INSERT INTO Link_Table VALUES (" + parentNode + ", " + childNode + ")";
		System.out.println(statement);
//		createConnAndAddToTable(statement);
	}
	
	public static void addToInvertedIndex(String word, int nodeID){
		String statement = "INSERT INTO Inverted_Index VALUES (" + word + ", " + nodeID + ")";
		System.out.println(statement);
//		createConnAndAddToTable(statement);
	}

}