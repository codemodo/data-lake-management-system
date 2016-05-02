package DBProject.Extractor;

public class TreeNode {
	
	int nodeID;
	String key;
	String value;
	int docID;
	
	public TreeNode(int nodeID, String key, String value, int docID){
		this.nodeID = nodeID;
		this.key = key;
		this.value = value;
		this.docID = docID;
	}

}
