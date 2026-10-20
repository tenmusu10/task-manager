package servlet;

import java.io.IOException;
import java.sql.Timestamp;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

/**
 * Servlet implementation class ProjectCreateServlet
 */
public class ProjectCreateServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProjectCreateServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		try {
			ProjectBean insertproject = new ProjectBean();
			insertproject.setProjectName(request.getParameter("project_name" ));
			
			insertproject.setDeadline(request.getParameter("deadline"));
		
			HttpSession session=request.getSession();
			String loginid=(String)session.getAttribute("loginid");
			int userid=ProjectBean.selectuser(loginid);
			
			insertproject.setCreatedBy(userid);
			
			int projectid=ProjectBean.prjid();
			insertproject.setProjectId(projectid+1);
			
			 Timestamp timestamp=new Timestamp(System.currentTimeMillis());
			insertproject.setCreatedAt(timestamp);
			
			ProjectBean.insertData(insertproject);
		
		}catch(Exception e) {
		 e.printStackTrace();
		}finally{
		
		}ServletContext context=getServletContext();
		RequestDispatcher rd=context.getRequestDispatcher("/ProjectListServlet");
		rd.forward(request, response);

	}
}