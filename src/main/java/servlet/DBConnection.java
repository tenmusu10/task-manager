package servlet;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class DBConnection {
    public static Connection getConnection() throws SQLException {
        try {
            InitialContext context = new InitialContext();
            DataSource ds = (DataSource) context.lookup("java:comp/env/jdbc/task_board");
            return ds.getConnection();
        } catch (NamingException e) {
            throw new SQLException("データベースの取得に失敗しました", e);
        }
    }
}