<html>
	<head>
		<title>Bouncehouse Emporium - Create Alert</title>
	</head>
	<body>
		<h1>
			<center>
				Bouncehouse Emporium
			</center>
		</h1>
		<h3>
			<center>
				Create Alert
			</center>
		</h3>
		<hr>
		Looking for a specific item, but can't find one that's just right? Fill out the following form and we'll alert you when the perfect item is ready:
		<form action = "CreateAlert" method = post>
			<br>
			Bounciness (scored on a scale of one to ten)
			<br>
			<select name = "bounciness" id = "bounciness">
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
			<select name = "category" id = "category">
				<option value = "House">House</option>
				<option value = "Slide">Slide</option>
				<option value = "Activity">Castle</option>
				<option value = "Other">Other</option>
			</select>
			<br>
			<br>
			Color
			<br>
			<select name = "color" id = "color">
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
			Size
			<br>
			<select name = "size" id = "size">
				<option value = 'S'>Small</option>
				<%-- <option value = 'M'>Medium</option> --%>
				<option value = 'L'>Large</option>
				<option value = 'XL'>Extra Large</option>
			</select>
			<br>
			<br>
			Subcategory
			<br>
			<select name = "subcategory" id = "subcategory">
				<option value = "Standard">Standard</option>
				<option value = "Medieval">Medieval</option>
				<option value = "Pirate">Pirate</option>
				<option value = "Character">Character (e.g. Spooderman)</option>
			</select>
			<br>
			<br>
			<input type = "submit" value = "Submit">
		</form>
	</body>
</html>