package Servlets;

import BB.Account;
import BB.SQLTablesManager;
import DAOs.AccountDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response){
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        SQLTablesManager db = (SQLTablesManager) request.getServletContext().getAttribute("db");
        AccountDAO AccDao = db.getAccDAO();
        try {
            Account temp = AccDao.tryLogin(username, password);
            if (temp == null){
                request.getSession().setAttribute("error", "either the username, or password, or both were incorrect");
                RequestDispatcher rd = request.getRequestDispatcher("login.jsp");
                rd.forward(request, response);
            } else {
                request.getSession().setAttribute("account", temp);
                request.getSession().setAttribute("error", "");
                request.getSession().setAttribute("buyError", "");
                RequestDispatcher rd = request.getRequestDispatcher("index.jsp");
                rd.forward(request, response);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
    }
}
