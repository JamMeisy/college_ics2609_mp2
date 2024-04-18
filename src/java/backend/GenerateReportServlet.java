/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package backend;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import exceptions.InvalidSessionException;
import java.io.OutputStream;
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

            ArrayList<UserData> data = new ArrayList<>();
            System.out.println("4) Recording Queries...");

            while (rs.next()) {
                data.add(new UserData(rs.getString("username"), rs.getString("password"), rs.getString("role")));
            }

            // Close the connection
            rs.close();
            stmt.close();
            conn.close();

            System.out.println("5) Data recorded... Generating...");

            System.out.println("---------------------------------------------");
            System.out.println("GENERATING REPORT / CREDENTIALS");
            System.out.println("---------------------------------------------");

            // Get user info
            String userRole = (String) request.getAttribute("role");
            String userPass = (String) request.getAttribute("password");
            String username = (String) request.getAttribute("username");
            
            System.out.println(username);
            
            // Security
            // boolean authorized = false;
            for (UserData i : data) {
                if (i.getUsername().equals(username)) {
                    if (i.getPassword().equals(sec.encrypt(userPass))) {
                        //authorized = true;
                        break;
                    }
                    else
                        break;
                }
            }
            
            //if (!authorized)
            //    throw new InvalidSessionException("Unauthorized Access");
            

            // Set content type
            response.setContentType("application/pdf");

            // Create PDF document
            try (OutputStream out = response.getOutputStream()) {
                Document document = new Document(PageSize.LETTER);
                PdfWriter writer = PdfWriter.getInstance(document, out);

                document.open();

                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC);

                Paragraph header = new Paragraph("DATABASE REPORT", headerFont);
                header.setAlignment(Element.ALIGN_CENTER);
                document.add(header);
                document.add(Chunk.NEWLINE);

                // Create a PdfPTable with 2 columns if the user is not an admin, otherwise 3 columns
                PdfPTable table = new PdfPTable("Admin".equals(userRole) ? 3 : 2);

                Font boldFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 15);

                // Create table header cells
                PdfPCell cellUsername = new PdfPCell(new Phrase("Username", boldFont));
                cellUsername.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellUsername.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellUsername.setPadding(8);
                cellUsername.setFixedHeight(40);
                cellUsername.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cellUsername.setBorderWidth(1f);
                cellUsername.setBorderColor(BaseColor.BLACK);

                PdfPCell cellRole = new PdfPCell(new Phrase("Role", boldFont));
                cellRole.setHorizontalAlignment(Element.ALIGN_CENTER);
                cellRole.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cellRole.setPadding(8);
                cellRole.setFixedHeight(40);
                cellRole.setBackgroundColor(BaseColor.LIGHT_GRAY);
                cellRole.setBorderWidth(1f);
                cellRole.setBorderColor(BaseColor.BLACK);

                // Add table headers
                table.addCell(cellUsername);
                table.addCell(cellRole);

                // Add user data to the table
                for (UserData userData : data) {
                    PdfPCell usernameCell = new PdfPCell(new Phrase(userData.getUsername(), regularFont));
                    PdfPCell roleCell = new PdfPCell(new Phrase(userData.getRole(), regularFont));

                    table.addCell(usernameCell);
                    table.addCell(roleCell);
                }

                document.add(table);

                // Footer
                Font footerFont = new Font(Font.FontFamily.HELVETICA, 15, Font.ITALIC);

                PdfContentByte cb = writer.getDirectContent();
                cb.beginText();
                cb.setFontAndSize(footerFont.getCalculatedBaseFont(false), 15); 
                cb.setTextMatrix(document.leftMargin(), document.bottomMargin() - 10); 
                cb.showText("Currently logged in user: " + username); 
                cb.endText();

                // Adds the page number (x of y)
                int totalPages = writer.getPageNumber();
                for (int i = 1; i <= totalPages; i++) {
                    cb.beginText();
                    cb.setFontAndSize(footerFont.getCalculatedBaseFont(false), 15); 
                    cb.setTextMatrix(document.right() - 100, document.bottomMargin() - 10); 
                    cb.showText("Page " + i + " of " + totalPages); 
                    cb.endText();
                }

                document.close();
            }

        } catch (SQLException | ClassNotFoundException | DocumentException sqle) {
            sqle.printStackTrace();
            response.getWriter().println("Error generating report.");
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
