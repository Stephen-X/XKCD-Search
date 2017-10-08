<%-- 
    Document   : welcome page
    Author     : Stephen Xie <***@andrew.cmu.edu>
--%>

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
        <form action="XKCDSearch" method="GET">
            <span>Search XKCD titles for:&nbsp;</span>
            <input type="text" name="title">&nbsp;
            <input type="submit" value="Search">
        </form>
        
        <% if (request.getAttribute("keywordIsEmpty") != null) { %>
        <p class="info"><strong>You've entered an empty string! Please type
                something...</strong></p>
        <% } %>
        
        <% if (request.getAttribute("fetchUnsuccessful") != null) { %>
        <!-- if content fetch is unsuccessful when instantiating the server model,
             site admin will need to reboot the server after XKCD is online or
             issues have been resolved. -->
        <p class="info"><strong>Something's wrong when fetching content from XKCD.
                   Please ask site administrator to investigate.</strong></p>
        <% } %>
        
        <footer>
            <a href="https://xkcd.com/">XKCD</a> is a webcomic by Randall Munroe.
        </footer>
    </body>
</html>
