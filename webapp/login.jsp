<%@page import = "BB.SQLTablesManager" %>


<html>
<head>
    <link rel="stylesheet" href="css/login.css">
</head>
<body>
<div class="whole">
    <div class="titles">
        <h1> log in or <a style="color: white; text-decoration: none" href="create_account.jsp">sign up</a></h1>
        <%
            if(!request.getSession().getAttribute("error").equals(null)){
                out.print("<div class=\"ror\">"+request.getSession().getAttribute("error")+"</div>");
                request.getSession().setAttribute("error", "");
            }
        %>
    </div>
    <div class="inputs">
        <form action="login" method="post" style="display: flex; flex-direction: column; align-items: center; justify-content: center">
            Username: <input type="text" name="username"/> <br/>
            Password: <input type="password" name="password"/> <br/>
            <input type="submit" value="login"/>
        </form>
    </div>
</div>
</body>
</html>
