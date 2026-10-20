package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class TaskDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String taskIdStr = request.getParameter("task_id");
        String projectIdStr = request.getParameter("project_id");

        // プロジェクトIDが無ければ戻り先が分からないので一覧へ
        if (projectIdStr == null || projectIdStr.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/ProjectListServlet");
            return;
        }

        if (taskIdStr != null && !taskIdStr.isEmpty()) {
            int taskId = Integer.parseInt(taskIdStr);

            // ProjectDeleteServlet と同じく論理削除（status を「削除」に更新）
            if (TaskBean.deleteTask(taskId)) {
                // task_board.jsp で完了ポップアップを出すためのフラグ
                HttpSession session = request.getSession();
                session.setAttribute("deleteTaskSuccess", true);
            }
        }

        // 削除後、タスク一覧画面へリダイレクト
        response.sendRedirect(request.getContextPath() + "/TaskBoardServlet?project_id=" + projectIdStr);
    }
}
