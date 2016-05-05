package DBProject.Extractor;

import java.io.File;

/**
 * Hello world!
 *
 */
public class App {
	
	public static boolean isTest = false;

	static DatabaseConnector dbc = new DatabaseConnector();

	public static void deleteTables() {
		if(isTest) {
			dbc.deleteTable("test_ii_table");
			dbc.deleteTable("test_edge_table");
			dbc.deleteTable("test_node_table");
		}
		else {
			dbc.deleteTable("ii_table");
			dbc.deleteTable("edge_table");
			dbc.deleteTable("node_table");
		}

	}
	
	public static void createTables() {
		dbc.createNodeTable();
		dbc.createEdgeTable();
		dbc.createIITable();
	}
	
	
	public static void printTables() {
		if(isTest) {
			dbc.printTable("test_node_table");
			dbc.printTable("test_edge_table");
			dbc.printTable("test_ii_table");
		}
		else {
			dbc.printTable("node_table");
			dbc.printTable("edge_table");
			dbc.printTable("ii_table");
			dbc.printTable("Document");
		}
	}
	
	public static boolean parseFile(File file, DatabaseConnector dbc, int docId, String docName, String extension) {
//		deleteTables();
//		createTables();
				
		if(extension.equals("csv")) {
			CommonsCSVParser.parseWithCommonsCSV(file, docId, docName);
		}
		else if(extension.equals("json")) {
			JacksonJSONParser.parseWithJackson(file, docId, docName);	
		}
		else if(extension.equals("xml")) {
			JaxpXMLParser.parseWithJaxp(file, docId, docName);
		}
		else {
			System.err.println("This file format is not supported.");
		}
		//printTables();
		return true;
	}
	
	public static String getFileExtension(String name) {
		try {
			return name.substring(name.lastIndexOf(".") + 1);
		} catch (Exception e) {
			return "";
		}
	}
	
	public static void main(String[] args) {
//		String[] arguments = args[0].split(" ");
//		int docId = Integer.parseInt(arguments[0]);
		int docId = 347;
//		String docName = arguments[1];
//		String docName = "Employee_Salaries_-_March_2016_test.xml";
//		String docName = "philaHistoricSites.xml";
//		String docName = "CityPhotoData.csv";
//		String docName = "Phila_Health_Centers.csv";
		String docName = "DocData.csv";
		String extension = getFileExtension(docName);
//		String path = arguments[2];
//		File input = new File(path);
		File input = new File(docName);
		dbc.createConnection(isTest);
		parseFile(input, dbc, docId, docName, extension);
		//printTables();
		dbc.closeConnection();
	}
}