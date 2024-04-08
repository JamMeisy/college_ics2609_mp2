package backend;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
import exceptions.*;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class LoginServlet extends HttpServlet {

    String driver, url, dbuser, dbpass;

    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        driver = config.getInitParameter("driver");
        url = config.getInitParameter("url");
        dbuser = config.getInitParameter("user");
        dbpass = config.getInitParameter("pass");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String userCaptcha = request.getParameter("captcha");
        String generatedCaptcha = (String) session.getAttribute("captcha");

        System.out.println("---------------------------------------------");
        try {
            // Load Driver & Establishing Connection
            Class.forName(driver);
            System.out.println("1) Loaded Driver: " + driver);
            Connection conn = DriverManager.getConnection(url, dbuser, dbpass);
            System.out.println("2) Connected to: " + url);

            // Login Verification
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM user_info WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            System.out.println("3) Executed Query: " + query);

            System.out.println("4) Verifying Login Credentials");

            if (!rs.next()) {
                // User not in DB
                System.out.println("--- Username \"" + username + "\" does not exist");
                if (password.isEmpty()) {
                    // Password is blank
                    throw new WrongUserNullPassException("Incorrect Username, Blank Password");
                } else {
                    // Password is incorrect
                    throw new AuthenticationType2Exception("Incorrect Username, Incorrect Password");
                }
            }

            String verify = rs.getString("password");
            String role = rs.getString("role");

            if (!password.equals(verify)) {
                // Incorrect Password
                throw new AuthenticationType1Exception("Correct Username, Incorrect Password");
            }

            System.out.println("5) Verification Successful");

            if (generatedCaptcha == null || !generatedCaptcha.equals(userCaptcha)) {
                throw new WrongCaptchaException("CAPTCHA verification failed");
            }
            
            System.out.println("6) Captcha Verification Successful");
            
            session.setAttribute("username", username);
            session.setAttribute("role", role);

            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);

            response.sendRedirect("success.jsp");

            // Close the connection
            rs.close();
            pstmt.close();
            conn.close();

        } catch (SQLException | ClassNotFoundException sqle) {
            sqle.printStackTrace();
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
