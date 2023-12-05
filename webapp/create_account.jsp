<%--
  Created by IntelliJ IDEA.
  User: User
  Date: 7/6/2023
  Time: 11:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <title>Create Account</title>
  <link rel="stylesheet" href="css/create_accounts.css">
</head>
<body>
<div class="whole">
  <div class="title">
    <h1>Create New Account</h1>
    <%out.print("<div class=\"ror\">"+request.getSession().getAttribute("error")+"</div>");
      request.getSession().setAttribute("error", "");
    %>
  </div>
  <div class="inputs">
    <form action="createAccount" method="post" enctype="multipart/form-data" style="display: flex; flex-direction: column; align-items: center; justify-content: center">
      <label for="userNameLabel">Username: </label>
      <input type="text" id="userNameLabel" name="username"><br>

      <label for="passwordLabel">Password: </label>
      <input type="password" id="passwordLabel" name="password"><br>

      <label for="repeatPasswordLabel">Repeat Password: </label>
      <input type="password" id="repeatPasswordLabel" name="repeatPassword"><br>

      <label for="numberLabel">Phone Number: </label>
      <input type="text" id="numberLabel" name="number"><br>

      <input type="file" name="img" value="Choose File"/> <br/>
      <input type="submit" value="Register" name="register" >
    </form>
  </div>
</div>
</body>
</html>
