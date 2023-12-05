package Servlets;

import BB.SQLTablesManager;
import DAOs.AccountDAO;
import DAOs.EventDAO;
import DAOs.TicketDAO;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024 * 1, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
public class AdminServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("type");
        SQLTablesManager db = (SQLTablesManager) request.getServletContext().getAttribute("db");
        EventDAO eventDAO = (EventDAO) request.getServletContext().getAttribute("eventdao");
        AccountDAO AccDAO = (AccountDAO) request.getServletContext().getAttribute("accdao");
        TicketDAO TickDAO = (TicketDAO) request.getServletContext().getAttribute("ticketdao");

        if(action.equals("create event")){
            String name = request.getParameter("eventname");
            String date = request.getParameter("date");
            String time = request.getParameter("time");
            String desc = request.getParameter("desc");
            String img = "default.png";
            int popularity = Integer.parseInt(request.getParameter("popularity"));
            String category = request.getParameter("category");

            for(Part p : request.getParts()){
                if(p.getContentType() != null){
                    if(p.getContentType().equals("image/jpeg") || p.getContentType().equals("image/png")){
                        p.write(System.getProperty("user.dir") + "/src/main/webapp/eventpicks/" +request.getParameter("eventname")+".jpg");
                        img = name + ".jpg";
                    }
                }
            }
            try {
                eventDAO.addevent(name, popularity, date, time, category, img, desc, 1);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else if (action.equals("remove event")){
            String name = request.getParameter("eventname");
            String newname = "";
            for (int j = 0; j < name.length(); j++) {
                if (name.charAt(j) != '+') newname += name.charAt(j);
                else newname += ' ';
            }
            try {
                eventDAO.removeevent(newname);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else if (action.equals("remove user")){
            String name = request.getParameter("username");
            try {
                AccDAO.removeAccount(name);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (action.equals("remove ticket")){
            String name = request.getParameter("t_id");
            try {
                TickDAO.removeticket(name);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }  else if (action.equals("make admin")){
            String name = request.getParameter("username");
            try {
                ((AccountDAO) request.getServletContext().getAttribute("accdao")).makeModerator(name);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (action.equals("see event stats")){
            String event = request.getParameter("eventname");
            String user = request.getParameter("username");
            try {
                if(event.equals("")){
                    request.getSession().setAttribute("searchstat", user + " has bought " + ((TicketDAO) request.getServletContext().getAttribute("ticketdao")).getStatistics(event, user) + " tickets");
                } else if(user.equals("")){
                    request.getSession().setAttribute("searchstat", event + " has sold " + ((TicketDAO) request.getServletContext().getAttribute("ticketdao")).getStatistics(event, user) + " tickets");
                } else request.getSession().setAttribute("searchstat", user + " has bought " + ((TicketDAO) request.getServletContext().getAttribute("ticketdao")).getStatistics(event, user) + " tickets for " + event);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (action.equals("Approve Event")){
            String eventName = request.getParameter("approveEventName");
            try {
                eventDAO.approveEvent(eventName);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } else if (action.equals("Delete Event")) {
            String eventName = request.getParameter("approveEventName");
            try {
                eventDAO.removeevent(eventName);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        RequestDispatcher rd = request.getRequestDispatcher("adminpage.jsp");
        rd.forward(request, response);
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response){
    }
}
