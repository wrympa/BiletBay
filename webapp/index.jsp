<%@page import = "BB.SQLTablesManager" %>
<%@page import = "BB.Account" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="BB.Event" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <link rel="stylesheet" href="css/index.css">
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
<div class="contents">
    <div class="interests" style="overflow-y: scroll">
        <%
            if(acc != null){
                out.println("<p class=\"titler\">Your interests</li>");
                ArrayList<Event> events = db.getInterestDAO().getInterestedEvents(acc.getUsername());
                for(int i=0; i<events.size(); i++){
                    String intrname = events.get(i).getEvent_name();
                    String img = events.get(i).getEvent_picture();
                    out.print("<div class=\"interest\"><a style=\"text-decoration:none; color:white;\" href=\"event.jsp?id="+intrname +"\" class=\"xshift\">"+ intrname +"</a><img class=\"intimage\" src=\"eventpicks/" +img+"\"></div><br>");
                }
            }
            else out.print("<a style=\"text-decoration:none; color:white\" class=\"titler\">please log in to view interests</a><br>");

        %>
    </div>
    <div class="events">
        <div class="top3">
            <%
                ArrayList<Event> top3 = db.getEventDAO().gettop3();
                for (int i = 0; i < top3.size(); i++) {
                    out.print("<a class=\"n"+(i+1)+"\" style=\"text-decoration:none; color:white;\" href=\"event.jsp?id="+top3.get(i).getEvent_name()+"\"><img class=\"t3i\" style=\"object-fit: cover; width:100%; height:100%\" src=\"eventpicks/"+top3.get(i).getEvent_name()+".jpg\"></a>");
                }
            %>
        </div>
        <div class="lists">
            <div class="eventlist" style="overflow-y: scroll">
                <div class="titler" style="margin-left: 42%; position: absolute; display: flex; flex-direction: column; justify-content: space-evenly; align-items: center">All events<br> <form action="index.jsp" method="post">
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

                    <input type="submit" value="Sort" name="sortevents">
                </form></div>
                <%
                    List<Event> e = db.getEventDAO().GetAllEvents();
                    if (request.getParameter("sortevents") != null && request.getParameter("sortevents").equals("Sort")) {
//                    out.println("DAISORTAAA");
                        String sortBy = request.getParameter("sortByType");
                        if (sortBy.equals("date")) sortBy = "event_date";
//                    out.println("SORT BY " + sortBy);
                        String order = request.getParameter("order");
//                    out.println("ORDER " + order);
                        e = db.getEventDAO().getSortedEvents(sortBy, order);

                    }
                    for(int i=0; i<e.size(); i++){
                        String intrname =  e.get(i).getEvent_name();
                        String img = e.get(i).getEvent_picture();
                        out.print("<div class=\"event\"><a style=\"text-decoration:none; color:white\" href=\"event.jsp?id="+intrname +"\" class=\"xshift\">"+ intrname +"</a><img class=\"evimage\" src=\"eventpicks/" +img+"\"></div><br>");
                    }
                %>
            </div>
            <div class="categorylist" style="overflow-y: scroll">
                <%
                    out.println("<p class=\"titler\">All categories");
                    ArrayList<String> cats = (ArrayList<String>) request.getServletContext().getAttribute("categories");
                    for(int i=0; i<cats.size(); i++){
                        out.print("<div class=\"category\"><a style=\"text-decoration:none; color:white;\" href=\"event_by_category.jsp?id="+cats.get(i)+"\" name=\"category\" class=\"xshift\">"+ cats.get(i)+"</a></div><br>");
                    }
                %>
            </div>
        </div>
    </div>
</div>
</body>
</html>