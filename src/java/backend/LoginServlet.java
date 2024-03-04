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
    
    // Takes data from ServletConfig, NOT ServletContext
    String driver, url, dbuser, dbpass;
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        driver = config.getInitParameter("driver");
        url = config.getInitParameter("url");
        dbuser = config.getInitParameter("user");
        dbpass = config.getInitParameter("pass");        
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        System.out.println("---------------------------------------------");
        try {
            // Load Driver & Establishing Connection
            Class.forName(driver);
            System.out.println("1) Loaded Driver: " + driver);
            Connection conn = DriverManager.getConnection(url, dbuser,dbpass);
            System.out.println("2) Connected to: " + url);
            
            // Login Verification
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM user_info";
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("3) Executed Query: " + query);
                
            System.out.println("4) Verifying Login Credentials");
            
            // Case 1: User is blank
            if (username.equals(""))
                throw new NullPointerException();
            
            boolean userExists = false;
            while (rs.next()) {
                String checkUser = rs.getString("username");
                if (username.equals(checkUser)) {
                    userExists = true;
                    break;
                }
            }
            
            // Case 2 & 3: User does not exist
            if (!userExists) {
                System.out.println("--- Username \"" + username + "\" does not exist");
                System.out.println("--- Password = \"" + password + "\"");
                
                // Case 2: No Password
                if (password.equals(""))
                    throw new WrongUserNullPassException("Incorrect Username, Blank Password");
                // Case 3: Password is incorrect
                else                  
                    throw new AuthenticationType2Exception("Incorrect Username, Incorrect Password");    
            }
            
            else {
                System.out.println("--- Username \"" + username + "\" exists!");
                String verify = rs.getString("password");
                String role = rs.getString("role");
                
                // Case 4: Correct Username with Incorrect Password
                if (!password.equals(verify))
                    throw new AuthenticationType1Exception("Correct Username, Incorrect Password");
               
                System.out.println("5) Verification Successful");
                
                session.setAttribute("username", username);
                session.setAttribute("role", role);
                
                // Directly send to desired page with session attributes (no data transferred)
                // Can be modified for ADVANCED
                response.sendRedirect("success.jsp");   
            }
            
            // Close the connection
            rs.close();
            stmt.close();
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
