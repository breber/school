$().ready(function() {
	$(".menuitems").hide();
	alert("Hello! I am breber");
	
	$("#dmenu > li > .menuitems > li").click(function() {
		console.log("Menuitem clicked: " + this.innerText);
		event.stopPropagation();
	});
	
	$("#dmenu > li").click(function() {
		console.log("List Item clicked: " + this.innerText);
		// If the item clicked on has text that starts
		// with '__', toggle the children menuitems
		if (this.innerText.indexOf("__") == 0) {
			$(this).children(".menuitems").toggle(100);
		}
	});
});