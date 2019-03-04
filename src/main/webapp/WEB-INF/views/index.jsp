<%--
  Created by IntelliJ IDEA.
  User: maurice
  Date: 6/27/17
  Time: 10:57 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<html>
<head>
    <title>Student Picker</title>
</head>
<body>

<a href="admin"><h2>Admin Page</h2></a>

<select id="groups" name="group">
    <c:forEach items="${groupslist}" var ="group">
        <option value="${group.name}">${group.displayName}</option>
    </c:forEach>
</select>
<form action="getstudentlist" method="GET">
    <input type="hidden" class="groupselect" name="group" />
    <input type="submit" value="Get Student List">
</form>
<script src="js/common.js"></script>
</body>
</html>
