
import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CookieSet extends HttpServlet 
{
  public void doGet(HttpServletRequest request,
                    HttpServletResponse response)
      throws ServletException, IOException 
  {
	 response.setContentType("text/html");
	 PrintWriter out = response.getWriter(); 
     String prefVal = request.getParameter("pref");
     if (prefVal != null)
     {
       Cookie mailPref = new Cookie("mailpreference", prefVal);
       mailPref.setMaxAge(10000);
       response.addCookie(mailPref);
       out.println(" Cookie is set to " + prefVal + "");
     }
     else
    	 out.println("No Cookie for YOU!!");
     
  }
}

