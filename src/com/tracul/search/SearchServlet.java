package com.tracul.search;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.Hits;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;

import com.tracul.common.Constant;
import com.tracul.entity.News;

public class SearchServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public SearchServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {		
		
		request.setCharacterEncoding("UTF-8");
		String service = request.getParameter("service");
		int fieldId = 0;
		if(request.getParameter("filedId")!=null){
			fieldId = Integer.parseInt(request.getParameter("filedId"));
		}
		
		SearchFun search = new SearchFun();
		List<News> newsAll = null;
		
		//简单查询
		if(service.equals("SimpleQuery")){
			String keyWord = request.getParameter("keyWord");
			try {
				newsAll = search.simpleQuery(keyWord, fieldId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("keyWord", keyWord);
		}
		//boolean查询
		if(service.equals("Boolean")){
			String keyword1 = request.getParameter("keyWord1");
			String conditions1 = request.getParameter("condition1");
			String keyword2 = request.getParameter("keyWord2");
			String conditions2 = request.getParameter("condition2");
			
			try {
				newsAll =search.booleanQuery(keyword1, conditions1, keyword2, conditions2, fieldId);
			} catch (Exception e) {
				e.printStackTrace();
			} 
			request.setAttribute("keyWord", keyword1+" and "+ keyword2);
		}
		
		//短语查询
		if(service.equals("PhraseQuery")){
			String keyWord = request.getParameter("keyWord");
			try {
				newsAll = search.phraseQuery(keyWord, fieldId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("keyWord", keyWord);
		}
		
		//前缀查询
		if(service.equals("PrefixQuery")){
			String keyWord = request.getParameter("keyWord");
			try {
				newsAll = search.PrefixQuery(keyWord, fieldId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("keyWord", keyWord);
		}
		//时间范围查询
		if(service.equals("RangeQuery")){
			int startQueryString = 0;
			int endQueryString = 0;
			try {
				 startQueryString = Integer.parseInt(request.getParameter("startQueryString"));
				 endQueryString = Integer.parseInt(request.getParameter("endQueryString"));
				 newsAll = search.RangeQuery(startQueryString, endQueryString);
			} catch (Exception e) {
				e.printStackTrace();
			}

			request.setAttribute("keyWord", "from "+ startQueryString+" to "+ endQueryString);
		}
		//模糊查询
		if(service.equals("FazzyQuery")){
			String keyWord = request.getParameter("keyWord");
			try {
				newsAll = search.FazzyQuery(keyWord, fieldId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("keyWord", keyWord);
		}
		// 通配符查询 wildcardQuery
		if(service.equals("wildcardQuery")){
			String keyWord = request.getParameter("keyWord");
			try {
				newsAll = search.wildcardQuery(keyWord, fieldId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			request.setAttribute("keyWord", keyWord);
		}
		//多字段组合查询
		if(service.equals("jointQuery")){
			String txt1 = request.getParameter("txt1");
			String txt2 = request.getParameter("txt2");
			String txt3 = request.getParameter("txt3");
			try {
				newsAll = search.multiPhraseQuery(txt1,txt2,txt3);
			} catch (Exception e) {

				e.printStackTrace();
			}
			request.setAttribute("keyWord", txt1+" and "+ txt2 + "and" + txt3);
		}

		request.setAttribute("newsAll", newsAll);
		RequestDispatcher rd = request.getRequestDispatcher("/result.jsp");
	    rd.forward(request,response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
