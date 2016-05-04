package project.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import project.database.Document;
import project.database.Edge;
import project.database.Node;

public class TwoWordSearch {
	public HashMap<Integer, Document> idToDocs;
	public List<Document> docsList;
	public HashSet<Node> nodesSet;
	public HashSet<Edge> edgesSet;
	public String searchTerm1;
	public String searchTerm2;
	public ArrayList<ArrayList<Node>> shortestPaths;
	public HashMap<Node, ArrayList<Node>> adjacencyList;
	
	public TwoWordSearch() {
		idToDocs = new HashMap<Integer, Document>();
		docsList = new ArrayList<Document>();
		nodesSet = new HashSet<Node>();
		edgesSet = new HashSet<Edge>();
	}
	
	public void createAdjacencyMatrix() {
		adjacencyList = new HashMap<Node, ArrayList<Node>>();
		HashMap<Integer, Node> idToNode = getIdToNodeMap();
		for (Node node : nodesSet)
			adjacencyList.put(node, new ArrayList<Node>());
		for (Edge edge : edgesSet) {
			Node parent = idToNode.get(edge.getNode1ID());
			Node child = idToNode.get(edge.getNode2ID());
			adjacencyList.get(parent).add(child);
			adjacencyList.get(child).add(parent);
		}
	}
	
	public HashMap<Integer, Node> getIdToNodeMap() {
		HashMap<Integer, Node> idToNode = new HashMap<Integer, Node>();
		for (Node node : nodesSet)
			idToNode.put(node.getId(), node);
		return idToNode;
	}
	
	public void addDoc(Document doc) {
		if (idToDocs.get(doc.getId()) == null) {
			idToDocs.put(doc.getId(), doc);
			docsList.add(doc);
		}
	}
	
	public void addNodes(List<Node> nodes) {
		nodesSet.addAll(nodes);
	}
	
	public void addEdges(List<Edge> edges) {
		edgesSet.addAll(edges);
	}


	public String getSearchTerm1() {
		return searchTerm1;
	}

	public void setSearchTerm1(String searchTerm1) {
		this.searchTerm1 = searchTerm1;
	}

	public String getSearchTerm2() {
		return searchTerm2;
	}

	public void setSearchTerm2(String searchTerm2) {
		this.searchTerm2 = searchTerm2;
	}

	public List<Document> getDocList() {
		return docsList;
	}
	
	
}
