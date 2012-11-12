import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class ForwardServlet extends HttpServlet
{

	public void doGet(HttpServletRequest request, HttpServletResponse response)
					throws ServletException, IOException
	{
		
		String message = (String)request.getAttribute("message");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html><body>\n");
		out.println("<h1>This is forwared to ForwardServlet:" + message + "</h1>");
		
		//	Lets get some information
		ServletContext context = getServletContext(); 
		String serverName = context.getServerInfo();
		out.println("<h1>ForwardServlet writes the ServerName = " + serverName + "</h1>");
		
		RequestDispatcher dispatcher = context.getRequestDispatcher("/servlet/IncludedServlet");
		if (dispatcher == null)
		{
			out.println(response.SC_NO_CONTENT);
		}
		else
		{
			dispatcher.include(request, response);
		}
		
		out.println("<h1>Back to ForwardServlet</h1>");
		out.println("</body></html>");
		out.close();
	}
}
