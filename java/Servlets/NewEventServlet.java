package Servlets;

import BB.Ticket;
import DAOs.EventDAO;
import DAOs.TicketDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
@WebServlet(name = "NewEventServlet", urlPatterns = "/addEvent")

public class NewEventServlet extends HttpServlet{
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException{
        TicketDAO ticketdao = (TicketDAO) httpServletRequest.getServletContext().getAttribute("ticketdao");
        EventDAO eventDAO = (EventDAO) httpServletRequest.getServletContext().getAttribute("eventdao");
        HttpSession s = httpServletRequest.getSession();

        Ticket t = (Ticket) s.getAttribute("ticketInfo");

        String event_name = httpServletRequest.getParameter("name");
        String description = httpServletRequest.getParameter("description");
        String category = httpServletRequest.getParameter("category");
        String date = httpServletRequest.getParameter("date");
        String time = httpServletRequest.getParameter("time");
        String img = "default.png";

        try {
            for(Part p : httpServletRequest.getParts()){
                if(p.getContentType() != null){
                    if(p.getContentType().equals("image/jpeg") || p.getContentType().equals("image/png")){
                        p.write(System.getProperty("user.dir") + "/src/main/webapp/eventpicks/" + event_name + ".jpg");
                        img = event_name + ".jpg";
                    }
                }
            }
            int app =0;
            if(s.getAttribute("source").equals("adminpage")) app = 1;
            eventDAO.addevent(event_name, 0, date, time, category, img, description, app);

            if(s.getAttribute("source").equals("adminpage")){
            }else if(s.getAttribute("source").equals("add_ticket")){
                ticketdao.addticket(t.getTicket_id(), event_name, t.getPost_date(), t.getPosted_by(),
                        "null", category, t.getPrice(), t.getDescription(), t.getImage(),
                        t.getType(), "null", t.getAuctDate(), t.getAuctTime());
            }else{
                ticketdao.edit(t.getTicket_id(), event_name, category, t.getPrice(), t.getDescription(),
                                t.getType(), t.getAuctDate(), t.getAuctTime());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        httpServletResponse.sendRedirect("my_page.jsp");
    }
}
