package servlet;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginBean implements Serializable {
	private String loginId=null;
	private String password=null;
	
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public static LoginBean login(String loginname) throws SQLException{
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		LoginBean loginbean = null;
		
		try {
			loginbean = new LoginBean();
			conn = new ResourceFinder().getConnection();
			st = conn.prepareStatement("SELECT login_id,password FROM users WHERE login_id=?");
			st.setString(1, loginname);
			rs = st.executeQuery();
			if(rs.next()) {
			    loginbean.setLoginId(rs.getString("login_id"));
			    loginbean.setPassword(rs.getString("password"));
            }
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			try {
				if (rs != null) rs.close();
				if (st != null) st.close();
				if (conn != null) conn.close();
			}catch(SQLException e) {
				throw e;
			}
		}
		return loginbean; 
	}
}