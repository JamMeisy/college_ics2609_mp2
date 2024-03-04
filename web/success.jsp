<%-- 
    Document   : success
    Created on : 02 23, 24, 11:29:37 AM
    Author     : Jam
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import = "exceptions.InvalidSessionException"%>

<%  
    // Disable Caching
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); // HTTP 1.1
    response.setHeader("Pragma", "no-cache");                                   // HTTP 1.0
    response.setDateHeader("Expires", 0);                                       // Proxies
   
    // Storing Data
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
    String header = (String) getServletContext().getInitParameter("Header");
    String footer = (String) getServletContext().getInitParameter("Footer");
    
    System.out.println("-- Current User: " + username);
    
    if (username == null || username.equals("")) {
        throw new InvalidSessionException("Unauthorized Access");
    }
    
    // Nullifying session atttribute to logout when exited
    session.removeAttribute("username");
    
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Company DBMS</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="static/styles-success.css">
    </head>
    <body>
        <!-- Header -->
        <header>
            <nav>
                <div class="left-item">
                    <img src="<%= header %>" alt="logo"/>
                </div>
                <div class="center-item">
                    <div class="center-item">
                        <a href="logout" class="logout-button">Logout</a>
                    </div>
                </div>
                <div class="right-item">
                    <span class="header-text">Welcome <%= username %></span>
                </div>
            </nav>
        </header>

        <!-- Body -- Greeting Prompt -->
        <section>
            <div class="box">
                <h1>Welcome <%= username %></h1>
                <h2>Your role is: <%= role %></h2>     
            </div>
        </section>
        <footer class="footer">
            <%= footer %>
        </footer>
        
    </body>
</html>
