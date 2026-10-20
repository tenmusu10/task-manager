package servlet;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

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

    // ヘッダーに表示する現在日時の形式（例：2026/10/20（火） 15:30）
    private static final DateTimeFormatter DATE_TIME_FORMAT =
        DateTimeFormatter.ofPattern("yyyy/MM/dd（E） HH:mm", Locale.JAPANESE);

    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        // 各画面のヘッダーで ${currentDateTime} として表示する（画面を開いた時点の日時）
        request.setAttribute("currentDateTime", LocalDateTime.now(ZoneId.of("Asia/Tokyo")).format(DATE_TIME_FORMAT));

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
