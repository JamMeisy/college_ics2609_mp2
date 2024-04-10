<%-- Document : index Created on : 02 23, 24, 11:26:19 AM Author : Jam --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%> 
<%
    // Storing Data 
    String header = (String) getServletContext().getInitParameter("Header");
    String footer = (String) getServletContext().getInitParameter("Footer");
%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <title>Company DBMS</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="static/styles-login.css">
        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Plus+Jakarta+Sans:wght@400&display=swap">

        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script>
            $(document).ready(function () {
                $('#captcha').on("paste", function (e) {
                    e.preventDefault();
                });

                $('#generatedCaptcha').on("copy", function (e) {
                    e.preventDefault();
                });
            });

            function refreshCaptcha() {
                var xhr = new XMLHttpRequest();
                xhr.onreadystatechange = function () {
                    if (xhr.readyState === 4 && xhr.status === 200) {
                        document.getElementById('generatedCaptcha').value = xhr.responseText;
                    }
                };
                xhr.open("GET", "captcha", true);
                xhr.send();
            }

            function onPageShow() {
                refreshCaptcha();
            }

            window.addEventListener("pageshow", onPageShow);
        </script>
    </head>
    <body>
        <!-- Header -->
        <header>
            <nav>
                <div class="left-item">
                    <img src="<%= header %>" alt="logo">
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
                    <input type="text" name="username" id="username" />

                    <label for="password">Password</label>
                    <input type="password" name="password" id="password">

                    <label for="generatedCaptcha">Generated CAPTCHA</label>
                    <input type="text" name="generatedCaptcha" id="generatedCaptcha" readonly onfocus="this.blur()">

                    <label for="captcha">CAPTCHA:</label>
                    <input type="text" name="captcha" id="captcha" onpaste="return false;">

                    <div class="button-container">
                        <button type="button" onclick="refreshCaptcha()" class="refresh-button">&#8635;</button>
                        <button type="submit" class="submit-button">Submit</button>
                    </div>

                    <button type="submit">Submit</button>
                </form>
            </div>
        </section>
        <!-- Footer -->
        <footer class="footer"><%= footer%></footer>
    </body>
</html>

