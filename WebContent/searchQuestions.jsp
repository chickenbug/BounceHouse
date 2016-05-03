<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bouncehouse Emporium - Question Search</title>
</head>
<center><h1> Bouncehouse Emporium - Search Questions </h1></center>
<body>
<center>
Looking for a specific question topic? Choose topic to search by:
		<form action = "SearchQuestions" method = post>
			<br>
			Topic
			<br>
			<select name = "topic">
				<option value = "" selected>View All</option>
				<option value = "General">General</option>
				<option value = "Account">Account</option>
				<option value = "Auction">Auction</option>
				<option value = "Bid">Bid</option>
				<option value = "Other">Other</option>
			</select>
			<br>
			<br>
			<input type = "submit" value = "Search">
		</form>
		<br>
			<a href = "ViewQuestions">
			<br>
			<br>
			Return to Question Page		
			</a> 
			</center>
</body>
</html>