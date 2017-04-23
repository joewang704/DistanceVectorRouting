package dvr;

public class RoutingTableArray {
  private static RoutingTable[] routingTableArray;
  public int length;

  public RoutingTableArray(int numRouters) {
    routingTableArray = new RoutingTable[numRouters];

    length = numRouters;

    for (int i = 0; i < numRouters; i++) {
      routingTableArray[i] = new RoutingTable(numRouters, i);
    }
  }

  public RoutingTable getRoutingTable(int router) {
    return routingTableArray[router];
  }

  public static void print() {
    if (DistanceVectorRouting.binaryFlag == 0 || DistanceVectorRouting.binaryFlag == 1) {
      System.out.printf("|%8s|", "From/To");
      for (int i = 0; i < routingTableArray.length; i++) {
        System.out.printf("%6d |", i + 1);
      }
      for (int i = 0; i < routingTableArray.length; i++) {
        routingTableArray[i].print(i);
      }
      System.out.println("\n");
    } else {
      for (int i = 0; i < routingTableArray.length; i++) {
        System.out.println((i + 1) + ":");
        routingTableArray[i].print();
        System.out.println();
      }
    }
  }
}
