<%@ page import="DAOs.EventDAO" %>
<%@ page import="BB.Event" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%@ page import="DAOs.ReportDAO" %>
<%@ page import="BB.Report" %>
<%@ page import="DAOs.TicketDAO" %>
<html>
<body>
<div class="top"> <a style="text-decoration: none; color: white; margin-left: 20px" href="index.jsp">Back to Main Page</a><div class="filler"> </div><h1>Admin page</h1></div>
<head>
    <link rel="stylesheet" href="css/adminpage.css">
</head>
<div class="container">
    <h4>Please enter event details</h4>
    <form action="/addEvent" method="post" enctype="multipart/form-data" style="display: flex; flex-direction: column; justify-content: center; align-items: center">
        <%
            request.getSession().setAttribute("source", "adminpage");
        %>
        <label for="name">Event name:</label>
        <input type="text" name="name" id="name" required>
        <br>

        <label for="description">Description:</label>
        <textarea name="description" id="description" required></textarea>
        <br>

        <label for="image">Image:</label>
        <input type="file" name="image" id="image">

        <%ArrayList<String> arr = (ArrayList<String>)application.getAttribute("categories");%>
        Category:
            <select name="category" required>
                <%for (int i = 0; i < arr.size(); i++){%>
                <option value=<%=arr.get(i)%>><%=arr.get(i)%></option>
                <%}%>
            </select>

        <label for="date">Date:</label>
        <input type="date" id="date" name="date" min="" required>
        <br><br>

        <label for="time">Time:</label>
        <input type="time" id="time" name="time" required>
        <br><br>

        <button type="submit"><%="Add"%></button>
    </form>
</div>
<div class="filler"></div>
<div class="container">
    <form action="admin" method="post" class="columnflex">
        remove event. <br>
        event name:  <br>
        <%
            EventDAO evdao = (EventDAO) request.getServletContext().getAttribute("eventdao");
            ArrayList<Event> evts = evdao.GetAllEvents();
        %>
        <select name="eventname" required>
            <%for (int i = 0; i < evts.size(); i++){%>
            <%String newname = "";
                for (int j = 0; j < evts.get(i).getEvent_name().length(); j++) {
                    if (evts.get(i).getEvent_name().charAt(j) != ' ') newname += evts.get(i).getEvent_name().charAt(j);
                    else newname += '+';
                }

            %>
            <option value=<%=newname%>><%=evts.get(i).getEvent_name()%></option>
            <%}%>
        </select>
        <input type="submit" value="remove event" name="type"/>
    </form>
</div>
<div class="filler"></div>
<div class="container">
    <form action="admin" method="post" class="columnflex">
        remove user.
        username:  <input type="text" name="username"/>
        <input type="submit" value="remove user" name="type"/>
    </form>
</div>
<div class="filler"></div>
<div class="container">
    <form action="admin" method="post" class="columnflex">
        remove ticket.
        ticket_id:  <input type="text" name="t_id"/>
        <input type="submit" value="remove ticket" name="type"/>
    </form>
</div>
<div class="filler"></div>
<div class="container">
    <form action="admin" method="post" class="columnflex">
        make a user an admin
        username:  <input type="text" name="username"/>
        <input type="submit" value="make admin" name="type"/>
    </form>
</div>
<div class="filler"></div>
<div class="container">
    <div class="stats">
        <div class="label">Statistics</div>
        <div class="label">
            total tickets sold: <%=((TicketDAO)request.getServletContext().getAttribute("ticketdao")).getsoldcount()%>
        </div>
        <div class="selector">
            <form action="admin" method="post" class="columnflex">
                View sold tickets:<br>
                event name:  <input type="text" name="eventname"/><br>
                user name:  <input type="text" name="username"/><br>
                <input type="submit" value="see event stats" name="type"/><br>
                <%
                    String statres = (String) request.getSession().getAttribute("searchstat");
                    if(!statres.equals("nothing")){
                        out.println(statres);
                    }

                %>
            </form>
        </div>
    </div>
</div>

<div class="filler"></div>
<div class="repcontainer" style="overflow-y: scroll; width: 30%; height: 20%">
    <%
        ReportDAO reportDAO = (ReportDAO) request.getServletContext().getAttribute("reportdao");
        ArrayList<Report> reps = reportDAO.GetAllReports();
        for (int i = 0; i < reps.size(); i++) {
            out.println("<div class=\"torep\">"+reps.get(i).getReported()+" is reported by: "+reps.get(i).getReporter()+" because "+reps.get(i).getReason()+"" +
                    "<form action=\"RepUser\" method=\"post\">\n" +
                    "    <input type=\"hidden\" name=\"torep\" value=\""+reps.get(i).getReported()+"\">\n" +
                    " <input type=\"hidden\" name=\"repr\" value=\""+reps.get(i).getReporter()+"\">\n" +
                    " <input type=\"submit\" name=\"act\" value=\"approve report\">\n" +
                    " <input type=\"submit\" name=\"act\" value=\"cancel report\">\n" +
                    "</form></div>");
        }
    %>
</div>
<h3>New Events:</h3>
<ul>
    <%
        List<Event> events = evdao.getUnapprovedEvents();
        if (!events.isEmpty()) {
            out.print("<h3>New Events:</h3>");
        } else out.print("<h3>No Event Requests:</h3>");
        for (Event event : events) {

    %>
    <li>
        <p>Event Name: <%=event.getEvent_name()%> </p>
        <p>Event Description: <%=event.getEvent_description()%> </p>
        <p>Event Date: <%=event.getEvent_date()%> </p>
        <p>Event Time: <%=event.getEvent_time()%> </p>
        <p>Event Category: <%=event.getEvent_category()%> </p>
        <p>Event Picture: <img src="eventpicks/<%=event.getEvent_picture()%>"  width="150" height="150">
        </p>
        <form action="admin" method="post">
            <input type="hidden" value="<%=event.getEvent_name()%>" name="approveEventName">

            <input type="submit" value="Approve Event" name="type"></p>
            <input type="submit" value="Delete Event" name="type"></p>
        </form>
        <p>_______________________________________________</p>
    </li>
    <%
            }
    %>
</ul>
</body>
</html>
