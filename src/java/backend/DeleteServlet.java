package backend;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class DeleteServlet extends HttpServlet {
    
    String driver, url, dbuser, dbpass;
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        driver = getServletContext().getInitParameter("driver");
        url = getServletContext().getInitParameter("url");
        dbuser = getServletContext().getInitParameter("user");
        dbpass = getServletContext().getInitParameter("pass");        
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        
        HttpSession session = request.getSession();
        String username = request.getParameter("username");

        System.out.println("---------------------------------------------");
        try {
            // Safety Protocols
            System.out.println("1) Initializing Preliminary Safety Protocols...");
            
            if (session.getAttribute("username").equals(username)) {
                System.out.println("-- Error: You cannot delete user of current session!");
                
                request.setAttribute("message-type", "error");
                request.setAttribute("message", "You cannot delete user of current session!");
                request.getRequestDispatcher("/app").forward(request, response);
                return;
            }
            
            // Load Driver & Establishing Connection
            Class.forName(driver);
            System.out.println("2) Loaded Driver: " + driver);
            Connection conn = DriverManager.getConnection(url, dbuser,dbpass);
            System.out.println("3) Connected to: " + url);

            // Delete User
            String query = "DELETE FROM user_info WHERE username=?";
            PreparedStatement delete = conn.prepareStatement(query);
            delete.setString(1, username);
            int rows = delete.executeUpdate();
 
            if (rows > 0) {
                System.out.println("4) User " + username + " has been deleted successfully!");
                
                request.setAttribute("message-type", "success");
                request.setAttribute("message", "User " + username + " has been deleted successfully!");
            }
            else {
                System.out.println("-- Error: Something went wrong! ");
                
                request.setAttribute("message-type", "error");
                request.setAttribute("message", "Something went wrong.");
            }

            // Close the connection
            delete.close();
            conn.close();           
            request.getRequestDispatcher("/app").forward(request, response);
            
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
