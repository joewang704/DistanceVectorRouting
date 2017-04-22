package dvr;

import java.io.BufferedReader;
import java.io.FileReader;

public class DistanceVectorRouting {

  public static int variation = 0;

  public static void main(String[] args) {
    if (args.length != 3 || !args[0].contains(".txt") ||
      !args[1].contains(".txt") || (!args[2].equals("0") && !args[2].equals("1"))) {
      System.out.println("Usage: java textfile.txt eventFile.txt binaryFlag");
      System.exit(1);
    }

    String initFile = args[0];
    String eventFile = args[1];
    int binaryFlag = Integer.valueOf(args[2]);

    runVariation(initFile, eventFile, binaryFlag, 0);
    //runVariation(initFile, eventFile, binaryFlag, 1);
    //runVariation(initFile, eventFile, binaryFlag, 2);
  }

  public static void runVariation(String initFile, String eventFile, int binaryFlag, int variation) {
    System.out.println("=======================");
    System.out.println("Variation " + variation);
    System.out.println("=======================");
    DistanceVectorRouting.variation = variation;

    try (BufferedReader eventBr = new BufferedReader(new FileReader(eventFile))) {
      String line;

      // add events to event queuq
      while ((line = eventBr.readLine()) != null) {
        int round = Integer.valueOf(line.split(" ")[0]);
        int fromRouter = Integer.valueOf(line.split(" ")[1]) - 1;
        int toRouter = Integer.valueOf(line.split(" ")[2]) - 1;
        int cost = Integer.valueOf(line.split(" ")[3]);
        EventQueue.push(round, fromRouter, toRouter, cost);
      }
      //EventQueue.print();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getClass().getCanonicalName());
      e.printStackTrace();
    }

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

        RoutingTable fromTable = routingTables.getRoutingTable(from);
        RoutingTable toTable = routingTables.getRoutingTable(to);

        // add edges to node neighbors
        fromTable.addEdge(to, cost);
        toTable.addEdge(from, cost);

        // add values to routing table
        fromTable.addEntry(from, to, cost, to, 1);
        toTable.addEntry(to, from, cost, from, 1);
      }

      // add initial update queue values
      for (int i = 0; i < numRouters; i++) {
        RoutingTable t = routingTables.getRoutingTable(i);
        UpdateQueue.push(t.getDistanceVector(), i);
      }

      // run DVR algorithm until convergence
      RunDVR.run(routingTables);

    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getClass().getCanonicalName());
      e.printStackTrace();
    }
  }
}
