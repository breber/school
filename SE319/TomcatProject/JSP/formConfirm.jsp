<html> 
<head> 
<title>ComS 319x: first response form</title> 
</head> 
<body>
Thanks, 
<% 
    String sName = request.getParameter("username");
    out.print(sName);
%>
<br/>
<br/>
Your info has been received:
<%
    String wName = request.getParameter("website");
    out.print(wName);
%>
</body>
</html>
