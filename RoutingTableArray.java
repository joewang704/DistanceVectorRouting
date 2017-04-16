package dvr;

public class RoutingTableArray {
  private RoutingTable[] routingTableArray;
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

  public void print() {
    for (int i = 0; i < routingTableArray.length; i++) {
      System.out.println((i + 1) + ":");
      routingTableArray[i].print();
      System.out.println();
    }
  }
}
