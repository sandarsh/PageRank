package pagerank;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
//import java.util.Map;

public class PageRank {
	
	static private double perplexity;
	static private double count = 0;
	
	public HashSet<Page> loadAllPages(File graphFile){
		try{
			HashSet<Page> p = new HashSet<Page>();
			FileInputStream f = new FileInputStream(graphFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(f));
			String lineItem = null;
			while ((lineItem = br.readLine()) != null) {
				String[] lineSplit = lineItem.split(" ");
				int l = lineSplit.length;
				String pageName = lineSplit[0];
				HashSet<String> temp = new HashSet<String>();
				for(int i = 1;i<l;i++){
					temp.add(lineSplit[i]);
				}
				Page newPage = new Page(pageName, temp);
				p.add(newPage);
			}
			br.close();
			p.toString();
			return p;
		}
		catch(IOException e){
			e.printStackTrace();
		}
		return null;
	}
		
	public HashSet<Page> loadAllSinkNodesAndCount(HashSet<Page> P, HashMap<String, Page> lookup){
		HashSet<Page> s = new HashSet<Page>();
		for(Page p:P){
			for(String parent:p.inLinks){
				lookup.get(parent).numberOfOutlinks ++;
			}
		}
		for(Page pg:P){
			if(pg.numberOfOutlinks == 0){
				pg.isSink = true;
				s.add(pg);
			}
		}
		return s;
	}
			
	private boolean isConverged(HashSet<Page> P){
		double entropy = 0;
		double perplexity = 0;
		for (Page pg:P){
			entropy += ( pg.pageRank * ( Math.log(pg.pageRank) / Math.log(2) ) );
		}
		perplexity = Math.pow(2.0 ,(-1 * entropy));
		//System.out.println(perplexity);
		double diff = Math.abs(perplexity - PageRank.perplexity);
		if(diff < 1){
			PageRank.count ++; 
		}
		else{
			PageRank.count = 0;
		}
		if(count >= 5){
			return true;
		}
		PageRank.perplexity = perplexity;
		return false;
	}
	
	public HashSet<Page> rank(HashSet<Page> P, HashSet<Page> S, double d, HashMap<String, Page> lookup){
		double N = P.size();
		//System.out.println(N);
		for(Page p:P){
			p.pageRank = 1/N;
		}
		double entropy = 0;
		for (Page pg:P){
			entropy += ( pg.pageRank * ( Math.log(pg.pageRank) / Math.log(2) ) );
		}
		PageRank.perplexity = Math.pow(2.0 ,(-1 * entropy));
		//System.out.println(P.toString());
		//int count = 0;
		//int size =lookup.get("Main_Page").inLinks.size();
		//System.out.println(size);
		while(!isConverged(P)){
			double sinkPR = 0;
			for(Page s:S){
				//System.out.println("sink ran");
				sinkPR = s.pageRank + sinkPR;
			}
			for(Page p:P){
				p.newRank = ( 1.0 - d ) / N;
				p.newRank += ( (d * sinkPR) / N );
				for (String q:p.inLinks){
					p.newRank += ( d * (lookup.get(q).pageRank / lookup.get(q).numberOfOutlinks) );
				}
			}
			for(Page p:P){
				p.pageRank = p.newRank;
			}
//			if(count == 33){
//				break;
//			}
			//System.out.println("Iteration" + count);
			//count ++ ;
		}
		//System.out.println(P.toString());
		return P;
	}	
}


