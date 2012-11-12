import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;

/*
 * Count the number of times ServletCounter is invoked by any client
 */
public class ServletCounter extends HttpServlet
{
	
	// instance variable declaration: shared by all threads 
    private int accessCount;

    /*
     *   init is invoked only when the ServletCounter class is loaded 
     */
    public void init() throws ServletException
    {
      accessCount = 0;   // initialzation of instance variable
    }
 
    public void doGet(HttpServletRequest request, HttpServletResponse response)
                     throws IOException, ServletException
    {
      int localCount = 0;
      
      // lock, read, increment, assign, release-lock
      synchronized(this)
      {
        accessCount++;
        localCount = accessCount;
      }
      
      response.setContentType("text/html");
      PrintWriter out = response.getWriter();
      out.println("<HTML>\n" +
                "<HEAD><TITLE>Counter-Page</TITLE></HEAD>\n" +
                "<BODY>\n" +
                "<h4>This page has been accessed " + localCount + " times</h4>" +
                "<BODY>\n" +
                "<HTML>");
    }
}

