package pagerank;

import java.util.List;
import java.util.ArrayList;
//import java.util.Arrays;
import java.util.Collections;
import java.io.File;
import java.util.HashMap;
//import java.io.IOException;
import java.util.HashSet;
//import java.util.Map;

public class Main {
	
	public static void main(String[] args) {
//		String filename = "urls_crawled.txt";
//		String path = new String("wiki/");
//		File file = new File(filename);		
//		
//		Graph graph = new Graph(file, path);
//		graph.buildInlinkGraph(); //creates G1.txt
//		
		//All operations to be done on G1 or G2 from here onwards
		//String graphFileName = "ranktest.txt";
		//String graphFileName = "G1.txt";
		String graphFileName = "G2.txt";
		
		
		File graphFile = new File(graphFileName);
		HashSet<Page> p = new HashSet<Page>();
		HashSet<Page> s = new HashSet<Page>();
		HashSet<Page> rankedP = new HashSet<Page>();
		HashMap<String, Page> lookup = new HashMap<String, Page>();
		double d = 0.85;
		
		PageRank g = new PageRank();
		p = g.loadAllPages(graphFile);
		int n = p.size();
		System.out.println("Running page rank on "+ n + " nodes...");
		for(Page page:p){
			lookup.put(page.pageName, page);
		}
		s = g.loadAllSinkNodesAndCount(p, lookup);
		//System.out.println(p.toString());
		rankedP = g.rank(p,s,d, lookup);
		//System.out.println(rankedP.toString());
		List<Page> rankedPageList = new ArrayList<Page>(rankedP);
		//System.out.println(rankedPageList.toString());
		Collections.sort(rankedPageList);
		if (n > 50){
			n = 50;
		}
		for(int i =0; i<n; i++){	
			System.out.println(rankedPageList.get(i).toString());
		}
		double sum = 0.0;
		for (Page page: rankedP){
			sum += page.newRank;
		}
		System.out.println("Combined probability (for verification) : " + sum);
	}
}
