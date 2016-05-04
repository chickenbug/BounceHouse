document.addEventListener('DOMContentLoaded', function(){
	console.log("DOM Loaded");
	init();
	});

function init() {
	console.log("Executing init");
	var query = window.location.search.substring(1);
	if(query == "dt-error") { // plain login error.
		/** missing date error */
		console.log("dt-error");
		var message = document.getElementById('error');
		message.style.color = "RED";
		message.style.borderRadius = "2px";
		message.style.opacity = ".75";
		message.style.padding = "2px";
		message.innerHTML = "Close Date and Time is a required field";
	}
	else if (query == "past-error"){
		/** past date error */
		console.log("past-error");
		var message = document.getElementById('error');
		message.style.color = "RED";
		message.style.borderRadius = "2px";
		message.style.opacity = ".75";
		message.style.padding = "2px";
		message.innerHTML = "Close Date and Time must be in the future";
	}
}