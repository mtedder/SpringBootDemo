<%--
  Created by IntelliJ IDEA.
  User: maurice
  Date: 6/28/17
  Time: 1:22 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>

<html>
<head>
    <title>Student List</title>
</head>
<body>

<a href="/"><h2>Home</h2></a>
<a href="admin"><h2>Admin Page</h2></a>

<p>
    <h3>Legend</h3>
    A = Available <br>
    T = Temporary state (pre-unavailable) <br>
    X = Unavailable <br>
</p>
<table border="1">
    <th>First Name</th>
    <th>Last Name</th>
    <th>Status</th>
    <th></th>
    <c:forEach items="${results}" var ="list">
        <tr>
            <td>${list.firstName}</td>
            <td>${list.lastName}</td>
            <%-- <td>${list.status}</td> --%>
			<td><!-- Find a better way to do this later -->
				<form action="updatestatus">
				<c:choose>
					<c:when test="${list.status == 'A'}">
						<input type="radio" name="status" value="A" checked> A
					</c:when>
					
					<c:otherwise>
					 	<input type="radio" name="status" value="A"> A
					</c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${list.status == 'X'}">
						<input type="radio" name="status" value="X" checked> X
					</c:when>
					
					<c:otherwise>
					 	<input type="radio" name="status" value="X"> X
					</c:otherwise>
				</c:choose>	
				
				<c:choose>
					<c:when test="${list.status == 'T'}">
						<input type="radio" name="status" value="T"> T  
					</c:when>
					
					<c:otherwise>
					 	<input type="radio" name="status" value="T"> T  
					</c:otherwise>
				</c:choose>	
				<input type="hidden" name="studentid" value="${list.id}">
				<td><input type="submit" value="Update Status"> </td>													
				</form>
			</td>           
        </tr>
    </c:forEach>

</table>
</body>
</html>
