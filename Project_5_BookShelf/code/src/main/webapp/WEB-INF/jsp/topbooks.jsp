<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<style>
    table, th, td {
      border: 1px solid;
    }
</style>
</head>
<body background="book2.jpeg">
     <h2> Book-Shelf </h2>
    <form name="topbooksData" action="/topbooks" method="post">
        <p> To view Top 5 NYT recommended books of the month, click on TOP BOOKS button </p>
        <input type="submit" value="TOP BOOKS" name="topbooks"/>
        <h4>${topbooksErrorMsg}</h4>
    </form>
    <table>

      <c:forEach items="${topbooksMsg}" var="item">
        <tr>
          <td><c:out value="${item}" /></td>
        </tr>
      </c:forEach>
    </table>

</body>
</html>