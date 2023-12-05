<%@ page import="BB.Account" %><%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/12/2023
  Time: 6:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Edit Profile</title>
    <link rel="stylesheet" href="css/edit.css">
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
<h3 style="color:white;">Only Type in fields you want to change.</h3>
<%out.println("<p style=\"color:red\">"+request.getSession().getAttribute("editError")+"</p>");
    request.getSession().setAttribute("editError", "");
%>
    <form action="editProfile" method="post" enctype="multipart/form-data" class="inputs">
        <input type="submit" value="Remove Image" name="action" > <br>

        <label for="userNameLabel">New Username: </label>
        <input type="text" id="userNameLabel" name="username"><br>

        <label for="oldPasswordLabel">Old Password: </label>
        <input type="text" id="oldPasswordLabel" name="oldPassword"><br>

        <label for="passwordLabel">New Password: </label>
        <input type="text" id="passwordLabel" name="password"><br>

        <label for="repeatPasswordLabel">Repeat Password: </label>
        <input type="text" id="repeatPasswordLabel" name="repeatPassword"><br>

        <label for="numberLabel">Phone Number: </label>
        <input type="text" id="numberLabel" name="number"><br>

        <input type="file" name="file" value="Choose File"/> <br/>

        <input type="submit" value="Save Changes" name="action" >
    </form>

</body>
</html>
