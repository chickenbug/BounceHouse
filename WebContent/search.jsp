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
		Looking for a specific item? Use the following form to search:
		<form action = "Search" method = post>
			<br>
			Bounciness (scored on a scale of one to ten)
			<br>
			<select name = "bounciness">
				<option value = "" selected>No Preference</option>
				<option value = "1">1</option>
				<option value = "2">2</option>
				<option value = "3">3</option>
				<option value = "4">4</option>
				<option value = "5">5</option>
				<option value = "6">6</option>
				<option value = "7">7</option>
				<option value = "8">8</option>
				<option value = "9">9</option>
				<option value = "10">10</option>
			</select>
			<br>
			<br>
			Category
			<br>
			<select name = "category">
				<option value = "" selected>No Preference</option>
			</select>
			<br>
			<br>
			Color
			<br>
			<select name = "color">
				<option value = "" selected>No Preference</option>
				<option value = "Black">Black</option>
				<option value = "Blue">Blue</option>
				<option value = "Brown">Brown</option>
				<option value = "Green">Green</option>
				<option value = "Multi">Multi-colored</option>
				<option value = "Orange">Orange</option>
				<option value = "Pink">Pink</option>
				<option value = "Purple">Purple</option>
				<option value = "Red">Red</option>
				<option value = "White">White</option>
				<option value = "Yellow">Yellow</option>
			</select>
			<br>
			<br>
			Description - please enter any words or phrases that would describe an item you want, e.g. "spacious" or "durable".
			<br>
			<input type = "text" name = "desc">
			<br>
			<br>
			Size
			<br>
			<select name = size>
				<option value = "" selected>No Preference</option>
				<option value = "XS">Extra Small</option>
				<option value = 'S'>Small</option>
				<option value = 'M'>Medium</option>
				<option value = 'L'>Large</option>
				<option value = 'XL'>Extra Large</option>
			</select>
			<br>
			<br>
			Subcategory
			<br>
			<select name = "subcategory">
				<option value = "" selected>No Preference</option>
			</select>
			<br>
			<br>
			<input type = "submit" value = "Search">
		</form>
	</body>
</html>