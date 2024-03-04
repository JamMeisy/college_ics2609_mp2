<%-- 
    Document   : error_1
    Created on : 02 23, 24, 11:29:05 AM
    Author     : Jam
--%>

<!-- Incorrect Username, Blank Password --> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Company DBMS</title>
        <meta name="viewport" content="width = device-width, initial-scale = 1.0 ">
        <link rel="stylesheet" href="static/styles-error.css">

        <style>
            .return-button {
                margin-top: 10px;
            }
        </style>

    </head>
    <body>
        <!-- Header -->
        <header>
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
                <h2>ERROR: Incorrect Username and No Password Entered!</h2>
                <button class="return-button" onclick="window.location.href = 'index.jsp'">Return</button>
            </div>
        </section>
        <!-- Footer -->
        <footer class="footer">
            <%= getServletContext().getInitParameter("Footer")%>
        </footer>
    </body>
</html>
