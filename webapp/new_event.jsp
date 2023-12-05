<%@ page import="java.util.ArrayList" %>
<%@ page import="BB.Ticket" %>
<%@ page import="DAOs.EventDAO" %>
<%@ page import="BB.Event" %>
<%@ page import="BB.Account" %><%--
  Created by IntelliJ IDEA.
  User: User
  Date: 8/4/2023
  Time: 13:14
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
  String source = request.getQueryString().split("=")[1];
  String title, header, button, name = "", desc = "", img = "", date = "", time = "", category = "";
  if(source.equals("edit_ticket")){
    title ="Edit Ticket";
    header = "Edit Ticket";
    button = "Save Changes";
    request.getSession().setAttribute("source", "edit_ticket");
    Ticket t = (Ticket) request.getSession().getAttribute("ticketToEdit");
    name = t.getEvent_name();
    EventDAO eDAO = (EventDAO) request.getServletContext().getAttribute("eventdao");
    Event e = eDAO.GetEventByName(name);
    desc = e.getEvent_description();
    date = e.getEvent_date();
    time = e.getEvent_time();
    category = e.getEvent_category();
    img = e.getEvent_picture();
  }else{
    title = "Add Ticket";
    header = "Add New Ticket";
    button = "Add";
    request.getSession().setAttribute("source", "add_ticket");
  }
%>
<html>
<head>
    <title><%=title%></title>
    <link rel="stylesheet" href="css/edit.css">
</head>
<body onload="setDateRestriction()">
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

<h1 style="color:white;">Add new Event</h1>
<h3 style="color:white;">Please enter event details</h3>
<form action="/addEvent" method="post" enctype="multipart/form-data" style="color: white; display: flex; flex-direction: column; justify-content: center; align-items: center">

  <label for="name">Event name:</label>
  <input type="text" name="name" id="name" value="<%=name%>" required>
  <br>

  <label for="description">Description:</label>
  <textarea name="description" id="description" required><%=desc%></textarea>
  <br>

  <label for="image">Image:</label>
  <img src="/eventpicks/<%=img%>" width="100" height="200">
  <input type="file" name="image" id="image">

  <%ArrayList<String> arr = (ArrayList<String>)application.getAttribute("categories");%>
  <p>Category:
    <select name="category" value=<%=session.getAttribute("category")%> required>
      <%for (int i = 0; i < arr.size(); i++){%>
      <option value=<%=arr.get(i)%> <%if(!category.isEmpty()){if(category == arr.get(i)) out.print("selected");}%>><%=arr.get(i)%></option>
      <%}%>
    </select>
  </p>

  <label for="date">Date:</label>
  <input type="date" id="date" name="date" min="" value="<%=date%>" required>
  <br>

  <label for="time">Time:</label>
  <input type="time" id="time" name="time" value="<%=time%>" required>
  <br>

  <script>
    function setDateRestriction() {
      const curDate = new Date();
      const formatted = curDate.toISOString().split('T')[0];
      document.getElementById("date").setAttribute("min", formatted);
    }
    setDateRestriction();
  </script>

  <button type="submit"><%=button%></button>
</form>
<form action="my_page.jsp">
  <button type="submit" onclick="changeRequirements()">Back</button>
</form>
</body>
</html>
