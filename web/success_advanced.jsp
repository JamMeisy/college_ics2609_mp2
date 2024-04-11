<%-- 
    Document   : success_advanced
    Created on : 02 28, 24, 2:26:17 PM
    Author     : Lejan Juico
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import = "exceptions.InvalidSessionException"%>
<%@page import = "user.UserData"%>
<%@page import = "backend.Security"%>
<%@page import = "java.util.ArrayList"%>

<%
    // Storing Data 
    String username = (String) session.getAttribute("username");
    String role = (String) session.getAttribute("role");
    String header = (String) getServletContext().getInitParameter("Header");
    String footer = (String) getServletContext().getInitParameter("Footer");
    String message = (String) request.getAttribute("message");
    String messageType = (String) request.getAttribute("message-type");
    
    System.out.println("-- Current User:" + username);
    
    //For now this serves as an easy way to add encrypted passwords
    /*
    // Disable Caching
    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate"); //HTTP 1.1 response.setHeader("Pragma", "no-cache"); // HTTP 1.0
    response.setDateHeader("Expires", 0); // Proxies 
    
    // Verifying the Session
    if (username == null || username.equals("")) {
        throw new InvalidSessionException("Unauthorized Access");
    }
    // Nullifying session atttribute to logout when exited 
    session.removeAttribute("username");
    */
    
    username = "ADMIN CONSOLE";
    role = "Console";
    
    // Instantiating the collected data from UserDB
    ArrayList<UserData> data = (ArrayList<UserData>) request.getAttribute("data");
    Security sec = new Security(getServletContext().getInitParameter("key"), getServletContext().getInitParameter("cipher"));
%>

<!-- Sources here should be blended with original -->
<!DOCTYPE html>
<html>
    <head>
        <title>Company DBMS</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="static/styles-success-advanced.css">
        <script type="text/javascript" src="./admin.js"></script>
    </head>
    <body>
        <!-- Header -->
        <header>
            <nav>
                <div class="left-item">
                    <img src="<%= header %>" alt="logo" />
                </div>
                <div class="center-item">
                    <a href="logout" class="logout-button">Logout</a>
                </div>
                <div class="right-item">
                    <span class="header-text"><%= username %></span>
                </div>
            </nav>
        </header>

        <!-- Body -- Greeting Prompt -->
        <div class="content">
        <div>
            <section>
                <h1>Welcome <%= username %></h1>
                <h2>Your role is: <%= role %></h2>      
            </section>

            <% if (role.equals("Console")) { %> 
            <!-- Body -- Display Database -->   
            <section>
                <div class="dbdisplay">
                    <table>
                        <tr>
                            <th>Username</th>
                            <th>Password</th>
                            <th>Role</th>
                            <th>Decrypted</th>
                        </tr>
                    <%        
                        for (UserData user : data) {
                            out.print("<tr>");
                            out.print("<td>" + user.getUsername() + "</td>");
                            out.print("<td>" + user.getPassword() + "</td>");
                            out.print("<td>" + user.getRole() + "</td>");
                            out.print("<td>" + sec.decrypt(user.getPassword()) + "</td>");
                            out.print("</tr>");
                        }
                    %>
                    </table>
                </div>
            </section>
        </div>   
                
        <!-- Body -- Database Functions -->     
        <section class="dbmsfunctions">
             <!-- Body -- Error / Success Message with Coded Color based on type-->
            <% if (message != null) { %>
                <div class="
                         <%= (messageType.equals("success")) ? "success" : "error" %>"
                         id = "message-box">    

                    <h3><%= message %></h3>
                    <button type="button" onclick="removeMessage()">X</button>       
                </div>
            <% } %>
            
            
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
                    <div class="role-selection">
                        <input type="radio" name="role" id="add-roleAdmin" value="Admin" required>
                        <label for="add-roleAdmin">Admin</label>
                        <input type="radio" name="role" id="add-roleGuest" value="Guest" required>
                        <label for="add-roleGuest">Guest</label>
                    </div>
                    
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
                    <div class="role-selection">
                        <input type="radio" name="role" id="update-roleAdmin" value="Admin" required>
                        <label for="update-roleAdmin">Admin</label>
                        <input type="radio" name="role" id="update-roleGuest" value="Guest" required>
                        <label for="update-roleGuest">Guest</label>
                    </div>    
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
        </div>
        <!-- Footer -->
        <footer class="footer">
            <%= footer %>
        </footer>
    </body>
</html>
