<html>
<head>
   <title>Order</title>
</head>

<body>

<jsp:useBean id="bookStock" class="myBeans.Stock"/>

<!-- Get a session object, create one if not already present -->
<%@ page session="true" %>

<%
// Declaring local variables
// theBooks: to get all the book information
myBeans.Stock.Book[] theBooks = bookStock.getBooks();

// oldCart: to get the all cart information from the session, may return null
// Table data will reflect information from existing cart 
myBeans.Stock.Cart oldCart = (myBeans.Stock.Cart)session.getAttribute("cart");
%>

<h4>The available books are as follows:</h4>

<!-- create a form to submit to response.jsp -->
<table border="2">
	<tr>
		<th> Name of the book </th>
		<th> Author of the book </th>
		<th> Price </th>
		<th> Select </th>
	</tr>
<%
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
%>
	<tr>
		<td><input type="text" id="name<%=i %>" name="name<%=i %>" value="<%=theBooks[i].getName() %>" readonly="readonly" /></td>
		<td><input type="text" id="author<%=i %>" name="author<%=i %>" value="<%=theBooks[i].getAuthor() %>" readonly="readonly" /></td>
		<td><input type="text" id="price<%=i %>" name="price<%=i %>" value="<%=theBooks[i].getPrice() %>" readonly="readonly" /></td>
<%
    if (checked ==1 ) 
    {
%>    		
		<td><input type="checkbox" id="check<%=i %>" name="check<%=i %>" checked="checked"/></td>
<%  } else
    {
%>	  
        <td><input type="checkbox" id="check<%=i %>" name="check<%=i %>"/></td> 	
<%  }
%>
	</tr>
<%
}
%>
</table>

</body>
</html>
