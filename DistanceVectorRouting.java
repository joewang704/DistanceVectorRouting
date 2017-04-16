package dvr;

import java.io.BufferedReader;
import java.io.FileReader;

public class DistanceVectorRouting {

  public static void main(String[] args) {
    if (args.length != 3 || !args[0].contains(".txt") ||
      !args[1].contains(".txt") || (!args[2].equals("0") && !args[2].equals("1"))) {
      System.out.println("Usage: java textfile.txt eventFile.txt binaryFlag");
      System.exit(1);
    }

    String initFile = args[0];
    String eventFile = args[1];
    int binaryFlag = Integer.valueOf(args[2]);

    try (BufferedReader br = new BufferedReader(new FileReader(initFile))) {
      String line;
      line = br.readLine();
      int numRouters = Integer.valueOf(line);
      GlobalMatrix globalMatrix = new GlobalMatrix(numRouters);
      RoutingTableArray routingTables = new RoutingTableArray(numRouters);

      // initialize adjacency list and routing tables
      while ((line = br.readLine()) != null) {
        int from = Integer.valueOf(line.split(" ")[0]) - 1;
        int to = Integer.valueOf(line.split(" ")[1]) - 1;
        int cost = Integer.valueOf(line.split(" ")[2]);

        // add edges to matrix graph
        globalMatrix.setEdge(from, to, cost);

        // add edges to routing table
        RoutingTable fromTable = routingTables.getRoutingTable(from);
        fromTable.addEntry(from, to, cost, to);
        RoutingTable toTable = routingTables.getRoutingTable(to);
        toTable.addEntry(to, from, cost, from);
      }
      //globalMatrix.printMatrix();

      routingTables.print();

      // run DVR algorithm until convergence
      RunDVR.run(routingTables);

    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getClass().getCanonicalName());
      e.printStackTrace();
    }
  }
}
