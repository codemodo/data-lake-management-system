package project.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import project.database.*;

public class SingleWordSingleSearch {
	public Document document;
	public HashSet<Node> nodesSet;
	public HashSet<Edge> edgesSet;
	public String searchTerm;
	public ArrayList<Node> pathFromRoot;
	public HashMap<Node, ArrayList<Node>> adjacencyList;
	
	public SingleWordSingleSearch() {
		nodesSet = new HashSet<Node>();
		edgesSet = new HashSet<Edge>();
	}
	
	public ArrayList<Node> getPathFromRoot() {
		if (pathFromRoot != null)
			return pathFromRoot;
		Node rootNode = identifyRootNode();
		createAdjacencyMatrix();
		pathFromRoot = bfs(rootNode);
		return pathFromRoot;
	}
	
	public ArrayList<Node> bfs(Node root) {
		Queue<ArrayList<Node>> queue = new LinkedList<ArrayList<Node>>();
		ArrayList<Node> startingList = new ArrayList<Node>();
		startingList.add(root);
		queue.add(startingList);
		
		while (!queue.isEmpty()) {
			ArrayList<Node> path = queue.poll();
			Node nextNode = path.get(path.size() - 1);
			if ((nextNode.getKey() != null && nextNode.getKey().equalsIgnoreCase(searchTerm)) ||
					(nextNode.getValue() != null && nextNode.getValue().equalsIgnoreCase(searchTerm))) {
				return path;
			}
			
			ArrayList<Node> adjacentNodes = adjacencyList.get(nextNode);
			for (Node node : adjacentNodes) {
				ArrayList<Node> newPath = new ArrayList<Node>();
				newPath.addAll(path);
				newPath.add(node);
				queue.add(newPath);
			}
		}
		return null;
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
		}
	}
	
	public HashMap<Integer, Node> getIdToNodeMap() {
		HashMap<Integer, Node> idToNode = new HashMap<Integer, Node>();
		for (Node node : nodesSet)
			idToNode.put(node.getId(), node);
		return idToNode;
	}
	
	public Node identifyRootNode() {
		HashSet<Integer> receivingNodes = new HashSet<Integer>();
		for (Edge edge : edgesSet)
			receivingNodes.add(edge.getNode2ID());
		for (Node node : nodesSet)
			if (!receivingNodes.contains(node.getId()))
				return node;
		throw new IllegalStateException("The document contains cycles.");
	}
	
	
	public void setDocument(Document document) {
		this.document = document;
	}
	public Document getDocument() {
		return document;
	}
	public void addNode(Node node) {
		nodesSet.add(node);
	}
	public void addEdge(Edge edge) {
		edgesSet.add(edge);
	}

	public String getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(String searchTerm) {
		this.searchTerm = searchTerm;
	}
	
	
}
