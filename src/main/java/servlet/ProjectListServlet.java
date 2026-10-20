package servlet;

import java.io.IOException;
import java.util.ArrayList;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class ProjectListSevlert
 */
public class ProjectListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProjectListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet
		(HttpServletRequest request, HttpServletResponse response)
				throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		try {
			
			HttpSession session = request.getSession();
			ArrayList<ProjectBean>projectlist=ProjectBean.selectAllList();
			session.setAttribute("projectlist", projectlist);
			
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		ServletContext context=getServletContext();
		RequestDispatcher rd=context.getRequestDispatcher("/jsp/project_list.jsp");
		rd.forward(request, response);
	}
}
