package com.formation.project.computerDatabase.conf;

import java.io.File;  
import java.io.FileReader;  
import java.io.IOException;

import java.sql.Connection;
import java.sql.SQLException;  

import javax.servlet.ServletContext;

import com.formation.project.computerDatabase.dao.DaoFactory;
import com.formation.project.computerDatabase.dao.DataSourceFactory;

public final class DBInit {

	public static void initDatabase(ServletContext context) throws SQLException  
	{  
		Connection conn	  = DataSourceFactory.INSTANCE.getConn();
		DBScriptRunner sr = new DBScriptRunner(conn,false,false);


		try {			
			sr.runScript(new FileReader(new File(context.getRealPath("WEB-INF/sql/initDBStructure.sql"))));	
			sr.runScript(new FileReader(new File(context.getRealPath("WEB-INF/sql/initDefaultEntries.sql"))));
			
		} catch (IOException e) {
			System.out.println("Error in conf.DBInit: " + e.getMessage());
		} finally {
			DataSourceFactory.closeConn(conn);
		}


	}
}