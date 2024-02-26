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
    </head>
    <body>
        <!-- Header -->
        <header>
            <nav>
                <img src="" alt="logo"/>
                <a href="logout">
                    <img src="" alt="logout">
                </a>
            </nav>
        </header>

        <!-- Body -->
        <section>
            <div>
                <form action="login" method="POST">
                    <label for="username">Username</label>
                    <input type="email" name="username" id="username">

                    <label for="password">Password</label>
                    <input type="password" name="password" id="password">

                    <button type="submit">Submit</button>
                </form>
            </div>
        </section>
        <!-- Footer -->
        <footer>

        </footer>
    </body>
</html>
