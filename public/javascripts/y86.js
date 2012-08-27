$("#commandBox").focus(function() {
  Mousetrap.bind("enter", function(e) {
    $("#commandBox").val(function(i, v) {  return v + "\n=> "; });
    $("#commandBox").scrollTop(99999);
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
