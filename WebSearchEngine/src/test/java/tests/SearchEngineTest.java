package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import javax.sql.DataSource;

import org.json.simple.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import project.components.SearchEngine;
import project.components.SingleWordSingleSearch;
import project.components.TwoWordSearch;
import project.database.DocumentJdbcTemplate;
import project.database.EdgeJdbcTemplate;
import project.database.Node;
import project.database.NodeJdbcTemplate;
import project.database.User;
import project.database.UserJdbcTemplate;

public class SearchEngineTest {
	
	SearchEngine engine;
	UserJdbcTemplate userTemp;
	public UserJdbcTemplate getUserJdbcTemplate() {
		UserJdbcTemplate userTemp = new UserJdbcTemplate();
		userTemp.setDataSource(getDataSource());
		return userTemp;
	}
	
	public DocumentJdbcTemplate getDocJdbcTemplate() {
		DocumentJdbcTemplate docTemp = new DocumentJdbcTemplate();
		docTemp.setDataSource(getDataSource());
		return docTemp;
	}
	
	public NodeJdbcTemplate getNodeJdbcTemplate() {
		NodeJdbcTemplate nodeTemp = new NodeJdbcTemplate();
		nodeTemp.setDataSource(getDataSource());
		return nodeTemp;
	}
	
	public EdgeJdbcTemplate getEdgeJdbcTemplate() {
		EdgeJdbcTemplate edgeTemp = new EdgeJdbcTemplate();
		edgeTemp.setDataSource(getDataSource());
		return edgeTemp;
	}

	public DataSource getDataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
	    dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
	    dataSource.setUrl("jdbc:mysql://ec2-50-19-202-216.compute-1.amazonaws.com/EMP" + 
	    		"?useUnicode=true&useJDBCCompliantTimezoneShift=true&serverTimezone=UTC");
	    dataSource.setUsername("ryan");
	    dataSource.setPassword("mysqladmin");
	    return dataSource;
	}
	
	@Before
	public void setUp() throws Exception {
		engine = new SearchEngine();
		engine.setDocJdbc(getDocJdbcTemplate());
		engine.setEdgeJdbc(getEdgeJdbcTemplate());
		engine.setNodeJdbc(getNodeJdbcTemplate());
		userTemp = getUserJdbcTemplate();
	}

