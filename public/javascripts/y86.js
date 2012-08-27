$("#commandBox").val("=> ");

Mousetrap.bind('enter', function(e) {
  e.preventDefault();
  $("#commandBox").val(function(i, v) {  return v + "\n=> "; });
  return false;
});
