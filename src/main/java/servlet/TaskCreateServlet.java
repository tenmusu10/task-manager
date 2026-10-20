package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TaskCreateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        String projectIdStr = request.getParameter("project_id");
        if (projectIdStr == null || projectIdStr.isEmpty()) {
            projectIdStr = request.getParameter("projectId");
        }

        if (projectIdStr != null && !projectIdStr.isEmpty()) {
            int projectId = Integer.parseInt(projectIdStr);
            ProjectBean project = ProjectBean.getProjectById(projectId);
            request.setAttribute("project", project);
            request.setAttribute("projectId", projectId);
        } else {
            // ★調査用ログ：もし一覧画面からIDが渡ってきていなければ報告
            System.out.println("【警告】作成画面を開くためのプロジェクトIDが届いていません！");
        }
        
        List<UserBean> userList = UserBean.getAllUsers();
        request.setAttribute("userList", userList);
        
        request.getRequestDispatcher("/jsp/task_form.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        
        // ★調査用ログ：ボタンが押されたことを報告
        System.out.println("=== タスク作成ボタンが押されました ===");
        
        String projectIdStr = request.getParameter("project_id");
        String taskName = request.getParameter("task_name");
        String deadline = request.getParameter("deadline");
        String status = request.getParameter("status");
        String assigneeIdStr = request.getParameter("assignee_id");
        
        // ★調査用ログ：画面から飛んできた中身をすべて表示
        System.out.println("受信データ -> プロジェクトID: [" + projectIdStr + "], タスク名: [" + taskName + "], 期限: [" + deadline + "], 担当者ID: [" + assigneeIdStr + "]");

        if (projectIdStr == null || projectIdStr.isEmpty()) {
            System.out.println("【原因判明】プロジェクトIDが空っぽだったため、保存せずに一覧へ戻ります！");
            response.sendRedirect(request.getContextPath() + "/ProjectListServlet");
            return;
        }

        int projectId = Integer.parseInt(projectIdStr);
        int assigneeId = (assigneeIdStr != null && !assigneeIdStr.isEmpty()) ? Integer.parseInt(assigneeIdStr) : 1;
        
        System.out.println("★これからデータベースに保存します...");
        boolean result = TaskBean.insertTask(projectId, taskName, deadline, status, assigneeId);
        System.out.println("★保存結果（trueなら成功）: " + result);
        
        response.sendRedirect(request.getContextPath() + "/TaskBoardServlet?project_id=" + projectId);
    }
}