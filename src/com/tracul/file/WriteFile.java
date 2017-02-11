package com.tracul.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import com.tracul.common.Constant;

public class WriteFile {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		writeFile();
	}

	private static void writeFile() {
		// TODO Auto-generated method stub
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;

		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/lucene?user=root&password=sunliying0213");
			stmt = conn.createStatement();
			//rs = stmt.executeQuery("select * from html where id='615' order by id");
			rs = stmt.executeQuery("select * from html order by id");
			while (rs.next()) {
				System.out.println(rs.getRow());
				String text = rs.getString("text");
				File f = new File(Constant.FILE_PATH + rs.getString("id") + ".txt");
				if(!f.exists()){
					try {
						f.createNewFile();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				Writer out = new BufferedWriter( new OutputStreamWriter(new FileOutputStream(f),"UTF-8"));
				/**
				 * FileOutputStream fs = new FileOutputStream(f);
                 *OutputStreamWriter ow = new OutputStreamWriter(fs,"UTF-8");
                 *BufferedWriter out = new BufferedWriter(ow);
				 */
				out.write(text);
				out.flush();
				out.close();
			}
			// System.out.println("Has Added Total: " + Fnamelist.length);
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
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
