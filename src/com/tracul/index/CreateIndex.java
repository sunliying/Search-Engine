package com.tracul.index;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.DateField;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.tracul.common.Constant;

public class CreateIndex{
	public static void main(String[] args){

		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		Directory dir;

		try {
			SmartChineseAnalyzer analyzer = new SmartChineseAnalyzer();
			IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
			dir = FSDirectory.open(Paths.get(Constant.INDEX_PATH));
			// 第一个参数是文件，不能是字符串
			//创建索引
			IndexWriter writer = new IndexWriter(dir,iwc);
			//连接数据库，从数据库中获取所有的数据
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/lucene?user=root&password=sunliying0213");
			stmt = conn.createStatement();
			rs = stmt.executeQuery("select * from html order by id");
			while (rs.next()) {
				//数据库中的每一个记录对应一个索引文件的文档
				//再根据字段建立索引字段
				Document doc = new Document();
				File file = new File(Constant.FILE_PATH + rs.getString("id") + ".txt");
				/**此处是相对lucene4.*之前的版本改动比较大的地方，不能再直接new Field，而是new IntField，StringField，TextField等，其中
			     * TextField默认分词，StringField默认不分词
			     * */
				// 添加基本文件信息和文件内容
				
				doc.add(new StringField("path", file.getPath(),Field.Store.YES));
				doc.add(new StringField("modified",DateField.timeToString(file.lastModified()),Field.Store.YES));
				FileInputStream in = new FileInputStream(file);
 				Reader reader = new BufferedReader(new InputStreamReader(in,"utf-8"));
				doc.add(new TextField("textReader",reader));
				
				// 添加文件内容信息
				StringField idFld = new StringField("id",rs.getString("id"),Field.Store.YES);
				StringField parentIdFld = new StringField("parentId",rs.getString("parent_id"),Field.Store.YES);
				StringField urlFld = new StringField("url", rs.getString("url"),Field.Store.YES);
				TextField titleFld = new TextField("title", rs.getString("title"),Field.Store.YES);
				TextField sourceFld = new TextField("source", rs.getString("source"),Field.Store.YES);
				StringField dateFld = new StringField("date", rs.getString("date"),Field.Store.YES);
				StringField commentFld = new StringField("comment", rs.getString("comment"),Field.Store.YES);
				TextField textFld = new TextField("text", rs.getString("text"),Field.Store.YES);
				String summary = "";
				if(rs.getString("summary")!=null){
					summary = rs.getString("summary");
				}
				TextField summaryFld = new TextField("summary", summary,Field.Store.YES);
				
				doc.add(idFld);
				doc.add(parentIdFld);
				doc.add(urlFld);
				doc.add(titleFld);
				doc.add(sourceFld);
				doc.add(dateFld);
				doc.add(commentFld);
				doc.add(textFld);
				doc.add(summaryFld);
				writer.addDocument(doc);
			}
			writer.close();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException ex) {
			ex.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
					rs = null;
				}
				if (stmt != null) {
					stmt.close();
					stmt = null;
				}
				if (conn != null) {
					conn.close();
					conn = null;
				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
	
	}
}