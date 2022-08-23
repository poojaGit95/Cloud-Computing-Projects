<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
<style>
    body {
        text-align: center;
    }
    .button-container-div {
        width: 100;
        height: 100;
        display: flex;
        justify-content: center;
        align-items: center;
    }
</style>
</head>
<body background="book2.jpeg">
     <h1> BookShelf </h1>
        <h4>${loadMsg}</h4>
        <form name="start" action="/welcome" method="post">
             <p> Book-Shelf is a public book respository.
              It lets anyone download books.
              You can get updates of new books added to the repository.
              You can also contribute to the repository by uploading books.
              Book-shelf also gives you information of the NewYork Times top 5
              educational books of the month.</p>
              <p> Happy Reading!!! </p>
        </form>
        <div class="button-container-div">
            <form name="button1" action="/download" method="post">
                 <input type="submit" value="DOWNLOAD" name="downloadWelcomePage" />
            </form>

            <form name="button2" action="/upload" method="post">
                 <input type="submit" value="UPLOAD" name="uploadWelcomePage"/>
            </form>

            <form name="button3" action="/subscribe" method="post">
                 <input type="submit" value="SUBSCRIBE" name="subscribeWelcomePage"/>
            </form>

            <form name="button4" action="/topbooks" method="post">
                 <input type="submit" value="TOP BOOKS INFO" name="top5booksWelcomePage"/>
            </form>
        </div>
</body>
</html>