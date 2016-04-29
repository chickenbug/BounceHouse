<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="model.Auction, model.Item"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
	<td>Min Bid</td>
	<td></td>
	</tr>
	<tr>
	<td>Seller</td>
	<td></td>
	</tr>
	<tr>
	<td>Winning Bid</td>
	<td></td>
	</tr>
	</table>

</body>
</html>