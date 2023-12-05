package Servlets;

import BB.Account;
import BB.SQLTablesManager;
import DAOs.AccountDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import javax.servlet.annotation.MultipartConfig;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
@WebServlet(name = "EditProfileServlet", urlPatterns = "/editProfile")
public class EditProfileServlet extends HttpServlet {
    private boolean invalidNumber(String number) {
        if (number.length() == 0) return false;
        if (number.length() != 9 || number.charAt(0) != '5') return true;
        for (int i = 0; i < number.length(); i++) {
            if (!Character.isDigit(number.charAt(i))) return true;
        }
        return false;
    }
    private boolean badInput(AccountDAO AccDAO, Account account, HttpSession session, String username, String password, String repeatPassword, String number, String oldPassowrd) throws SQLException {
        if (username.length() < 4 && username.length() != 0) {
            session.setAttribute("editError", "New username must contain at least four symbols. Try new one.");
            return true;
        } else if (AccDAO.contains(username)) {
            session.setAttribute("editError", "New username is already used. Try new one.");
            return true;
        } else if (!account.checkPassToChange(oldPassowrd) && password.length() != 0) {
            session.setAttribute("editError", "Old password you entered is incorrect. Try again.");
            return true;
        } else if (password.length() < 4 && password.length() != 0) {
            session.setAttribute("editError", "New Password is too short. Try new one.");
            return true;
        } else if (!password.equals(repeatPassword)) {
            session.setAttribute("editError", "Passwords don't match. Try again.");
            return true;
        } else if (invalidNumber(number)) {
            session.setAttribute("editError", "New phone number is invalid. Try again.");
            return true;
        }
        return false;
    }

    private void deleteImage(Account account) {
        if (!account.getImage().equals("default.jpg")) {
            Path imagesPath = Paths.get(
                    System.getProperty("user.dir") + "/src/main/webapp/pfps/" + account.getImage());
            try {
                Files.delete(imagesPath);
                System.out.println("File "
                        + imagesPath.toAbsolutePath().toString()
                        + " successfully removed");
            } catch (IOException e) {
                System.err.println("Unable to delete "
                        + imagesPath.toAbsolutePath().toString()
                        + " due to...");
                e.printStackTrace();
            }
        }
        account.changeImage("default.jpg");
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        Account account = (Account) session.getAttribute("account");
        SQLTablesManager baseManager = (SQLTablesManager) request.getServletContext().getAttribute("db");
        AccountDAO AccDAO = baseManager.getAccDAO();
        if (request.getParameter("action").equals("Remove Image")) {
            deleteImage(account);
            updateTable(account, account.getUsername(), AccDAO);
            RequestDispatcher dispatch = request.getRequestDispatcher("my_page.jsp");
            dispatch.forward(request, response);
            return;
        }
        String username = request.getParameter("username");
        String oldPassword = request.getParameter("oldPassword");
        String newPassword = request.getParameter("password");
        String repeatPassword = request.getParameter("repeatPassword");
        String number = request.getParameter("number");

        String oldUsername = account.getUsername();
        try {
            if (badInput(AccDAO, account, session, username, newPassword, repeatPassword, number, oldPassword)) {
                RequestDispatcher dispatch = request.getRequestDispatcher("edit_profile.jsp");
                dispatch.forward(request, response);
                return;
            } else {
                if (username.length() != 0) {
                    account.changeUsername(username);
                }
                if (newPassword.length() != 0) account.changePassword(oldPassword, newPassword);
                if (number.length() != 0) account.changePhoneNumber(number);
//
//                if (request.getParameter("removeImage") != null) {
//                    deleteImage(account);
//                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        for (Part p : request.getParts()) {
            if (p.getContentType() != null) {
                if (p.getContentType().equals("image/jpeg") || p.getContentType().equals("image/png")) {
                    deleteImage(account);
                    p.write(System.getProperty("user.dir") + "/src/main/webapp/pfps/" + account.getUsername() + ".jpg");
                    account.changeImage(account.getUsername() + ".jpg");
                }
            }
        }
        updateTable(account, oldUsername, AccDAO);
        RequestDispatcher dispatch = request.getRequestDispatcher("my_page.jsp");
        dispatch.forward(request, response);
    }
    private void updateTable(Account account, String oldUsername, AccountDAO AccDAO) {
        try {
            AccDAO.editAccount(oldUsername, account.getUsername(), account.getImage(), account.getPhoneNumber(), account.getPasswordHash());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatch = request.getRequestDispatcher("my_page.jsp");
        dispatch.forward(request, response);
    }
}
