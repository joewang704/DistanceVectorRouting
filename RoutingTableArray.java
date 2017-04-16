package dvr;

public class RoutingTableArray {
  private RoutingTable[] routingTableArray;

  public RoutingTableArray(int numRouters) {
    routingTableArray = new RoutingTable[numRouters];

    for (int i = 0; i < numRouters; i++) {
      routingTableArray[i] = new RoutingTable(numRouters);
    }
  }
}
