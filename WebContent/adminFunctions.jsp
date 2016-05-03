<html>
<%if (request.getSession().getAttribute("userID") == null) {
	response.sendError(403, "You are not authorized to access this page.");
}
	if (!request.getSession().getAttribute("role").equals("admin")) {
		response.sendRedirect("GetContent");
		
	}%>
	<head>
		<title>
			Admin Functions - Bouncehouse Emporium
		</title>
	</head>
	<body>
		<h1>
			<center>
				Admin Functions
			</center>
		</h1>
		<center>
		<a href = "addRep.jsp">
			<br>
			<br>
			Add Customer Rep
		</a>
		<a href = "salesReports.jsp">
			<br>
			<br>
			Generate Sales Reports
		</a> 
		<a href = "GetContent">
			<br>
			<br>
			Home	
			</a> 
		</center>
	</body>
</html>