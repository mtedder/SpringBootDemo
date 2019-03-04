<%--
  Created by IntelliJ IDEA.
  User: maurice
  Date: 8/15/17
  Time: 1:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<html>
<head>
    <title>Admin Page</title>
</head>
<body>

<p>${message}</p>

<select id="groups" name="group">
    <c:forEach items="${groupslist}" var ="group">
        <option value="${group.name}">${group.displayName}</option>
    </c:forEach>
</select><br>

<form action="addstudents" method="POST">

    <label for="namecsv">Enter Student names in csv format</label><br>
    <textarea type="text" id="namecsv" name="namecsv" cols="40" rows="5"></textarea><br>
    <input type="submit" value="Add name(s) to list">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
    <input type="hidden" class="groupselect" name="group" />
</form>
<br>
<form action="getrandomstudents" method="get">
    <input type="number" id="numberofstudents" name="numberofstudents" step="1" min="0" value="1"><br>
    <input type="hidden" class="groupselect" name="group" />
    <input type="submit" value="Get Random student(s)">
</form>

<%--Show results--%>
<c:choose>
    <c:when test="${empty studentlist && !(empty pageContext.request.queryString)}">
        <%--only show this message if a query has been submitted (request parameters are in the url)--%>
        <h1>No students available, you may need to do a reset.</h1>
    </c:when>
    <c:otherwise>
        <h1>On Deck</h1>
        <c:forEach items="${studentlist}" var ="student">
            ${student.firstName} &nbsp; ${student.lastName} <br>
        </c:forEach>
    </c:otherwise>
</c:choose>

<form action="resetstudentlist" method="GET">
    <input type="hidden" class="groupselect" name="group" />
    <input type="submit" value="Reset Student List">
</form>

<form action="getstudentlist" method="GET">
    <input type="hidden" class="groupselect" name="group" />
    <input type="submit" value="Get Student List">
</form>
<%--<a href="resetstudentlist"><h2>Reset</h2></a>--%>
<a href="/"><h2>Home</h2></a>
<%--<a href="getstudentlist"><h2>Get Student List</h2></a>--%>
<form action="/logout" method="POST">
   <!--  <input type="hidden" name="" value="" /> -->
    <input type="submit" value="Logout" />
</form>

       <!--  <form action="@{/logout}" method="post">
            <input type="submit" value="Sign Out"/>
        </form> -->

<script src="js/common.js"></script>
</body>
</html>
