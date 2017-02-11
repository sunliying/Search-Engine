<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>高级检索</title>
    
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
    	<h2>高级检索</h2>
    	
    	<div id="FazzyQuery">
			<form action="SearchServlet?service=FazzyQuery" method="POST">
				<span> 模糊检索：</span><input type="text" name="keyWord" width="50px">
				<select name="filedId">
					<option value="0">标题</option>
					<option value="1">来源</option>
					<option value="2">时间</option>
					<option value="3">正文</option>
					<option value="4">摘要</option>
				</select> <input type="submit" value="检索" />
			</form>
		</div>
		<hr>
		
		<div id="wildcardQuery">
			<form action="SearchServlet?service=wildcardQuery" method="POST">
				<span> 通配符查询：</span><input type="text" name="keyWord" width="50px">
				<select name="filedId">
					<option value="0">标题</option>
					<option value="1">来源</option>
					<option value="2">时间</option>
					<option value="3">正文</option>
					<option value="4">摘要</option>
				</select> <input type="submit" value="检索" />
			</form>
		</div>
		<hr>
		<div id="prefix">
			<form action="SearchServlet?service=PrefixQuery" method="POST">
				<span> 前缀检索：</span><input type="text" name="keyWord" width="50px">
				<select name="filedId">
					<option value="0">标题</option>
					<option value="1">来源</option>
					<option value="2">时间</option>
					<option value="3">正文</option>
					<option value="4">摘要</option>
				</select> <input type="submit" value="检索" />
			</form>
		</div>
		<hr>
		<div id="boolean">
			<form action="SearchServlet?service=Boolean" method="POST">
				<span> 布尔检索：</span><input type="text" name="keyWord1" width="50px">
				<select name="condition1">
					<option value="0">MUST</option>
					<option value="1">MUST_NOT</option>
					<option value="2">SHOULD</option>
				</select> <input type="text" name="keyWord2" width="50px"> <select
					name="condition2">
					<option value="0">MUST</option>
					<option value="1">MUST_NOT</option>
					<option value="2">SHOULD</option>
				</select> <select name="filedId">
					<option value="0">标题</option>
					<option value="1">来源</option>
					<option value="2">时间</option>
					<option value="3">正文</option>
					<option value="4">摘要</option>
				</select> <input type="submit" value="检索" />
			</form>
		</div>
		<hr>
		<div id="range">
			<form action="SearchServlet?service=RangeQuery" method="POST">
				<span> 范围检索：</span>
				<input type="text" name="startQueryString" width="50px" placeholder="201"> 
				<input type="text" name="endQueryString" width="50px" placeholder="1213"> 
				<input type="submit" value="检索" />
			</form>
		</div>
		<hr>
		<div id="phrase">
			<form action="SearchServlet?service=PhraseQuery" method="POST">
				<span> 短语检索：</span><input type="text" name="keyWord" width="50px">
				<select name="filedId">
					<option value="0">标题</option>
					<option value="1">来源</option>
					<option value="2">时间</option>
					<option value="3">正文</option>
					<option value="4">摘要</option>
				</select> <input type="submit" value="检索" />
			</form>
		</div>
		<hr>
		<div id="joint">
			<form action="SearchServlet?service=jointQuery" method="POST">
				<span> 组合检索：</span>
				标题:<input type="text" name="txt1" id="txt1" style="height:30px;width:300px">
    			<br>
    			摘要:<input type="text" name="txt2" id="txt2" style="height:30px;width:300px">
    			<br>
    			正文:<input type="text" name="txt3" id="txt3" style="height:30px;width:300px">
    			<br>
				<input type="submit" value="检索" />
			</form>
		</div>
    </center>
  </body>
</html>
