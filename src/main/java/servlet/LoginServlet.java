package servlet;

import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public LoginServlet() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		LoginBean loginbean = null;
		String url = null;
		try {
			String loginId = request.getParameter("login_id");
			String password = request.getParameter("password");
			
			if(loginId == null || loginId.isEmpty() || password == null || password.isEmpty()) {
				url = "/jsp/login.jsp";
				request.setAttribute("errorMessage", "IDとパスワードを入力してください。");
			} else { 
				loginbean = LoginBean.login(loginId);
				
				if(loginbean != null && loginbean.getLoginId() != null && 
				   loginbean.getLoginId().equals(loginId) && loginbean.getPassword().equals(password)) {
					
					HttpSession session = request.getSession();
					session.setAttribute("login", true);
					session.setAttribute("loginid", loginId);
					
					response.sendRedirect(request.getContextPath() + "/ProjectListServlet");
					return;
					
				} else {
					url = "/jsp/login.jsp";
					request.setAttribute("errorMessage", "ID、またはパスワードが違います。");
				}
			}
		} catch(Exception e) {
			e.printStackTrace();
			url = "/jsp/login.jsp";
			request.setAttribute("errorMessage", "システムエラーが発生しました。");
		}
		
		ServletContext context = getServletContext();
		RequestDispatcher rd = context.getRequestDispatcher(url);
		rd.forward(request, response);
	}
}