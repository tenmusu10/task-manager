package servlet;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class UserBean implements Serializable {
    private int userId;
    private String loginId;
    private String userName;

    // --- ゲッター・セッター ---
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getLoginId() { return loginId; }
    public void setLoginId(String loginId) { this.loginId = loginId; }

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    // --- DB操作：登録されている全ユーザーの一覧を取得する（担当者プルダウン用） ---
    public static List<UserBean> getAllUsers() {
        List<UserBean> userList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = new ResourceFinder().getConnection();
            String sql = "SELECT user_id, login_id, user_name FROM users ORDER BY user_id ASC";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();

            while (rs.next()) {
                UserBean user = new UserBean();
                user.setUserId(rs.getInt("user_id"));
                user.setLoginId(rs.getString("login_id"));
                user.setUserName(rs.getString("user_name"));
                userList.add(user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return userList;
    }
}