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
	
	static boolean parseWithJaxp(File file) {
		// TODO Auto-generated method stub

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		// dbf.setValidating(true);
		dbf.setNamespaceAware(true);

		try {
			DocumentBuilder builder = dbf.newDocumentBuilder();
			Document document = builder.parse(file);
			Node root = document.getDocumentElement();
			parseWithJaxp(root);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	static void parseWithJaxp(Node node) {
		
		if (node.getNodeType() == Node.ELEMENT_NODE) {
	        Element elem = (Element) node;
	        System.out.println("TagName: " + elem.getTagName());
	        NamedNodeMap attrs = elem.getAttributes();
	        if (attrs != null){
	        	for (int i = 0; i < attrs.getLength(); i++){
	        		Attr a = (Attr) attrs.item(i);
	        		System.out.println("attr key: " + a.getName());
	        		System.out.println("attr value: " + a.getValue());
	        	}
	        }
	    }
		
		
		String value = node.getNodeValue() == null ? "" : node.getNodeValue().trim();

		System.out.println("value: " + value);
		System.out.println("type: " + node.getNodeType() + "\n");
		
		NodeList children = node.getChildNodes();

		if (children != null) {
			for (int i = 0; i < children.getLength(); i++) {
				parseWithJaxp(children.item(i));
			}
		}
	}

}
