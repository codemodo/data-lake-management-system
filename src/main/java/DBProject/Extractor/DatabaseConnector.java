package DBProject.Extractor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;

public class DatabaseConnector {

	public static Connection conn = null;
	public static Statement stmt = null;

	public void createWordTable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS word_table "
					+ "(word_id INTEGER, " + " word VARCHAR(255), "
					+ " PRIMARY KEY (word_id))";

			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating word table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void addToWordTable(int word_id, String word) {
		try {
			String sql = "INSERT INTO word_table " + "VALUES (" + word_id
					+ ", '" + word + "')";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error inserting row into word table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void createDocTable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS doc_table "
					+ "(doc_id INTEGER, " + "PRIMARY KEY (doc_id))";

			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating doc table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void addToDocTable(int docID) {
		try {
			String sql = "INSERT INTO doc_table " + "VALUES (" + docID + ")";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error inserting row into doc table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void createNodeTable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS node_table "
					+ "(node_id INTEGER, " + "k VARCHAR(255), "
					+ "v VARCHAR(255), " + "doc_id INTEGER, "
					+ "PRIMARY KEY (node_id))";// ,"
			// + "FOREIGN KEY (doc_id) REFERENCES doc_table(doc_id))";

			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating node table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void addToNodeTable(int nodeID, String key, String value,
			int docID) {
		try {
			String sql = "INSERT INTO node_table " + "VALUES (" + nodeID
					+ ", '" + key + "', '" + value + "', " + docID + ")";
			PreparedStatement ps = conn.prepareStatement(sql);
			if (value.equalsIgnoreCase("MANAGING DIRECTOR'S OFFICE")){
				int i =1;
			}

			ps.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error inserting row into node table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void addToNodeTable(TreeNode n) {
		try {
			String sql = "INSERT INTO node_table " + "VALUES (" + n.nodeID
					+ ", '" + n.k + "', '" + n.v + "', " + n.docID + ")";
			PreparedStatement ps = conn.prepareStatement(sql);

			ps.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error inserting row into node table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void createIITable() {
		try {
			String sql = "CREATE TABLE IF NOT EXISTS ii_table "
					+ "(word_id INTEGER, " + " node_id INTEGER, "
					+ " PRIMARY KEY (word_id, node_id))";
			// + " FOREIGN KEY (word_id) REFERENCES word_table(word_id),"
			// + " FOREIGN KEY (node_id) REFERENCES node_table(node_id))";

			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating inverted index table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void addToIITable(int wordID, int nodeID) {
		try {
			String sql = "INSERT INTO ii_table VALUES (" + wordID + ", "
					+ nodeID + ")";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in adding to inverted index table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			System.out.println("WordID: " + wordID + " ndoeID: " + nodeID);
		}
	}

	public void createEdgeTable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS edge_table "
					+ "(node_1 INTEGER, " + " node_2 INTEGER, "
					+ " link_type CHAR(1)," + " PRIMARY KEY (node_1, node_2))";// ,"
			// + " FOREIGN KEY (node_1) REFERENCES node_table(node_id),"
			// + " FOREIGN KEY (node_2) REFERENCES node_table(node_id),"
			// + " FOREIGN KEY (doc_id) REFERENCES doc_table(doc_id))";

			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating edge table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void addToEdgeTable(int parentNode, int childNode, char type) {
		try {
			String sql = "SELECT * FROM edge_table WHERE node_1=" + parentNode
					+ " AND node_2=" + childNode;
			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				System.err
						.println("This edge pair already exists in database.");
			} else {
				sql = "INSERT INTO edge_table " + "VALUES (" + parentNode
						+ ", " + childNode + ", '" + type + "')";
				ps = conn.prepareStatement(sql);
				ps.executeUpdate(sql);
			}

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error inserting row into edge table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void addToEdgeTable(int parentNode, int childNode) {
		addToEdgeTable(parentNode, childNode, 'T');
	}

	public void addToEdgeTable(Edge e) {
		addToEdgeTable(e.node_1, e.node_2, 'T');
	}

	public int getWordId(String word) {
		int id = -1;
		try {
			String sql = "SELECT word_id FROM word_table WHERE word='" + word
					+ "'";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet w = ps.executeQuery();
			if (w.next()) {
				id = Integer.parseInt(w.getString("word_id"));
			}
		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error finding word id.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return id;
	}

	public int getMaxNodeId() {
		int id = -1;
		try {
			String sql = "SELECT MAX(node_id) AS maxId FROM node_table";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				id = rs.getInt("maxId");
			} else {
				id = 0;
			}
			// System.out.println("id is " + id);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error getting max nodeId.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return id;
	}

	public int getMaxWordId() {
		int id = -1;
		try {
			String sql = "SELECT MAX(word_id) AS maxId FROM word_table";
			ResultSet rs = stmt.executeQuery(sql);
			if (rs.next()) {
				id = rs.getInt("maxId");
			} else {
				id = 0;
			}
			// System.out.println("id is " + id);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error getting max wordId.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		return id;
	}

	public void createConnection() {
		try {

			conn = DriverManager
					.getConnection(
							"jdbc:mysql://datalake.c2lclaii6yaq.us-west-2.rds.amazonaws.com/Datalake",
							"admin", "testing1234");
			stmt = conn.createStatement();

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error connecting to database.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void printTable(String table) {
		try {
			String sql = "SELECT * FROM " + table;
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			System.out.println("TABLE: " + table + " # of columns: "
					+ columnsNumber);
			while (rs.next()) {
				for (int i = 1; i <= columnsNumber; i++) {
					System.out.print(rs.getString(i) + " ");
				}
				System.out.println();
			}
		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error printing table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

	}

	public void getTableNames() {
		try {

			DatabaseMetaData dbmd = conn.getMetaData();
			String[] types = { "TABLE" };
			ResultSet rs = dbmd.getTables(null, null, "%", types);
			while (rs.next()) {
				System.out.println(rs.getString("TABLE_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void getColumnNames(String table) {
		try {

			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet rs = dbmd.getColumns(null, null, "edge_table", null);
			while (rs.next()) {
				System.out.println(rs.getString("COLUMN_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void deleteTable(String table) {
		try {
			String sql = "DROP TABLE " + table;
			stmt.executeUpdate(sql);
			// sql="SELECT * FROM " + table;
			// stmt.executeQuery(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error deleting " + table + " from database.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void closeConnection() {
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