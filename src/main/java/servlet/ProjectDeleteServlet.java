package servlet;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class ProjectDeleteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String projectIdStr = request.getParameter("project_id");

        if (projectIdStr != null && !projectIdStr.isEmpty()) {
            int projectId = Integer.parseInt(projectIdStr);

            // ★大修正：直接SQLを書かず、ProjectBeanの削除処理（論理削除）を呼び出す！
            // これにより、一覧画面で「ステータスが削除以外のものを表示する」という元の設計と完璧に噛み合います。
            ProjectBean.deleteProject(projectId);

            // JSP側でポップアップやメッセージを出すための「削除成功フラグ」を保存
            HttpSession session = request.getSession();
            session.setAttribute("deleteSuccess", true);
        }

        // 削除後、プロジェクト一覧画面へリダイレクト
        response.sendRedirect(request.getContextPath() + "/ProjectListServlet");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 万が一GETでアクセスされても安全に処理する
        doPost(request, response);
    }
}