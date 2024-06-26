package backend;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */


import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class UpdateServlet extends HttpServlet {
    
    String driver, url, dbuser, dbpass, key, cipher;
    Security sec;
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        driver = getServletContext().getInitParameter("driver");
        url = getServletContext().getInitParameter("url");
        dbuser = getServletContext().getInitParameter("user");
        dbpass = getServletContext().getInitParameter("pass");
        key = getServletContext().getInitParameter("key");
        cipher = getServletContext().getInitParameter("cipher");
        sec = new Security(key, cipher);
    }
    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException  {
        
        
        
        HttpSession session = request.getSession();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmpassword = request.getParameter("confirmpassword");
        String role = request.getParameter("role");             
        
        
        
        // Inserted password is being encrypted
        String encryptedPassword = sec.encrypt(password);       
        System.out.println("0) Encrypting Password ");
        System.out.println("-- Password: " + password);
        System.out.println("-- Encrypted Password: " + encryptedPassword);
         
        
        try {
            // Safety Protocols (Not used at the moment)
            /* 
            System.out.println("1) Initializing Preliminary Safety Protocols...");
            
            if (!password.equals(confirmpassword)) {
                System.out.println("-- Error: Passwords do not match!");
                
                request.setAttribute("message-type", "error");
                request.setAttribute("message", "Passwords do not match!");
                request.getRequestDispatcher("/app").forward(request, response);
                return;
            }
            if (session.getAttribute("username").equals(username) && role.equals("Guest")) {
                System.out.println("-- Error: You cannot set your own role to a Guest!");
                
                request.setAttribute("message-type", "error");
                request.setAttribute("message", "You cannot set your own role to a Guest!");
                request.getRequestDispatcher("/app").forward(request, response);
                return;
            }
            */
            
            // Load Driver & Establishing Connection
            Class.forName(driver);
            System.out.println("2) Loaded Driver: " + driver);
            Connection conn = DriverManager.getConnection(url, dbuser,dbpass);
            System.out.println("3) Connected to: " + url);

            // Delete User
            String query = "UPDATE user_info SET password=?, role=? WHERE username=?";
            PreparedStatement update = conn.prepareStatement(query);    
            update.setString(1, encryptedPassword);
            update.setString(2, role);
            update.setString(3, username);
            int rows = update.executeUpdate();
 
            if (rows > 0) {
                System.out.println("4) User " + username + " has been updated successfully!");
                
                request.setAttribute("message-type", "success");
                request.setAttribute("message", "User " + username + " has been updated successfully!");
            }
            else {
                System.out.println("-- Error: Something went wrong! ");
                
                request.setAttribute("message-type", "error");
                request.setAttribute("message", "Something went wrong!");
            }

            // Close the connection
            update.close();
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
