
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.*;

public class ShowSession extends HttpServlet 
{
  public void doGet(HttpServletRequest request, HttpServletResponse response)
         throws ServletException, IOException 
  {
    response.setContentType("text/html");
    PrintWriter out = response.getWriter();
    String heading;

    HttpSession session = request.getSession(true);
    int accessCount = 0;;
    
    if (session.isNew()) 
      heading = "Welcome, Newcomer";
    else 
    {
      heading = "Welcome Back";
      Integer oldAccessCount = (Integer)session.getAttribute("accessCount"); 
      if (oldAccessCount != null) 
      {
        accessCount = oldAccessCount.intValue() + 1;
      }
    }
    session.setAttribute("accessCount", new Integer(accessCount)); 
    
    
    out.println("<HTML><BODY>\n<H2>" + heading + "</H2>\n"); 
    out.println("<B>Session-id:</B> " + session.getId() + "<BR/>");
    out.println("<B>Created-on:</B> " + new Date(session.getCreationTime()) + "<BR/>");
    out.println("<B>Last-accessed:</B> " + new Date(session.getLastAccessedTime()) + "<BR/>");
    out.println("<B>Previous-accesses:</B> " + accessCount);
    out.println("</BODY></HTML>");

  }

  public void doPost(HttpServletRequest request,
                     HttpServletResponse response)
      throws ServletException, IOException 
  {
    doGet(request, response);
  }
}

