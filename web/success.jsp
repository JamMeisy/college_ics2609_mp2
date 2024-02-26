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
    if (session.getAttribute("username") == null)
        throw new InvalidSessionException("Attempting to access without authorization");
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
        <script type="text/javascript" src="./admin.js"></script>
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

        <!-- Body -- Greeting Prompt -->
        <section>
            <h1>Welcome <% out.print(session.getAttribute("username")); %></h1>
            <h2>Your role is: <% out.print(session.getAttribute("role")); %></h2>      
        </section>
        
        <!-- Body -- Error / Success Message with Coded Color based on type-->
        <% if (request.getAttribute("message") != null) { %>
            
        <section class="
                 <% if (request.getAttribute("message-type").equals("success")) out.print("success"); else out.print("error"); %>"
                 id = "message-box">
            
            
            <h3><% out.print(request.getAttribute("message")); %></h3>
            <button type="button" onclick="removeMessage()">X</button>            
        </section>
        
        <% } %>
        <!-- Body -- Display Database -->   
        <section>
            <div class="dbdisplay">
                <%
                    for (UserData user : data) {
                        out.print("<br>Username: " + user.getUsername());
                        out.print("<br>Password: " + user.getPassword());
                        out.print("<br>Role: " + user.getRole());
                    }
                %>
            </div>
        </section>
        <!-- Body -- Database Functions -->
        <% if (session.getAttribute("role").equals("Admin")) { %>
        
        <section>
            <!-- DBMS functions -->
            <div id="optionsPrompt">
                <button onclick="addPrompt()">Add</button>
                <button onclick="updatePrompt()">Update</button>
                <button onclick="deletePrompt()">Delete</button>
            </div>

            <div class="hidden" id="addPrompt">
                <h3>Add</h3>
                <form action="insert" method="POST" id="addForm" name="addForm">
                    <label for="add-username">Username</label>
                    <input type="email" name="username" id="add-username" required>

                    <label for="add-password">Password</label>
                    <input type="password" name="password" id="add-password" required>
                    
                    <label for="add-confirmpassword">Confirm Password</label>
                    <input type="password" name="confirmpassword" id="add-confirmpassword" required>

                    Role:
                    <input type="radio" name="role" id="add-roleAdmin" value="Admin" required>
                    <label for="add-roleAdmin">Admin</label>
                    <input type="radio" name="role" id="add-roleGuest" value="Guest" required>
                    <label for="add-roleGuest">Guest</label>
                    
                    <button type="button" onclick="cancel()">Cancel</button>
                    <button type="submit" form="addForm">Submit</button>
                </form>
            </div>
            
            <div class="hidden" id="updatePrompt">
                <h3>Update</h3>
                <form action="update" method="POST" id="updateForm" name="updateForm">
                    <label for="update-username">Username</label>
                    <select name="username" id="update-username">
                        <%          
                            for (UserData user : data) {
                                out.print("<option value='" + user.getUsername() + "'>" + user.getUsername() + "</option>");
                            }
                        %>
                    </select>

                    <label for="update-password">Password</label>
                    <input type="password" name="password" id="update-password" required>
                    
                    <label for="update-confirmpassword">Confirm Password</label>
                    <input type="password" name="confirmpassword" id="update-confirmpassword" required>
                    
                    Role:
                    <input type="radio" name="role" id="update-roleAdmin" value="Admin" required>
                    <label for="update-roleAdmin">Admin</label>
                    <input type="radio" name="role" id="update-roleGuest" value="Guest" required>
                    <label for="update-roleGuest">Guest</label>

                    <button type="button" onclick="cancel()">Cancel</button>
                    <button type="submit" form="updateForm">Submit</button>
                </form>
            </div>
                    
            <div class="hidden" id="deletePrompt">
                <h3>Delete</h3>
                <form action="delete" method="POST" id="deleteForm" name="deleteForm">
                    <label for="delete-username">Username</label>
                    <select name="username" id="delete-username">
                        <%
                                for (UserData user : data) {
                                    out.print("<option value='" + user.getUsername() + "'>" + user.getUsername() + "</option>");
                                }
                        %>
                    </select>

                    <button type="button" onclick="cancel()">Cancel</button>
                    <button type="submit" form="deleteForm">Submit</button>
                </form>
            </div>        
        </section>
        
        <% } %>
        <!-- Footer -->
        <footer>

        </footer>
    </body>
</html>
