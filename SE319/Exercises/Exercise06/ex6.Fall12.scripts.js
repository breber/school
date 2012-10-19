/*****************************************************
* SE 319 - Ex6
* Brian Reber (breber)
*****************************************************/

/*****************************************************
* PART 1
*
* - There isn't a big model part in this example. The
*   model is the data we are manipulating, so it could
*   be the div content that we are manipulating.
* - The view is the div we are changing the background
*   color of, and the buttons we are clicking. It is
*   essentially all of the HTML that is displayed.
* - The controller is the logic below that handles
*   the clicks of the buttons, and changes the
*   background color.
*****************************************************/
$("#Button1").click(function() {
	$("div:first").css("background-color", "red");
});

$("#Button2").click(function() {
	$("div:first").css("background-color", "green");
});

$("#Button3").click(function() {
	$("div:first").css("background-color", "blue");
});

$("#Button4").click(function() {
	$("div:first").css("background-color", "lightgreen");
});

/*****************************************************
* PART 2
*
* - The setTimeout function is used to delay the
*   execution of a function a specified amount of time.
*   It is what allows us to continue running the myclock
*   function every second.
*
*****************************************************/
var started = false;

function myclock() {
    var date=new Date();
    var hour=date.getHours();
    var min=date.getMinutes();
    var sec=date.getSeconds();
    formatmin=format(min);
    formatsec=format(sec);
    
    if (started) {
        $("#clocktext").val(hour + ":" + formatmin + ":" + formatsec);
        timeout = setTimeout("myclock()", 1000);
    }
}

function format(x) {
    if (x < 10) {
        x = "0" + x;
    }
    return x;
}

$("#clockb").click(function() {
	var clockButtonText = $("#clockb").val();

	if ("Start Clock" === clockButtonText) {
        started = true;
		myclock();
		$("#clockb").val("Stop Clock");
	} else {
	    started = false;
	    $("#clockb").val("Start Clock");
	}
});
