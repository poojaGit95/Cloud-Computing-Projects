<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body background="book2.jpeg">
     <h2> Book-Shelf </h2>
        <form name="uploadData" action="/upload" method="post" enctype="multipart/form-data">
            <table>
                <tbody>
                <tr>
                    <p> To upload a book enter book title and file path then click on UPLOAD button </p>
                </tr>
                <tr>
                    <td>Book Title:</td>
                    <td><input type="text" name="bookTitleU" value="" size ="50"/></td>
                </tr>
                </tbody>
            </table>
            <input type="file" name="file" /><br/><br/>
            <br>
            <input type="submit" value="UPLOAD" name="upload"/>
            <h4>${uploadErrorMsg}</h4>
            <h4>${uploadMsg}</h4>
        </form>

</body>
</html>