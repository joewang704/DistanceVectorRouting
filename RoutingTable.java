package dvr;

public class RoutingTable {
  private RoutingTableEntry[][] table;
  public int length;

  public RoutingTable(int numRouters, int router) {
    table = new RoutingTableEntry[numRouters][numRouters];
    // add table entry for itself
    table[router][router] = new RoutingTableEntry(router, 0);
    length = numRouters;
  }

  public void addEntry(int from, int to, int cost, int newHop) {
    RoutingTableEntry entry = table[from][to];
    if (entry != null) {
      entry.updateNextHop(newHop);
      entry.updateCost(cost);
    } else {
      table[from][to] = new RoutingTableEntry(newHop, cost);
    }
  }

  public void addEntry(int from, int to, RoutingTableEntry entry) {
    if (entry != null) {
      addEntry(from, to, entry.getCost(), entry.getNextHop());
    } else {
      table[from][to] = null;
    }
  }

  public RoutingTableEntry getEntry(int from, int to) {
    return table[from][to];
  }

  public void print() {
    System.out.printf("|%7s|", "From/To");
    for (int i = 0; i < table.length; i++) {
      System.out.printf("%2d |", i + 1);
    }
    System.out.println();
    for (int i = 0; i < table.length; i++) {
      System.out.printf("|%7d|", i + 1);
      for (int j = 0; j < table.length; j++) {
        RoutingTableEntry entry = table[i][j];
        System.out.printf("%2d |", entry == null ? -1 : entry.getCost());
      }
      System.out.println();
    }
  }

}
