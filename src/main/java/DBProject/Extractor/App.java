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
	
	public static boolean isTest = true;

	static DatabaseConnector dbc = new DatabaseConnector();

	public static void deleteTables() {
		if(isTest) {
			dbc.deleteTable("test_ii_table");
			dbc.deleteTable("test_edge_table");
			dbc.deleteTable("test_node_table");
			dbc.deleteTable("test_doc_table");
		}
		else {
			dbc.deleteTable("ii_table");
			dbc.deleteTable("edge_table");
			dbc.deleteTable("node_table");
			dbc.deleteTable("doc_table");
		}

	}
	
	public static void createTables() {
		if(isTest){
			dbc.createDocTable();
			dbc.createNodeTable();
			dbc.createEdgeTable();
			dbc.createIITable();
		}
		else {
			dbc.deleteTable("ii_table");
			dbc.deleteTable("edge_table");
			dbc.deleteTable("node_table");
			dbc.deleteTable("doc_table");
		}
	}
	
	
	public static void printTables() {
		if(isTest) {
			dbc.printTable("test_node_table");
			dbc.printTable("test_edge_table");
			dbc.printTable("test_ii_table");
			dbc.printTable("test_doc_table");
		}
		else {
			dbc.printTable("node_table");
			dbc.printTable("edge_table");
			dbc.printTable("ii_table");
			dbc.printTable("doc_table");
		}
	}
	
	public static boolean parseFile(File file, DatabaseConnector dbc) {
//		if (!CommonsCSVParser.parseWithCommonsCSV(file)) {
			//if (!
		JaxpXMLParser.parseWithJaxp(file);//) {
				//if (!
						//JacksonJSONParser.parseWithJackson(file, dbc);
						//) {
					//return TikaParser.parseWithTika(file);
				//}
			//}
//		}

		return true;
	}
	
	public static void main(String[] args) {
		//File input = new File("json_ex.json");
		//File input = new File("City_Facilities_pub.geojson");
		File input = new File("phila_housing.rss");

		dbc.createConnection(isTest);
		deleteTables();
		createTables();
		parseFile(input, dbc);
		printTables();
		dbc.closeConnection();
	}
}
