package dvr;

public class RoutingTable {
  private RoutingTableEntry[][] table;
  public int length;
  public int router;
  public int[] neighborCosts;

  public RoutingTable(int numRouters, int router) {
    table = new RoutingTableEntry[numRouters][numRouters];
    // add table entry for itself
    table[router][router] = new RoutingTableEntry(router, 0);
    length = numRouters;
    this.router = router;
    neighborCosts = new int[numRouters];
  }

  public void addEdge(int to, int cost) {
    neighborCosts[to] = cost;
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

  public void receiveDistanceVector(RoutingTableEntry[] distanceVector, int fromRouter) {
    // only receive if router that sent DV is a neighbor
    if (neighborCosts[fromRouter] != 0) {
      for (int i = 0; i < distanceVector.length; i++) {
        // copies value to table
        table[fromRouter][i] = distanceVector[i];
        // update own distance vector

        int dest = i;
        int min = Integer.MAX_VALUE;
        int newNextHop = -1;

        // ignore path to itself and if middleRouter has no path to dest
        if (dest != router) {
          for (int j = 0; j < neighborCosts.length; j++) {
            int startToMid = neighborCosts[j];
            RoutingTableEntry midToDest = table[j][dest];
            if (startToMid != 0 && midToDest != null) {
              int newCost = startToMid + midToDest.getCost();
              if (newCost < min) {
                min = newCost;
                newNextHop = j;
              }
            }
          }

          if (min != Integer.MAX_VALUE && (table[router][dest] != null || min != table[router][dest])) {
            //table[router][dest] = new RoutingTableEntry(middleRouter, newCost);
            // TODO: send new distance vector on next round
            // instead of updating now, queue update for next round to be sent
            RoutingTableEntry[] newDV = this.getDistanceVector();
            newDV[dest] = new RoutingTableEntry(newNextHop, min);
            UpdateQueue.push(newDV, router);
          }
        }
      }
    }
  }

  public RoutingTableEntry[] getDistanceVector() {
    return table[router];
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
