/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package backend;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itextpdf.text.Document;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import javax.servlet.ServletConfig;
import user.UserData;

/**
 *
 * @author Jam
 */
public class GenerateReportServlet extends HttpServlet {
    
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
        
        // Generating Data
        System.out.println("LOADING REPORT / CREDENTIALS");
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

            System.out.println("5) Data recorded... Generating...");  

            System.out.println("---------------------------------------------");
            System.out.println("GENERATING REPORT / CREDENTIALS");
            System.out.println("---------------------------------------------");

            if (request.getAttribute("role") == "Admin") {
                for ( UserData i : data)
                {
                    for (UserData j : data) {
                        
                    }
                }
            }
            else {

            }   

        } catch (SQLException | ClassNotFoundException sqle) {
            sqle.printStackTrace();
        }
        
        
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */

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
