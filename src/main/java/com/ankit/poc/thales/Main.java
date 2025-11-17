package com.ankit.poc.thales;

import java.util.Date;

public class Main {
  public static void main(String[] args) {

    String currentUserLoginName = "Ankit";
    Point point1 = new Point();
    Point point2 = new Point();

    point1.setDimension(2);
    point1.setCount(4);
    point1.setCreatedBy(currentUserLoginName);
    point1.setCreatedOn(new Date());


    point2.setDimension(2);
    point2.setCount(4);
    System.out.println(Main.checkEquals(point1, point2));



  }

  public static Boolean checkEquals(Point point1, Point point2) {
    if (point1.getCount().equals(point2.getCount())
        && point1.getDimension().equals(point2.getDimension())) {
      return true;
    }
    return false;
  }
}
