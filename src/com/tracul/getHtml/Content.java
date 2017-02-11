package com.tracul.getHtml;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.filters.AndFilter;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.HasParentFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.Span;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.NodeFilter;

public class Content {
	
	private String id;
	private String parentId;
	private String url;
	private String title;
	private String source;
	private String date;
	private String commentCount;
	private String clickCount;
	private String summary;
	private String text;
	private String preUrl;
	private String nextUrl;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public String getClickCount() {
		return clickCount;
	}

	public void setClickCount(String clickCount) {
		this.clickCount = clickCount;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getPreUrl() {
		return preUrl;
	}

	public void setPreUrl(String preUrl) {
		this.preUrl = preUrl;
	}

	public String getNextUrl() {
		return nextUrl;
	}

	public void setNextUrl(String nextUrl) {
		this.nextUrl = nextUrl;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lucene?user=root&password=sunliying0213");
			

			CrawlLanmu cl = new CrawlLanmu();
			cl.lanmu();
			for(Lanmu lanmu :cl.getLanmuList()){
				System.out.println(lanmu.getCatId() + "	" + lanmu.getCatName() + "	" + lanmu.getFirstUrl());
			}
			ArrayList<Lanmu> lanmuList = cl.getLanmuList();

			
			for(int i = 0; i < lanmuList.size(); i++){
				crawPage(lanmuList.get(i).getFirstUrl(),conn);
			}
			
		} catch(ClassNotFoundException e){
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		finally{
			try {
				if(conn != null){
				conn.close();
				conn = null;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private static void crawPage(String url, Connection conn) {
		// TODO Auto-generated method stub
		Content content = new Content();
		content.setUrl(url);
		String cId = url.substring(url.indexOf("&id") + 4, url.length());
		content.setId(cId);
		String pId = url.substring(url.indexOf("&catid") + 7 ,url.indexOf("&id"));
		content.setParentId(pId);
		content.crawlTitle(url,content);
		content.crawlSpan(url,content);
		content.crawlComment(url,content);
		content.crawlSummary(url,content);
		content.crawlText(url,content);
		content.crawlPre(url,content);
		
		PreparedStatement pstmt;
		try {
			String sql = "insert into html(id,parent_id,url,title,source,date,comment,summary,text,pre_url,next_url) values(?,?,?,?,?,?,?,?,?,?,?)";
			pstmt = conn.prepareStatement(sql);
			
			pstmt.setString(1, content.getId());
			pstmt.setString(2, content.getParentId());
			pstmt.setString(3, content.getUrl());
			pstmt.setString(4, content.getTitle());
			pstmt.setString(5, content.getSource());
			pstmt.setString(6, content.getDate());
			pstmt.setString(7, content.getCommentCount());
			pstmt.setString(8, content.getSummary());
			pstmt.setString(9, content.getText());
			pstmt.setString(10, content.getPreUrl());
			pstmt.setString(11, content.getNextUrl());
			
			pstmt.executeUpdate();
	        pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		if(!(content.getPreUrl().equals("javascript:alert('最后一页');"))){
			crawPage(content.getPreUrl(),conn);
		}
	}

	private Content crawlPre(String url, Content content) {
		Parser parser;
		try {
			parser = new Parser(url);
	        parser.setEncoding("gb2312");
	        NodeFilter filter = new AndFilter(new TagNameFilter("p"),new HasAttributeFilter("class","f14"));
	        NodeFilter aFilter = new AndFilter(new TagNameFilter("a"),new HasParentFilter(filter));
	        NodeList nodes = parser.extractAllNodesThatMatch(aFilter);
	        Node pre = nodes.elementAt(0);
	        LinkTag preNode = (LinkTag) pre;
	        String preUrl = preNode.getAttribute("href");
	        preUrl = preUrl.trim();
	        Node next = nodes.elementAt(1);
	        LinkTag nextNode = (LinkTag) next;
	        String nextUrl = nextNode.getAttribute("href");
        	nextUrl = nextUrl.trim();
        	content.setPreUrl(preUrl);
        	content.setNextUrl(nextUrl);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
		
	}
	
	private Content crawlText(String url, Content content) {
		Parser parser;
		try {
			parser = new Parser(url);
	        parser.setEncoding("gb2312");
	        NodeFilter Filter = new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class","content"));
	        NodeList nodes = parser.extractAllNodesThatMatch(Filter);
	        Node node = nodes.elementAt(0);
	        if(node!=null){
	        String contentText =  node.toHtml();
	        String text = replaceMore(contentText);
	        text= text.trim();
	        text = text.replaceAll("&nbsp;", "");
	        text = text.length()>10000?text.substring(0, 10000):text;
        	content.setText(text);
	        }
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
		
	}

	private String replaceMore(String contentText) {
		// TODO Auto-generated method stub
		String text = contentText;
		while(text.contains("<")){
			int l = text.indexOf('<');
			int g = text.indexOf('>');
			text =  text.substring(0, l) + text.substring(g + 1, text.length());
		}
		return text;
	}

	private String crawlClick(String url) {
		Parser parser;
		try {
			parser = new Parser(url);
	        parser.setEncoding("gb2312");
	        NodeFilter Filter = new AndFilter(new TagNameFilter("span"),new HasAttributeFilter("id","hits"));
	        NodeList nodes = parser.extractAllNodesThatMatch(Filter);
	        Span span = (Span) nodes.elementAt(0);
	        clickCount = span.getFirstChild().toHtml();
        	System.out.println(clickCount);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}

	private Content crawlComment(String url, Content content) {
		Parser parser;
		try {
			parser = new Parser(url);
	        parser.setEncoding("gb2312");
	        NodeFilter Filter = new AndFilter(new TagNameFilter("a"),new HasAttributeFilter("id","comment"));
	        NodeList nodes = parser.extractAllNodesThatMatch(Filter);
	        Node node = nodes.elementAt(0);
        	commentCount = node.getFirstChild().toHtml();
        	content.setCommentCount(commentCount);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
		
	}

	private Content crawlSpan(String url, Content content) {
		// TODO Auto-generated method stub
		Parser parser;
		try {
			parser = new Parser(url);
	        parser.setEncoding("gb2312");
	        NodeFilter articleFilter = new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("id","Article"));
	        NodeFilter hFilter = new AndFilter(new TagNameFilter("h1"),new HasParentFilter(articleFilter));
	        NodeFilter spanFilter = new AndFilter(new TagNameFilter("span"),new HasParentFilter(hFilter));
	        NodeList nodesSpan = parser.extractAllNodesThatMatch(spanFilter);
	        Node nodeSpan = nodesSpan.elementAt(0);
        	String span = nodeSpan.getFirstChild().toHtml();
        	System.out.println(span);
        	String str[] = span.split("&nbsp;&nbsp;&nbsp;");
        	date = str[0];
        	source = str[1].substring(3, str[1].length());
        	content.setDate(date);
        	content.setSource(source);
        	
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}
	
	private Content crawlSummary(String url, Content content) {
		// TODO Auto-generated method stub
		Parser parser;
		try {
			parser = new Parser(url);
	        parser.setEncoding("gb2312");
	        NodeFilter summaryFilter = new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("class","summary"));
	        NodeList nodes = parser.extractAllNodesThatMatch(summaryFilter);
	        Node node = nodes.elementAt(0);
	        if(node!=null){
	        	summary = node.getFirstChild().toHtml();
	        	summary = summary.trim();
	        	summary = summary.length()>500?summary.substring(0, 500):summary;
	        	content.setSummary(summary);
	        }
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}

	private Content crawlTitle(String url, Content content) {
		// TODO Auto-generated method stub

		Parser parser;
		try {
			parser = new Parser(url);
	        parser.setEncoding("gb2312");
	        NodeFilter articleFilter = new AndFilter(new TagNameFilter("div"),new HasAttributeFilter("id","Article"));
	        NodeFilter hFilter = new AndFilter(new TagNameFilter("h1"),new HasParentFilter(articleFilter));
	        NodeList nodesH = parser.extractAllNodesThatMatch(hFilter);
	        Node nodeH = nodesH.elementAt(0);
        	title = nodeH.getFirstChild().toHtml();
        	title = title.trim();
        	content.setTitle(title);
		} catch (ParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return content;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
