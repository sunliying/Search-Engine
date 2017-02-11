package com.tracul.search;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.FuzzyQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.NumericRangeQuery;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.PrefixQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.search.RangeQuery;

import com.tracul.common.Constant;
import com.tracul.entity.News;


public class SearchFun {
	private static String searchField[] = { "title", "source","date","text","summary"};
	private News news;
	/**
	 * 使用indexReader方式创建初始搜索索引
	 * @return
	 * @throws IOException
	 */
	public static IndexSearcher initIndexSearcher() throws IOException{
		Directory dir = FSDirectory.open(Paths.get(Constant.INDEX_PATH));
		IndexReader reader = DirectoryReader.open(dir);
		IndexSearcher indexSearcher = new IndexSearcher(reader);
		return indexSearcher;
	}
	/**
	 * 查询高亮显示
	 * @param query
	 * @return
	 */
	public static Highlighter HightQuery(Query query){
		QueryScorer scorer = new QueryScorer(query);
		Fragmenter fragmenter = new SimpleSpanFragmenter(scorer);
		SimpleHTMLFormatter simhtmlform = new SimpleHTMLFormatter("<b><font color='red'>","</font></b>");
		Highlighter highlighter = new Highlighter(simhtmlform,scorer);
		highlighter.setTextFragmenter(fragmenter);
		return highlighter;
	}
	/**
	** 简单查询 
	*  简单查询使用 前台传入的查询字段进行检索，单独查询一个字段
	**/
	public List<News> simpleQuery(String queryString, int searchFieldId) throws Exception {
		// 根据查询字段建立中文分析器
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		QueryParser parser = new QueryParser(searchField[searchFieldId], analyzer);
		
		Query query = parser.parse(queryString);
		IndexSearcher indexSearcher = SearchFun.initIndexSearcher();
		TopDocs topDocs = indexSearcher.search(query, 100); //查询前100条语句
		SearchFun.HightQuery(query);
		List<News> newsAll = new ArrayList<News>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			news = new News(document.get("id"), document.get("title"),document.get("source"),
					document.get("date"),document.get("summary"),document.get("url"));
			newsAll.add(news);
		}

