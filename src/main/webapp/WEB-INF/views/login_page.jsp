<%--
  Created by IntelliJ IDEA.
  User: maurice
  Date: 8/14/17
  Time: 10:01 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%--<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>--%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
    <title>Login Form</title>
</head>
<body>
    <br /> <br /> <br />
    <h2>Login to access the Admin page</h2>
    <br />
    <div style="text-align: left; padding: 30px;border: 1px solid green;width: 100%;">
        <c:url var="loginUrl" value="/login" />
        <form method="POST" action="${loginUrl}">
            <table>
                <tr>
                    <td colspan="2" style="color: red">${message}</td>

                </tr>
                <tr>
                    <td>User Name:</td>
                    <td><input type="text" name="username" />
                    </td>
                </tr>
                <tr>
                    <td>Password:</td>
                    <td><input type="password" name="password" />
                    </td>
                </tr>
                <tr>
                    <td> </td>
                    <td><input type="submit" value="Login" />
                    </td>

                </tr>
            </table>
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>
        <br>
        <a href="/">Home</a><br>
        <form action="/logout" method="POST">
            <input type="hidden" name="" value="" />
            <input type="submit" value="Logout" />
        </form>
 <%--       <h3><a href="${logoutUrl}">Logout</a></h3>--%>
    <%--<h1><a href="adduser">Sign Up</a></h1>--%>
    </div>
</body>
</html>
