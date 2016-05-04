<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<!DOCTYPE html!>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Successful Bid</title>
</head>
<body>
<h1>Your <%=(String)request.getAttribute("type")%>Bid with value <%= (String)request.getAttribute("value")%> was Successful!</h1>
<h2>If you do not see yourself as the winning bid, you may have been out-bid by an AutoBid(TM)</h2>
<a href="./auction?<%=(String)request.getAttribute("auction")%>">Return to Auction</a>
</body>
</html>