package test;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import exceptions.AuthenticationType1Exception;
import exceptions.WrongUserNullPassException;
import exceptions.AuthenticationType2Exception;
import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class DBMSServlet extends HttpServlet {
    
    String driver, url, dbuser, dbpass;
    public void init(ServletConfig config) throws ServletException {
        driver = getServletContext().getInitParameter("driver");
        url = getServletContext().getInitParameter("url");
        dbuser = getServletContext().getInitParameter("user");
        dbpass = getServletContext().getInitParameter("pass");        
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        
        HttpSession session = request.getSession();

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            // Load Driver & Establishing Connection
            Class.forName(driver);
            System.out.println("1) Loaded Driver: " + driver);
            Connection conn = DriverManager.getConnection(url, dbuser,dbpass);
            System.out.println("2) Connected to: " + url);
            
            // Login Verification
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM user_info WHERE username = " + username;
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("3) Executed Query: " + query);
            
                // TODO: Implement counting login attempts
                
            if (username == null)
                // User is blank
                throw new NullPointerException();
            
            else if (!rs.next()) {
                
                // User not in DB
                System.out.println("4) Username \"" + username + "\" does not exist");
                
                if (password == null)
                    // Pass is blank
                    throw new WrongUserNullPassException("Incorrect Username, Blank Password");
                
                    // Pass is incorrect
                    throw new AuthenticationType2Exception("Incorrect Username, Incorrect Password");    
            }
            
            else {
                System.out.println("4) Username \"" + username + "\" exists!");
                String verify = rs.getString("password");
                String role = rs.getString("role");
                
                if (!password.equals(verify))
                    throw new AuthenticationType1Exception("Correct Username, Incorrect Password");
               
                session.setAttribute("username", username);
                session.setAttribute("role", role);
                response.sendRedirect("success.jsp");
               
            }
            
            // Close the connection
            rs.close();
            stmt.close();
            conn.close();
            
        } catch (SQLException | ClassNotFoundException sqle) {
            sqle.printStackTrace();
        } catch (WrongUserNullPassException | AuthenticationType1Exception | AuthenticationType2Exception e) {
            e.printStackTrace();
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
