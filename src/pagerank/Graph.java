package pagerank;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.Queue;
import java.util.Scanner;
//import java.util.Set;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Graph {
	
	private File url_list;
	private String root_path;
	
	public Graph(File list, String path){
		this.url_list = list;
		this.root_path = path;
	}
	
	private String extractArticleName(String url){
		int index = url.lastIndexOf(this.root_path);
		return url.substring(index+5);
	}
	
	private String extractUrlPath(String url){
		int index = url.lastIndexOf("wiki/");
		return url.substring(index);
	}
	
	private static boolean is_valid(String url){
		if(is_prefix_valid(url) && is_not_administrative(url)){
			return true;
		}
		return false;
	}
	
	//Checks if prefix is valid
	private static boolean is_prefix_valid(String link){
		String prefix = "/wiki/";
		boolean ans = link.startsWith(prefix);
		return ans;
	}
	
	//Checks if link is administrative
	private static boolean is_not_administrative(String link){
		int original_len = link.length();
		int new_len = link.replace(":", "").length();
		if(original_len != new_len){
			return false;
		}
		return true;
	}
	
	private static void writeEntryToFile(String key, HashSet<String> value){
		try{
			PrintWriter pw = new PrintWriter(new FileOutputStream(new File("G1.txt"),true));
			pw.print(key);
			for(String parent:value){
				pw.print(" " + parent);
			}
			pw.println();
			pw.close();
		}
		catch (IOException ioe){
			ioe.printStackTrace();
		}
	}
	
	private static String truncate_url(String url){
		int ind = url.indexOf("#");
		if(ind<0){
			return url;
		}
		else{
			url = url.substring(0, ind);
			return url;
		}
	}
	
	
	public void buildInlinkGraph(){
		Map<String,HashSet<String>> graph = new HashMap<String, HashSet<String>>();
		
		
		try{
			FileInputStream fis = new FileInputStream(this.url_list);
			BufferedReader br = new BufferedReader(new InputStreamReader(fis));
			String crawled_url = null;
			while ((crawled_url = br.readLine()) != null) {
				String key = extractArticleName(crawled_url);
				//System.out.println(key);
				graph.put(key,null);
			}
			br.close();
		}
		catch(IOException e){
			e.printStackTrace();
		}
		
		try{
			FileInputStream fins = new FileInputStream(this.url_list);
			BufferedReader brd = new BufferedReader(new InputStreamReader(fins));
			String crawled_url = null;
			int count = 1;
			int total = 1001;
			while ((crawled_url = brd.readLine()) != null){
				int completed = ( count * 100 / total );
				System.out.print(completed + "% completed\r");
				count++;
				String path = extractUrlPath(crawled_url);
				String page = path + ".html";
				String article_name = extractArticleName(path); 
				if(page.equals("wiki/M/s.html")){
					page = "wiki/M_s.html";
				}
				
				
				File page_file = new File(page);
				Scanner scan = new Scanner(page_file);
				String content = scan.useDelimiter("\\Z").next();
				Document doc = Jsoup.parse(content);
				Elements links = doc.select("a[href]");
				scan.close();
				
				
				for(Element link:links){
					String url = link.attr("href");	
					//cleaning
					url = truncate_url(url);
					//System.out.println(url);
					if(is_valid(url)){
						//System.out.println(url);
						String key = extractArticleName(url);
						if(graph.containsKey(key)){
							if(graph.get(key) == null){
								HashSet<String> values = new HashSet<String>();
								values.add(article_name);
								graph.put(key, values);
							}
							else{
								HashSet<String> values = graph.get(key);
								values.add(article_name);
								graph.put(key, values);
							}
						}	
					}
				}
//				if(count == 1){
//					break;
//				}
				
			}
			brd.close();
			for(String url: graph.keySet()){
				writeEntryToFile(url, graph.get(url));
			}
		}
		catch(IOException | StringIndexOutOfBoundsException e){
			e.printStackTrace();
		}
		System.out.print("\r100% completed");
	}
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
