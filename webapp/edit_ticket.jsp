<%@ page import="DAOs.EventDAO" %>
<%@ page import="BB.Event" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="BB.Ticket" %>
<%@ page import="BB.Account" %>
<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/30/2023
  Time: 16:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Ticket</title>
    <link rel="stylesheet" href="css/edit.css">
</head>
<body>
<div class="header">
    <div class="welcome"><a style="text-decoration:none; color:white" href="index.jsp" onclick="function changeRequirements() {
                                            document.getElementById('price').required = false;
                                            document.getElementById('ticket_description').required = false;
                                            document.getElementById('ticket_image').required = false;
                                            document.getElementById('event_name').required = false;
                                        }
                        changeRequirements()">
        <h1>Welcome To BiletBay!</h1></a></div>
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

        %>
    </div>
<h1 style="color:white;">Edit Ticket</h1>
<%
  if(request.getSession().getAttribute("err_ticket") != ""){
    out.print(request.getSession().getAttribute("err_ticket"));
    request.getSession().setAttribute("err_ticket", "");
  }
%>

<body onload="setDateRestriction()">

<%
    Ticket t = (Ticket)session.getAttribute("ticketToEdit");
    String event = t.getEvent_name();
    String type = t.getType();
    String date = "";
    String time = "";
    if(t.getType().equals("auc")){
        date = t.getAuctDate().toString();
        time = t.getAuctTime().toString();
    }
    boolean isSelected = false;
%>


<form action="/editTicket" method="post" enctype="multipart/form-data" style="color: white; display: flex; flex-direction: column; justify-content: center; align-items: center">
    <label>Ticket ID: <%=t.getTicket_id()%></label>
    <br>

    <label for="price">Ticket Price:</label>
    <input type="text" name="price" id="price" required pattern="\d+(\.\d{1,2})?" value="<%=t.getPrice()%>">
    <br>

    <label for="ticket_description">Ticket Description:</label>
    <textarea name="ticket_description" id="ticket_description" required><%=t.getDescription()%></textarea>
    <br>

    <label for="ticket_image">Ticket Image:</label>
    <img src="/ticktpicks/<%=t.getImage()%>" width="100" height="200">
    <input type="file" id="ticket_image" name="ticket_image">
    <br>

    <label for="event_name">Event Name:</label>
    <%
        EventDAO evdao = (EventDAO) request.getServletContext().getAttribute("eventdao");
        ArrayList<Event> evts = evdao.GetAllEvents();
    %>
    <select name="event_name" id="event_name" required>
        <%for (int i = 0; i < evts.size(); i++){%>
        <%String newname = "";
            for (int j = 0; j < evts.get(i).getEvent_name().length(); j++) {
                if (evts.get(i).getEvent_name().charAt(j) != ' ') newname += evts.get(i).getEvent_name().charAt(j);
                else newname += '+';
            }

        %>
        <option value=<%=newname%> <%if(event.equals(evts.get(i).getEvent_name())){ out.print("selected"); isSelected = true;}%>>
            <%=evts.get(i).getEvent_name()%></option>
        <%}%>
        <option value="other" <%if(!isSelected) out.print("selected");%>>other</option>
    </select>
    <br>

    <label for="selling_type">Selling type:</label>
    <%ArrayList<String> arr = new ArrayList();
        arr.add("buy");
        arr.add("auction");
    %>
    <select name="selling_type" id="selling_type" required>
        <%for (int i = 0; i < arr.size(); i++){%>
        <option value=<%=arr.get(i)%> <%if(type.equals(arr.get(i).substring(0,3))) out.print("selected");%>><%=arr.get(i)%></option>
        <%}%>
    </select>
    <br>

    <script>
        function setDateRestriction() {
            const curDate = new Date();
            const formatted = curDate.toISOString().split('T')[0];
            document.getElementById("aucDate").setAttribute("min", formatted);
        }
        setDateRestriction();
    </script>

    <label for="aucDate">Auction Date:</label>
    <input type="date" id="aucDate" name="auction_date" min="" value="<%=date%>">
    <br>

    <label for="aucTime">Auction Time:</label>
    <input type="text" id="aucTime" name="auction_time" placeholder="00:00:00" value="<%=time%>">
    <br>

    <button type="submit">Save Changes</button>
</form>
<form action="my_page.jsp">
    <button type="submit" onclick="changeRequirements()">Back</button>
</form>
</body>
</html>
