package DBProject.Extractor;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class TestDatabaseConnector extends DatabaseConnector {

	public static Connection conn = null;
	public static Statement stmt = null;

	public void createDocTable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS test_doc_table "
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
			String sql = "INSERT INTO test_doc_table " + "VALUES (" + docID
					+ ")";
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
			String sql = "CREATE TABLE IF NOT EXISTS test_node_table "
					+ "(node_id INTEGER, " + "k VARCHAR(255), "
					+ "v VARCHAR(255), " + "doc_id INTEGER, "
					+ "PRIMARY KEY (node_id))";

			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating test_node table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void addToNodeTable(int nodeID, String key, String value,
			int docID) {
		try {
			PreparedStatement ps = conn
					.prepareStatement("INSERT INTO test_node_table "
							+ "VALUES (?, ?, ?, ?)");
			ps.setInt(1, nodeID);
			ps.setString(2, key);
			ps.setString(3, value);
			ps.setInt(4, docID);
			ps.executeUpdate();

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error inserting row into test_node table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void addToNodeTable(ArrayList<TreeNode> batch) {
		try {
			conn.setAutoCommit(false);
			String sql = "INSERT INTO test_node_table (node_id, k, v, doc_id) VALUES (?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);

			for (TreeNode node : batch) {
				ps.setInt(1, node.nodeID);
				ps.setString(2, node.k);
				ps.setString(3, node.v);
				ps.setInt(4, node.docID);
				ps.addBatch();
			}

			ps.executeBatch();
			conn.commit();
			ps.close();
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
			String sql = "INSERT INTO test_node_table " + "VALUES (" + n.nodeID
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
			String sql = "CREATE TABLE IF NOT EXISTS test_ii_table "
					+ "(word VARCHAR(255), " + " node_id INTEGER, "
					+ " PRIMARY KEY (word, node_id))";

			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating inverted index table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void addToIITable(ArrayList<InvertedIndexEntry> batch) {
		try {
			String sql = "INSERT INTO test_ii_table (word, node_id) VALUES (?, ?)";
			conn.setAutoCommit(false);
			PreparedStatement ps = conn.prepareStatement(sql);

			for (InvertedIndexEntry entry : batch) {
				ps.setString(1, entry.word);
				ps.setInt(2, entry.nodeID);

				ps.addBatch();
			}

			ps.executeBatch();
			conn.commit();
			ps.close();
		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error inserting row into node table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public void addToIITable(String word, int nodeID) {
		try {
			PreparedStatement ps = conn
					.prepareStatement("INSERT INTO test_ii_table VALUES (?, ?)");
			ps.setString(1, word);
			ps.setInt(2, nodeID);
			ps.executeUpdate();

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in adding to inverted index table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
			System.out.println("Word: " + word + " nodeID: " + nodeID);
		}
	}

	public void createEdgeTable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS test_edge_table "
					+ "(node_1 INTEGER, " + " node_2 INTEGER, "
					+ " link_type CHAR(1)," + " PRIMARY KEY (node_1, node_2))";

			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating edge table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void addToEdgeTable(ArrayList<Edge> batch) {
		try {
			// double time = System.nanoTime();
			String sql = "INSERT INTO test_edge_table (node_1, node_2) VALUES (?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			conn.setAutoCommit(false);

			for (Edge entry : batch) {
				ps.setInt(1, entry.node_1);
				ps.setInt(2, entry.node_2);
				ps.addBatch();
			}

			ps.executeBatch();
			conn.commit();
			ps.close();
			// System.out.println("edge batch took " + (System.nanoTime() -
			// time));
		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error inserting row into node table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void addToEdgeTable(int parentNode, int childNode, char type) {
		try {
			String sql = "SELECT * FROM test_edge_table WHERE node_1="
					+ parentNode + " AND node_2=" + childNode;
			PreparedStatement ps = conn.prepareStatement(sql);

			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				System.err
						.println("This edge pair already exists in database.");
			} else {
				sql = "INSERT INTO test_edge_table " + "VALUES (" + parentNode
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

	public int getMaxNodeId() {
		int id = -1;
		try {
			String sql = "SELECT MAX(node_id) AS maxId FROM test_node_table";
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
			String sql = "SELECT MAX(word_id) AS maxId FROM test_word_table";
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

	public void getColumnNames(String table) {
		try {

			DatabaseMetaData dbmd = conn.getMetaData();
			ResultSet rs = dbmd.getColumns(null, null, "test_edge_table", null);
			while (rs.next()) {
				System.out.println(rs.getString("COLUMN_NAME"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
