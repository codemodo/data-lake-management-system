package DBProject.Extractor;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeType;

public class JacksonJSONParser extends DataParser {
	public static int docId;
	public static int nextNodeId;

	public static boolean parseWithJackson(File file, int doc, String docName) {
		

		docId = doc;
		nextNodeId = dbc.getMaxNodeId() + 1;
		// nextNodeId = 1;
		// System.out.println("next nodeId is: " + nextNodeId);
		int nid = nextNodeId;
		ObjectMapper m = new ObjectMapper();
		JsonNode rootNode;
		TreeNode n = new TreeNode(nid, docName, "", docId);
		nodeList.addToList(n);
		nextNodeId++;
		// System.out.println("Added to doc root to node table, id=" + (nid));
		try {
			rootNode = m.readTree(file);
			parseJsonTree2(rootNode, nid);
			finishAllRemainingBatches();
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

	public static void parseJsonTree2(JsonNode node, int parentNodeId) {
		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();
		String nodeKey = "";
		String nodeValue = "";
		int nid = nextNodeId;
		while (fieldsIterator.hasNext()) {
			nid = nextNodeId;
			nextNodeId++;
			nodeValue = "";
			Map.Entry<String, JsonNode> field = fieldsIterator.next();
			nodeKey = field.getKey();
			JsonNodeType t = field.getValue().getNodeType();
			if (t.equals(JsonNodeType.STRING)) {
				// System.out.println("string");
				nodeValue = field.getValue().asText();
				TreeNode n = new TreeNode(nid, nodeKey, nodeValue, docId);
				nodeList.addToList(n);
				addToInvertedIndex(n);
				// System.out.println("Added to node table:\tId: " + nid +
				// "\tKey: " + nodeKey + "\tValue: " + nodeValue);
				Edge e = new Edge(parentNodeId, nid);
				edgeList.addToList(e);
				// System.out.println("Added to edge table:\tparentId: " +
				// parentNodeId + "\tchildNodeId: " + nid);
			} else if (t.equals(JsonNodeType.ARRAY)) {
				//System.out.println("this is an array");
				TreeNode n = new TreeNode(nid, nodeKey, nodeValue, docId);
				nodeList.addToList(n);
				addToInvertedIndex(n);
				//System.out.println("nodeKey: " + nodeKey + " nodeValue: " + nodeValue );
				// System.out.println("Added to node table:\tId: " + nid +
				// "\tKey: " + nodeKey + "\tValue: " + nodeValue);
				Edge e = new Edge(parentNodeId, nid);
				edgeList.addToList(e);
				// System.out.println("Added to edge table:\tparentId: " +
				// parentNodeId + "\tchildNodeId: " + nid);
				Iterator<JsonNode> childNodes = field.getValue().elements();
				while (childNodes.hasNext()) {
					JsonNode child = childNodes.next();
					//System.out.println("child text: " + child.asText());
					if(child.isTextual()) {
						n = new TreeNode(nextNodeId, child.asText(), "", docId);
						nextNodeId++;
						nodeList.addToList(n);
						addToInvertedIndex(n);
						e = new Edge(nid, n.nodeID);
						edgeList.addToList(e);
						//System.out.println("added everything");
					}
					else {
						parseJsonTree2(child, nid);
					}
				}
			} else {
				// System.out.println("not a string or array");
				TreeNode n = new TreeNode(nid, nodeKey, nodeValue, docId);
				nodeList.addToList(n);
				addToInvertedIndex(n);
				// System.out.println("Added to node table:\tId: " + nid +
				// "\tKey: " + nodeKey + "\tValue: " + nodeValue);
				Edge e = new Edge(parentNodeId, nid);
				edgeList.addToList(e);
				// System.out.println("Added to edge table:\tparentId: " +
				// parentNodeId + "\tchildNodeId: " + nid);
				Iterator<JsonNode> childNodes = field.getValue().elements();
				childNodes = node.elements();
				while (childNodes.hasNext()) {
					JsonNode child = childNodes.next();
					parseJsonTree2(child, nid);
				}
			}
		}
	}

	

}