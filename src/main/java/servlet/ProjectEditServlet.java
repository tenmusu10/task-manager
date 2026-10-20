package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProjectEditServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String projectIdStr = request.getParameter("project_id");
        
        if (projectIdStr != null && !projectIdStr.isEmpty()) {
            int projectId = Integer.parseInt(projectIdStr);
            ProjectBean project = ProjectBean.getProjectById(projectId);
            request.setAttribute("project", project);
        }

        request.getRequestDispatcher("/jsp/project_form.jsp").forward(request, response);
    }
}