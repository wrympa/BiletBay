package Servlets;

import BB.SQLTablesManager;
import DAOs.AccountDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
@WebServlet(name = "CreateAccountServlet", urlPatterns = "/createAccount")
public class CreateAccountServlet extends HttpServlet {
   private boolean invalidNumber(String number) {
        if (number.length() != 9 || number.charAt(0) != '5') return true;
        for (int i = 0; i < number.length(); i++) {
            if (!Character.isDigit(number.charAt(i))) return true;
        }
        return false;
    }
    private boolean badInput(AccountDAO AccDao, SQLTablesManager baseManager, HttpSession session, String username, String password, String repeatPassword, String number) throws SQLException {
        if (username.length() < 4) {
            session.setAttribute("error", "Username must contain at least four symbols. Try new one.");
            return true;
        } else if (AccDao.contains(username)) {
            session.setAttribute("error", "Username is already used. Try new one.");
            return true;
        } else if (password.length() < 4) {
            session.setAttribute("error", "Password is too short. Try new one.");
            return true;
        } else if (!password.equals(repeatPassword)) {
            session.setAttribute("error", "Passwords don't match. Try again.");
            return true;
        } else if (invalidNumber(number)) {
            session.setAttribute("error", "Phone number is invalid. Try again.");
            return true;
        }
        return false;
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        SQLTablesManager baseManager = (SQLTablesManager) request.getServletContext().getAttribute("db");
        AccountDAO AccDAO = (AccountDAO) request.getServletContext().getAttribute("accdao");

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String repeatPassword = request.getParameter("repeatPassword");
        String number = request.getParameter("number");

        try {
            if (badInput(AccDAO, baseManager, session, username, password, repeatPassword, number)) {
                RequestDispatcher dispatch = request.getRequestDispatcher("create_account.jsp");
                dispatch.forward(request, response);
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        boolean haspic = false;
        for (Part p : request.getParts()) {
            if (p.getContentType() != null) {
                if (p.getContentType().equals("image/jpeg") || p.getContentType().equals("image/png")) {
                    haspic = true;
                    p.write(System.getProperty("user.dir") + "/src/main/webapp/pfps/" + request.getParameter("username") + ".jpg");
                }
            }
        }
        try {
            if (haspic) {
                AccDAO.addAccount(username, username + ".jpg", number, password);
            } else {
                AccDAO.addAccount(username, "default.jpg", number, password);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        RequestDispatcher dispatch = request.getRequestDispatcher("login.jsp");
        dispatch.forward(request, response);
    }
}
