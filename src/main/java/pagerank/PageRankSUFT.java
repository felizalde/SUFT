package pagerank;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;

import core.UThread;
import searcher.Response;
import edu.uci.ics.jung.algorithms.scoring.PageRank;
import edu.uci.ics.jung.graph.DirectedSparseGraph;


public class PageRankSUFT {
		private PageRank ranking;
		private DirectedSparseGraph graph;
		private ComparatorRank cmp;


		public PageRankSUFT(){
				this.graph = new DirectedSparseGraph();
				this.cmp = new ComparatorRank(this);
			}

		public void createGraph(String path) throws IOException{
	    	FileInputStream fstream = new FileInputStream(path);
	    	DataInputStream in = new DataInputStream(fstream);
	    	BufferedReader buffer = new BufferedReader(new InputStreamReader(in));
	    	String strLine;
	    	while ((strLine = buffer.readLine()) != null)   {
	    			String[] partes = strLine.split(":");
	    			if (!graph.containsVertex(partes[0])){
	    					graph.addVertex(partes[0]);
	    			}
	    			if (!graph.containsVertex(partes[1])){
	    					graph.addVertex(partes[1]);
	    			}
	    			graph.addEdge((partes[0]+" -> "+partes[1]), partes[0],partes[1]);
	    	}
				graph.addVertex("unlinked")
	    	in.close();
			}

		public void evaluate(double alpha){
				// alpha is the random jump probability.
				this.ranking = new PageRank(graph, alpha);
				this.ranking.evaluate();
			}

		public Response sort(Response response){
				List<UThread> threads = new ArrayList<>();
				threads.addAll(response.getItems());
				Collections.sort(threads, cmp);
				Response sorted = new Response(response.getQuery());
				sorted.setItems(threads);
				return sorted;
			}

		public Double getScore(String vertex){
			if (this.graph.containsVertex(vertex)){
				return (Double)this.ranking.getVertexScore(vertex);
			}else{
				//HACK.
				return (Double)this.ranking.getVertexScore("unlinked");
			}


}
