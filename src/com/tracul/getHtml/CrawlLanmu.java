package com.tracul.getHtml;

import java.util.ArrayList;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class CrawlLanmu {

	/**
	 * @param args
	 */
	
	private String hostUrl = "http://www.zgctwh.com.cn/";
	private ArrayList<Lanmu> lanmuList = new ArrayList<Lanmu>();

	public ArrayList<Lanmu> getLanmuList() {
		return lanmuList;
	}

	public void setLanmuList(ArrayList<Lanmu> lanmuList) {
		this.lanmuList = lanmuList;
	}

	public String getHostUrl() {
		return hostUrl;
	}

	public void setHostUrl(String hostUrl) {
		this.hostUrl = hostUrl;
	}

	public void lanmu() {
          
        try{  
        	//使用一个parser类创建一个解释器
            Parser parser = new Parser(hostUrl);
            //设置编码格式
            parser.setEncoding("gb2312");
            // 获取需要的节点，参数是节点名和父节点名
            NodeFilter  tableFilter=new AndFilter(new TagNameFilter("table"),new HasParentFilter(new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "dhright")))); 
            NodeFilter trFilter = new AndFilter(new TagNameFilter("tr"),new HasParentFilter(tableFilter));
            NodeFilter tdFilter = new AndFilter(new TagNameFilter("td"),new HasParentFilter(trFilter));
            NodeFilter aFilter = new AndFilter(new TagNameFilter("a"),new HasParentFilter(tdFilter));
            NodeList nodesA = parser.extractAllNodesThatMatch(aFilter);
            //对节点内容进行分析，找出所有含有href的a标签，并获取内容
            for(int i = 0; i < nodesA.size() - 1; i++){
            	Node nodeA = nodesA.elementAt(i);
            	System.out.println(nodeA.toHtml());
            	if (nodeA instanceof LinkTag) {
            		LinkTag node = (LinkTag) nodeA;
            		String href = node.getAttribute("href");
            		System.out.println(href);
            		System.out.println(node.getFirstChild().toHtml());
            		String firstNews = firstNews(href);
            		Lanmu lanmu = new Lanmu();
            		String catId = href.substring(href.indexOf("catid") + 6, href.length());
            		System.out.println(catId);
            		lanmu.setCatId(catId);
            		String catName = node.getFirstChild().toHtml();
            		lanmu.setCatName(catName);
            		lanmu.setFirstUrl(firstNews);
            		lanmuList.add(lanmu);
            	}
            }
            catLanmu();
        }catch(Exception e){  
            e.printStackTrace();  
        }  
  
    }
	
	private void catLanmu() {
		// TODO Auto-generated method stub          
        try{  
            Parser parser = new Parser(hostUrl + "index.php?m=content&c=index&a=lists&catid=20");
            parser.setEncoding("gb2312");
    		ArrayList<Lanmu> li = catId();
    		for(Lanmu lanmu : li){
    			System.out.println(lanmu.getCatId() + "	" + lanmu.getCatName() + "	" + lanmu.getFirstUrl());
    		}
            NodeFilter newsbt2Filter = new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class","newsbt2"));
            NodeFilter nameFilter = new AndFilter(new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "newsbttxt")),new HasParentFilter(newsbt2Filter));
            NodeList nodesName = parser.extractAllNodesThatMatch(nameFilter);
            for(int i = 0; i < 12; i++){
            	Lanmu lanmu = new Lanmu();
            	String catName = nodesName.elementAt(i).getFirstChild().toHtml();
            	System.out.println(catName);
            	lanmu.setCatName(catName);
            	lanmu.setCatId(li.get(i).getCatId());
            	lanmu.setFirstUrl(li.get(i).getFirstUrl());
            	lanmuList.add(lanmu);
            }
        }catch(Exception e){  
            e.printStackTrace();  
        } 
		
	}

	private ArrayList<Lanmu> catId() {
		// TODO Auto-generated method stub
		ArrayList<Lanmu> li = new ArrayList<Lanmu>();
        try{  
            Parser parser = new Parser(hostUrl + "index.php?m=content&c=index&a=lists&catid=20");
            parser.setEncoding("gb2312");
            
            NodeFilter newsbt2Filter = new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class","newsbt2"));
            NodeFilter moreFilter = new AndFilter(new AndFilter(new TagNameFilter("div"), new HasAttributeFilter("class", "more")),new HasParentFilter(newsbt2Filter));
            NodeFilter aFilter = new AndFilter(new TagNameFilter("a"),new HasParentFilter(moreFilter));
        	NodeList nodesA = parser.extractAllNodesThatMatch(aFilter);
        	for(int i = 0; i < 12; i++){
            	Node nodeA = nodesA.elementAt(i);
            	System.out.println(nodeA.toHtml());
            	if (nodeA instanceof LinkTag) {
            		Lanmu lanmu = new Lanmu();
            		LinkTag node = (LinkTag) nodeA;
            		String href = node.getAttribute("href");
            		System.out.println(href);
            		String firstNews = CatFirstNews(href);
            		String cId = href.substring(href.indexOf("catid") + 6, href.length());
            		System.out.println(cId);
            		lanmu.setCatId(cId);
            		lanmu.setFirstUrl(firstNews);
            		li.add(lanmu);
            	}
        	}
        }catch(Exception e){  
            e.printStackTrace();  
        }
        return li;
	}

	private String firstNews(String href) {
		// TODO Auto-generated method stub
		String newsUrl = "";
		try {
			Parser parser = new Parser(hostUrl + href);
			System.out.println(hostUrl + href);
            parser.setEncoding("gb2312");
            NodeFilter ulFilter = new AndFilter(new TagNameFilter("ul"),new HasAttributeFilter("class","list lh24 f14"));
            //System.out.println(parser.extractAllNodesThatMatch(ulFilter).elementAt(0).toHtml());
            NodeFilter liFilter = new AndFilter(new TagNameFilter("li"),new HasParentFilter(ulFilter));
            NodeFilter aFilter = new AndFilter(new TagNameFilter("a"),new HasParentFilter(liFilter));
            NodeList nodesA = parser.extractAllNodesThatMatch(aFilter);
            Node nodeA = nodesA.elementAt(0);
        	System.out.println(nodeA.toHtml());
        	if (nodeA instanceof LinkTag) {
        		LinkTag node = (LinkTag) nodeA;
        		newsUrl = node.getAttribute("href");
        		System.out.println(newsUrl);
        		System.out.println(node.getFirstChild().toHtml());
        	}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newsUrl;
	}
	
	private String CatFirstNews(String href) {
		// TODO Auto-generated method stub
		String newsUrl = "";
		try {
			Parser parser = new Parser(href);
            parser.setEncoding("gb2312");
            NodeFilter ulFilter = new AndFilter(new TagNameFilter("ul"),new HasAttributeFilter("class","list lh24 f14"));
            //System.out.println(parser.extractAllNodesThatMatch(ulFilter).elementAt(0).toHtml());
            NodeFilter liFilter = new AndFilter(new TagNameFilter("li"),new HasParentFilter(ulFilter));
            NodeFilter aFilter = new AndFilter(new TagNameFilter("a"),new HasParentFilter(liFilter));
            NodeList nodesA = parser.extractAllNodesThatMatch(aFilter);
            Node nodeA = nodesA.elementAt(0);
        	System.out.println(nodeA.toHtml());
        	if (nodeA instanceof LinkTag) {
        		LinkTag node = (LinkTag) nodeA;
        		newsUrl = node.getAttribute("href");
        		System.out.println(newsUrl);
        		System.out.println(node.getFirstChild().toHtml());
        	}
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return newsUrl;
	}
	
	public static void main(String[] args) {
		CrawlLanmu cl = new CrawlLanmu();
//		cl.catLanmu();
		cl.lanmu();

		for(Lanmu lanmu :cl.getLanmuList()){
			System.out.println(lanmu.getCatId() + "	" + lanmu.getCatName() + "	" + lanmu.getFirstUrl());
		}
    }
	

}
