package servlet;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskBean implements Serializable {
    private int taskId;
    private int projectId;
    private String taskName;
    private String description;
    private String status;
    private int assigneeId;
    private String assigneeName; // ★追加：担当者の「名前」を表示するため（複数の場合は「、」区切り）
    private List<Integer> assigneeIds = new ArrayList<>(); // ★追加：複数担当者のユーザーID
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
    public List<Integer> getAssigneeIds() { return assigneeIds; } // ★追加
    public void setAssigneeIds(List<Integer> assigneeIds) { this.assigneeIds = assigneeIds; } // ★追加
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
            rs.close();
            st.close();

            // ★追加：task_assignees から担当者名をまとめて取得し「、」区切りにする
            //        （task_assignees に登録が無い古いタスクは assignee_id の名前のまま）
            String sql2 = "SELECT ta.task_id, u.user_name FROM task_assignees ta "
                        + "JOIN users u ON ta.user_id = u.user_id "
                        + "JOIN tasks t ON ta.task_id = t.task_id "
                        + "WHERE t.project_id = ? ORDER BY ta.task_id, u.user_id";
            st = conn.prepareStatement(sql2);
            st.setInt(1, projectIdParam);
            rs = st.executeQuery();
            Map<Integer, String> names = new HashMap<>();
            while (rs.next()) {
                int id = rs.getInt("task_id");
                String name = rs.getString("user_name");
                names.put(id, names.containsKey(id) ? names.get(id) + "、" + name : name);
            }
            for (TaskBean task : taskList) {
                if (names.containsKey(task.getTaskId())) {
                    task.setAssigneeName(names.get(task.getTaskId()));
                }
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

            // ★追加：担当者（複数）のIDを取得。未登録の古いタスクは assignee_id を使う
            if (task != null) {
                rs.close();
                st.close();
                st = conn.prepareStatement("SELECT user_id FROM task_assignees WHERE task_id = ? ORDER BY user_id");
                st.setInt(1, taskIdParam);
                rs = st.executeQuery();
                while (rs.next()) {
                    task.getAssigneeIds().add(rs.getInt("user_id"));
                }
                if (task.getAssigneeIds().isEmpty() && task.getAssigneeId() > 0) {
                    task.getAssigneeIds().add(task.getAssigneeId());
                }
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
    // ★修正：担当者を複数受け取る（assignee_id 列には先頭の担当者を入れる）
    public static boolean updateTask(int taskId, String taskName, String deadline, String status, int[] assigneeIds) {
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
            st.setInt(4, assigneeIds[0]);
            st.setInt(5, taskId);

            int rows = st.executeUpdate();
            if (rows > 0) {
                saveAssignees(conn, taskId, assigneeIds); // ★追加
                result = true;
            }
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
    // ★修正：担当者を複数受け取る（assignee_id 列には先頭の担当者を入れる）
    public static boolean insertTask(int projectId, String taskName, String deadline, String status, int[] assigneeIds) {
        Connection conn = null;
        PreparedStatement st = null;
        boolean result = false;

        try {
            conn = new ResourceFinder().getConnection();
            
            // ★超重要：descriptionとcreated_byを必ずセットし、DBのエラーを回避！
            String sql = "INSERT INTO tasks (project_id, task_name, description, deadline, status, assignee_id, created_by) VALUES (?, ?, '', ?, ?, ?, ?)";
            st = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // ★修正：採番された task_id を受け取る
            st.setInt(1, projectId);
            st.setString(2, taskName);
            
            // 日付が空欄の場合はNULLをセット
            if (deadline == null || deadline.isEmpty()) {
                st.setNull(3, java.sql.Types.DATE);
            } else {
                st.setDate(3, java.sql.Date.valueOf(deadline)); // ★日付を安全に変換
            }
            
            st.setString(4, status);
            st.setInt(5, assigneeIds[0]);
            st.setInt(6, assigneeIds[0]); // created_byにも担当者IDをセット

            int rows = st.executeUpdate();
            if (rows > 0) {
                // ★追加：採番された task_id で担当者を登録
                ResultSet keys = st.getGeneratedKeys();
                if (keys.next()) {
                    saveAssignees(conn, keys.getInt(1), assigneeIds);
                }
                keys.close();
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

    // --- ★追加：担当者（複数）を task_assignees に保存（いったん削除してから登録し直す） ---
    private static void saveAssignees(Connection conn, int taskId, int[] assigneeIds) throws SQLException {
        try (PreparedStatement del = conn.prepareStatement("DELETE FROM task_assignees WHERE task_id = ?")) {
            del.setInt(1, taskId);
            del.executeUpdate();
        }
        try (PreparedStatement ins = conn.prepareStatement("INSERT INTO task_assignees (task_id, user_id) VALUES (?, ?)")) {
            for (int userId : assigneeIds) {
                ins.setInt(1, taskId);
                ins.setInt(2, userId);
                ins.executeUpdate();
            }
        }
    }

    // --- ★追加：画面から届いた担当者ID（複数）を int 配列に変換。未選択なら 1（従来どおり） ---
    public static int[] toAssigneeIds(String[] values) {
        if (values == null || values.length == 0) {
            return new int[] { 1 };
        }
        int[] ids = new int[values.length];
        for (int i = 0; i < values.length; i++) {
            ids[i] = Integer.parseInt(values[i]);
        }
        return ids;
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