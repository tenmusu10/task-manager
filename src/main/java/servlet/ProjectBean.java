package servlet;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;

public class ProjectBean implements Serializable {
    ///private static final long serialVersionUID = 1L;

    private int projectId;
    private String projectName;
    private String description;
    private String deadline; // Stringに変更
    private String status;
    private int createdBy;
    private String createdByName;
    private Timestamp createdAt;

    // 1019追加
    private int totalTasks;      // 総タスク数
    private int doneTasks;       // 完了タスク数
    private int progressPercent; // 進捗率（数値）    
    // 1019追加ここまで
    
    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }

    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    // 1019追加   
    public int getTotalTasks() { return totalTasks; }
    public void setTotalTasks(int totalTasks) { this.totalTasks = totalTasks; }

    public int getDoneTasks() { return doneTasks; }
    public void setDoneTasks(int doneTasks) { this.doneTasks = doneTasks; }

    public int getProgressPercent() { return progressPercent; }
    public void setProgressPercent(int progressPercent) { this.progressPercent = progressPercent; }   
    // 1019追加ここまで  
    
    public static ArrayList<ProjectBean> selectAllList() throws SQLException {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        ArrayList<ProjectBean> projectlist = new ArrayList<>();
        
        try {
            conn = new ResourceFinder().getConnection();
            String sql = "SELECT p.*, "
                       + "(SELECT COUNT(*) FROM tasks t WHERE t.project_id = p.project_id AND t.status != '削除') AS total_tasks, "
                       + "(SELECT COUNT(*) FROM tasks t WHERE t.project_id = p.project_id AND t.status = '完了') AS done_tasks "
                       + "FROM projects p WHERE p.status != '削除'";
            st = conn.prepareStatement(sql);
            rs = st.executeQuery();
            
            while (rs.next()) {
                ProjectBean prj = new ProjectBean();
                prj.setProjectId(rs.getInt("project_id"));
                prj.setProjectName(rs.getString("project_name"));
                prj.setDescription(rs.getString("description"));
                prj.setDeadline(rs.getString("deadline"));
                
                int total = rs.getInt("total_tasks");
                int done = rs.getInt("done_tasks");
           
                prj.setTotalTasks(total); 
                prj.setDoneTasks(done);   
                int percent = 0;
             
                if (total == 0) {
                    prj.setStatus("0%"); 
                } else {
                    percent = (int) Math.round((double) done / total * 100);
                    prj.setStatus(percent + "%"); 
                }
                
                prj.setProgressPercent(percent); 
                
                prj.setCreatedBy(rs.getInt("created_by"));
                prj.setCreatedAt(rs.getTimestamp("created_at"));
                
                projectlist.add(prj); 
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return projectlist;
    }

    public static ProjectBean getProjectById(int projectId) {
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;
        ProjectBean project = null;

        try {
            conn = new ResourceFinder().getConnection();
            String sql = "SELECT * FROM projects WHERE project_id = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, projectId);
            rs = st.executeQuery();

            if (rs.next()) {
                project = new ProjectBean();
                project.setProjectId(rs.getInt("project_id"));
                project.setProjectName(rs.getString("project_name"));
                project.setDescription(rs.getString("description"));
                project.setDeadline(rs.getString("deadline"));
                project.setStatus(rs.getString("status"));
                project.setCreatedBy(rs.getInt("created_by"));
                project.setCreatedAt(rs.getTimestamp("created_at"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (st != null) st.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return project;
    }
    
    public static void insertData(ProjectBean prj) {
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = new ResourceFinder().getConnection();
            st = conn.prepareStatement(
                "INSERT INTO projects(project_id, project_name, description, deadline, status, created_by, created_at) "
                + "VALUES(?, ?, ?, ?, '未着手', ?, ?)");
            
            st.setInt(1, prj.getProjectId());
            st.setString(2, prj.getProjectName());
            st.setString(3, prj.getDescription()); 
            st.setString(4, prj.getDeadline());
            st.setInt(5, prj.getCreatedBy());
            st.setTimestamp(6, prj.getCreatedAt());
            
            st.executeUpdate();
            st.clearParameters();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    
    
    
    
    
    
    
    public static int selectuser(String loginid) {
    	//ログインしているユーザのuser_idを取得
    	  Connection conn = null;
          PreparedStatement st = null;
          ResultSet rs = null;
          
          int userid=0;
    	try {
            conn = new ResourceFinder().getConnection();
            String sql = "SELECT user_id FROM users WHERE login_id = ?";
            st = conn.prepareStatement(sql);
            st.setString(1, loginid);
            rs = st.executeQuery();
            rs.next();
            userid= rs.getInt("user_id");
           
    	}catch(Exception e) {
    		e.printStackTrace();
    	} finally {
            try {
                if (st != null) st.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
		return userid;
		
    }
    
    public static int prjid() {
    	//新規project_idを取得
    	 Connection conn = null;
         Statement st = null;
         ResultSet rs = null;
         int projectid=0;
         try {
             conn = new ResourceFinder().getConnection();
             String sql = "SELECT project_id FROM projects order by project_id desc ";
             st=conn.createStatement();
             rs = st.executeQuery(sql);
             rs.next();
             projectid=rs.getInt("project_id");
             
         }catch(SQLException e) {
        	 e.printStackTrace();
         }finally {
        	 try {
        		 if (st != null) st.close();
        		 if (conn != null) conn.close();
        	 } catch (SQLException e) {
        		 e.printStackTrace();
        	 }
         }return projectid;
    }
    










    /**
     * プロジェクトのステータスを「削除」に更新する（論理削除）
     * 独立したメソッドとして正しい位置に修正完了
     */
    public static void deleteProject(int projectId) {
        Connection conn = null;
        PreparedStatement st = null;
        try {
            conn = new ResourceFinder().getConnection();
            String sql = "UPDATE projects SET status = '削除' WHERE project_id = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, projectId);
            st.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (st != null) st.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }       
    }








    // --- ★1016追加：プロジェクトを編集して上書き保存するメソッド ---
       public static void updateProject(int projectId, String projectName, String deadline) {
           Connection conn = null;
           PreparedStatement st = null;
           try {
               conn = new ResourceFinder().getConnection();
               String sql = "UPDATE projects SET project_name = ?, deadline = ? WHERE project_id = ?";
               st = conn.prepareStatement(sql);
               st.setString(1, projectName);
               
               // 日付が空欄の場合はNULLをセット
               if (deadline == null || deadline.isEmpty()) {
                   st.setNull(2, java.sql.Types.DATE);
               } else {
                   st.setString(2, deadline);
               }
               
               st.setInt(3, projectId);
               st.executeUpdate();
               
           } catch (Exception e) {
               e.printStackTrace();
           } finally {
               try {
                   if (st != null) st.close();
                   if (conn != null) conn.close();
               } catch (Exception e) {
                   e.printStackTrace();
               }
           }
       
    }
}
