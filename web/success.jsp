<%-- 
    Document   : success
    Created on : 02 23, 24, 11:29:37 AM
    Author     : Jam
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import = "exceptions.InvalidSessionException"%>
<%@page import = "test.UserData"%>
<%@page import = "java.util.ArrayList"%>

<%   
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
    response.setHeader("Pragma", "no-cache");
    response.setDateHeader("Expires", 0);
    
    String username = (String) session.getAttribute("username");
    System.out.println("Current User: " + username);
    
    if (username == null || username.equals("")) {
        System.out.println("Back button triggered");
        throw new InvalidSessionException("Unauthorized Access");
    }   
    
%>
<%
    //Instantiating the collected data from UserDB
    ArrayList<UserData> data = (ArrayList<UserData>) request.getAttribute("data");
%>

<!DOCTYPE html>
<html>
    <head>
        <title>Company DBMS</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="static/styles-success.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400&display=swap">
        <script type="text/javascript" src="./admin.js"></script>
    </head>
    <body>
        <!-- Header -->
        <header>
            <nav>
                <div class="left-item">
                    <img src=<%= getServletContext().getInitParameter("Header")%> alt="logo"/>
                </div>
                <div class="center-item">
                    <div class="center-item">
                        <a href="logout" class="logout-button">Logout</a>
                    </div>
                </div>
                <div class="right-item">
                    <span class="header-text">Welcome <% out.print(session.getAttribute("username")); %></span>
                </div>
            </nav>
        </header>

        <!-- Body -- Greeting Prompt -->
        <section>
            <div class="box">
                <h1>Welcome <% out.print(session.getAttribute("username")); %></h1>
                <h2>Your role is: <% out.print(session.getAttribute("role"));%></h2>     
            </div>
        </section>
        <footer class="footer">
            <%= getServletContext().getInitParameter("Footer")%>
        </footer>
        
       <% session.setAttribute("username", null); %>
    </body>
</html>
