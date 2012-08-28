package models;

import com.google.gson.*;
import java.util.*;

public class Test {

  public ArrayList<ArrayList<String>> testData = new ArrayList<>();

  public Test() {
    ArrayList<String> registers = new ArrayList<>();
    ArrayList<String> stack = new ArrayList<>();
    ArrayList<String> programCounter = new ArrayList<>();
    ArrayList<String> flag = new ArrayList<>();
    for (Integer i = 0; i < 8; i++) {
      registers.add(i.toString());
    }
    stack.add("0x0000FFF");
    stack.add("123x345");
    programCounter.add("0x1111");
    flag.add("true");
    flag.add("false");
    flag.add("true");
    testData.add(registers);
    testData.add(flag);
    testData.add(stack);
    testData.add(programCounter);
  }

  public int interpret(String message) {
    Random r = new Random();
    int derp = r.nextInt(6);
    return derp;
  }

  public String getState() {
    Gson gson = new Gson();
    String testJSON = gson.toJson(testData);
    System.out.println("TEST");
    System.out.println(testJSON);
    return testJSON;
  }

}
