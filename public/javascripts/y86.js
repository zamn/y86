$("#commandBox").focus(function() {
  Mousetrap.bind("enter", function(e) {
    var commands = $("#commandBox").val().split("\n");
    var command = commands[commands.length-1].substring(3);
    if (command === "clear") {
      $("#commandBox").val("=> ");
    }
    else {
      var returnCode;
      $.post("/y86/interpret?command=" + command, function(data) {
        returnCode = parseInt(data, 10);
        console.log(returnCode);
        console.log(typeof(returnCode));
        switch (returnCode) {
          case 0:
            $("#commandBox").val(function(i, v) {  return v + "\nStatement Completed."; });
            break;
          case 1:
            console.log('hi');
            $("#commandBox").val(function(i, v) {  return v + "\nMemory out of bounds!"; });
            break;
          case 2:
            $("#commandBox").val(function(i, v) {  return v + "\nNon-existant label for JMP operation"; });
            break;
          case 3:
            $("#commandBox").val(function(i, v) {  return v + "\nPlease input integer/character"; });
            break;
          case 4:
            $("#commandBox").val(function(i, v) {  return v + "\nEmpty Stack."; });
            break;
          case 5:
            $.post("/y86/state?type=output", function(data) {
              $("#commandBox").val(function(i, v) {  return v + "\nOutput: " + data; });
            });
            break;
        }
        $("#commandBox").val(function(i, v) {  return v + "\n=> "; });
        $("#commandBox").scrollTop(99999);
      });
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
