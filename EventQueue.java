package dvr;

import java.util.*;

public class EventQueue {
  public static ArrayList<EventUpdateItem> queue = new ArrayList<>();

  public static void push(int round, int fromRouter, int toRouter, int cost) {
    queue.add(new EventUpdateItem(round, fromRouter, toRouter, cost));
  }

  public static EventUpdateItem peek() {
    return queue.get(0);      
  }

  public static void print() {
    for (EventUpdateItem event : queue) {
      System.out.println("round: " + event.round);
      System.out.println("fromRouter: " + event.fromRouter);
      System.out.println("toRouter: " + event.toRouter);
      System.out.println("cost: " + event.cost);
    }
  }
}
