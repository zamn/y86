$("#commandBox").val("=> ");

Mousetrap.bind('enter', function() {
  event.preventDefault();
  $("#commandBox").val(function(i, v) {  return v + "\n=> "; });
});
