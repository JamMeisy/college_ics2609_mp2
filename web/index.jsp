<%-- 
    Document   : index
    Created on : 02 23, 24, 11:26:19 AM
    Author     : Jam
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Company DBMS</title>
        <meta name="viewport" content="width = device-width, initial-scale = 1.0 ">
        <link rel="stylesheet" href="static/styles-login.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400&display=swap">
    </head>
    <body>
        <!-- Header -->
        <header>
            
            <nav>
                <div class="left-item">
                    <img src=<%= getServletContext().getInitParameter("Header") %> alt="logo"/>
                </div>
                <div class="right-item">
                    <span class="header-text">Login Page</span>
                </div>
            </nav>
        </header>

        <!-- Body -->
        <section>
            <div class="login-box">
                <h2>Welcome To Our Database!</h2>
                <form action="login" method="POST">
                    <label for="username">Username</label>
                    <input type="text" name="username" id="username">

                    <label for="password">Password</label>
                    <input type="password" name="password" id="password">

                    <button type="submit">Submit</button>
                </form>
            </div>
        </section>
        <!-- Footer -->
        <footer class="footer">
            <%= getServletContext().getInitParameter("Footer") %>
        </footer>
    </body>
</html>
