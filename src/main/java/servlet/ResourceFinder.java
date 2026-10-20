package servlet;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class ResourceFinder {
	private static DataSource dataSource = null; 
	public Connection getConnection() throws SQLException{
		if(dataSource == null) {
			try {
				Context context = new InitialContext();
				dataSource = (DataSource)context.lookup("java:comp/env/jdbc/TaskBoardDB");
			}catch(NamingException e) {
				throw new SQLException(e.getMessage());
			}
		}
		return dataSource.getConnection();
	}
}