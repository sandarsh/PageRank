package pagerank;

import java.util.HashSet;

public class Page implements Comparable<Page>{
	public String pageName;
	public HashSet<String> inLinks = new HashSet<String>();
	public boolean isSink = false;
	public int numberOfOutlinks = 0;
	public double pageRank = -1;
	public double newRank = -1;

	public Page(String name, HashSet<String> inLinks){
		this.pageName = name;
		this.inLinks = inLinks;
	}
	
	public String toString(){
		return this.pageName + " " + 
				this.inLinks.size() + " " +
				this.isSink + " " + 
				this.numberOfOutlinks + " " + 
				this.pageRank + " "+ 
				this.newRank + "";	
	}

	@Override
	public int compareTo(Page o) {
		// TODO Auto-generated method stub
		double ans = (o.pageRank - this.pageRank);
		if(ans > 0.0) return 1; 
		if(ans == 0.0) return 0;
		return -1;
	}
}
