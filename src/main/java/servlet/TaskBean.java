package servlet;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TaskBean implements Serializable {
    private int taskId;
    private int projectId;
    private String taskName;
    private String description;
    private String status;
    private int assigneeId;
    private String assigneeName; // ★追加：担当者の「名前」を表示するため
    private String deadline;
    private int createdBy;

    // --- ゲッター・セッター ---
    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }
    public int getProjectId() { return projectId; }
    public void setProjectId(int projectId) { this.projectId = projectId; }
    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getAssigneeId() { return assigneeId; }
    public void setAssigneeId(int assigneeId) { this.assigneeId = assigneeId; }
    public String getAssigneeName() { return assigneeName; } // ★追加
    public void setAssigneeName(String assigneeName) { this.assigneeName = assigneeName; } // ★追加
    public String getDeadline() { return deadline; }
    public void setDeadline(String deadline) { this.deadline = deadline; }
    public int getCreatedBy() { return createdBy; }
    public void setCreatedBy(int createdBy) { this.createdBy = createdBy; }

    // --- 1. 指定したプロジェクトのタスク一覧を取得 ---
    public static List<TaskBean> getTaskList(int projectIdParam) {
        List<TaskBean> taskList = new ArrayList<>();
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = new ResourceFinder().getConnection();
            // ★修正：usersテーブルを結合して、担当者の「名前」も一緒に持ってくる！
            String sql = "SELECT t.*, u.user_name FROM tasks t LEFT JOIN users u ON t.assignee_id = u.user_id WHERE t.project_id = ? AND t.status != '削除'";
            st = conn.prepareStatement(sql);
            st.setInt(1, projectIdParam);
            rs = st.executeQuery();

            while (rs.next()) {
                TaskBean task = new TaskBean();
                task.setTaskId(rs.getInt("task_id"));
                task.setProjectId(rs.getInt("project_id"));
                task.setTaskName(rs.getString("task_name"));
                task.setDescription(rs.getString("description"));
                task.setStatus(rs.getString("status"));
                task.setAssigneeId(rs.getInt("assignee_id"));
                task.setAssigneeName(rs.getString("user_name")); // ★追加：名前をセット
                task.setDeadline(rs.getString("deadline"));
                task.setCreatedBy(rs.getInt("created_by"));
                
                taskList.add(task);
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
        return taskList;
    }

    // --- 2. 編集用：タスクIDを指定して1件だけ取得 ---
    public static TaskBean getTaskById(int taskIdParam) {
        TaskBean task = null;
        Connection conn = null;
        PreparedStatement st = null;
        ResultSet rs = null;

        try {
            conn = new ResourceFinder().getConnection();
            String sql = "SELECT * FROM tasks WHERE task_id = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, taskIdParam);
            rs = st.executeQuery();

            if (rs.next()) {
                task = new TaskBean();
                task.setTaskId(rs.getInt("task_id"));
                task.setProjectId(rs.getInt("project_id"));
                task.setTaskName(rs.getString("task_name"));
                task.setDescription(rs.getString("description"));
                task.setStatus(rs.getString("status"));
                task.setAssigneeId(rs.getInt("assignee_id"));
                task.setDeadline(rs.getString("deadline"));
                task.setCreatedBy(rs.getInt("created_by"));
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
        return task;
    }

    // --- 3. 更新用：編集された内容でDBをUPDATE ---
    public static boolean updateTask(int taskId, String taskName, String deadline, String status, int assigneeId) {
        Connection conn = null;
        PreparedStatement st = null;
        boolean result = false;

        try {
            conn = new ResourceFinder().getConnection();
            String sql = "UPDATE tasks SET task_name = ?, deadline = ?, status = ?, assignee_id = ? WHERE task_id = ?";
            st = conn.prepareStatement(sql);
            st.setString(1, taskName);
            if (deadline == null || deadline.isEmpty()) {
                st.setNull(2, java.sql.Types.DATE);
            } else {
                st.setDate(2, java.sql.Date.valueOf(deadline)); // ★日付を安全に変換
            }
            st.setString(3, status);
            st.setInt(4, assigneeId);
            st.setInt(5, taskId);

            int rows = st.executeUpdate();
            if (rows > 0) result = true;
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
        return result;
    }

    // --- 4. 新規作成用：タスクをDBに登録 ---
    public static boolean insertTask(int projectId, String taskName, String deadline, String status, int assigneeId) {
        Connection conn = null;
        PreparedStatement st = null;
        boolean result = false;

        try {
            conn = new ResourceFinder().getConnection();
            
            // ★超重要：descriptionとcreated_byを必ずセットし、DBのエラーを回避！
            String sql = "INSERT INTO tasks (project_id, task_name, description, deadline, status, assignee_id, created_by) VALUES (?, ?, '', ?, ?, ?, ?)";
            st = conn.prepareStatement(sql);
            st.setInt(1, projectId);
            st.setString(2, taskName);
            
            // 日付が空欄の場合はNULLをセット
            if (deadline == null || deadline.isEmpty()) {
                st.setNull(3, java.sql.Types.DATE);
            } else {
                st.setDate(3, java.sql.Date.valueOf(deadline)); // ★日付を安全に変換
            }
            
            st.setString(4, status);
            st.setInt(5, assigneeId);
            st.setInt(6, assigneeId); // created_byにも担当者IDをセット

            int rows = st.executeUpdate();
            if (rows > 0) {
                result = true;
                System.out.println("★成功：タスクがDBに保存されました！");
            }
        } catch (Exception e) {
            System.out.println("★失敗：タスクの保存中にエラーが起きました↓↓");
            e.printStackTrace(); // 何が原因で保存できなかったかコンソールに表示
        } finally {
            try {
                if (st != null) st.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    // --- 5. 削除用：ステータスを「削除」に更新（論理削除） ---
    public static boolean deleteTask(int taskId) {
        Connection conn = null;
        PreparedStatement st = null;
        boolean result = false;

        try {
            conn = new ResourceFinder().getConnection();
            String sql = "UPDATE tasks SET status = '削除' WHERE task_id = ?";
            st = conn.prepareStatement(sql);
            st.setInt(1, taskId);

            int rows = st.executeUpdate();
            if (rows > 0) result = true;
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
        return result;
    }
}