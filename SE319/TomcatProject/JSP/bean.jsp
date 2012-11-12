
<html>
  <head><title>Bean Example</title></head>
<body>

<jsp:useBean id="testBean" class="myBeans.TestBean"/>

I have not set the name yet. It is: 
<jsp:getProperty name="testBean" property="name"/>

<p/>

<jsp:setProperty name="testBean" property="name" value="Joe"/>
I set the name to: <jsp:getProperty name="testBean" property="name"/>
<br/>
using &lt;jsp:setProperty name="testBean" property="name" value="Joe"/>


<p/>

<jsp:setProperty name="testBean" property="name" param="uname"/>
<br/>
I just set the name to whatever you requested: <jsp:getProperty name="testBean" property="name"/>
<br/>
using &lt;jsp:setProperty name="testBean" property="name" param="uname"/>

<p/>

<jsp:setProperty name="testBean" property="*"/>
I just set the name to with wildcard mapping: 
<jsp:getProperty name="testBean" property="name"/>
<br/> 
using &lt;jsp:setProperty name="testBean" property="*"/>

<p/>


<%
testBean.setName("Obelix");
%>
You can also use the get/set methods using the bean-id.
<pre>
&lt;%
testBean.setName("Obelix");
%>
</pre>
Name is now: <%=testBean.getName()%>



<p/>
You can access objects of the Bean
<pre>
&lt%
myBeans.TestBean.Address addr = testBean.getAddress();
%>
</pre>
<%
myBeans.TestBean.Address addr = testBean.getAddress();
%>
The building of the address is: <%=addr.getBldg()%>

</body>
</html>