//	@Test
//	public void getNodesAndPrepareClassesTest() {
//		User user = userTemp.getUser("rysmit");
//		assertNotNull(user);
//		List<SingleWordSingleSearch> swssList = engine.getNodesAndPrepareClasses("t", user);
//		assertNotNull(swssList);
//		assertEquals(swssList.size(), 1);
//		assertEquals(swssList.get(0).getDocument().getId(), 1);
//		assertEquals(swssList.get(0).edgesSet.size(), 11);
//		assertEquals(swssList.get(0).nodesSet.size(), 12);
//	}
//	
//	@Test 
//	public void testIdentifyRootNode() {
//		User user = userTemp.getUser("rysmit");
//		assertNotNull(user);
//		SingleWordSingleSearch swss = engine.getNodesAndPrepareClasses("t", user).get(0);
//		Node rootNode = swss.identifyRootNode();
//		assertNotNull(rootNode);
//		assertEquals(rootNode.getId(), 1);
//	}
//	
//	@Test
//	public void testGetNodesJson() {
//		User user = userTemp.getUser("rysmit");
//		assertNotNull(user);
//		SingleWordSingleSearch swss = engine.getNodesAndPrepareClasses("t", user).get(0);
//		assertEquals(swss.searchTerm, "t");
//		ArrayList<Node> sp = swss.getPathFromRoot();
//		JSONArray jArray = swss.getNodesJson();
//		System.out.println(jArray);
//	}
//	
//	@Test
//	public void testGetEdgesJson() {
//		User user = userTemp.getUser("rysmit");
//		assertNotNull(user);
//		SingleWordSingleSearch swss = engine.getNodesAndPrepareClasses("t", user).get(0);
//		assertEquals(swss.searchTerm, "t");
//		ArrayList<Node> sp = swss.getPathFromRoot();
//		JSONArray jArray = swss.getEdgesJson();
//		System.out.println(jArray);
//	}
//	
//	@Test
//	public void testGetPathFromRoot() {
//		User user = userTemp.getUser("rysmit");
//		assertNotNull(user);
//		SingleWordSingleSearch swss = engine.getNodesAndPrepareClasses("a", user).get(0);
//		assertEquals(swss.searchTerm, "a");
//		ArrayList<Node> sp = swss.getPathFromRoot();
//		for (Node n : sp) {
//			System.out.print(n.getId() + " ");
//		}
//		assertNotNull(sp);
//		assertEquals(sp.size(), 1);
//		assertEquals(sp.get(0).getId(), 1);
//		System.out.println("");
//		
//		swss.pathFromRoot = null;
//		swss.setSearchTerm("e");
//		assertNull(swss.pathFromRoot);
//		assertEquals(swss.searchTerm, "e");
//		sp = swss.getPathFromRoot();
//		for (Node n : sp) {
//			System.out.print(n.getId() + " ");
//		}
//		assertNotNull(sp);
//		assertEquals(sp.size(), 2);
//		assertEquals(sp.get(0).getId(), 1);
//		assertEquals(sp.get(1).getId(), 3);
//		System.out.println("");
//		
//		swss.pathFromRoot = null;
//		swss.setSearchTerm("t");
//		assertNull(swss.pathFromRoot);
//		assertEquals(swss.searchTerm, "t");
//		sp = swss.getPathFromRoot();
//		for (Node n : sp) {
//			System.out.print(n.getId() + " ");
//		}
//		assertNotNull(sp);
//		assertEquals(sp.size(), 5);
//		assertEquals(sp.get(0).getId(), 1);
//		assertEquals(sp.get(1).getId(), 4);
//		assertEquals(sp.get(2).getId(), 7);
//		assertEquals(sp.get(3).getId(), 8);
//		assertEquals(sp.get(4).getId(), 10);
//		System.out.println("");
//	}
//	
//	@Test
//	public void testCreateAdjacencyMatrix() {
//		User user = userTemp.getUser("rysmit");
//		assertNotNull(user);
//		SingleWordSingleSearch swss = engine.getNodesAndPrepareClasses("t", user).get(0);
//		swss.createAdjacencyMatrix();
//		assertNotNull(swss.adjacencyList);
//		Node rootNode = swss.identifyRootNode();
//		ArrayList<Node> rootNodeList = swss.adjacencyList.get(rootNode);
//		HashMap<Integer, Node> idToNode = swss.getIdToNodeMap();
//		HashSet<Node> nodeSet = new HashSet<Node>();
//		nodeSet.addAll(rootNodeList);
//		assertEquals(nodeSet.size(), 3);
//		assertTrue(nodeSet.contains(idToNode.get(2)));
//		assertTrue(nodeSet.contains(idToNode.get(3)));
//		assertTrue(nodeSet.contains(idToNode.get(4)));
//		
//		rootNodeList = swss.adjacencyList.get(idToNode.get(2));
//		nodeSet = new HashSet<Node>();
//		nodeSet.addAll(rootNodeList);
//		assertEquals(nodeSet.size(), 2);
//		assertTrue(nodeSet.contains(idToNode.get(5)));
//		assertTrue(nodeSet.contains(idToNode.get(6)));
//		
//		rootNodeList = swss.adjacencyList.get(idToNode.get(8));
//		nodeSet = new HashSet<Node>();
//		nodeSet.addAll(rootNodeList);
//		assertEquals(nodeSet.size(), 2);
//		assertTrue(nodeSet.contains(idToNode.get(9)));
//		assertTrue(nodeSet.contains(idToNode.get(10)));
//		
//		rootNodeList = swss.adjacencyList.get(idToNode.get(5));
//		nodeSet = new HashSet<Node>();
//		nodeSet.addAll(rootNodeList);
//		assertEquals(nodeSet.size(), 1);
//		assertTrue(nodeSet.contains(idToNode.get(11)));
//		
//		rootNodeList = swss.adjacencyList.get(idToNode.get(6));
//		nodeSet = new HashSet<Node>();
//		nodeSet.addAll(rootNodeList);
//		assertEquals(nodeSet.size(), 1);
//		assertTrue(nodeSet.contains(idToNode.get(12)));
//		
//		rootNodeList = swss.adjacencyList.get(idToNode.get(4));
//		nodeSet = new HashSet<Node>();
//		nodeSet.addAll(rootNodeList);
//		assertEquals(nodeSet.size(), 1);
//		assertTrue(nodeSet.contains(idToNode.get(7)));
//		
//		rootNodeList = swss.adjacencyList.get(idToNode.get(7));
//		nodeSet = new HashSet<Node>();
//		nodeSet.addAll(rootNodeList);
//		assertEquals(nodeSet.size(), 1);
//		assertTrue(nodeSet.contains(idToNode.get(8)));
//		
//		//check Leaves
//		rootNodeList = swss.adjacencyList.get(idToNode.get(11));
//		nodeSet = new HashSet<Node>();
//		nodeSet.addAll(rootNodeList);
//		assertTrue(nodeSet.isEmpty());
//		rootNodeList = swss.adjacencyList.get(idToNode.get(12));
//		nodeSet = new HashSet<Node>();
//		nodeSet.addAll(rootNodeList);
//		assertTrue(nodeSet.isEmpty());
//		rootNodeList = swss.adjacencyList.get(idToNode.get(9));
//		nodeSet = new HashSet<Node>();
//		nodeSet.addAll(rootNodeList);
//		assertTrue(nodeSet.isEmpty());
//		rootNodeList = swss.adjacencyList.get(idToNode.get(10));
//		nodeSet = new HashSet<Node>();
//		nodeSet.addAll(rootNodeList);
//		assertTrue(nodeSet.isEmpty());
//		rootNodeList = swss.adjacencyList.get(idToNode.get(3));
//		nodeSet = new HashSet<Node>();
//		nodeSet.addAll(rootNodeList);
//		assertTrue(nodeSet.isEmpty());
//	}
	
	@Test
	public void testPrepareNodesAndClassesTwoWord() {
		User user = userTemp.getUser("rysmit");
		assertNotNull(user);
		TwoWordSearch tws = engine.getNodesAndPrepareClassesTwoWord("t", "cc", user);
		assertNotNull(tws);
		assertEquals(tws.getDocList().size(), 2);
		assertEquals(tws.idToDocs.keySet().size(), 2);
		assertTrue(tws.idToDocs.containsKey(1));
		assertTrue(tws.idToDocs.containsKey(5));
		assertEquals(tws.searchTerm1, "t");
		assertEquals(tws.searchTerm2, "cc");
		assertEquals(tws.nodesSet.size(), 24);
		assertEquals(tws.edgesSet.size(), 24);
	}

	@Test
	public void testCreateAdjacencyMatrixTwoWords() {
		User user = userTemp.getUser("rysmit");
		assertNotNull(user);
		TwoWordSearch tws = engine.getNodesAndPrepareClassesTwoWord("t", "cc", user);
		assertNotNull(tws);
		tws.createAdjacencyMatrix();
		assertNotNull(tws.adjacencyList);
		
		HashMap<Integer, Node> idToNode = tws.getIdToNodeMap();
		
		List<Node> rootNodeList = tws.adjacencyList.get(idToNode.get(1));
		HashSet<Node> nodeSet = new HashSet<Node>();
		nodeSet.addAll(rootNodeList);
		assertEquals(nodeSet.size(), 3);
		assertTrue(nodeSet.contains(idToNode.get(2)));
		assertTrue(nodeSet.contains(idToNode.get(3)));
		assertTrue(nodeSet.contains(idToNode.get(4)));
		
		rootNodeList = tws.adjacencyList.get(idToNode.get(2));
		nodeSet = new HashSet<Node>();
		nodeSet.addAll(rootNodeList);
		assertEquals(nodeSet.size(), 3);
		assertTrue(nodeSet.contains(idToNode.get(5)));
		assertTrue(nodeSet.contains(idToNode.get(6)));
		assertTrue(nodeSet.contains(idToNode.get(1)));
		
		rootNodeList = tws.adjacencyList.get(idToNode.get(8));
		nodeSet = new HashSet<Node>();
		nodeSet.addAll(rootNodeList);
		assertEquals(nodeSet.size(), 3);
		assertTrue(nodeSet.contains(idToNode.get(9)));
		assertTrue(nodeSet.contains(idToNode.get(10)));
		assertTrue(nodeSet.contains(idToNode.get(7)));
		
		rootNodeList = tws.adjacencyList.get(idToNode.get(5));
		nodeSet = new HashSet<Node>();
		nodeSet.addAll(rootNodeList);
		assertEquals(nodeSet.size(), 2);
		assertTrue(nodeSet.contains(idToNode.get(11)));
		assertTrue(nodeSet.contains(idToNode.get(2)));
		
		rootNodeList = tws.adjacencyList.get(idToNode.get(6));
		nodeSet = new HashSet<Node>();
		nodeSet.addAll(rootNodeList);
		assertEquals(nodeSet.size(), 3);
		assertTrue(nodeSet.contains(idToNode.get(12)));
		assertTrue(nodeSet.contains(idToNode.get(2)));
		assertTrue(nodeSet.contains(idToNode.get(209)));
		
		rootNodeList = tws.adjacencyList.get(idToNode.get(4));
		nodeSet = new HashSet<Node>();
		nodeSet.addAll(rootNodeList);
		assertEquals(nodeSet.size(), 2);
		assertTrue(nodeSet.contains(idToNode.get(7)));
		assertTrue(nodeSet.contains(idToNode.get(1)));
		
		rootNodeList = tws.adjacencyList.get(idToNode.get(7));
		nodeSet = new HashSet<Node>();
		nodeSet.addAll(rootNodeList);
		assertEquals(nodeSet.size(), 3);
		assertTrue(nodeSet.contains(idToNode.get(8)));
		assertTrue(nodeSet.contains(idToNode.get(4)));
		assertTrue(nodeSet.contains(idToNode.get(205)));
		
	}
}
