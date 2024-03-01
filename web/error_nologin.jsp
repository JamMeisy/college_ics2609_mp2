<%-- 
    Document   : noLoginCredentials
    Created on : 02 25, 24, 12:52:44 PM
    Author     : Jam
--%>

<!-- No login credentials (blank username and password) -->
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Company DBMS</title>
        <meta name="viewport" content="width = device-width, initial-scale = 1.0 ">
        <link rel="stylesheet" href="static/styles-error.css">  
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400&display=swap">
    </head>
    <body>
        <!-- Header -->
        <<header>
            <nav>
                <div class="left-item">
                    <img src=<%= getServletContext().getInitParameter("Header")%> alt="logo"/>
                </div>
                <div class="right-item">
                    <span class="header-text">Error</span>
                </div>
            </nav>
        </header>

        <!-- Body -->
        <section>
            <div class="box">
                <h2>ERROR: No Login Credentials</h2>
                <button class="return-button" onclick="window.location.href='index.jsp'">Return</button>
            </div>
        </section>
        <!-- Footer -->
        <footer class="footer">
            <%= getServletContext().getInitParameter("Footer") %>
        </footer>
    </body>
</html>
