package DBProject.Extractor;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class JacksonJSONParser extends DataParser{
	public static int docId;
	public static int nextNodeId;
	public static Set<Integer> docIdList;
	public static Set<TreeNode> nodeList;
	public static Set<Edge> edgeList;
	public static DatabaseConnector dbc;
	
	public static boolean parseWithJackson(File file, DatabaseConnector databaseConnector) {
		dbc = databaseConnector;
		docIdList = new HashSet<Integer>();
		nodeList = new HashSet<TreeNode>();
		edgeList = new HashSet<Edge>();
		docId = getDocID(file);
		docIdList.add(docId);	
		nextNodeId = dbc.getMaxNodeId() + 1;
		//nextNodeId = 1;
		//System.out.println("next nodeId is: " + nextNodeId);
		int nid = nextNodeId;
		ObjectMapper m = new ObjectMapper();
		JsonNode rootNode;
		TreeNode n = new TreeNode(nid, "", "", docId);
		nodeList.add(n);
		nextNodeId++;
		//System.out.println("Added to doc root to node table, id=" + (nid));
		try {
			rootNode = m.readTree(file);
			parseJsonTree2(rootNode, nid);
			addToDatabase();
			return true;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return false;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	public static void addToDatabase() {
		Iterator<TreeNode> in = nodeList.iterator();
		while(in.hasNext()) {
			TreeNode n = in.next();
			for(String s : n.k.split(" ")) {
				if(!s.equals("")) {
					int k_id = dbc.getWordId(s);
					if(k_id == -1) {
						k_id = dbc.getMaxWordId() + 1;
						dbc.addToWordTable(k_id, s);
					}
					dbc.addToIITable(k_id, n.nodeID);
				}
			}
			for(String s : n.v.split(" ")) {
				if(!s.equals("")) {
					int v_id = dbc.getWordId(s);
					if(v_id == -1) {
						v_id = dbc.getMaxWordId() + 1;
						dbc.addToWordTable(v_id, s);
					}
					dbc.addToIITable(v_id, n.nodeID);
				}
			}
			//System.out.println("Node_id: " + n.node_id + " key: " + n.k + " value: " + n.v);
			dbc.addToNodeTable(n);
		}
		Iterator<Edge> ie = edgeList.iterator();
		while(ie.hasNext()) {
			Edge e = ie.next();
			//System.out.println("Edge: Node_1: " + e.node_1 + " node_2: " + e.node_2);
		    dbc.addToEdgeTable(e);
		}
		Iterator<Integer> i = docIdList.iterator();
		while(i.hasNext()) {
			int id = i.next();
			//System.out.println("doc id: " + id);
			dbc.addToDocTable(id);
		}
	}
	
	public static void parseJsonTree2(JsonNode node, int parentNodeId) {
		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();
		String nodeKey = "";
		String nodeValue = "";
		int nid = nextNodeId;
		while(fieldsIterator.hasNext()) {
			nid = nextNodeId;
			nextNodeId++;
			nodeValue = "";
			Map.Entry<String, JsonNode> field = fieldsIterator.next();
			nodeKey = field.getKey();
			JsonNodeType t = field.getValue().getNodeType();
			if(t.equals(JsonNodeType.STRING)) {
				//System.out.println("string");
				nodeValue = field.getValue().asText();
				TreeNode n = new TreeNode(nid, nodeKey, nodeValue, docId);
				nodeList.add(n);
				//System.out.println("Added to node table:\tId: " + nid + "\tKey: " + nodeKey + "\tValue: " + nodeValue);	
				Edge e = new Edge(parentNodeId, nid);
				edgeList.add(e);
				//System.out.println("Added to edge table:\tparentId: " + parentNodeId + "\tchildNodeId: " + nid);
			}
			else if(t.equals(JsonNodeType.ARRAY)){
				//System.out.println("not a string");
				TreeNode n = new TreeNode(nid, nodeKey, nodeValue, docId);
				nodeList.add(n);
				//System.out.println("Added to node table:\tId: " + nid + "\tKey: " + nodeKey + "\tValue: " + nodeValue);	
				Edge e = new Edge(parentNodeId, nid);
				edgeList.add(e);
				//System.out.println("Added to edge table:\tparentId: " + parentNodeId + "\tchildNodeId: " + nid);
				Iterator<JsonNode> childNodes = field.getValue().elements();
				while(childNodes.hasNext()) {
					JsonNode child = childNodes.next();
					parseJsonTree2(child, nid);
				}
			}
			else {
				//System.out.println("not a string or array");
				TreeNode n = new TreeNode(nid, nodeKey, nodeValue, docId);
				nodeList.add(n);
				//System.out.println("Added to node table:\tId: " + nid + "\tKey: " + nodeKey + "\tValue: " + nodeValue);	
				Edge e = new Edge(parentNodeId, nid);
				edgeList.add(e);
				//System.out.println("Added to edge table:\tparentId: " + parentNodeId + "\tchildNodeId: " + nid);
				Iterator<JsonNode> childNodes = field.getValue().elements();
				childNodes = node.elements();
				while(childNodes.hasNext()) {
					JsonNode child = childNodes.next();
					parseJsonTree2(child, nid);
				}
			}
		}	
	}

}

