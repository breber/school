import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

public class IncludedServlet extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response)
						throws ServletException, IOException
	{
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<h1>Inside the included servlet</h1>");
		
	}
	
}
