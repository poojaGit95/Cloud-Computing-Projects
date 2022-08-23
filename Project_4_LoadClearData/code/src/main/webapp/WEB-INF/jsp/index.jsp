<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<body>
     <h2 style="text-align:center"> Program 4 </h2>
        <form name="loadData" method="post">
             <h4 id="loadStatus"> To Load the data click on LOAD button </h4>
             <input type="submit" value="LOAD" name="load" onClick="loadProgressFunction()"/>
             <script>
             function loadProgressFunction() {
               document.getElementById("loadStatus").innerHTML = "Data Loading in progress, Please wait...";
               document.getElementById("status").style.display='none'
             }
             </script>
        </form>
        <br> </br>
        <form name="clearData" method="post">
             <h4 id="clearStatus"> To clear the data click on CLEAR button </h4>
             <input type="submit" value="CLEAR" name="clear" onClick="clearProgressFunction()"/>
             <script>
             function clearProgressFunction() {
               document.getElementById("clearStatus").innerHTML = "Data Clearing in progress, Please wait...";
               document.getElementById("status").style.display='none'
             }
             </script>
        </form>
        <br> </br>
        <form name="queryData" method="post">
            <table>
                <tbody>
                <tr>
                     <h4 id="queryStatus"> To get user details enter First Name or Last Name or both:</h4>
                </tr>
                <tr>
                    <td>First Name:</td>
                    <td><input type="text" name="firstName" value="" size ="50"/></td>
                </tr>
                <tr>
                     <td>Last Name:</td>
                     <td><input type="text" name="lastName" value="" size ="50"/></td>
                </tr>
                <tr>
                    <td><input type="submit" value="QUERY" name="query" onClick="queryProgressFunction()"/> </td>
                </tr>
                </tbody>
            </table>

            <br> </br>
            <script>
            function queryProgressFunction() {
              document.getElementById("queryStatus").innerHTML = "Fetching data in progress, Please wait...";
              document.getElementById("status").style.display='none'
            }
            </script>
            <h4 id="status">${detailsMsg}</h4>
        </form>
</body>
</html>