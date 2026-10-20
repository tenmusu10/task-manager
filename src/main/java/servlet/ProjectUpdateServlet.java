package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProjectUpdateServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String projectIdStr = request.getParameter("project_id");
        String projectName = request.getParameter("project_name");
        String deadline = request.getParameter("deadline");

        if (projectIdStr != null && !projectIdStr.isEmpty()) {
            int projectId = Integer.parseInt(projectIdStr);
            // ★ProjectBeanに追加する「更新メソッド」を呼び出す
            ProjectBean.updateProject(projectId, projectName, deadline);
        }

        // 更新後、一覧画面へ戻る
        response.sendRedirect(request.getContextPath() + "/ProjectListServlet");
    }
}