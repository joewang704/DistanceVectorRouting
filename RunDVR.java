package dvr;

public class RunDVR {
  public static int lastEventRound = -1;
  public static int round = 1;

  public static void run(RoutingTableArray tables) {
    lastEventRound = -1;
    round = 1;
    // repeat iterations of DVR until convergence
    try {
      if (DistanceVectorRouting.binaryFlag == 1 ||
          DistanceVectorRouting.binaryFlag == 2) {
        //System.out.println("Round 1");
        FileIO.print.println("Round 1");
        RoutingTableArray.print();
      }
      while (!UpdateQueue.queue.isEmpty()  || !EventQueue.queue.isEmpty()) {
        int size = UpdateQueue.queue.size();

        while (!EventQueue.queue.isEmpty() && EventQueue.peek().round == round) {
          EventUpdateItem event = EventQueue.queue.remove(0);
          int fromRouter = event.fromRouter;
          int toRouter = event.toRouter;
          int cost = event.cost;
          lastEventRound = event.round;

          // update edge value with value from event
          RoutingTable fromTable = tables.getRoutingTable(fromRouter);
          fromTable.addEdge(toRouter, cost);
          RoutingTable toTable = tables.getRoutingTable(toRouter);
          toTable.addEdge(fromRouter, cost);

          // make event to update from routing table
          fromTable.update();

          // make event to update to routing table
          toTable.update();
        }

        for (int i = 0; i < size; i++) {
          UpdateItem item = UpdateQueue.queue.remove(0);
          sendDistanceVector(item, tables);
        }

        for (int i = 0; i < tables.length; i++) {
          RoutingTable table = tables.getRoutingTable(i);
          table.update();
        }

        round++;

        if (DistanceVectorRouting.binaryFlag == 1 ||
            DistanceVectorRouting.binaryFlag == 2 ||
           (EventQueue.queue.isEmpty() && UpdateQueue.queue.isEmpty())) {
          if (DistanceVectorRouting.binaryFlag == 2) {
            System.out.println("Round " + round);
          } else {
            FileIO.print.println("Round " + round);
          }
          RoutingTableArray.print();
        }
      }
      if (DistanceVectorRouting.binaryFlag == 0 || DistanceVectorRouting.binaryFlag == 1) {
        FileIO.print.println("Convergence delay: " + (round - lastEventRound));
      } else if (DistanceVectorRouting.binaryFlag == 2) {
        System.out.println("Convergence delay: " + (round - lastEventRound));
      }
    } catch (Exception e) {
      RoutingTableArray.print();
    }
  }

  public static void sendDistanceVector(UpdateItem item, RoutingTableArray routingTables) {
    int fromRouter = item.fromRouter;
    RoutingTableEntry[] distanceVector = item.distanceVector;
    for (int i = 0; i < routingTables.length; i++) {
      // avoid sending to itself
      if (i != fromRouter) {
        RoutingTable rcvTable = routingTables.getRoutingTable(i);
        rcvTable.receiveDistanceVector(distanceVector, fromRouter);
      }
    }
  }

  public static void updateTables(RoutingTableArray tables) {
    /*for (int i = 0; i < tables.length; i++) {
      RoutingTable table = tables.getRoutingTable(i);
      for (int j = 0; j < table.length; j++) {
        // do not need to update path to itself
        if (j != i) {
          // run bellman ford
        }
      }
    }*/
  }
}
