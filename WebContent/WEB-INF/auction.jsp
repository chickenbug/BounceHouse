	<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	    pageEncoding="ISO-8859-1"%>
	<%@ page import="model.Auction, model.Item, model.User, model.Bid"%>
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
		   	if(a.completed == 1){
			   	if(a.win_bid >= a.min_bid && Bid.maxBidUsername(a.AuctionID)!=null){
			   		out.write("<h2> Winnner is " + Bid.maxBidUsername(a.AuctionID)+ "</h2>");
			   	}
			   	else{
			   		out.write("No Winners");
			   	}
		   	}
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
		<td>Bid Increment</td>
		<td>
		<%=a.increment%>
		</td>
		</tr>
		<tr>
		<td>Top Bid</td>
		<td><%=a.win_bid%></td>
		</tr>
		<tr>
		<td>Top Bid Owner</td>
		<td>
		<%
			if(Bid.maxBidUsername(a.AuctionID) != null) out.write(Bid.maxBidUsername(a.AuctionID));
			else out.write("none");	
		%>
		</td>
		</tr>
		</table>
		
		<h1>Bidding Area</h1>
		All Bids or AutoBids must be at least $1 more than the current top bid <br><br>
		<form action="bid" method="post" onsubmit="return confirm('Confirm Bid?');">
			<b>Standard Bid-</b><br>
			<input type = "number" name = "bid" step = "any" min="<%=a.win_bid + a.increment%>" required>
			<input type="hidden" name="auction" value="<%=a.AuctionID%>">
			<%if(a.completed == 0) out.write("<input type=\"submit\" value=\"bid\">"); %>
		</form>
		<form action="MakeAutoBid" method="post" onsubmit="return confirm('Confirm Auto-Bid?');">
		<b>Autobid-</b><br>
		If someone else has the winning bid, Autobid(TM pending) will bid $1 higher for you until upperLimit is reached <br>
		If you already have an Autobid setup, this will replace your current autobid. <br>
		<br>
		MaxBid
		<input type = "number" name = "MaxBid" step = "any" min="<%=a.win_bid+a.increment%>" required>
		<input type="hidden" name="auction" value="<%=a.AuctionID%>">
		<%if(a.completed == 0) out.write("<input type=\"submit\" value=\"SetUp Autobid\">"); %>
		</form>
		
		<a style="padding-right:10px" href="./create_auction"> Create Another Auction   </a>
		<a href="./GetContent"> Home </a>
		

	</body>
	</html>