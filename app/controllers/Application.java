package controllers;

import play.*;
import play.mvc.*;

import views.html.*;
import models.*;

public class Application extends Controller {
  
  public static Result index() {
    return ok(
      index.render()
    );
  }

  public static Result interpret(String command) {
    Test t = new Test();
    return ok(
      interpret.render(t.interpret(command))
    );
  }

  public static Result getRegisters() {
    return ok(
      registers.render()
    );
  }
  
}
