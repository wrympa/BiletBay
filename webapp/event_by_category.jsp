<%@ page import="java.sql.SQLException" %>
<%@ page import="BB.Event" %>
<%@ page import="java.util.List" %>
<%@ page import="BB.SQLTablesManager" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="BB.Account" %>
<%@ page import="DAOs.EventDAO" %><%--
  Created by IntelliJ IDEA.
  User: liza
  Date: 10.07.23
  Time: 14:06
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<%
    SQLTablesManager baseManager = (SQLTablesManager) request.getServletContext().getAttribute("db");
    String category = request.getParameter("id");
    if (category == null) category = request.getParameter("category");
%>
<head>
    <title><%=category%></title>
    <link rel="stylesheet" href="css/event_by_category.css">
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

    %>
</div>
<div style="width: 100%; display: flex; flex-direction: row; justify-content: center; align-content: center"><h4 style="color:white; font-size: 40px"><%=category + " events"%></h4></div>
<%
    out.println("<p class=\"titler\">All events");%>
<form action="event_by_category.jsp" method="post">
    <label for="sortByType">Sort By:</label>
    <%
        ArrayList<String> arr = new ArrayList();
        arr.add("date");
        arr.add("popularity");
    %>
    <select name="sortByType" id="sortByType" required>
        <%for (int i = 0; i < arr.size(); i++){%>
        <option value=<%=arr.get(i)%>><%=arr.get(i)%></option>
        <%}%>
    </select>
    <br>

    <label for="order">Select Order:</label>
    <%
        ArrayList<String> sortarr = new ArrayList();
        sortarr.add("ascending");
        sortarr.add("descending");
    %>
    <select name="order" id="order" required>
        <%for (int i = 0; i < sortarr.size(); i++){%>
        <option value=<%=sortarr.get(i)%>><%=sortarr.get(i)%></option>
        <%}%>
    </select><br>

    <input type="hidden" value="<%=category%>" name="category">
    <input type="submit" value="Sort" name="sortevents">
</form>

    <% try {
        SQLTablesManager db = (SQLTablesManager) request.getServletContext().getAttribute("db");
        EventDAO eventDAO = db.getEventDAO();

        String sortBy = "";
        String order = "";
        if (request.getParameter("sortevents") != null && request.getParameter("sortevents").equals("Sort")) {
            sortBy = request.getParameter("sortByType");
            if (sortBy.equals("date")) sortBy = "event_date";
            order = request.getParameter("order");
        }
        List<Event> events = eventDAO.events_of_category(category, sortBy, order);
        for (Event e : events) {
            out.print(e.get_Html_Style());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    %>

</body>
</html>
