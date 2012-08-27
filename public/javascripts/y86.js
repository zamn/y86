$("#commandBox").val("=> ");

$("#commandBox").keypress(function(e) {
  if (e.which == 13) {
    event.preventDefault();
    $("#commandBox").val(function(i, v) {  return v + "\n=> "; });
  }
});
