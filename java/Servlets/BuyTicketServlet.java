package Servlets;

import BB.Account;
import BB.Ticket;
import DAOs.EventDAO;
import DAOs.RequestDAO;
import DAOs.TicketDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class BuyTicketServlet extends HttpServlet {

    private TicketDAO ticketDAO;
    private RequestDAO requestDAO;
    private Account me;
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String req = request.getParameter("buyTicket");
        me = (Account) request.getSession().getAttribute("account");
        ticketDAO = (TicketDAO) request.getServletContext().getAttribute("ticketdao");
        String ticketId = request.getParameter("tickid");
        Ticket ticket = null;
        try {
            ticket = ticketDAO.getticketbyname(ticketId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        request.setAttribute("curEvent", ticket.getEvent_name());

        if (me == null) {
            request.getSession().setAttribute("buyError", "Please Log In To Buy a Ticket");
            RequestDispatcher dispatch = request.getRequestDispatcher("event.jsp?id=\""+ticket.getEvent_name()+"\"");
            dispatch.forward(request, response);
            return;
        }
        if (req.equals("Buy Ticket")) {
            requestDAO = (RequestDAO) request.getServletContext().getAttribute("requestdao");
            String sentBy = me.getUsername();
            String sentTo = request.getParameter("poster");
            try {
                if (requestDAO.containsRequest(sentBy, sentTo, ticketId)) {
                    request.getSession().setAttribute("buyError", "You have already requested to buy this ticket");
                    RequestDispatcher dispatch = request.getRequestDispatcher("event.jsp?id=\""+ticket.getEvent_name()+"\"");
                    dispatch.forward(request, response);
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            try {
                requestDAO.sendRequest(sentBy, sentTo, ticketId);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            RequestDispatcher dispatch = request.getRequestDispatcher("event.jsp?id=\""+ticket.getEvent_name()+"\"");
            dispatch.forward(request, response);
        } else {
            String inputBid = request.getParameter("bid");
            if (inputBid.length() == 0) {
                RequestDispatcher dispatch = request.getRequestDispatcher("event.jsp?id=\""+ticket.getEvent_name()+"\"");
                dispatch.forward(request, response);
                return;
            }
            float bid = Float.parseFloat(inputBid);
            String sentBy = me.getUsername();
            if (ticket.getPrice() >= bid) {
                request.getSession().setAttribute("buyError", "You Need To Place a Higher Bid.");
                RequestDispatcher dispatch = request.getRequestDispatcher("event.jsp?id=\""+ticket.getEvent_name()+"\"");
                dispatch.forward(request, response);
                return;
            }
            try {
                ticketDAO.placebid(ticketId, sentBy, bid);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            RequestDispatcher dispatch = request.getRequestDispatcher("event.jsp?id=\""+ticket.getEvent_name()+"\"");
            dispatch.forward(request, response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
