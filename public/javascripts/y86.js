$("#commandBox").focus(function() {
  Mousetrap.bind("enter", function(e) {
    var commands = $("#commandBox").val().split("\n");
    var command = commands[commands.length-1].substring(3);
    console.log(command);
    if (command === "clear") {
      $("#commandBox").val("=> ");
    }
    else {
      $.post("/y86/interpret?command=" + command);
      $("#commandBox").val(function(i, v) {  return v + "\n=> "; });
      $("#commandBox").scrollTop(99999);
    }

  $.getJSON("/y86/state", function(data) {
      console.log("Great success!");
      console.log(data);
  });

  return false;
  });
});

$("#commandBox").blur(function() {
  Mousetrap.unbind("enter");
});

$("#commandBox").bind("keyup change", function() {
  this.value = this.value;
});

$("#commandBox").focus();
$("#commandBox").val("=> ");

$("#registerArea").load("/y86/registers");
