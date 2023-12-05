package Servlets;

import BB.Account;
import DAOs.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class InterestedServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        EventDAO eventDAO = (EventDAO) request.getServletContext().getAttribute("eventdao");
        InterestDAO interestDAO = (InterestDAO) request.getServletContext().getAttribute("interestdao");
        Account acc = (Account) request.getSession().getAttribute("account");
        if(request.getParameter("interest").equals("already interested")){
            try {
                interestDAO.RemoveInterest(acc, request.getParameter("event"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            acc = (Account) request.getSession().getAttribute("account");
            try {
                interestDAO.AddInterest(acc, request.getParameter("event"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        RequestDispatcher rd = request.getRequestDispatcher("event.jsp?id="+request.getParameter("event"));
        rd.forward(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
