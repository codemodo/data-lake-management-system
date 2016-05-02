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

	public static void createWordTable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS word_table "
					+ "(word_id INTEGER, " 
					+ " word VARCHAR(255), "
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

	public static void createDocTable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS doc_table "
					+ "(doc_id INTEGER, " 
					+ "PRIMARY KEY (doc_id))";

			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating doc table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}
	
	public static void addToDocTable(int docID) {
		try {
			String sql = "INSERT INTO doc_table " +
                "VALUES (" + docID + ")";
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
	
	public static void createNodeTable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS node_table "
					+ "(node_id INTEGER, " 
					+ "k VARCHAR(255), "
					+ "v VARCHAR(255), " 
					+ "doc_id INTEGER, "
					+ "PRIMARY KEY (node_id))";//,"
					//+ "FOREIGN KEY (doc_id) REFERENCES doc_table(doc_id))";

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
			String sql = "INSERT INTO node_table " +
                "VALUES (" + nodeID + ", '" + key + "', '" + value + "', " + docID + ")";
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
	
	public static void createIITable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS ii_table "
					+ "(word_id INTEGER, " 
					+ " node_id INTEGER, "
					+ " PRIMARY KEY (word_id, node_id))";
//					+ " FOREIGN KEY (word_id) REFERENCES word_table(word_id),"
//					+ " FOREIGN KEY (node_id) REFERENCES node_table(node_id))";

			stmt.executeUpdate(sql);

		} catch (SQLException ex) {
			// handle any errors
			System.err.println("Error in creating inverted index table.");
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
	}

	public static void createEdgeTable() {

		try {
			String sql = "CREATE TABLE IF NOT EXISTS edge_table "
					+ "(node_1 INTEGER, " 
					+ " node_2 INTEGER, "
					+ " link_type CHAR(1),"
					+ " PRIMARY KEY (node_1, node_2))";//,"
//					+ " FOREIGN KEY (node_1) REFERENCES node_table(node_id),"
//					+ " FOREIGN KEY (node_2) REFERENCES node_table(node_id),"
//					+ " FOREIGN KEY (doc_id) REFERENCES doc_table(doc_id))";

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
			String sql = "SELECT * FROM edge_table WHERE node_1=" + parentNode + " AND node_2=" + childNode;
			ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                System.err.println("This edge pair already exists in database.");
            }
            else {
            	sql = "INSERT INTO edge_table " +
                      "VALUES (" + parentNode + ", " + childNode + ", '" + type + "')";
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
	
//	public static void fillInvertedIndex(int lastNodeId) {
//		try {
//			String sql = "SELECT * FROM node_table WHERE node_id>" + lastNodeId;
//			ResultSet rs = stmt.executeQuery(sql);
//            while(rs.next()) {
//                (rs.)
//                
//            } 
//		
//		} catch (SQLException ex) {
//    	    // handle any errors
//			System.err.println("Error inserting row into edge table.");
//    	    System.out.println("SQLException: " + ex.getMessage());
//    	    System.out.println("SQLState: " + ex.getSQLState());
//    	    System.out.println("VendorError: " + ex.getErrorCode());
//    	} 
//	}
	
	public static int getMaxNodeId() {
		int id = -1;
		try {
			String sql = "SELECT MAX(node_id) AS maxId FROM node_table";
			ResultSet rs = stmt.executeQuery(sql);
			if(rs.next()) {
				id = rs.getInt("maxId");
			}
			else {
				id = 0;
			}
			//System.out.println("id is " + id);
		
		} catch (SQLException ex) {
    	    // handle any errors
			System.err.println("Error getting max nodeId.");
    	    System.out.println("SQLException: " + ex.getMessage());
    	    System.out.println("SQLState: " + ex.getSQLState());
    	    System.out.println("VendorError: " + ex.getErrorCode());
    	} 
		return id;
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

	public static void printTable(String table) {
		try {
			String sql = "SELECT * FROM " + table;
			ResultSet rs = stmt.executeQuery(sql);
			ResultSetMetaData rsmd = rs.getMetaData();
			int columnsNumber = rsmd.getColumnCount();
			System.out.println("TABLE: " + table + " # of columns: " + columnsNumber);
			while(rs.next()) {
				for(int i = 1; i <= columnsNumber; i++) {
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