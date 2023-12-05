<%@ page import="BB.Account" %><%@ page import="BB.Account" %>
<%@ page import="java.util.List" %>
<%@ page import="BB.SQLTablesManager" %>
<%@ page import="DAOs.TicketDAO" %>
<%@ page import="BB.Ticket" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="DAOs.EventDAO" %>
<%@ page import="DAOs.RequestDAO" %>
<%@ page import="BB.Request" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title><%=((Account)request.getSession().getAttribute("account")).getUsername()%></title>
    <link rel="stylesheet" href="css/my_page.css">
</head>
<body>
<%
    SQLTablesManager db = (SQLTablesManager) request.getServletContext().getAttribute("db");
    Account acc = (Account) session.getAttribute("account");
%>
<div class="header">
    <div class="welcome"><a style="text-decoration:none; color:white" href="index.jsp"><h1>Welcome To BiletBay!</h1></a></div>
    <div class="filler">
        <div style="color: white; display: flex; flex-direction: row">Search for an event <form action="search_event.jsp" method="post" style="margin-left: 10px"><input type="search" name="search event"> </form></div>
        <div style="color: white; display: flex; flex-direction: row">Search for a user <form action="search_account.jsp" method="post" style="margin-left: 10px"><input type="search" name="search user"></form></div>
    </div>
    <%
        if(acc != null){
            String accid = ((Account) session.getAttribute("account")).getUsername();
            String imgdest = ((Account) session.getAttribute("account")).getImage();
            out.print("<div class=\"login\">" +
                    "<form action=\"/logOut\" method=\"post\" class=\"logoutbutton\">" +
                    "<input type=\"submit\" value=\"Log Out\" name=\"logOut\"  style=\"margin-top: 25px; border-radius: 10px; background-color: red; color: black; border: darkred solid 3px\"></p> </form> " +
                    "<a style=\"text-decoration:none; color:white\" href=\"my_page.jsp\" class=\"logoutbutton\">Hi, "+ accid+"</a>" +
                    "<a style=\"text-decoration:none; color:white\" href=\"my_page.jsp\" class=\"logoutbutton\"><img src=\"pfps/"+imgdest+"\" width=\"60\" height=\"60\" ></img></a></div>");
        }
        else out.print("<div class=\"login\"><a style=\"text-decoration:none; color:white\" href=\"login.jsp\">login or sign up</a></div>");

    %>
</div>
<div class="conts">
    <div class="achievements">
        <%=db.getAccDAO().getAchievementsAsHtml(acc, db.getTickDAO())%>
    </div>
    <div class="pfpandnam">
        <img class="pfp" src="./pfps/<%=acc.getImage()%>">
        <div class="name">
            <%=acc.getUsername()%><br>
            <%=((Account)session.getAttribute("account")).getPhoneNumber()%><br>
            <%="My Rating: "+db.getAccDAO().getRating(acc.getUsername())%><br>
            <% if(((Account)session.getAttribute("account")).isModerator()) {
                out.println("<form action = \"adminpage.jsp\" method = \"post\" style=\"margin-bottom:10px\" >\n" +
                        "        <input type = \"submit\" value = \"admin page\" name = \"mod\" style=\"background-color: darkred; color: black; border-radius: 10px; font-family: oswald; border: red solid 3px;\">\n" +
                        "    </form >");
            }%>
            <form action="edit_profile.jsp" method="post" style="margin-bottom: 10px">
                <input type="submit" value="Edit Profile" name="edit" style="background-color: lightseagreen; border: aquamarine solid 3px; border-radius: 10px">
            </form>

            <form action="add_ticket.jsp" method="post" style="margin-bottom: 10px">
                <input type="submit" value="Add Ticket" name="addTicket" style="background-color: lightseagreen; border: aquamarine solid 3px; border-radius: 10px">
            </form>

            <p style="font-size: 17px; color: white; margin-top: 0px">Total Money Spent: <%=db.getTickDAO().getSpent(acc.getUsername())%></p>
            <p style="font-size: 17px; color: white; margin-top: 0px">Total Money Earned: <%=db.getTickDAO().getEarned(acc.getUsername())%></p>
        </div>
    </div>
</div>
<div class="ticktypes">
    <a href="my_page.jsp?id=bought" style="background-color: lightseagreen; border: aquamarine solid 3px; border-radius: 10px; color: white; text-decoration: none">Bought Tickets</a>
    <a href="my_page.jsp?id=posted" style="background-color: lightseagreen; border: aquamarine solid 3px; border-radius: 10px; color: white; text-decoration: none">Posted Tickets</a>
    <a href="my_page.jsp?id=requests" style="background-color: lightseagreen; border: aquamarine solid 3px; border-radius: 10px; color: white; text-decoration: none">Ticket Requests</a>
