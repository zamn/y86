$("#commandBox").focus(function() {
  Mousetrap.bind("enter", function(e) {
    var commands = $("#commandBox").val().split("\n");
    var command = commands[commands.length-1].substring(3);
    if (command.indexOf("%") != -1) {
      command = command.replace(/%/g, "%25");
    }
    if (command === "clear") {
      $("#commandBox").val("=> ");
    } else if (command !== "") {
      $.post("/y86/interpret?command=" + command, function(returnCode) {
        switch (parseInt(returnCode, 10)) {
          case 0:
            updateCommandBox("\nStatement Completed.");
            break;
          case 1:
            updateCommandBox("\nMemory out of bounds!");
            break;
          case 2:
            updateCommandBox("\nNon-existant label for JMP operation");
            break;
          case 3:
            updateCommandBox("\nPlease input integer/character");
            break;
          case 4:
            updateCommandBox("\nEmpty Stack.");
            break;
          case 5:
            $.post("/y86/state?format=output", function(data) {
              updateCommandBox("\nOutput: " + data);
            });
            break;
        }

        $("#commandBox").val(function(i, v) {  return v + "\n=> "; });
        $("#commandBox").scrollTop(99999);
      });
    }

  $.getJSON("/y86/state?format=standard", function(data) {
    var registers      = data[0],
        flags          = data[1],
        stack          = data[2],
        programCounter = data[3][0];

    //Parse to proper types where necessary.
    //registers      = registers.map(function(x) { return parseInt(x, 10); });
    //flags          = flags.map(    function(x) { return x === "true";    });

    //Update the registers table.
    $("#registers tr > :nth-child(2)").each(function(i, x) {
      greenIfChange(x, $(x).text(), registers[i]);
      $(x).text(registers[i]);
    });

    //Update the flags table.
    $("#flags tr > :nth-child(2)").each(function(i, x) {
      greenIfChange(x, $(x).text(), flags[i]);
      $(x).text(flags[i]);
    });

    //Update the programCounter table.
    greenIfChange(
        $("#programCounter td")[0],
        $("#programCounter td").text(),
        programCounter);
    $("#programCounter td").text(programCounter);

    //Clear and update the stack table.
    $("#stack tr td").parent().remove();
    stack.reverse().forEach(function(x) {
      $("#stack").append("<tr><td>"+x+"</td></tr>");
    });

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

//Helper Functions
var updateCommandBox = function(text) {
  $("#commandBox").val(function(i, v) {  return v + text; });
}
var greenIfChange = function(el, oldval, newval) {
  oldval !== newval ?
    $(el).addClass("green") : $(el).removeClass("green");
}
