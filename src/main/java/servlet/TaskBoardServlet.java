package servlet;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class TaskBoardServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        String projectIdStr = request.getParameter("project_id");
        
        if (projectIdStr != null && !projectIdStr.isEmpty()) {
            int projectId = Integer.parseInt(projectIdStr);
            
            List<TaskBean> taskList = TaskBean.getTaskList(projectId);
            
            // ★追加：親プロジェクトの情報も取得してJSPに渡す（これが無いとタイトルが出ません）
            ProjectBean project = ProjectBean.getProjectById(projectId);
            request.setAttribute("project", project);
            
            request.setAttribute("taskList", taskList);
            request.setAttribute("projectId", projectId); 
        }

        request.getRequestDispatcher("/jsp/task_board.jsp").forward(request, response);
    }
}