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
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
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

            username = (String) request.getSession().getAttribute("username");
            userRole = (String) request.getSession().getAttribute("role");
            userPass = (String) request.getSession().getAttribute("password");

            System.out.println(username);
            System.out.println(userRole);
            System.out.println(userPass);

            // Security
            boolean authorized = false;
            for (UserData i : data) {
                if (i.getUsername().equals(username)) {
                    if (i.getPassword().equals(sec.encrypt(userPass))) {
                        authorized = true;
                        break;
                    } else {
                        break;
                    }
                }
            }

            if (!authorized) {
                throw new InvalidSessionException("Unauthorized Access");
            }
            // Set content type
            response.setContentType("application/pdf");

            // Create PDF document
            try (OutputStream out = response.getOutputStream()) {
                Document document;
                if ("Admin".equals(userRole)) {
                    document = new Document(PageSize.LETTER);
                } else {
                    Rectangle customPageSize = new Rectangle(400f, 250f); // 1 inch = 72 points
                    document = new Document(customPageSize);
                }
                PdfWriter writer = PdfWriter.getInstance(document, out);

                // HEADER AND FOOTER
                if ("Admin".equals(userRole)) {
                    PdfHeaderFooter event = new PdfHeaderFooter(username, userRole, data.size());
                    writer.setPageEvent(event);
                } else {
                    PdfHeaderFooter event = new PdfHeaderFooter(username, userRole, 1);
                    writer.setPageEvent(event);
                }

                document.open();

                if ("Admin".equals(userRole)) {
                    float[] columnWidths = {8f, 40f, 20f};
                    PdfPTable table = new PdfPTable(columnWidths);

                    table.setHeaderRows(1);

                    Font boldFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
                    Font regularFont = FontFactory.getFont(FontFactory.HELVETICA, 15);

                    PdfPCell cellNum = new PdfPCell(new Phrase("No.", boldFont));
                    cellNum.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cellNum.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellNum.setPadding(5);
                    cellNum.setFixedHeight(40);
                    cellNum.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    cellNum.setBorderWidth(1f);
                    cellNum.setBorderColor(BaseColor.BLACK);
                    cellNum.setColspan(1);

                    PdfPCell cellUsername = new PdfPCell(new Phrase("Username", boldFont));
                    cellUsername.setHorizontalAlignment(Element.ALIGN_CENTER);
                    cellUsername.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    cellUsername.setPadding(5);
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

                    table.addCell(cellNum);
                    table.addCell(cellUsername);
                    table.addCell(cellRole);

                    int cellCount = 0;
                    int numCell = 1;
                    for (UserData userData : data) {
                        PdfPCell numCellCell = new PdfPCell(new Phrase(String.valueOf(numCell), regularFont));
                        numCellCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                        PdfPCell usernameCell = new PdfPCell(new Phrase(userData.getUsername(), regularFont));
                        usernameCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                        PdfPCell roleCell = new PdfPCell(new Phrase(userData.getRole(), regularFont));
                        roleCell.setHorizontalAlignment(Element.ALIGN_CENTER);

                        table.addCell(numCellCell);
                        table.addCell(usernameCell);
                        table.addCell(roleCell);

                        numCell++;
                        cellCount++;

                        if (cellCount == 25) {
                            document.add(table);
                            document.newPage();
                            table.deleteBodyRows();
                            cellCount = 0;
                        }
                    }
                    document.add(table);
                } else if ("Guest".equals(userRole)) {
                    PdfPTable table = new PdfPTable(1);
                    PdfPCell headerCell = new PdfPCell(new Phrase("Credentials"));
                    headerCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                    headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                    headerCell.setBackgroundColor(BaseColor.LIGHT_GRAY);
                    headerCell.setPadding(5);
                    table.addCell(headerCell);

                    // Add username, password, and role to the table
                    table.addCell("Username: " + username);
                    table.addCell("Password: " + userPass);

                    document.add(table);
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

    class PdfHeaderFooter extends PdfPageEventHelper {

        private String username;
        private String role;
        private int maxPage;

        public PdfHeaderFooter(String username, String role, int dataLength) {
            this.username = username;
            this.role = role;
            this.maxPage = (int) Math.ceil(dataLength / 25.0);
            
            if (maxPage < 1){
                maxPage = 1;
            }
        }

        @Override
        public void onStartPage(PdfWriter writer, Document document) {
            Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLDITALIC);
            Paragraph header = new Paragraph(role.toUpperCase() + " REPORT", headerFont);
            header.setAlignment(Element.ALIGN_CENTER);
            try {
                document.add(header);
                document.add(Chunk.NEWLINE);
            } catch (DocumentException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onEndPage(PdfWriter writer, Document document) {
            PdfContentByte cb = writer.getDirectContent();
            Font footerFont = FontFactory.getFont(FontFactory.HELVETICA, 10, Font.ITALIC);

            // Footer
            cb.beginText();
            cb.setFontAndSize(footerFont.getBaseFont(), 10);
            cb.setTextMatrix(document.leftMargin(), document.bottomMargin() - 10);
            cb.showText("Current User: " + username);
            cb.endText();

            // Page number
            int pageNumber = writer.getPageNumber();
            cb.beginText();
            cb.setFontAndSize(footerFont.getBaseFont(), 10);
            cb.setTextMatrix(document.right() - 100, document.bottomMargin() - 10);
            cb.showText("Page " + pageNumber + " of " + maxPage);
            cb.endText();
        }
    }

}
