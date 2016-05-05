package linker.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import linker.data.entity.*;

/**
 * This class static methods for pulling and pushing data.
 *
 */
public class Database {
	
	// Database information and credentials
	private static final String DB_URL = "datalake.c2lclaii6yaq.us-west-2.rds.amazonaws.com";
	private static final String DB_NAME = "Datalake?rewriteBatchedStatements=true";
	private static final String USER = "admin";
	private static final String PASSWORD = "testing1234";
	
	// Connection and Statement objects used throughout the methods below
	static Connection conn = null;
	static Statement stmt = null;
	
	/**
	 * Constructor.  Private to force static-only class.
	 */
	private Database() {}
	
	
	/**
	 * Pulls all data and creates Dataset object
	 * @return Dataset containing collections of objects corresponding to tables
	 */
	public static Dataset pullDataset() {
		Dataset ds = null;
		try {
			// Establish connection with the database
			conn = DriverManager.getConnection("jdbc:mysql://" + DB_URL + "/" + DB_NAME, USER, PASSWORD);
			
			// Each helper method handles corresponding data pull and creation of object set
			//ds = new Dataset(loadDocs(), loadNodes(), loadTreeEdges(), loadIndexEntries());
			//ds = new Dataset(loadNodes(), loadIndexEntries());
			ds = new Dataset(loadDocs(), loadNodes(), loadIndexEntries());
			//getColumnNames("ii_table");
			
			// Close connection
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return ds;
	}
	
	
	/**
	 * Pushes newly discovered links up to the database
	 * @param edges
	 */
	public static void pushLinks(Set<LinkedEdge> edges) {
		try {
			conn = DriverManager.getConnection("jdbc:mysql://" + DB_URL + "/" + DB_NAME, USER, PASSWORD);
			stmt = conn.createStatement();
//			
//			conn.setAutoCommit(false);
//			String sql = "INSERT INTO edge_table(node_1, node_2, link_type) VALUES (?, ?, ?)";
//			PreparedStatement ps = conn.prepareStatement(sql);
//
//			for (LinkedEdge e : edges) {
//				ps.setInt(1, e.getNode1());
//				ps.setInt(2, e.getNode2());
//				ps.setString(3, e.getEdgeType());
//				ps.addBatch();
//			}
//
//			try {
//				ps.executeBatch();
//				conn.commit();
//				ps.close();
//				conn.close();
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}	
			
			for (LinkedEdge e : edges) {
				String sql = "insert into edge_table(node_1, node_2, link_type) values(" +
						e.getNode1() + ", " + e.getNode2() + ", '" + e.getEdgeType() + "')";
				try {
					stmt.executeUpdate(sql);
				} catch (java.sql.SQLIntegrityConstraintViolationException e1) {
					// simply catch these so that duplicate node entries don't interrupt
				}
			}
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (conn != null)
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}
	
	
	/**
	 * Loads nodes from node_table and creates set of corresponding objects
	 * @return Set of Node objects
	 * @throws SQLException
	 */
	private static Set<Node> loadNodes() throws SQLException {
		stmt = conn.createStatement();
	    String sql;
		sql = "select * from node_table where not (k = '' and v = '')";
		ResultSet rs = stmt.executeQuery(sql);
		Set<Node> nodes = new HashSet<Node>();
	    while(rs.next()){
	    	int nodeID = rs.getInt("node_id");
	    	String key = rs.getString("k");
	    	String val = rs.getString("v");
	    	int docID = rs.getInt("doc_id");
	    	nodes.add(new Node(nodeID, key, val, docID));
	    }
	    //close resources and connection
	    rs.close();
	    stmt.close();
		return nodes;
	}
	
	
	/**
	 * Loads docs from Document table and creates set of corresponding objects
	 * @return Set of Document objects
	 * @throws SQLException
	 */
	private static Set<Document> loadDocs() throws SQLException {
		stmt = conn.createStatement();
	    String sql;
		sql = "select * from Document";
		ResultSet rs = stmt.executeQuery(sql);
		Set<Document> docs = new HashSet<Document>();
	    while(rs.next()){
	    	int docID = rs.getInt("id");
	    	String docName = rs.getString("name");
	    	docs.add(new Document(docID, docName));
	    }
	    //close resources and connection
	    rs.close();
	    stmt.close();
		return docs;
	}
	
	
	/**
	 * Loads extracted edges from edge_table and creates set of corresponding objects
	 * @return Set of TreeEdge objects
	 * @throws SQLException
	 */
	private static Set<TreeEdge> loadTreeEdges() throws SQLException {
		stmt = conn.createStatement();
	    String sql;
		sql = "select * from edge_table where link_type='T'";
		ResultSet rs = stmt.executeQuery(sql);
		
		Set<TreeEdge> tEdges = new HashSet<TreeEdge>();
	    while(rs.next()){
	    	int nodeID1 = rs.getInt("node_1");
	    	int nodeID2 = rs.getInt("node_2");
	    	String type = rs.getString("link_type");
	    	
	    	assert(type.equals("T"));
	    	tEdges.add(new TreeEdge(nodeID1, nodeID2, type));
	    }
	    //close resources and connection
	    rs.close();
	    stmt.close();
		return tEdges;
	}
	
	
//	/**
//	 * Loads word_table and creates set of corresponding objects
//	 * @return Set of Word objects
//	 * @throws SQLException
//	 */
//	private static Set<Word> loadWords() throws SQLException {
//		stmt = conn.createStatement();
//		stmt.setFetchSize(100);
//	    String sql;
//		sql = "select * from word_table";
//		ResultSet rs = stmt.executeQuery(sql);
//		
//		Set<Word> words = new HashSet<Word>();
//	    while(rs.next()){
//	    	int wordID = rs.getInt("word_id");
//	    	String wordString = rs.getString("word");
//	    	words.add(new Word(wordID, wordString));
//	    }
//	    //close resources and connection
//	    rs.close();
//	    stmt.close();
//		return words;
//	}
	
	
	/**
	 * Loads ii_table and creates set of corresponding objects
	 * @return Set of IndexEntry objects
	 * @throws SQLException
	 */
	private static Set<IndexEntry> loadIndexEntries() throws SQLException {
		stmt = conn.createStatement();
		//stmt.setFetchSize(100);
	    String sql;
		sql = "select * from ii_table";
		ResultSet rs = stmt.executeQuery(sql);
		
		Set<IndexEntry> indexEntries = new HashSet<IndexEntry>();
	    while(rs.next()){
	    	String word = rs.getString("word");
	    	int nodeID = rs.getInt("node_id");
	    	indexEntries.add(new IndexEntry(word, nodeID));
	    }
	    //close resources and connection
	    rs.close();
	    stmt.close();
		return indexEntries;
	}
	
	
	// Helper method to quickly check tables
	@SuppressWarnings("unused")
	private static List<String> getTableNames() throws SQLException {
	    DatabaseMetaData md = conn.getMetaData();
	    String[] types = {"TABLE"};
	    ResultSet rs = md.getTables(null, null, "%", types);
	    List<String> tables = new ArrayList<String>();
	    while (rs.next()) {
	      tables.add(rs.getString("TABLE_NAME"));
	    }
	    rs.close();
	    stmt.close();
	    return tables;
	}
	
	// Helper method to quickly check columns
	@SuppressWarnings("unused")
	private static void getColumnNames(String table) throws SQLException {
	    stmt = conn.createStatement();
	    ResultSet rs = stmt.executeQuery("select * from " + table);
	    ResultSetMetaData m = rs.getMetaData();
	    int cols = m.getColumnCount();
	    for (int i = 1; i <= cols; i++) {
	    	System.out.println(m.getColumnName(i));
	    }
	    rs.close();
	    stmt.close();
	}
}