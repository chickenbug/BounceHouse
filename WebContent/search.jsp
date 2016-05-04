<html>
	<head>
		<title>Bouncehouse Emporium - Search</title>
	</head>
	<body>
		<h1>
			<center>
				Bouncehouse Emporium
			</center>
		</h1>
		<h3>
			<center>
				Search
			</center>
		</h3>
		<hr>
		Looking for a specific item? Use the following form to search (note: this will only return items currently for sale):
		<form action = "Search" method = post>
			<br>
			Bounciness (scored on a scale of one to ten)
			<br>
			<select name = "bounciness" id = "bounciness">
				<option value = "" selected>No Preference</option>
				<option value = "1">1</option>
				<option value = "2">2</option>
				<option value = "3">3</option>
				<option value = "4">4</option>
				<option value = "5">5</option>
			</select>
			<br>
			<br>
			Category
			<br>
			<select name = "category" id = "category">
				<option value = "" selected>No Preference</option>
				<option value = "House">House</option>
				<option value = "Slide">Slide</option>
				<option value = "Activity">Castle</option>
				<option value = "Other">Other</option>
			</select>
			<br>
			<br>
			Size
			<br>
			<select name = "size" id = "size">
				<option value = "" selected>No Preference</option>
				<option value = 'S'>Small</option>
				<option value = 'M'>Medium</option>
				<option value = 'L'>Large</option>
			</select>
			<br>
			<br>
			Subcategory
			<br>
			<select name = "subcategory" id = "subcategory">
				<option value = "" selected>No Preference</option>
				<option value = "Standard">Standard</option>
				<option value = "Medieval">Medieval</option>
				<option value = "Pirate">Pirate</option>
				<option value = "Character">Character (e.g. Spooderman)</option>
			</select>
			<br>
			<br>
			<input type = "submit" value = "Search">
		</form>
		<br>
		<a href = "GetContent">Home</a>
	</body>
</html>