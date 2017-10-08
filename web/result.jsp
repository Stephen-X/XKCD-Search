<%-- 
    Document   : result page
    Author     : Stephen Xie <***@andrew.cmu.edu>
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>XKCD Comics Archive Search</title>
        <link rel="stylesheet" type="text/css" href="css/global.css">
    </head>
    
    <body>
        <h1>XKCD Comics Archive Search</h1>
        <h3>There are a total of <%=request.getAttribute("total")%> XKCD comics in the archive.</h3>
        
        <% if (((Integer) request.getAttribute("count")) <= 0) { %>
        
        <h3>There's no XKCD comic that matches your search term "<%=request.getAttribute("keyword")%>".</h3>
        
        <% } else if (((Integer) request.getAttribute("count")) == 1) { %>
        
        <h3>Only 1 comic had "<%=request.getAttribute("keyword")%>" in its title.</h3>
        <h3>Here it is:</h3>
        <h2><%=request.getAttribute("title")%></h2>
        <img src="<%=request.getAttribute("url")%>">
        
        <% } else { %> <%-- count > 1 --%>
        
        <h3><%=request.getAttribute("count")%> comics had "<%=request.getAttribute("keyword")%>" in their titles.</h3>
        
        <h3>Here is one of them:</h3>
        <h2><%=request.getAttribute("title")%></h2>
        <img src="<%=request.getAttribute("url")%>">
        
        <% } %>
        
        <form action="XKCDSearch" method="GET">
            <span>Search XKCD titles for:&nbsp;</span>
            <input type="text" name="title">&nbsp;
            <input type="submit" value="Search">
        </form>
        
        <footer>
            <a href="https://xkcd.com/">XKCD</a> is a webcomic by Randall Munroe.
        </footer>
    </body>
</html>
