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

	public static Connection conn = null;
	public static Statement stmt = null;

	public static void createNodeTable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS node_table "
					+ "(id INTEGER, " + " k VARCHAR(255), "
					+ " v VARCHAR(255), " + " doc_id INTEGER, "
					+ " PRIMARY KEY (id))";

			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating node table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void createWordTable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS word_table "
					+ "(id INTEGER, " + " word VARCHAR(255), "
					+ " PRIMARY KEY (id))";

			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating word table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void createIIWordTable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS ii_word_table "
					+ "(word_id INTEGER, " + " word VARCHAR(255), "
					+ " PRIMARY KEY (id))";

			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating inverted index word table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void createIINodeTable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS ii_node_table "
					+ "(node_id INTEGER, " + " word_id INTEGER, "
					+ " PRIMARY KEY (node_id, word_id))";

			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating inverted index node table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void createEdgeTable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS edge_table "
					+ "(parent_node INTEGER, " + " child_node INTEGER, "
					+ " doc_id INTEGER, " + " PRIMARY KEY (parent_node))";

			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating edge table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	public static int getMaxNodeId() {
		int id = -1;
		try {
			String sql = "SELECT MAX(id) AS maxId FROM node_table";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				id = rs.getInt("maxId");
			}
			else {
				id = 0;
			}
			System.out.println("id is " + id);
		
		} catch (SQLException ex) {
    	    // handle any errors
			System.err.println("Error getting max nodeId.");
    	    System.out.println("SQLException: " + ex.getMessage());
    	    System.out.println("SQLState: " + ex.getSQLState());
    	    System.out.println("VendorError: " + ex.getErrorCode());
    	} 
		return id;
	}

	public static void main(String[] args) {
		// File input = new File(args[0]);
		// File input = new File(
		// "/Users/joshkessler/Documents/workspace/Extractor/CustomerSvcCalls.json");
		// File input = new File(
		// "/Users/joshkessler/Documents/workspace/Extractor/philaHistoricSites.xml");
		File input = new File(
				"/Users/joshkessler/Documents/workspace/Extractor/Employee_Salaries_-_March_2016.csv");
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
			addToNodeTable(docID, "", "", docID);
			int tupleNumber = 0;
			for (CSVRecord record : parser) {
				tupleNumber++;
				int tupleID = getTupleID(tupleNumber, docID);
				addToEdgeTable(docID, tupleID,docID);

				for (String key : headers.keySet()) {
					String value = record.get(key);
					int leafID = getNodeID(key, value, docID, tupleID);
					addToNodeTable(leafID, key, value, docID);
					addToEdgeTable(tupleID, leafID, docID);
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
	
	public static void createConnection() {
    	try {
    		
    	    conn = DriverManager.getConnection("jdbc:mysql://datalake.c2lclaii6yaq.us-west-2.rds.amazonaws.com/Datalake", "admin", "testing1234");
    	    stmt = conn.createStatement();
    	    
    	} catch (SQLException ex) {
    	    // handle any errors
    		System.err.println("Error connecting to database.");
    	    System.out.println("SQLException: " + ex.getMessage());
    	    System.out.println("SQLState: " + ex.getSQLState());
    	    System.out.println("VendorError: " + ex.getErrorCode());
    	} 
    }

	

	public static void addToNodeTable(int nodeID, String key, String value,
			int docID) {
		try {
			String sql = "INSERT INTO node_table " +
                "VALUES (" + nodeID + ", '" + key + "', '" + value + "', " + docID + ")";
		stmt.executeUpdate(sql);
		
		} catch (SQLException ex) {
    	    // handle any errors
			System.err.println("Error inserting row into node table.");
    	    System.out.println("SQLException: " + ex.getMessage());
    	    System.out.println("SQLState: " + ex.getSQLState());
    	    System.out.println("VendorError: " + ex.getErrorCode());
    	} 
	}

	public static void addToEdgeTable(int parentNode, int childNode, int docID) {
		try {
			String sql = "SELECT * FROM edge_table WHERE parent_node=" + parentNode + " AND child_node=" + childNode;
			ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                System.err.println("This edge pair already exists in database.");
            }
            else {
            	sql = "INSERT INTO edge_table " +
                      "VALUES (" + parentNode + ", " + childNode + ", " + docID + ")";
            	stmt.executeUpdate(sql);
            }   
		
		} catch (SQLException ex) {
    	    // handle any errors
			System.err.println("Error inserting row into edge table.");
    	    System.out.println("SQLException: " + ex.getMessage());
    	    System.out.println("SQLState: " + ex.getSQLState());
    	    System.out.println("VendorError: " + ex.getErrorCode());
    	} 
	}

	public static void addToInvertedIndex(String word, int nodeID) {
		String statement = "INSERT INTO Inverted_Index VALUES (" + word + ", "
				+ nodeID + ")";
		System.out.println(statement);
		// createConnAndAddToTable(statement);
	}
	
	public static void getTableNames() {
        try {

            DatabaseMetaData dbmd = conn.getMetaData();
            String[] types = {"TABLE"};
            ResultSet rs = dbmd.getTables(null, null, "%", types);
            while(rs.next()) {
                System.out.println(rs.getString("TABLE_NAME"));
            }
        } 
            catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public static void getColumnNames(String table) {
        try {

            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rs = dbmd.getColumns(null, null, "edge_table", null);
            while(rs.next()) {
                System.out.println(rs.getString("COLUMN_NAME"));
            }
        } 
            catch (SQLException e) {
            e.printStackTrace();
        }
    }
	
	public static void deleteTable(String table) {
    	try {
    		String sql = "DROP TABLE " + table;
    		stmt.executeUpdate(sql);
    		//sql="SELECT * FROM " + table;
    		//stmt.executeQuery(sql);
    	    
    	} catch (SQLException ex) {
    	    // handle any errors
    		System.err.println("Error deleting " + table + " from database.");
    	    System.out.println("SQLException: " + ex.getMessage());
    	    System.out.println("SQLState: " + ex.getSQLState());
    	    System.out.println("VendorError: " + ex.getErrorCode());
    	}      
	}
	
	public static void closeConnection() {
    	try {
    	    conn.close();
    	    stmt.close();
    	    
    	} catch (SQLException ex) {
    	    // handle any errors
    		System.err.println("Error closing connection to database.");
    	    System.out.println("SQLException: " + ex.getMessage());
    	    System.out.println("SQLState: " + ex.getSQLState());
    	    System.out.println("VendorError: " + ex.getErrorCode());
    	} 
    }

}