<html>
<head><title>response.jsp</title>
</head>

<body>


<jsp:useBean id="bookStock" class="myBeans.Stock"/>

<!-- Get the session, create one if it does not exist -->
<%@ page session="true" %>
<%
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
%>

<!--  presentation of selected books -->
<table border="2">
	<tr>
		<th> Name of the book </th>
		<th> Author of the book </th>
		<th> Price </th>
	<tr>
<%
//Float total = new Float(0.0);
double total = 0.0;
myBeans.Stock.Book[] theBooks = myCart.getItems();
for(int i=0; i < theBooks.length; i++)
{
%>
	<tr>
		<td><%=theBooks[i].getName() %></td>
		<td><%=theBooks[i].getAuthor() %></td>
		<td><%=theBooks[i].getPrice() %></td>
	</tr>
<%
	Thread.sleep(1000);
	total += Double.parseDouble(theBooks[i].getPrice().substring(1));
}
%>
</table>

<h4>Thanks for ordering. Your total cost is $<%=Math.ceil(total*100)/100 %></h4>

<p>
<b>If you want to revise your selection: <a href="order.jsp">Click Here</a>, Do not Press Back-Button</b>
</p>

</body>
</html>
