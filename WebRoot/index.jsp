<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>简单检索</title>
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
    <center>
    	<h1>中国传统文化网搜索引擎</h1>
    	<div style="height: 400px;width: 800px;">
	    	<div id="dim">
				<form action="SearchServlet?service=SimpleQuery" method="POST">
					<span> 简单检索：</span>
					<input type="text" name="keyWord" width="50px">
					<select name="filedId">
						<option value="0">标题</option>
						<option value="1">来源</option>
						<option value="2">时间</option>
						<option value="3">正文</option>
						<option value="4">摘要</option>
					</select> <input type="submit" value="检索" />
				</form>
			</div>
		
    		<div style="height:30px;width:800px;"></div>
    		<a href="./jiansuo.jsp">高级检索</a>
    		<div style="height: 100px;width: 800px; border: 1px;"></div>
    	</div>
    </center>
  </body>
</html>
