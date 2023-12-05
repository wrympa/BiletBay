package Servlets;

import BB.Account;
import BB.Ticket;
import BB.SQLTablesManager;
import DAOs.TicketDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

public class RemoveTicketServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String tickedId = request.getParameter("remtickid");
        HttpSession session = request.getSession();
        SQLTablesManager db = (SQLTablesManager)request.getServletContext().getAttribute("db");
        TicketDAO TickDAO = db.getTickDAO();
        Ticket t = null;
        try {
            t = TickDAO.getticketbyname(tickedId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        ((Account)session.getAttribute("account")).removeTicket(t.getTicket_id());
        try {
            TicketDAO.removeticket(t.getTicket_id());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        RequestDispatcher dispatch = request.getRequestDispatcher("my_page.jsp");
        dispatch.forward(request, response);
    }
}
