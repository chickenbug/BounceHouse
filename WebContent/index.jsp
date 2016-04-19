<html>
	<head>
		<title>
			Login - Bouncehouse Emporium
		</title>
	</head>
	<body>
		<h1>
			<center>
				Bouncehouse Emporium
			</center>
		</h1>
		Existing user? Login below. New user? Register <a href = "Registration">here</a>.
		<form action = "main.jsp" method = "post"> 
			<br>
			Username
			<br>
			<input type = "text" name = "username">
			<br>
			<br>
			Password
			<br>
			<input type = "password" name = "password">
			<br>
			<br>
			<input type = "submit" value = "Login">
		</form>
	</body>
</html>

	<%-- 
		Real quick : method = "post" refers to the HTTP "POST" info request method:

			"POST - Submits data to be processed to a specified resource.
		
			Some other notes on POST requests:

				POST requests are never cached
				POST requests do not remain in the browser history
				POST requests cannot be bookmarked
				POST requests have no restrictions on data length"
			"

		The POST method is just saying send form data to script specified by "action" attribute.

		replace example.jsp with appropriate script that will handle the login and check against DB.
		Login script should (when we need to create it) check credentials against DB and redirect to 
		main page on success (/main.jsp) or to an error page (/error.jsp) on fail. 

		"register here" should redirect to the createAccount script, which will allow the user to register.
	--%>
