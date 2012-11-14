/*
 * Brian Reber (breber)
 *
 * In order to make this solution work with the Exercise 7 spec,
 * I added a line that hides all of the submenus before expanding
 * the new submenu that was clicked. 
 */

/*
   Function handler for document ready
*/
$(document).ready(function(){
  flag = false;        // global flag variable to make sure second level li clicks
                       // do not collapse the first level ul
  makeInvisible();     // makeInvisible makes all second level li invisible
  makemenuDynamic();   // makemenuDynamic associates appropriate function handlers 
                       // for expansion and collapse
  showMenu();          // showMenu just shows the html page corresponding to second-level li
});

/*
  Update the style using the class definitions 
  (the spec ensures all second level unordered list has the same class specification)
*/
function makeInvisible() {
  $(".menuitems").css("display", "none");
  $('#content').load('./auxi/spec.html');   // no need just to show the homework specs
}

/*
  Find the appropriate list elements and associate toggling functionality
  -- first click will make the list elements visible
  -- second click will make the list elements invisible

  Find the appropriate list elements and associate simple click functionality
  -- grab the name of the list items in the second level unordered list and
     open the file with the same name
*/
function makemenuDynamic() {
  $($('#dmenu').children('li')).click(
    function(){
      if (!flag) {   // not because of second level click
        // No matter what, hide all submenus every time there is a click
        // Then we will slide down the one menu we want
        $('#dmenu > li').children('ul').slideUp();

        if ($(this).find('ul:visible').size() == 0) {  // collapsed tree
          $(this).children('ul').slideDown();
        } else {                                       // expanded tree
          $(this).children('ul').slideUp();  
        }
      }
      flag = false;  // reset the flag if it was set in the second level click
    }
  );
}

/*
  Click functionality for second level   
*/
function showMenu() {
  $($('.menuitems').children('li')).click(
    function() {
      flag = true; // set the flag so that first level click function does nothing
      var text = $(this).text();  // get the text
      if (/^(.*)$/.test(text)) {
        var matched = './auxi/' + RegExp.$1;
        openInContent(matched); // open the corresponding page 
      }
    }
  );
}

function openInContent(filename) {
  $('#content').empty();
  $('#content').load(filename + '.html');
}