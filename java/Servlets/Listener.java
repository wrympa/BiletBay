package Servlets;

import BB.AuctionChecker;
import BB.SQLTablesManager;
import DAOs.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.sql.SQLException;
import java.util.ArrayList;

@WebListener
public class Listener implements ServletContextListener, HttpSessionListener {

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        SQLTablesManager db;
        TicketDAO TickDAO;
        AccountDAO AccDAO;
        EventDAO EventDAO;
        RequestDAO requestDAO;
        ReportDAO reportDAO;
        InterestDAO interestDAO;
        ArrayList<String> categories = new ArrayList<>();
        categories.add("music");
        categories.add("sport");
        categories.add("cinema");
        categories.add("gaming");
        categories.add("theatre");
        categories.add("other");

        try {
            db = new SQLTablesManager("biletbay");
            EventDAO = db.getEventDAO();
            TickDAO = db.getTickDAO();
            AccDAO = db.getAccDAO();
            requestDAO = db.getRequestDAO();
            reportDAO = db.getReportDAO();
            interestDAO = db.getInterestDAO();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        servletContextEvent.getServletContext().setAttribute("db", db);
        servletContextEvent.getServletContext().setAttribute("ticketdao", TickDAO);
        servletContextEvent.getServletContext().setAttribute("eventdao", EventDAO);
        servletContextEvent.getServletContext().setAttribute("accdao", AccDAO);
        servletContextEvent.getServletContext().setAttribute("requestdao", requestDAO);
        servletContextEvent.getServletContext().setAttribute("reportdao", reportDAO);
        servletContextEvent.getServletContext().setAttribute("interestdao", interestDAO);
        servletContextEvent.getServletContext().setAttribute("categories", categories);
        try {
            AuctionChecker auccheck = new AuctionChecker(db.getconnection());
            auccheck.start();

            servletContextEvent.getServletContext().setAttribute("totcount", TickDAO.getsoldcount());
            servletContextEvent.getServletContext().setAttribute("auccheck", TickDAO.getsoldcount());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

    @Override
    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        httpSessionEvent.getSession().setAttribute("account", null);
        httpSessionEvent.getSession().setAttribute("searchstat", "nothing");
        httpSessionEvent.getSession().setAttribute("editError", "");
        httpSessionEvent.getSession().setAttribute("error", "");
        httpSessionEvent.getSession().setAttribute("err_ticket", "");
        httpSessionEvent.getSession().setAttribute("buyError", "");

    }

    @Override
    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

    }
}
