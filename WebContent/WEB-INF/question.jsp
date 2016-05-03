<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Bouncehouse - Question</title>
</head>
<body>
	<center><h1>View Question and Answer</h1>

	<%
		String topic = (String)request.getAttribute("topic");
		String qtext = (String)request.getAttribute("qtext");
		String answer = (String)request.getAttribute("answer");
		String username = (String)request.getAttribute("username");
		String repname = (String)request.getAttribute("repname");
		String role = (String)request.getAttribute("role");
		int qid = (Integer)request.getAttribute("qid");
	%>
	<h2>Question Info</h2>
	<table border="1">
	<tr>
	<td>Topic</td>
	<td><%=topic %></td>
	</tr>
	<tr>
	<td>Question</td>
	<td><%=qtext%></td>
	</tr>
	<tr>
	<td>Asking User</td>
	<td><%=username %></td>
	</tr>
	<tr>
	<td>Responding Rep</td>
	<td><%=repname %></td>
	</tr>
		<tr>
	<td>Answer</td>
	<%if(answer==null) out.println("<td>Question Currently Unanswered</td>");
	else out.println("<td>"+answer+"</td>");%>
	</tr>
	</table>
	<form action="question?<%=qid%>" method="post" onsubmit="return confirm('Confirm Answer?');">
	<br><br>
	<%if(role.equals("rep"))out.println("Answer Question<br><textarea name = \"questionAnswer\" cols = \"50\"></textarea>");
	out.println("<br><br><input type=\"submit\" value=\"Answer\">");%>
	<input type = "hidden" name = "qid" value = "<%=qid%>"></input>
	</form>
	<br>
	<br>
	<a href="ViewQuestions"> Question Home </a></center>
	</body>
</html>