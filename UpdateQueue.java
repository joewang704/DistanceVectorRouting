package dvr;

import java.util.*;

public class UpdateQueue {
  public static ArrayList<UpdateItem> queue = new ArrayList<>();

  public static void push(RoutingTableEntry[] entry, int fromRouter) {
    queue.add(new UpdateItem(Arrays.copyOf(entry, entry.length), fromRouter));
  }

}
