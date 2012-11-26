package org.apache.jsp.JSP;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.jsp.*;

public final class response_jsp extends org.apache.jasper.runtime.HttpJspBase
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
      out.write("<head><title>response.jsp</title>\n");
      out.write("</head>\n");
      out.write("\n");
      out.write("<body>\n");
      out.write("\n");
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
      out.write("<!-- Get the session, create one if it does not exist -->\n");
      out.write("\n");

myBeans.Stock.Cart myCart = bookStock.getCart();

boolean flag = false;
for(Integer i=0; request.getParameter("name"+i.toString()) != null; i++)
{
	flag = true; // if you go in once => you are coming from order.jsp
	// not null => the i-th book has been selected: add that to the cart
	if(request.getParameter("check"+i.toString()) != null)
	{
		myCart.add(request.getParameter("name"+i.toString()), 
				   request.getParameter("author"+i.toString()),
				   request.getParameter("price"+i.toString())); 
	}
}

// Check the number of items: if not zero save it in the session
if(myCart.getItems().length != 0)	
	session.setAttribute("cart", myCart);
// Nothing selected: get the cart from the session if it is not null	
else if(session.getAttribute("cart") != null && flag != true)
	myCart = (myBeans.Stock.Cart) session.getAttribute("cart");
// go back to show the order.jsp	
else 
{
	// empty the cart
	session.setAttribute("cart", null);
	response.sendRedirect("http://localhost:8080/TomcatProject/JSP/order.jsp");
}

      out.write("\n");
      out.write("\n");
      out.write("<!--  presentation of selected books -->\n");
      out.write("<table border=\"2\">\n");
      out.write("\t<tr>\n");
      out.write("\t\t<th> Name of the book </th>\n");
      out.write("\t\t<th> Author of the book </th>\n");
      out.write("\t\t<th> Price </th>\n");
      out.write("\t<tr>\n");

//Float total = new Float(0.0);
double total = 0.0;
myBeans.Stock.Book[] theBooks = myCart.getItems();
for(int i=0; i < theBooks.length; i++)
{

      out.write("\n");
      out.write("\t<tr>\n");
      out.write("\t\t<td>");
      out.print(theBooks[i].getName() );
      out.write("</td>\n");
      out.write("\t\t<td>");
      out.print(theBooks[i].getAuthor() );
      out.write("</td>\n");
      out.write("\t\t<td>");
      out.print(theBooks[i].getPrice() );
      out.write("</td>\n");
      out.write("\t</tr>\n");

	Thread.sleep(1000);
	total += Double.parseDouble(theBooks[i].getPrice().substring(1));
}

      out.write("\n");
      out.write("</table>\n");
      out.write("\n");
      out.write("<h4>Thanks for ordering. Your total cost is $");
      out.print(Math.ceil(total*100)/100 );
      out.write("</h4>\n");
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
