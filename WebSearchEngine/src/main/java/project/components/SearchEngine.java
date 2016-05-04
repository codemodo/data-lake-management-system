package project.components;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import project.database.Document;
import project.database.DocumentJdbcTemplate;
import project.database.Edge;
import project.database.EdgeJdbcTemplate;
import project.database.Node;
import project.database.NodeJdbcTemplate;
import project.database.UserJdbcTemplate;

@Service
public class SearchEngine {
	
	@Autowired
	@Qualifier("docJdbcBean")
	DocumentJdbcTemplate docJdbc;
	
	@Autowired
	@Qualifier("nodeJdbcBean")
	NodeJdbcTemplate nodeJdbc;
	
	@Autowired
	@Qualifier("edgeJdbcBean")
	EdgeJdbcTemplate edgeJdbc;
	
	@Cacheable("testCache")
	public int doSearch() throws InterruptedException {
		Thread.sleep(4000);
		return 1;
	}
	
	public List<SingleWordSingleSearch> getNodesAndPrepareClasses(String word, char permLevel) {
		List<Document> docs = docJdbc.getDocsByTermAndPerm(word, permLevel);
		List<Node> nodes = nodeJdbc.getNodesByDocList(docs);
		List<Edge> edges = edgeJdbc.getEdgesByNodeList(nodes);
		
		List<SingleWordSingleSearch> swssList = new ArrayList<SingleWordSingleSearch>();
		for (Document doc : docs) {
			SingleWordSingleSearch swss = new SingleWordSingleSearch();
			swss.setDocument(doc);
			swss.setSearchTerm(word);
			HashSet<Integer> nodeIDSet = new HashSet<Integer>();
			//add nodes
			Iterator<Node> nodesIter = nodes.iterator();
			while(nodesIter.hasNext()) {
				Node node = nodesIter.next();
				if (node.getDocID() == doc.getId()) {
					swss.addNode(node);
					nodeIDSet.add(node.getId());
					nodesIter.remove();
				}
			}
			//add edges
			Iterator<Edge> edgeIter = edges.iterator();
			while (edgeIter.hasNext()) {
				Edge edge = edgeIter.next();
				if (nodeIDSet.contains(edge.getNode1ID())) {
					swss.addEdge(edge);
					edgeIter.remove();
				}
			}
			swssList.add(swss);
		}
		return swssList;
	}

	
	//setters used during testing
	public void setDocJdbc(DocumentJdbcTemplate docJdbc) {
		this.docJdbc = docJdbc;
	}

	public void setNodeJdbc(NodeJdbcTemplate nodeJdbc) {
		this.nodeJdbc = nodeJdbc;
	}

	public void setEdgeJdbc(EdgeJdbcTemplate edgeJdbc) {
		this.edgeJdbc = edgeJdbc;
	}
	
	
	
	

}
