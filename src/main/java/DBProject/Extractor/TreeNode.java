package DBProject.Extractor;

public class TreeNode {
	
	int nodeID;
	String k;
	String v;
	int docID;
	
	public TreeNode(int nodeID, String key, String value, int docID){
		this.nodeID = nodeID;
		this.k = key;
		this.v = value;
		this.docID = docID;
	}

}
