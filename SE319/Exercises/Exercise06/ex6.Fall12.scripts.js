/*****************************************************/

function myclock() { 
    var date=new Date();
    var hour=date.getHours();
    var min=date.getMinutes();
    var sec=date.getSeconds();
    formatmin=format(min);
    formatsec=format(sec);
    timeout = setTimeout("myclock()", 1000); 
}

function format(x) {
  if (x<10) x="0"+x;
  return x;
}

