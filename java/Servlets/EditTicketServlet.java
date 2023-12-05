package Servlets;

import BB.Event;
import BB.Ticket;
import BB.SQLTablesManager;
import DAOs.EventDAO;
import DAOs.TicketDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
@WebServlet(name = "EditTicketServlet", urlPatterns = "/editTicket")

public class EditTicketServlet extends HttpServlet{
    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException{
        String ticketId = httpServletRequest.getParameter("editTicketId");
        httpServletRequest.getSession().setAttribute("edit_ticket", ticketId);

        HttpSession s = httpServletRequest.getSession();
        SQLTablesManager db = (SQLTablesManager)httpServletRequest.getServletContext().getAttribute("db");
        TicketDAO TickDAO = db.getTickDAO();

        Ticket t;
        try {
            t = TickDAO.getticketbyname(ticketId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        s.setAttribute("ticketToEdit", t);

        httpServletResponse.sendRedirect("edit_ticket.jsp");
    }

    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ServletException, IOException{
        TicketDAO ticketdao = (TicketDAO) httpServletRequest.getServletContext().getAttribute("ticketdao");
        EventDAO eventDAO = (EventDAO) httpServletRequest.getServletContext().getAttribute("eventdao");
        HttpSession s = httpServletRequest.getSession();

        String ticket_id = (String) s.getAttribute("edit_ticket");
        double price = Double.parseDouble(httpServletRequest.getParameter("price"));
        String description = httpServletRequest.getParameter("ticket_description");

        String event_name = "";
        String name = httpServletRequest.getParameter("event_name");
        for (int j = 0; j < name.length(); j++) {
            if (name.charAt(j) != '+') event_name += name.charAt(j);
            else event_name += ' ';
        }

        String type = httpServletRequest.getParameter("selling_type");
        Date auctdate = null;
        Time aucttime = null;
        if (type.equals("auction")) {
            String auction_date = httpServletRequest.getParameter("auction_date");
            String auction_time = httpServletRequest.getParameter("auction_time");
            if(auction_date.isEmpty() || auction_time.isEmpty()){
                s.setAttribute("err_ticket", "Please enter auction date and time");
                httpServletResponse.sendRedirect("edit_ticket.jsp");
                return;
            }
            if(!isValidTime(auction_time)){
                s.setAttribute("err_ticket", "Please enter valid auction time");
                httpServletResponse.sendRedirect("edit_ticket.jsp");
                return;
            }
            auctdate = Date.valueOf(auction_date);
            aucttime = Time.valueOf(auction_time);
        }

        try {
            for (Part p : httpServletRequest.getParts()) {
                if (p.getContentType() != null) {
                    if (p.getContentType().equals("image/jpeg") || p.getContentType().equals("image/png")) {
                        deleteImage(ticket_id);
                        p.write(System.getProperty("user.dir") + "/src/main/webapp/ticktpicks/"
                                + ticket_id + ".jpg");
                    }
                }
            }

            Event event;
            if(!event_name.equals("other")) {
                try {
                    event = eventDAO.GetEventByName(event_name);
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }else {
                Ticket t = new Ticket();
                t.setTicket_id(ticket_id);
                t.setPrice(price);
                t.setDescription(description);
                t.setType(type.substring(0, 3));
                t.setAuctDate(auctdate);
                t.setAuctTime(aucttime);
                s.setAttribute("ticketInfo", t);
                httpServletResponse.sendRedirect("new_event.jsp?source=edit_ticket");
                return;
            }

            ticketdao.edit(ticket_id, event_name, event.getEvent_category(), price, description,
                    type.substring(0,3), auctdate, aucttime);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }catch (ServletException e){
            throw new RuntimeException(e);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        httpServletResponse.sendRedirect("my_page.jsp");
    }


    private void deleteImage(String ticket_id) {
        Path imagesPath = Paths.get(
                System.getProperty("user.dir") + "/src/main/webapp/ticktpicks/" + ticket_id + ".jpg");
        try {
            Files.delete(imagesPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private boolean isValidTime(String time){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try{
            sdf.parse(time);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }
}
