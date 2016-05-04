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
//		File input = new File(
//				"/Users/joshkessler/Documents/workspace/Extractor/Employee_Salaries_-_March_2016_test.xml");
//		File input = new File(
//				"/Users/joshkessler/Documents/workspace/Extractor/philaHistoricSites.xml");
//		 File input = new File(
//		 "/Users/joshkessler/Documents/workspace/Extractor/Employee_Salaries_-_March_2016_test.json");
		File input = new File("Employee_Salaries_-_March_2016_truncated.csv");

		dbc.createConnection();
		dbc.deleteTable("ii_table");
		dbc.deleteTable("edge_table");
		dbc.deleteTable("node_table");
		dbc.deleteTable("doc_table");
		
		dbc.createDocTable();
		dbc.createNodeTable();
		dbc.createEdgeTable();
		dbc.createIITable();
		
		parseFile(input, dbc);

		dbc.printTable("node_table");
		dbc.printTable("edge_table");
		dbc.printTable("ii_table");
		dbc.printTable("doc_table");
		dbc.closeConnection();
	}

	public static boolean parseFile(File file, DatabaseConnector dbc) {
		if (!CommonsCSVParser.parseWithCommonsCSV(file)) {
			//if (!JaxpXMLParser.parseWithJaxp(file)) {
				//if (!
//						JacksonJSONParser.parseWithJackson(file, dbc);
						//) {
					//return TikaParser.parseWithTika(file);
				//}
			//}
		}

		return true;
	}
}