		return newsAll;
	}
	/**
	 * d
	 * boolean 查询
	 * 
	 * @throws Exception
	 */

	public List<News> booleanQuery(String preQueryString1,String conditions1,String preQueryString2,String conditions2, int searchFieldId) throws Exception {
		
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		QueryParser parser = new QueryParser(searchField[searchFieldId], analyzer);
		Query query1 = parser.parse(preQueryString1);
		Query query2 = parser.parse(preQueryString2);
		
		
		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
		if(conditions1.equals("0")){
			booleanQuery.add(query1, BooleanClause.Occur.MUST);
		}
		else if(conditions1.equals("1")){
			booleanQuery.add(query1, BooleanClause.Occur.MUST_NOT);
		}
		else {
			booleanQuery.add(query1, BooleanClause.Occur.SHOULD);
		}
		
		if(conditions2.equals("0")){
			booleanQuery.add(query2, BooleanClause.Occur.MUST);
		}
		else if(conditions2.equals("1")){
			booleanQuery.add(query2, BooleanClause.Occur.MUST_NOT);
		}
		else {
			booleanQuery.add(query2, BooleanClause.Occur.SHOULD);
		}
		
		IndexSearcher indexSearcher = SearchFun.initIndexSearcher();
		TopDocs topDocs = indexSearcher.search(booleanQuery.build(), 100);

		List<News> newsAll = new ArrayList<News>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			news = new News(document.get("id"), document.get("title"),document.get("source"),
					document.get("date"),document.get("summary"),document.get("url"));
			newsAll.add(news);
		}

		return newsAll;
	}
	/**
	 * 范围查询
	 * @throws Exception
	 */
	public List<News> RangeQuery(int startQueryString, int endQueryString) throws Exception {
		IndexSearcher indexSearcher = SearchFun.initIndexSearcher();
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		QueryParser parser = new QueryParser("id", analyzer);
		String queryString = "["+startQueryString+" TO "+endQueryString+"]";
		Query query = parser.parse(queryString);
		TopDocs topDocs = indexSearcher.search(query, 100);
		SearchFun.HightQuery(query);
		List<News> newsAll = new ArrayList<News>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			news = new News(document.get("id"), document.get("title"),document.get("source"),
					document.get("date"),document.get("summary"),document.get("url"));
			newsAll.add(news);
		}

		return newsAll;
	}
	/**
	 * 
	 * @param queryString
	 * @param searchFieldId
	 * @return
	 * @throws IOException
	 */
	public List<News> FazzyQuery (String queryString, int searchFieldId) throws IOException{
		IndexSearcher indexSearcher = SearchFun.initIndexSearcher();
        FuzzyQuery fuzzyQuery = new FuzzyQuery(new Term(searchField[searchFieldId], queryString),1);
        TopDocs topDocs = indexSearcher.search(fuzzyQuery, 100);
		List<News> newsAll = new ArrayList<News>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			news = new News(document.get("id"), document.get("title"),document.get("source"),
					document.get("date"),document.get("summary"),document.get("url"));
			newsAll.add(news);
		}

		return newsAll;
	}
	/**
	 * 
	 * @param queryString
	 * @param searchFieldId
	 * @return
	 * @throws IOException
	 */
	public List<News> wildcardQuery (String queryString, int searchFieldId) throws IOException{
		IndexSearcher indexSearcher = SearchFun.initIndexSearcher();
        Term term = new Term(searchField[searchFieldId], queryString);
        WildcardQuery wildcardQuery = new WildcardQuery(term);
        TopDocs topDocs = indexSearcher.search(wildcardQuery, 100);
		List<News> newsAll = new ArrayList<News>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			news = new News(document.get("id"), document.get("title"),document.get("source"),
					document.get("date"),document.get("summary"),document.get("url"));
			newsAll.add(news);
		}

		return newsAll;
	}
	

	/**
	 * 前缀查询
	 * 
	 * @throws Exception
	 */

	public List<News> PrefixQuery(String preQueryString, int searchFieldId) throws Exception {
		PrefixQuery query = new PrefixQuery(new Term(searchField[searchFieldId], preQueryString));
		IndexSearcher indexSearcher = SearchFun.initIndexSearcher();
		TopDocs topDocs = indexSearcher.search(query, 100);
		SearchFun.HightQuery(query);
		List<News> newsAll = new ArrayList<News>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			news = new News(document.get("id"), document.get("title"),document.get("source"),
					document.get("date"),document.get("summary"),document.get("url"));
			newsAll.add(news);
		}

		return newsAll;
	}

	/**
	 * 短语查询
	 * @param keyWord
	 * @param searchFieldId
	 * @return
	 * @throws Exception
	 */
	public List<News> phraseQuery(String keyWord, int searchFieldId) throws Exception {
		
		PhraseQuery phraseQuery = new PhraseQuery(searchField[searchFieldId],keyWord);
		IndexSearcher indexSearcher = SearchFun.initIndexSearcher();
		TopDocs topDocs = indexSearcher.search(phraseQuery, 100);
		SearchFun.HightQuery(phraseQuery);
		List<News> newsAll = new ArrayList<News>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			news = new News(document.get("id"), document.get("title"),document.get("source"),
					document.get("date"),document.get("summary"),document.get("url"));
			newsAll.add(news);
		}

		return newsAll;
	}
	/**
	 * 多字段查询
	 * @param keyWord
	 * @param searchFieldId
	 * @return
	 * @throws Exception
	 */
	public List<News> multiPhraseQuery(String stitle, String sabstract,String stext) throws Exception {
		IndexSearcher indexSearcher = SearchFun.initIndexSearcher();
		SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
		BooleanQuery.Builder booleanQuery = new BooleanQuery.Builder();
	    
    	if(!stitle.equals("")){
			QueryParser queryParser = new QueryParser("title",analyzer);  
			Query query = queryParser.parse(stitle);
			booleanQuery.add(query, BooleanClause.Occur.MUST);
    	}
    	if(!sabstract.equals("")){
			QueryParser queryParser = new QueryParser("summary",analyzer);  
			Query query = queryParser.parse(sabstract);
			booleanQuery.add(query, BooleanClause.Occur.MUST);	    		
    	}
    	if(!stext.equals("")){
			QueryParser queryParser = new QueryParser("text",analyzer);  
			Query query = queryParser.parse(stext);
			booleanQuery.add(query, BooleanClause.Occur.MUST);	    		
    	}

		TopDocs topDocs = indexSearcher.search(booleanQuery.build(), 100);
		List<News> newsAll = new ArrayList<News>();
		for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
			Document document = indexSearcher.doc(scoreDoc.doc);
			news = new News(document.get("id"), document.get("title"),document.get("source"),
					document.get("date"),document.get("summary"),document.get("url"));
			newsAll.add(news);
		}

		return newsAll;
	}
}
