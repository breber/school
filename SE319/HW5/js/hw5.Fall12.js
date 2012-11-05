// Brian Reber (breber)
// SE/ComS 319 Homework 5

// Set up all the handlers once the page is ready
$().ready(function() {
	// When the page is loaded, hide the submenus
	$("ul.menuitems").hide();
	
	// And show a popup identifying the author
	alert("Hello! I am breber");
	
	// When a submenu item has been clicked, load the content
	// of the "aux/[li text].html" into the div with ID = content
	$("#dmenu > li > ul.menuitems > li").click(function(event) {
		$("#content").load("auxi/" + $(this).text() + ".html");

		// Stop the propagation of the even so that the
		// first level list items don't collapse
		event.stopPropagation();
	});
	
	// On the click of a first-level menu item, check to see
	// that the text starts with "__", and if so, toggle the
	// display of the child menu
	$("#dmenu > li").click(function() {
		// If the item clicked on has text that starts
		// with '__', toggle the children menuitems
		if (/^__/m.test($(this).text())) {
			$(this).children("ul.menuitems").toggle(100);
		}
	});
});