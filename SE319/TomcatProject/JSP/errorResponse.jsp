<%@ page isErrorPage="true" %>
<HTML>
<HEAD><TITLE>Error Response Page</TITLE></HEAD>
<BODY>
<H2>Exception Information</H2>
<TABLE>
  <tr>
     <td>Exception Class:</td>
     <td><%= exception.getClass() %></td>
  </tr>
  <tr>
    <td>Message:</td>
    <td><%= exception.getMessage() %></td>
  </tr>
</TABLE>
</BODY>
</HTML>
