package servlet;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


public class UserRegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        String userName = request.getParameter("user_name");
        String loginId = request.getParameter("login_id");
        String password = request.getParameter("password");

        String sql = "INSERT INTO users (login_id, password, user_name) VALUES (?, ?, ?)";

        // ★修正：DBConnection ではなく ResourceFinder を使うように統一
        try (Connection conn = new ResourceFinder().getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, loginId);
            pstmt.setString(2, password);
            pstmt.setString(3, userName);

            int rowsInserted = pstmt.executeUpdate();

            if (rowsInserted > 0) {
                response.sendRedirect(request.getContextPath() + "/jsp/user_register_success.jsp");
            } else {
                request.setAttribute("errorMsg", "登録に失敗しました。もう一度お試しください。");
                request.getRequestDispatcher("/jsp/user_register.jsp").forward(request, response);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("errorMsg", "このIDは既に登録されているか、システムエラーが発生しました。");
            request.getRequestDispatcher("/jsp/user_register.jsp").forward(request, response);
        }
    }
}