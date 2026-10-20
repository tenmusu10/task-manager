package servlet;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * ログインしていない状態で各画面を開いた場合、ログイン画面へ戻すフィルタ
 * （@WebFilter で登録しているため web.xml の変更は不要）
 */
@WebFilter("/*")
public class LoginFilter implements Filter {

    // ログインしていなくても開けるURL
    private static final String[] ALLOW_PATHS = {
        "/", "/jsp/login.jsp", "/jsp/logout.jsp",
        "/jsp/user_register.jsp", "/jsp/user_register_success.jsp",
        "/LoginServlet", "/LogoutController",
        "/LoginToRegisterController", "/UserRegisterServlet",
    };

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String path = request.getRequestURI().substring(request.getContextPath().length());

        // CSS・画像などはそのまま通す
        if (path.startsWith("/css/") || path.startsWith("/images/") || path.startsWith("/js/")) {
            chain.doFilter(req, res);
            return;
        }
        for (String allow : ALLOW_PATHS) {
            if (path.equals(allow)) {
                chain.doFilter(req, res);
                return;
            }
        }

        // LoginServlet でセッションに保存した "login" があればログイン済み
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("login") != null) {
            chain.doFilter(req, res);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/jsp/login.jsp");
    }
}
