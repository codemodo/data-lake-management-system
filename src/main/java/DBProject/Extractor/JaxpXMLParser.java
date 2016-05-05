package DBProject.Extractor;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JaxpXMLParser extends DataParser {
	static int nextNodeID;
	static int docID;

	static boolean parseWithJaxp(File file, int doc, String docName) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// dbf.setValidating(true);
		dbf.setNamespaceAware(true);

		try {
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document document = builder.parse(file);
			docID = doc;
			nextNodeID = dbc.getMaxNodeId() + 1;
			Node root = document.getDocumentElement();
			TreeNode n = new TreeNode(nextNodeID, docName, "", docID);
			//nextNodeID++;
			nodeList.addToList(n);
			parseWithJaxp(root, n.nodeID);
			System.out.println(nodeList.toString());
			//System.out.println(edgeList.toString());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	static void parseWithJaxp(Node node, int parentNodeID) {
		// TreeNode treeNode = new TreeNode(docID, null, null, docID);
		// dbc.addToNodeTable(treeNode);
		String key = "";
		String value = "";		
		int nodeID = nextNodeID;
		if(nodeID == 6) {
			System.out.println("a");
		}
		if (node.getNodeType() == Node.ELEMENT_NODE) {
			nextNodeID++;
			if(nextNodeID == 6) {
				System.out.println("a");
			}
			nodeID = nextNodeID;
			Element elem = (Element) node;
			// System.out.println("TagName: " + elem.getTagName());
			key = elem.getTagName();
			value = "";
			TreeNode n = new TreeNode(nodeID, key, value, docID);
			nodeList.addToList(n);
			Edge e = new Edge(parentNodeID, nodeID);
			edgeList.addToList(e);
			NamedNodeMap attrs = elem.getAttributes();
			if (attrs != null) {
				for (int i = 0; i < attrs.getLength(); i++) {
					Attr a = (Attr) attrs.item(i);
					// System.out.println("attr key: " + a.getName());
					// System.out.println("attr value: " + a.getValue());
					nextNodeID++;
					if(nextNodeID == 6) {
						System.out.println("a");
					}
					edgeList.addToList(nodeID, nextNodeID);
					nodeList.addToList(nextNodeID, a.getName(), a.getValue(),
							docID);
					// System.out.println("Adding node with key " + a.getName()
					// + " and value " + a.getValue());
				}
			}
		} else {
			value = node.getNodeValue() == null ? "" : node.getNodeValue()
					.trim();
		}

		//nodeList.addToList(nodeID, key, value, docID);
		// System.out.println("Adding node with key " + key + " and value " +
		// value);
		//edgeList.addToList(parentNodeID, nodeID);

		NodeList children = node.getChildNodes();

		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				parseWithJaxp(children.item(i), nodeID);
			}
		}
	}

}
