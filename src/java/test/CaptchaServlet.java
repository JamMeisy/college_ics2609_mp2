package test;

import javax.servlet.*;
import javax.servlet.http.*;
import java.io.*;
import java.util.*;

public class CaptchaServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Set content type of the response so that the browser knows what to expect.
        response.setContentType("text/plain");

        // Create a CAPTCHA string
        String captcha = generateCaptcha(8); // Set the length of the CAPTCHA string as needed

        // Store the CAPTCHA string in the session for later verification
        HttpSession session = request.getSession();
        session.setAttribute("captcha", captcha);

        // Write CAPTCHA string to the response
        PrintWriter out = response.getWriter();
        out.print(captcha);
        out.flush();
    }

    // Generates a random CAPTCHA string of given length
    public String generateCaptcha(int length) {
        Random rand = new Random();
        StringBuilder captcha = new StringBuilder();
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

        for (int i = 0; i < length; i++) {
            captcha.append(characters.charAt(rand.nextInt(characters.length())));
        }

        return captcha.toString();
    }

    protected boolean checkCaptcha(HttpSession session, String userCaptcha) {
        String captcha = (String) session.getAttribute("captcha");
        return captcha != null && captcha.equals(userCaptcha);
    }
}
