package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {
  
  public static Result index() {
    return ok(
      index.render()
    );
  }

  public static Result interpret() {
    return ok(
      interpret.render()
    );
  }

  public static Result getRegisters() {
    return ok(
      registers.render()
    );
  }
  
}
