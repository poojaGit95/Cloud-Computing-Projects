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
        <form name="subscribeData" action="/subscribe" method="post">
            <table>
                <tbody>
                <tr>
                    <p> To get notified about new books added please enter your email id and click on SUBSCRIBE button </p>
                </tr>
                <tr>
                    <td>Email ID:</td>
                    <td><input type="text" name="email" value="" size="50"/></td>
                </tr>
                </tbody>
            </table>
            <br>
            <input type="submit" value="SUBSCRIBE" name="subscribe"/>
            <h4>${subscribeErrorMsg}</h4>
            <h4>${subscribeMsg}</h4>
        </form>

</body>
</html>