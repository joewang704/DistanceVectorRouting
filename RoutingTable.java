package dvr;

public class RoutingTable {
  private RoutingTableEntry[][] table;

  public RoutingTable(int numRouters) {
      table = new RoutingTableEntry[numRouters][numRouters];
  }

}
