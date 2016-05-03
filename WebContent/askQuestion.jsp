<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bouncehouse Emporium - Ask Question</title>
</head>
<body>
<center>
        <h1>Bouncehouse Emporium</h1>
        <h2> Ask A Question </h2>
		Have a question?
		<br>
		Please fill out the form below and submit when you are ready.
		<br>
		A customer representative will answer your question as soon as possible.
		<br><span id="error"></span>
		<form action = "AddQuestion" method = "post">
			<h2>New Question</h2>
			<br>
			<br>
			Topic
			<br>
			<select name = "topic" >
				<option value = "General">General</option>
				<option value = "Account">Account</option>
				<option value = "Auction">Auction</option>
				<option value = "Bid">Auction</option>
				<option value = "Other">Other</option>
			</select>
			<br>
			<br>
			Question
			<br>
			<textarea name="qtext" cols = "50" ></textarea>
			<br>
			<br>
			<input type = "submit" value = "Submit">
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