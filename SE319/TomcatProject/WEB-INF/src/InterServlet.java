import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;


public class InterServlet extends HttpServlet
{
	public void doGet(HttpServletRequest request, HttpServletResponse response)
						throws ServletException, IOException
	{
		RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/servlet/ForwardServlet");
		if (dispatcher != null)
		{
			request.setAttribute("message", new String("Be Warned"));
			dispatcher.forward(request, response);
		}
	}
	
}
