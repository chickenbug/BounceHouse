<html>
	<head>
		<title>
			Register - Bouncehouse Emporium
		</title>
		<script type="text/javascript">

			var monthtext=['January','February','March','April','May','June','July','August','September','October','November','December'];

			function populatedropdown(day, month, year){
				var today = new Date()
				var day = document.getElementById(day)
				var month = document.getElementById(month)
				var year = document.getElementById(year)
			
				for (var i = 31; i>0; i--)
						day.options[i-1]=new Option(i, i)
			
				day.options[today.getDate()-1] = new Option(today.getDate(), today.getDate(), true, true)
			
				for (var m = 0; m<12; m++)
					month.options[m] = new Option(monthtext[m], m+1)
			
				month.options[today.getMonth()] = new Option(monthtext[today.getMonth()], monthtext[today.getMonth()], true, true)
			
				var thisyear = today.getFullYear()
			
				for (var y = 0; y<=100; y++){
					year.options[y] = new Option(thisyear, thisyear)
					thisyear -= 1
				}
			
				year.options[0] = new Option(today.getFullYear(), today.getFullYear(), true, true)
			}
		</script>
	</head>
	<body>
		<h1>
			<center>
				Bouncehouse Emporium
			</center>
		</h1>
		Thanks for deciding to create an account with us! 
		Please fill out the form below and submit when you are ready.
		<form action = "Registration" method = "post">
			<br>
			Username
			<br>
			<input type = "text" name = "username" required>
			<br>
			<br>
			Password
			<br>
			<input type = "password" name = "password" required>
			<br>
			<br>
			Confirm Password
			<br>
			<input type = "password" name = "confirmpassword" required>
			<br>
			<br>
			E-Mail
			<br>
			<input type = "text" name = "email" required>
			<br>
			<br>
			First Name
			<br>
			<input type = "text" name = "firstname" required>
			<br>
			<br>
			Last Name
			<br>
			<input type = "text" name = "lastname" required>
			<br>
			<br>
			Birthdate
			<select name="daydropdown" id = "daydropdown"></select> 
			<select name="monthdropdown" id = "monthdropdown"></select> 
			<select name="yeardropdown" id = "yeardropdown"></select> 
			<script type="text/javascript">
				window.onload=function(){
					populatedropdown("daydropdown", "monthdropdown", "yeardropdown")
				}
			</script>
			<br>
			<br>
			Address
			<br>
			<input type = "text" name = "address">
			<br>
			<br>
			City
			<br>
			<input type = "text" name = "city">
			<br>
			<br>
			State/Province
			<br>
			<input type = "text" name = "state">
			<br>
			<br>
			Country
			<br>
			<input type = "text" name = "country">
			<br>
			<br>
			Post Code
			<br>
			<input type = "text" name = "postcode">
			<br>
			<br>
			Phone Number (Including Country Code and Area Code) 
			<br>
			<input type = "text" name = "phone"> 
			<%-- this could be type = number but older browsers dont support it (number is defined for HTML5 only) --%>
			<br>
			<br>
			<input type = "submit" value = "Create Account">
		</form>
		<br>
		<a href = "index.jsp">Back to Login</a>
	</body>
</html>