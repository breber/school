package org.apache.jsp.JSP;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class order_jsp extends org.apache.jasper.runtime.HttpJspBase
    implements org.apache.jasper.runtime.JspSourceDependent {

  private static final JspFactory _jspxFactory = JspFactory.getDefaultFactory();

  private static java.util.List _jspx_dependants;

  private javax.el.ExpressionFactory _el_expressionfactory;
  private org.apache.AnnotationProcessor _jsp_annotationprocessor;

  public Object getDependants() {
    return _jspx_dependants;
  }

  public void _jspInit() {
    _el_expressionfactory = _jspxFactory.getJspApplicationContext(getServletConfig().getServletContext()).getExpressionFactory();
    _jsp_annotationprocessor = (org.apache.AnnotationProcessor) getServletConfig().getServletContext().getAttribute(org.apache.AnnotationProcessor.class.getName());
  }

  public void _jspDestroy() {
  }

  public void _jspService(HttpServletRequest request, HttpServletResponse response)
        throws java.io.IOException, ServletException {

    PageContext pageContext = null;
    HttpSession session = null;
    ServletContext application = null;
    ServletConfig config = null;
    JspWriter out = null;
    Object page = this;
    JspWriter _jspx_out = null;
    PageContext _jspx_page_context = null;


    try {
      response.setContentType("text/html");
      pageContext = _jspxFactory.getPageContext(this, request, response,
      			null, true, 8192, true);
      _jspx_page_context = pageContext;
      application = pageContext.getServletContext();
      config = pageContext.getServletConfig();
      session = pageContext.getSession();
      out = pageContext.getOut();
      _jspx_out = out;

      out.write("<html>\n");
      out.write("<head>\n");
      out.write("   <title>Order</title>\n");
      out.write("</head>\n");
      out.write("\n");
      out.write("<body>\n");
      out.write("\n");
      myBeans.Stock bookStock = null;
      synchronized (_jspx_page_context) {
        bookStock = (myBeans.Stock) _jspx_page_context.getAttribute("bookStock", PageContext.PAGE_SCOPE);
        if (bookStock == null){
          bookStock = new myBeans.Stock();
          _jspx_page_context.setAttribute("bookStock", bookStock, PageContext.PAGE_SCOPE);
        }
      }
      out.write("\n");
      out.write("\n");
      out.write("<!-- Get a session object, create one if not already present -->\n");
      out.write("\n");
      out.write("\n");

// Declaring local variables
// theBooks: to get all the book information
myBeans.Stock.Book[] theBooks = bookStock.getBooks();

// oldCart: to get the all cart information from the session, may return null
// Table data will reflect information from existing cart 
myBeans.Stock.Cart oldCart = (myBeans.Stock.Cart)session.getAttribute("cart");

      out.write("\n");
      out.write("\n");
      out.write("<h4>The available books are as follows:</h4>\n");
      out.write("\n");
      out.write("<!-- create a form to submit to response.jsp -->\n");
      out.write("<form action=\"http://localhost:8080/TomcatProject/JSP/response.jsp\" method=\"post\">\n");
      out.write("<table border=\"2\">\n");
      out.write("\t<tr>\n");
      out.write("\t\t<th> Name of the book </th>\n");
      out.write("\t\t<th> Author of the book </th>\n");
      out.write("\t\t<th> Price </th>\n");
      out.write("\t\t<th> Select </th>\n");
      out.write("\t</tr>\n");

// loop over the books array and check which one of them are present in the cart
for(int i=0; i < theBooks.length; i++)
{
    // checked variable keeps track of whether the current book is present in the cart
    // bad!!! need a smarter way - too much looping
    int checked = 0;
    if (oldCart != null) 
    {
	    for (int j=0; j < oldCart.getItems().length; j++)
    	{
     	  	if (theBooks[i].getName().equals(oldCart.getItems()[j].getName())) 
       		{
       	   		checked = 1;
       	   		break;
       		}
    	}
    }

      out.write("\n");
      out.write("\t<tr>\n");
      out.write("\t\t<td><input type=\"text\" id=\"name");
      out.print(i );
      out.write("\" name=\"name");
      out.print(i );
      out.write("\" value=\"");
      out.print(theBooks[i].getName() );
      out.write("\" readonly=\"readonly\" /></td>\n");
      out.write("\t\t<td><input type=\"text\" id=\"author");
      out.print(i );
      out.write("\" name=\"author");
      out.print(i );
      out.write("\" value=\"");
      out.print(theBooks[i].getAuthor() );
      out.write("\" readonly=\"readonly\" /></td>\n");
      out.write("\t\t<td><input type=\"text\" id=\"price");
      out.print(i );
      out.write("\" name=\"price");
      out.print(i );
      out.write("\" value=\"");
      out.print(theBooks[i].getPrice() );
      out.write("\" readonly=\"readonly\" /></td>\n");

    if (checked ==1 ) 
    {

      out.write("    \t\t\n");
      out.write("\t\t<td><input type=\"checkbox\" id=\"check");
      out.print(i );
      out.write("\" name=\"check");
      out.print(i );
      out.write("\" checked=\"checked\"/></td>\n");
  } else
    {

      out.write("\t  \n");
      out.write("        <td><input type=\"checkbox\" id=\"check");
      out.print(i );
      out.write("\" name=\"check");
      out.print(i );
      out.write("\"/></td> \t\n");
  }

      out.write("\n");
      out.write("\t</tr>\n");

}

      out.write("\n");
      out.write("</table>\n");
      out.write("<input type=\"submit\" value=\"Order Now\"/>\n");
      out.write("</form>\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("\n");
      out.write("</body>\n");
      out.write("</html>\n");
    } catch (Throwable t) {
      if (!(t instanceof SkipPageException)){
        out = _jspx_out;
        if (out != null && out.getBufferSize() != 0)
          try { out.clearBuffer(); } catch (java.io.IOException e) {}
        if (_jspx_page_context != null) _jspx_page_context.handlePageException(t);
        else log(t.getMessage(), t);
      }
    } finally {
      _jspxFactory.releasePageContext(_jspx_page_context);
    }
  }
}
