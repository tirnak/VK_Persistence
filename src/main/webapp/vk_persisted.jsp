<%@page import="java.util.ArrayList"%>
<%@page import="tirnak.persistence.model.Post" %>
<%@page import="java.util.List" %>
<%
    List<Post> posts = (List<Post>) request.getAttribute("posts");
%>
<html>
<head>
    <title>Sample JSP Page</title>
</head>
<body>
Current date is: <%=new java.util.Date()%>

<table>
    <% for (Post post : posts) { %>
        <tr>
            <td><%=post.getAuthor().getFullName()%></td>
            <td><%=post.getId()%></td>
        </tr>
    <% } %>
</table>
</body>
</html>
