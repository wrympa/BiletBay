package Servlets;

import BB.Account;
import BB.Event;
import BB.Ticket;
import DAOs.EventDAO;
import DAOs.TicketDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 10,      // 10 MB
        maxRequestSize = 1024 * 1024 * 100   // 100 MB
)
@WebServlet(name = "AddTicketServlet", urlPatterns = "/addTicket")

public class AddTicketServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws IOException{
        TicketDAO ticketdao = (TicketDAO) httpServletRequest.getServletContext().getAttribute("ticketdao");
        EventDAO eventDAO = (EventDAO) httpServletRequest.getServletContext().getAttribute("eventdao");
        Account acc = (Account) httpServletRequest.getSession().getAttribute("account");
        HttpSession s = httpServletRequest.getSession();

        UUID id = UUID.randomUUID();
        String ticket_id = id.toString().substring(0,10);

        try {
            while(ticketdao.exists(ticket_id)){
                ticket_id = UUID.randomUUID().toString().substring(0,10);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String name = httpServletRequest.getParameter("event_name");
        String event_name = "";
        for (int j = 0; j < name.length(); j++) {
            if (name.charAt(j) != '+') event_name += name.charAt(j);
            else event_name += ' ';
        }

        double price = Double.parseDouble(httpServletRequest.getParameter("price"));
        String description = httpServletRequest.getParameter("ticket_description");
        String type = httpServletRequest.getParameter("selling_type");
        String curDate = new Date(System.currentTimeMillis()).toString();

        Date auctdate = null;
        Time aucttime = null;
        if (type.equals("auction")) {
            String auction_date = httpServletRequest.getParameter("auction_date");
            String auction_time = httpServletRequest.getParameter("auction_time");
            if(auction_date.isEmpty() || auction_time.isEmpty()){
                s.setAttribute("err_ticket", "Please enter auction date and time");
                httpServletResponse.sendRedirect("add_ticket.jsp");
                return;
            }
            if(!isValidTime(auction_time)){
                s.setAttribute("err_ticket", "Please enter valid auction time");
                httpServletResponse.sendRedirect("add_ticket.jsp");
                return;
            }
            auctdate = Date.valueOf(auction_date);
            aucttime = Time.valueOf(auction_time);
        }

        try {
            for (Part p : httpServletRequest.getParts()) {
                if (p.getContentType() != null) {
                    if (p.getContentType().equals("image/jpeg") || p.getContentType().equals("image/png")) {
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
            }else{
                Ticket t = new Ticket();
                t.setTicket_id(ticket_id);
                t.setPost_date(curDate);
                t.setPosted_by(acc.getUsername());
                t.setPrice(price);
                t.setDescription(description);
                t.setImage(ticket_id+".jpg");
                t.setType(type.substring(0,3));
                t.setAuctDate(auctdate);
                t.setAuctTime(aucttime);
                s.setAttribute("ticketInfo", t);
                httpServletResponse.sendRedirect("new_event.jsp?source=add_ticket");
                return;
            }

            ticketdao.addticket(ticket_id, event_name, curDate, acc.getUsername(), "null", event.getEvent_category(),
                    price, description, ticket_id + ".jpg", type.substring(0,3), "null", auctdate, aucttime);
        }catch (SQLException e){
            throw new RuntimeException(e);
        }catch (ServletException e){
            throw new RuntimeException(e);
        }catch (Exception e){
            throw new RuntimeException(e);
        }

        httpServletResponse.sendRedirect("my_page.jsp");
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