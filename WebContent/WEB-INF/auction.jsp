<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="model.Auction, model.Item, model.User"%>
<!DOCTYPE html">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Auction</title>
</head>
<body>
	<h1>View your Auction</h1>

	<%
		Auction a = (Auction)request.getAttribute("a");
	   	Item i = (Item)request.getAttribute("i");
	%>
	<h2>Item Info</h2>
	<table border="1">
	<tr>
	<td>Title</td>
	<td><%=i.title %></td>
	</tr>
	<tr>
	<td>Description</td>
	<td><%=i.description%></td>
	</tr>
	<tr>
	<td>Category</td>
	<td><%=i.category %></td>
	</tr>
	<tr>
	<td>Subcategory</td>
	<td><%=i.subCategory %></td>
	</tr>
		<tr>
	<td>Size</td>
	<td><%=i.size %></td>
	</tr>
	<tr>
	<td>Bounciness</td>
	<td><%=i.bounciness %></td>
	<tr>
	</table>
	<h2>Auction Info</h2>
	<table border="1">
	<tr>
	<td>Close Date</td>
	<td><%= a.close_date.toString() %></td>
	</tr>
	<tr>
	<td>Status</td>
	<td>
		<%
			if(a.completed == 1) out.write("closed");
			else out.write("open");	
		%>
	</td>
	</tr>
	<tr>
	<td>Seller</td>
	<td><%=User.findUName(a.user_id)%></td>
	</tr>
	<tr>
	<td>Top Bid</td>
	<td><%=a.win_bid%></td>
	</tr>
	</table>
	
	<h1>Bidding Area</h1>
	<form action="bid" method="post" onsubmit="return confirm('Confirm Bid?');">
	Standard-Bid
	<input type = "number" name = "bid" step = "any" min="<%=a.win_bid%>" required>
	<input type="hidden" name="auction" value="<%=a.AuctionID%>">
	
	<input type="submit" value="bid">
	</form>
	<form action="auto_bid" method="post" onsubmit="return confirm('Confirm Auto-Bid?');">
	Autobid- If someone else has the winning bid, Autobid(TM pending) will bid $1 higher for you until upperLimit is reached <br>
	----------- If you already have an Autobid setup, this will change the maxBid on your current autobid
	<br>
	MaxBid
	<input type = "number" name = "MaxBid" step = "any" required>
	<input type="submit" value="SetUp Autobid">
	</form>
	
	
	
	
	<a style="padding-right:10px" href="./create_auction"> Create Another Auction   </a>
	<a href="./main.jsp"> Home </a>
	

</body>
</html>