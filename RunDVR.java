package dvr;

public class RunDVR {
  public static void run(RoutingTableArray tables) {
    // repeat iterations of DVR until convergence
    //while (true) {
      // copy everything
      copyTables(tables);
      tables.print();
      updateTables(tables);
      // tables update themselves
    //}
  }

  public static void copyTables(RoutingTableArray routingTables) {
    for (int i = 0; i < routingTables.length; i++) {
      for (int j = 0; j < routingTables.length; j++) {
        // avoid copying to itself
        if (i != j) {
          RoutingTable iTable = routingTables.getRoutingTable(i);
          RoutingTable jTable = routingTables.getRoutingTable(j);
          for (int col = 0; col < iTable.length; col++) {
            iTable.addEntry(j, col, jTable.getEntry(j, col));
          }
        }
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
