<html>
	<head>
		<title>
			Auction List - Bouncehouse Emporium
		</title>
	</head>
	<body>
		<h1>
			<center>
				Auction List
			</center>
		</h1>
		<table id = "AuctionList" cellspacing = '0' border ="2" width = "100%">
			<thead>
				<tr>
        			<th>Auction ID</th>
        			<th>Item ID</th>
        			<th>Description</th>
        			<th>Options</th>
        		</tr>
        	</thead>
        	<tbody>
        	<c:forEach items="${auctions}" var = "auction">
				<tr>
   					<td><c:out value="${auction.auctionID}"/></td>
   					<td></td>
   					<td></td>
   					<td width = "9%">
   					<center>
					<form action="editPage.jsp" method="post" style="padding: 0; margin: 0">
					<input type="submit" value="Edit" name="E1">
					</form>
					<form action="removePage.jsp" method="post" style="padding: 0; margin: 0">
					<input type = "submit" value = "Remove" name = "R1">
					</form>
					</center>
					</td>
				</tr>
			</c:forEach>
        	</tbody>
		</table>
	</body>
</html>