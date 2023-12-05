package Servlets;

import BB.Account;
import DAOs.RequestDAO;
import DAOs.TicketDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class SellTicketServlet extends HttpServlet {
    private TicketDAO ticketDAO;
    private RequestDAO requestDAO;
    private Account me;
    private void sellTicket(String ids) throws SQLException {
        String sentBy = ids.substring(10);
        String tId = ids.substring(0, 10);
        ticketDAO.changetickettobought(tId, sentBy);
        requestDAO.deleteRequests(tId);
        me.removeTicket(tId);
    }

    private void  rejectUser(String ids) throws SQLException {
        String sentBy = ids.substring(10);
        String tId = ids.substring(0, 10);
        requestDAO.rejectRequest(sentBy, me.getUsername(), tId);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String req = request.getParameter("sellTicket");
        String ids = request.getParameter("ticketIdSentBy");
        me = (Account) request.getSession().getAttribute("account");
        ticketDAO = (TicketDAO) request.getServletContext().getAttribute("ticketdao");
        requestDAO = (RequestDAO) request.getServletContext().getAttribute("requestdao");
        if (req.contains("Sell")) {
            try {
                sellTicket(ids);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                rejectUser(ids);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        RequestDispatcher dispatch = request.getRequestDispatcher("my_page.jsp");
        dispatch.forward(request, response);
    }
}
