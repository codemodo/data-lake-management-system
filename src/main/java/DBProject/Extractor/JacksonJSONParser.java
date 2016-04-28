package DBProject.Extractor;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonJSONParser extends DataParser{
	
	public static boolean parseWithJackson(File file) {
		// TODO Auto-generated method stub
		ObjectMapper m = new ObjectMapper();
		// JsonParserFactor jpf = Json.createParserFactory();
		JsonNode rootNode;
		try {
			rootNode = m.readTree(file);
			parseJsonTree2(rootNode);
			// // lets see what type the node is
			// System.out.println(rootNode.getNodeType()); // prints OBJECT
			// // is it a container
			// System.out.println(rootNode.isContainerNode()); // prints true
			// System.out.println(rootNode.textValue());
			// Iterator<String> fieldNames = rootNode.fieldNames();
			// Iterator<JsonNode> childNodes = rootNode.elements();
			// while (fieldNames.hasNext()){
			// String fieldName = fieldNames.next();
			// System.out.println(fieldName);
			// }
			// while(childNodes.hasNext()){
			// JsonNode child = childNodes.next();
			// System.out.println(child.textValue());
			// }
			//
			//
			// parseJsonTree(rootNode);
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

	public static void parseJsonTree2(JsonNode node) {
		System.out.println("node type: " + node.getNodeType());

		Iterator<Map.Entry<String, JsonNode>> fieldsIterator = node.fields();
		while (fieldsIterator.hasNext()) {

			Map.Entry<String, JsonNode> field = fieldsIterator.next();
			System.out.println("Key: " + field.getKey() + "\tValue:"
					+ field.getValue());

		}
		
		Iterator<JsonNode> childNodes = node.elements();
		while (childNodes.hasNext()) {
			JsonNode child = childNodes.next();
			parseJsonTree2(child);
		}
		
		// Iterator<JsonNode> childNodes = node.elements();
		// while (childNodes.hasNext()) {
		// JsonNode child = childNodes.next();
		// System.out.println(child.textValue());
		// parseJsonTree2(child);
		// }
	}

}
