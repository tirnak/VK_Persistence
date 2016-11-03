<%@page import="java.util.ArrayList"%>
<%@page import="tirnak.persistence.model.Post" %>
<%@page import="java.util.List" %>
<%@ page import="tirnak.persistence.model.Picture" %>
<%@ page import="tirnak.persistence.model.Like" %>
<%
    List<Post> posts = (List<Post>) request.getAttribute("posts");
%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
    <title>Sample JSP Page</title>
    <link href="styles.css" rel="stylesheet">
</head>
<body>
<form method="post" action="/">
    <textarea name="query" cols="50" rows="5"></textarea>
    <input type="submit" value="query">
</form>

    <% for (Post post : posts) { %>
        <div class="post">
            <div class="author"><%=post.getAuthor().getFullName()%></div>
            <div class="date"><%=post.getDate()%></div>
            <div class="text"><%=post.getText()%></div>
            <% for (Picture pic : post.getImages()) { %>
                <img src="<%=pic.getHref()%>">
            <% } %>
            <%
                Post repost = post; do {
                    repost = repost.getRepostOf();
                    if (repost != null) {
            %>
                        <div class="repost">
                            <div class="author"><%=repost.getAuthor().getFullName()%></div>
                            <div class="date"><%=repost.getDate()%></div>
                            <% for (Picture pic : repost.getImages()) { %>
                                <img src="<%=pic.getHref()%>">
                            <% } %>

                        </div>
                <% } %>
            <% } while (repost != null); %>
            <div class="likes">
                <% for (Like like : post.getLikes()) { %>
                    <div class="like">
                        <%=like.getOwner().getFullName()%> <%=like.isReposted() ? "reposted" : "likes"%> this
                    </div>
                <% } %>
            </div>
            <div class="comments">
                <% for (Post comment : post.getComments()) { %>
                    <div class="comment">
                        <div class="author"><%=comment.getAuthor()%></div>
                        <div class="date"><%=comment.getDate()%></div>
                        <% for (Picture pic : comment.getImages()) { %>
                            <img src="<%=pic.getHref()%>">
                        <% } %>
                    </div>
                <% } %>
            </div>
        </div>
    <% } %>

    <style>
        .post {
            width: 70%;
            margin: 10px auto;
            border: dotted 1px lightgrey;
        }
        .author {
            font-size: larger;
        }
        .date {
            font-size: smaller;
            font-style: italic;
        }
    </style>
</body>
</html>
