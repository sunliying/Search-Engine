<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page language="java" import="org.apache.lucene.document.Document" %>
<%@ page import="org.apache.lucene.search.ScoreDoc" %>
<%@ page import="org.apache.lucene.search.TopDocs" %>
<%@ page import="org.apache.lucene.search.IndexSearcher" %>
<%@ page import="com.tracul.search.SearchFun" %>
<%@ page import="com.tracul.entity.News" %>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>Result</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
  	<%
  	String keywords = (String)request.getAttribute("keyWord"); 
   	List<News> newsAll = (List<News>)request.getAttribute("newsAll");
   	int docLen = newsAll.size();
	Iterator<News> iter = newsAll.iterator();
	News news;
	
   	%>
	<h3>检索关键词:<%=keywords %></h3>
	<h3>结果共有<%=docLen %>条</h3>
	  <table border="1">
	  	<tr>
	  	<th>标题</th>
	  	<th>id</th>
	  	<th>来源</th>
	  	<th>时间</th>
	  	<th>总结</th>
	  	</tr>
	  	
   	<%
   	while(iter.hasNext()){
		news = (News)iter.next();
		
   		%>
	     <tr>
	     	<td><a href="<%=news.getUrl() %>"><%=news.getTitle() %></a><td>
	     	<td><%=news.getId() %><td>
	     	<td><%=news.getSource() %><td>
	     	<td><%=news.getDate() %><td>
	     	<td><%=news.getSummary() %><td>
	     </tr>
	    <%
   	}
     %>
     </table>
  </body>
</html>
