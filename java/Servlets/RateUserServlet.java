package Servlets;

import BB.Ticket;
import DAOs.AccountDAO;
import DAOs.EventDAO;
import DAOs.TicketDAO;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

public class RateUserServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException {
        AccountDAO accdao = (AccountDAO) httpServletRequest.getServletContext().getAttribute("accdao");
        String rated = httpServletRequest.getParameter("rated");
        String rater = httpServletRequest.getParameter("rater");
        int rating = Integer.parseInt(httpServletRequest.getParameter("rating"));
        try {
            if(accdao.RatedAlready(rater, rated)){
                accdao.UpdateRating(rater, rated, rating);
            } else {
                accdao.AddRating(rater, rated, rating);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        httpServletResponse.sendRedirect("user_page.jsp?id="+rated);
    }
}
