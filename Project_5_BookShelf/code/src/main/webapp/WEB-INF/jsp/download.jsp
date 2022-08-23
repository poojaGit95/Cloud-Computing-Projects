<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body background="book2.jpeg">
     <h2> Book-Shelf </h2>
        <form name="downloadData" action="/download" method="post">
            <table>
                <tbody>
                <tr>
                    <p> To download a book enter book title or author name and click on DOWNLOAD button </p>
                </tr>
                <tr>
                    <td>Book Title:</td>
                    <td><input type="text" name="bookTitleD" value="" size ="50"/></td>
                </tr>
                </tbody>
            </table>
            <br>
            <input type="submit" value="DOWNLOAD" name="download"/>
            <h4>${downloadErrorMsg}</h4>
            <h4>${downloadMsg}</h4>
        </form>
</body>
</html>