/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package unused;

import user.UserData;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.*;

/**
 *
 * @author Jam
 */
public class AppServlet extends HttpServlet {

    String driver, url, dbuser, dbpass;
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        driver = getServletContext().getInitParameter("driver");
        url = getServletContext().getInitParameter("url");
        dbuser = getServletContext().getInitParameter("user");
        dbpass = getServletContext().getInitParameter("pass");
    }

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
            
        
        // For reloading Attributes
        String messagetype = (String) request.getAttribute("message-type");
        String message = (String) request.getAttribute("message");
        request.setAttribute("message-type", messagetype);
        request.setAttribute("message", message);
        
        if (message != null) {
            System.out.println("0)" + request.getAttribute("message"));
        }

        System.out.println("---------------------------------------------");
        try {
            // Load Driver & Establishing Connection
            Class.forName(driver);
            System.out.println("1) Loaded Driver: " + driver);
            Connection conn = DriverManager.getConnection(url, dbuser, dbpass);
            System.out.println("2) Connected to: " + url);

            // Transfer data
            Statement stmt = conn.createStatement();
            String query = "SELECT * FROM user_info ORDER BY username ASC";
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("3) Executed Query: " + query);

            ArrayList<UserData> data = new ArrayList<UserData>();
            System.out.println("4) Recording Queries...");

            while (rs.next()) {
                data.add(new UserData(rs.getString("username"), rs.getString("password"), rs.getString("role")));
            }

            for (UserData x : data) {
                System.out.println("-\tUsername: " + x.getUsername() + "\t\tPassword: " + x.getPassword() + "\t\tRole: " + x.getRole());
            }

            // Close the connection
            rs.close();
            stmt.close();
            conn.close();

            System.out.println("5) Data recorded... Transferring data");

            request.getSession().setAttribute("data", data);
            request.getRequestDispatcher("success.jsp").forward(request, response);

            System.out.println("6) Data transferred successfully!");

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
