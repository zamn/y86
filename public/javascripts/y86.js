var isHalted = false;

$("#commandBox").focus(function() {
  if (!isHalted) {
    Mousetrap.bind("enter", function(e) {
      var commands = $("#commandBox").val().split("\n"),
          command  = commands[commands.length-1].substring(3);

      //Save most recent command.
      $("#commandBox").data("command", command);

      if (command.indexOf("%") != -1) {
        command = command.replace(/%/g, "%25");
      } if (command === "clear") {
        $("#commandBox").val("=> ");
      } else if (command !== "") {
        $.post("/y86/interpret?command=" + command, function(returnCode) {
          var format = "standard";
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
              format = "output";
              $.get("/y86/state?format=output", function(data) {
                updateCommandBox("\n\tOutput: "+data.replace(/\n/, '')+"=> ");
                $("#commandBox").scrollTop(99999);
              });
              break;
            case 6:
              updateCommandBox("\nEnd of simulation.");
              isHalted = true;
              $("#commandBox").blur();
              break;
            case 7:
              updateCommandBox("\nNo label for previous jump. Instruction saved, not executed. Needs label for previous jump.");
              break;
          }

          if (format !== "output") {
            $("#commandBox").val(function(i, v) {  return v + "\n=> "; });
          }
          $("#commandBox").scrollTop(99999);
        });
      }

      // A dirty hack to allow the server time to update its data.
      setTimeout(function() {
        $.getJSON("/y86/state?format=standard&"+Date.now(), function(data) {
          var registers      = mapToString(data[0]),
              flags          = mapToString(data[1]),
              stack          = mapToString(data[2]),
              programCounter = data[3];

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
              "0x"+programCounter);
          $("#programCounter td").text("0x"+programCounter);

          //Clear and update the stack table.
          $("#stack tr td").parent().remove();
          stack.reverse().forEach(function(x) {
            $("#stack").append("<tr><td>"+x+"</td></tr>");
          });

        });
      }, 800);

    return false;
    });
  }
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
var mapToString = function(arr) {
  return arr.map(function(x) { return x.toString(); });
}