</div>
<% if(request.getParameter("id") == null || request.getParameter("id").equals("bought")){ %>
<h3 style="color:white; margin-left: 20px">Bought Tickets</h3>
    <% try {
        ServletContext servletContext = request.getServletContext();
        Account me = (Account)session.getAttribute("account");
        TicketDAO ticketDAO = (TicketDAO) servletContext.getAttribute("ticketdao");
        EventDAO eventDAO = (EventDAO) servletContext.getAttribute("eventdao");
        List<Ticket> tickets = ticketDAO.tickets_bought_by(me.getUsername());
        for (Ticket t : tickets) {

    %>
    <div class="ticket">
        <div class="tickdesc">
                <%=t.getEvent_name()%></br>
                <%=t.getDescription()%>
        </div>
        <div class="op">
            Posted by <a style="text-decoration: none; color:white" href="user_page.jsp?id=<%=t.getPosted_by()%>"><%=t.getPosted_by()%></a></br>
            Price: <%=t.getPrice()%></br>
            Time of event: <%=" "+db.getEventDAO().GetEventByName(t.getEvent_name()).getEvent_date()+" "+db.getEventDAO().GetEventByName(t.getEvent_name()).getEvent_time()%>
        </div>
        <div class="displaying">
            <img src="ticktpicks/<%=t.getImage()%>"  width="100" height="200">
        </div>
    </div>
    <%
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    %>
<% }else if( request.getParameter("id").equals("posted")){ %>
<h3 style="color:white; margin-left: 20px">Tickets Posted By Me</h3>
    <% try {
        ServletContext servletContext = request.getServletContext();
        TicketDAO td = (TicketDAO) servletContext.getAttribute("ticketdao");
        EventDAO ed = (EventDAO) servletContext.getAttribute("eventdao");
        List<Ticket> tickets = td.tickets_posted_by(((Account)session.getAttribute("account")).getUsername());
        for (Ticket t : tickets) {

    %>
    <div class="ticket">
        <div class="tickdesc">
                <%=t.getEvent_name()%></br>
            <%=t.getDescription()%>
        </div>
        <div class="op">
            Posted by <a style="text-decoration: none; color:white" href="user_page.jsp?id=<%=t.getPosted_by()%>"><%=t.getPosted_by()%></a></br>
            Price: <%=t.getPrice()%></br>
            Time of event: <%=" "+db.getEventDAO().GetEventByName(t.getEvent_name()).getEvent_date()+" "+db.getEventDAO().GetEventByName(t.getEvent_name()).getEvent_time()%>
        </div>
        <div class="displaying">
            <img src="ticktpicks/<%=t.getImage()%>"  width="100" height="200">
            <div style="display: flex; flex-direction: column; align-items: center; justify-content: center; margin-left: 15px">
                <form action="/removeTicket" method="post">
                    <input type="hidden" value="<%=t.getTicket_id()%>" name="remtickid">
                    <input type="submit" value="Delete Ticket" name="removeTicket" style="font-family: oswald; color: black; background-color: red; border-radius: 10px; border: darkred solid 5px;"></p>
                </form>
                <form action="/editTicket" method="get">
                    <input type="hidden" value="<%=t.getTicket_id()%>" name="editTicketId">
                    <input type="submit" value="Edit Ticket" name="editTicket" style="font-family: oswald; color: lightseagreen; background-color: yellow; border-radius: 10px; border: greenyellow solid 5px;"></p>
                </form>
            </div>
        </div>
    </div>
    <%
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    %>
<%} else if( request.getParameter("id").equals("requests")) {%>
<h3 style="color:white; margin-left: 20px">Buy Requests</h3>
    <% try {
        ServletContext servletContext = request.getServletContext();
        Account me = (Account)session.getAttribute("account");
        RequestDAO requestDAO = (RequestDAO) servletContext.getAttribute("requestdao");
        TicketDAO td = (TicketDAO) servletContext.getAttribute("ticketdao");
        EventDAO ed = (EventDAO) servletContext.getAttribute("eventdao");
        List<Request> requests = requestDAO.getRequests(me.getUsername());
        for (Request r : requests) {

    %>
    <div class="request">
        <div class="requestdesc">
            <%="&nbsp;sent by &nbsp; <a href=\"user_page.jsp?id="+r.getSentBy()+"\">&nbsp;"+r.getSentBy()+"</a>&nbsp;"%>
            <%Ticket t = db.getTickDAO().getticketbyname(r.getTicketId());%>
            <%="&nbsp; for event: <a style=\"text-decoration:none; color:white\" href=\"event.jsp?id="+t.getEvent_name()+"\"> "+t.getEvent_name()+"</a>"%>
            <%="&nbsp; for the price: "+t.getPrice()%>
        </div>
        <div class="accrej">
            <form action="/sellTicket" method="post" class="rowflex"> <%--SellTicketServlet sellTicket unda daemapos--%>
                <input type="hidden" value=<%=t.getTicket_id() + r.getSentBy()%> name="ticketIdSentBy">

                <input type="submit" value="Sell Ticket" name="sellTicket" style="background-color: greenyellow; color: lightseagreen; border-radius: 10px; font-family: oswald; border: yellow solid 3px"></p>
                <input type="submit" value="Delete Request" name="sellTicket" style="background-color: darkred; color: black; border-radius: 10px; font-family: oswald; border: red solid 3px"></p>
            </form>
        </div>
    </div>
    <%
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    %>
<%}%>
</body>
</html>