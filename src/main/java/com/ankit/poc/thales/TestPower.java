package com.ankit.poc.thales;

public class TestPower {
  // input is integer
  //
  // return true, if the input is 2^x
  //
  // if input is 1, which is 2^0, then return true
  //
  // if input is 8, which is 2^3, then return true
  //
  // if input 1024, return true
  //
  // if input 100 return false
  public static void main(String[] args) {
    System.out.println(isCheckPower(999399));
  }

  public static Boolean isCheckPower(Integer input) {
    if (input != null && input.equals(0)) {
      return false;
    }
    for (int i = 0; i < Integer.MAX_VALUE; i++) {
      if (Math.pow(2, i) == input) {
        return true;
      }
    }
    return false;
  }
}
