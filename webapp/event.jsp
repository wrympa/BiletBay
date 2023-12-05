<%@ page import="BB.Event" %>
<%@ page import="BB.Ticket" %>
<%@ page import="java.util.List" %>
<%@ page import="BB.SQLTablesManager" %>
<%@ page import="java.sql.SQLException" %>
<%@ page import="DAOs.TicketDAO" %>
<%@ page import="BB.Account" %>
<%@ page import="java.util.ArrayList" %><%--
  Created by IntelliJ IDEA.
  User: liza
  Date: 10.07.23
  Time: 13:28
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<% SQLTablesManager baseManager = (SQLTablesManager) request.getServletContext().getAttribute("db"); %>
<head>
    <% Event e = baseManager.getEventDAO().GetEventByName(request.getParameter("id"));
        boolean filter = false;
        if (e == null){
            e = baseManager.getEventDAO().GetEventByName((String) request.getAttribute("curEvent"));
            filter = true;
        }
        String title = "No Event Found";
        if (e != null) title = e.getEvent_name();
    %>
    <title><%=title%></title>
    <link rel="stylesheet" href="css/event.css">
</head>
<body>
<div class="header">
    <div class="welcome"><a style="text-decoration:none; color:white" href="index.jsp"><h1>Welcome To BiletBay!</h1></a></div>
    <div class="filler">
        <div style="color: white; display: flex; flex-direction: row">Search for an event <form action="search_event.jsp" method="post" style="margin-left: 10px"><input type="search" name="search event"> </form></div>
        <div style="color: white; display: flex; flex-direction: row">Search for a user <form action="search_account.jsp" method="post" style="margin-left: 10px"><input type="search" name="search user"></form></div>
    </div>
    <%
        Account acc = (Account) request.getSession().getAttribute("account");
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
        if (e != null) {
    %>
</div>
<img class="evbanner" src="eventpicks/<%=e.getEvent_picture()%>">
<div class="info">
    <div class="half1">
        <div class="name"><%=e.getEvent_name()%></div>
        <form action="/filterEvent" method="post" style="color: white; margin-top: 40px;">
            <%
                String max_price_value = "1000000";
                String min_price_value = "0";
                String sort_value = "ascending";
                String selling_type_value = "both";
                if(filter){
                    if(request.getParameter("upperprice") != null)
                        max_price_value = request.getParameter("upperprice");
                    if(request.getParameter("lowprice") != null)
                        min_price_value = request.getParameter("lowprice");
                    sort_value = request.getParameter("sort_type");
                    selling_type_value = request.getParameter("selling_type");
                }
            %>
            <input type="hidden" value="<%=e.getEvent_name()%>" name = "eventName">
            <label for="priceLabel">Enter your price range: </label>
            <input type="number" id="priceLabel" name="lowprice" value="<%=min_price_value%>">
            <input type="number" name="upperprice" value="<%=max_price_value%>"><br><br>
            <label for="selling_type">Select selling type:</label>
            <%
                ArrayList<String> arr = new ArrayList();
                arr.add("both");
                arr.add("buy");
                arr.add("auction");
            %>
            <select name="selling_type" id="selling_type" required>
                <%for (int i = 0; i < arr.size(); i++){%>
                <option value=<%=arr.get(i)%> <%if(arr.get(i).equals(selling_type_value)) out.print("selected");%>><%=arr.get(i)%></option>
                <%}%>
            </select>
            <br><br>

            <label for="sort_type">Sort by price:</label>
            <%
                ArrayList<String> sortarr = new ArrayList();
                sortarr.add("ascending");
                sortarr.add("descending");
            %>
            <select name="sort_type" id="sort_type" required>
                <%for (int i = 0; i < sortarr.size(); i++){%>
                <option value=<%=sortarr.get(i)%> <%if(sortarr.get(i).equals(sort_value)) out.print("selected");%>><%=sortarr.get(i)%></option>
                <%}%>
            </select><br><br>

            <input type="submit" value="Filter" name="filter" >
            <br><br>


        </form>

        <div class="desc"><p style="margin-left: 20px">Event category:<a style="text-decoration: none; color: white" href="event_by_category.jsp?id=<%=e.getEvent_category()%>"><%=" "+e.getEvent_category()%></a><br><%=e.getEvent_description()%></p></div>
    </div>
    <div class="half2">
        <div class="time">Event starts: <%=e.getEvent_date()%>, at <%=e.getEvent_time()%></div>
        <div class="interest">
            <%=baseManager.getEventDAO().getEvent_popularity(e.getEvent_name())%> people are interested<br>
            <input type="hidden" value=<%=e.getEvent_name()%>>
            <form action="/interest" method="post"><br>
                <%
                    String err = request.getSession().getAttribute("buyError").toString();
                    request.getSession().setAttribute("buyError", "");
                    acc = (Account) session.getAttribute("account");
                    if (acc != null && baseManager.getInterestDAO().isinterested(acc.getUsername(), e.getEvent_name())){
                        out.println("<input type=\"hidden\" value=\""+e.getEvent_name()+"\" name = \"event\">");
                        out.println("<input style=\"margin-left: 20px; margin-bottom: 20px; background-color: yellow; font-family: oswald; color: lightseagreen; border-radius: 10px\" type=\"submit\" name=\"interest\" value=\"already interested\">");
                    } else {
                        out.println("<input type=\"hidden\" value=\""+e.getEvent_name()+"\" name = \"event\">");
                        if (acc != null) out.println("<input style=\"margin-left: 20px; margin-bottom: 20px; background-color: lightseagreen; font-family: oswald; color: yellow; border-radius: 10px\" type=\"submit\" name=\"interest\" value=\"interested\">");
                    }

                %>
            </form>
        </div>
    </div>
</div>
<div>
    <h4 style="color:red; font-size: 20px; width: 100%; display: flex;justify-content: center"><%=err%></h4>
</div>
<div class="tickets">
    <% try {
        ServletContext servletContext = request.getServletContext();
        TicketDAO ticketDAO = ((SQLTablesManager) servletContext.getAttribute("db")).getTickDAO();
        List<Ticket> tickets = ticketDAO.getAllByEventName(e.getEvent_name());

        if (request.getParameter("filter")!= null) {

            String min = request.getParameter("lowprice");
            String max = request.getParameter("upperprice");

            if( min.equals("")) min = "0";
            if(max.equals("")) max = "1000000";


            int sorted = 0;
            String type = "";
            if((request.getParameter("selling_type")).equals("buy")) {
                type = "buy";
            } else if((request.getParameter("selling_type")).equals("auction")) {
                type = "auc";
            }


            if((request.getParameter("sort_type")).equals("ascending")) {
                sorted = 1;
            } else if((request.getParameter("sort_type")).equals("descending")) {
                sorted = 2;
            }

            tickets = ticketDAO.filter_sort(e.getEvent_name(),Double.valueOf(min),Double.valueOf(max),sorted,type);
        }

        if (tickets.isEmpty()) out.print("<h3 style=\"margin-left:20px\">No Tickets To Show</h3>");
        for (Ticket t : tickets) {

    %>
    <div class="ticket">
        <div class="tickdesc">
            <%=t.getDescription()%>
        </div>
        <div class="op">
            Posted by <a style="text-decoration: none; color:white" href="user_page.jsp?id=<%=t.getPosted_by()%>"><%=t.getPosted_by()%></a></br>
            Time of event: <%=" "+baseManager.getEventDAO().GetEventByName(t.getEvent_name()).getEvent_date()+" "+baseManager.getEventDAO().GetEventByName(t.getEvent_name()).getEvent_time()%>
        </div>
        <div class="buying">
            <%
                if(t.getType().equals("auc")){
                    out.println(" ticket is on auction, final date is <p style=\"color:red; margin-left:5px; margin-right:5px\"> " + t.getAuctDate() + " " + t.getAuctTime() + "</p> current bid is " + t.getPrice());
                } else out.println(t.getPrice());
            %>
            <br>
            <%if(t.getType().equals("auc")){%>
            <form action="/buyTicket" method="post">
                <input type="hidden" value="<%=t.getTicket_id()%>" name = "tickid">
                <input type="hidden" value="<%=t.getPosted_by()%>" name = "poster">
                <%if(acc != null && !ticketDAO.userOwnsTicket(acc.getUsername(), t.getTicket_id())) {%>
                <input style="background-color: lightseagreen; border: 10px; color: white; font-family: oswald; border-radius: 10px; margin-top: 30px; margin-left: 20px" type="number" name="bid" min="<%=t.getPrice()%>>">
                <input style="background-color: lightseagreen; border: 10px; color: white; font-family: oswald; border-radius: 10px; margin-top: 40px; margin-left: 20px" type="submit" value="Place Bid" name="buyTicket"></p>
                <%}%>
            </form>
            <%} else {%>
            <form action="/buyTicket" method="post">
                <input type="hidden" value="<%=t.getTicket_id()%>" name = "tickid">
                <input type="hidden" value="<%=t.getPosted_by()%>" name = "poster">
                <%if(acc != null && !ticketDAO.userOwnsTicket(acc.getUsername(), t.getTicket_id())) {%>
                <input style="background-color: lightseagreen; border: 10px; color: white; font-family: oswald; border-radius: 10px; margin-top: 30px; margin-left: 20px" type="submit" value="Buy Ticket" name="buyTicket"></p>
                <%}%>
            </form>
            <%}%>
        </div>
    </div>

    <%}
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }
    %>
</div>
<%} else %> <h1 style="color: white">This event is not available</h1>

</body>
</html>