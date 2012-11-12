
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;


public class HelloWorld extends HttpServlet {
	public void doGet(HttpServletRequest request,
			HttpServletResponse response)
	throws ServletException, IOException 
	{
		String inputval = request.getParameter("input1");
		// First set up the content type to indicate that the response is in HTML
		response.setContentType("text/html");
		   
		// grab the output writer
		PrintWriter out = response.getWriter();
		
		// start writing the HTML page
		out.println("<HTML>\n" + 
				"<HEAD><TITLE>Hello World</TITLE></HEAD>\n" +
				"<BODY>\n" + 
				"<H1>Hey" + inputval + "Billions of Blue Blistering Barnacles</H1>" +  
        		"</BODY></HTML>");   		
	}
	
	public void doPost(HttpServletRequest request, 
				HttpServletResponse response)
	throws ServletException, IOException
	{
		doGet(request, response);
	}
}
