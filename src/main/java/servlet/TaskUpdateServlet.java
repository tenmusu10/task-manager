package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TaskUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String taskIdStr = request.getParameter("task_id");
        String projectIdStr = request.getParameter("project_id");

        if (taskIdStr != null && !taskIdStr.isEmpty()) {
            int taskId = Integer.parseInt(taskIdStr);

            TaskBean task = TaskBean.getTaskById(taskId);
            request.setAttribute("task", task);

            int projectId = (task != null) ? task.getProjectId() : Integer.parseInt(projectIdStr);
            ProjectBean project = ProjectBean.getProjectById(projectId);
            request.setAttribute("project", project);
            request.setAttribute("projectId", projectId);
        }

        // ★復活：担当者一覧（userList）を取得してセット
        List<UserBean> userList = UserBean.getAllUsers();
        request.setAttribute("userList", userList);

        request.getRequestDispatcher("/jsp/task_form.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String taskIdStr = request.getParameter("task_id");
        String projectIdStr = request.getParameter("project_id");
        String taskName = request.getParameter("task_name");
        String deadline = request.getParameter("deadline");
        String status = request.getParameter("status");
        String[] assigneeIdStrs = request.getParameterValues("assignee_id"); // ★修正：担当者は複数

        // ★追加対策：エラー防止
        if (taskIdStr == null || taskIdStr.isEmpty() || projectIdStr == null || projectIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/ProjectListServlet");
            return;
        }

        int taskId = Integer.parseInt(taskIdStr);
        int projectId = Integer.parseInt(projectIdStr);
        int[] assigneeIds = TaskBean.toAssigneeIds(assigneeIdStrs);

        TaskBean.updateTask(taskId, taskName, deadline, status, assigneeIds);

        response.sendRedirect(request.getContextPath() + "/TaskBoardServlet?project_id=" + projectId);
    }
}