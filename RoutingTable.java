package dvr;

import java.util.*;

public class RoutingTable {
  private RoutingTableEntry[][] table;
  public int length;
  public int router;
  public int[] neighborCosts;

  public RoutingTable(int numRouters, int router) {
    table = new RoutingTableEntry[numRouters][numRouters];

    // add table entry for itself
    table[router][router] = new RoutingTableEntry(router, 0, 0);
    length = numRouters;
    this.router = router;
    neighborCosts = new int[numRouters];
    for (int i = 0; i < neighborCosts.length; i++) {
      if (i == router) {
        neighborCosts[i] = 0;
      } else {
        neighborCosts[i] = -1;
      }
    }
  }

  public void addEdge(int to, int cost) {
    neighborCosts[to] = cost;
  }

  public void addEntry(int from, int to, int cost, int newHop, int numHops) {
    RoutingTableEntry entry = table[from][to];
    if (entry != null) {
      entry.updateNextHop(newHop);
      entry.updateCost(cost);
      entry.updateNumHops(numHops);
    } else {
      table[from][to] = new RoutingTableEntry(newHop, cost, numHops);
    }
  }

  public void addEntry(int from, int to, RoutingTableEntry entry) {
    if (entry != null) {
      addEntry(from, to, entry.getCost(), entry.getNextHop(), entry.getNumHops());
    } else {
      table[from][to] = null;
    }
  }

  public RoutingTableEntry getEntry(int from, int to) {
    return table[from][to];
  }

  public void receiveDistanceVector(RoutingTableEntry[] distanceVector, int fromRouter) {
    System.out.print((fromRouter + 1) + " sends to " + (router + 1) + ": ");

    RoutingTableEntry[] sendDV = this.getDistanceVector();

    // only receive if router that sent DV is a neighbor or itself from edge change
    if (neighborCosts[fromRouter] != 0 || fromRouter == router) {
      boolean changed = false;
      for (int i = 0; i < distanceVector.length; i++) {
        RoutingTableEntry cur = distanceVector[i];
        RoutingTableEntry orig = table[router][i];

        /*
        // poison control/split horizon
        if (orig != null && i != fromRouter) {
          int nextHop = orig.getNextHop();
          int cost = orig.getCost();
          if (nextHop == fromRouter) {
            if (DistanceVectorRouting.variation == 1) {
              // does not advertise
              System.out.print(" nothing");
              continue;
            } else if (DistanceVectorRouting.variation == 2) {
              // advertises infinity
              distanceVector[i] = null;
              System.out.print(" inf");
            } else {
              System.out.print(cur == null ? " inf" : " a" + cur.getCost());
            }
          } else {
            System.out.print(cur == null ? " inf" : " b" + cur.getCost());
          }
        } else {
          System.out.print(cur == null ? " inf" : " c" + cur.getCost());
        }*/
        System.out.print(cur == null ? " inf" : " " + cur.getCost());

        // copies value to table
        table[fromRouter][i] = distanceVector[i];

        int dest = i;

        // ignore path to itself
        if (dest != router) {
          int min = Integer.MAX_VALUE;
          int newNextHop = -1;
          int newNumHops = -1;

          for (int j = 0; j < neighborCosts.length; j++) {
            int startToMid = neighborCosts[j];
            RoutingTableEntry midToDest = table[j][dest];
            if (startToMid > 0 && midToDest != null) {
              int newCost = startToMid + midToDest.getCost();
              if (newCost < min) {
                min = newCost;
                newNextHop = j;
                newNumHops = midToDest.getNumHops() + 1;
              }
            }
          }

          RoutingTableEntry oldMin = table[router][dest];

          if (min == Integer.MAX_VALUE) {
            // vertex is isolated, send infinity
            table[router][dest] = null;
            sendDV[dest] = null;
            if (table[router][dest] != null) {
              // if not already null, that means value has been changed
              changed = true;
            }
          } else if (oldMin == null || min != oldMin.getCost()) {
            table[router][dest] = new RoutingTableEntry(newNextHop, min, newNumHops);
            sendDV[dest] = new RoutingTableEntry(newNextHop, min, newNumHops);
            changed = true;
          }
        }
      }
      if (changed) {
        // if any value has been changed, send new distance vector
        UpdateQueue.push(sendDV, router);
      }
      System.out.println();
    }
  }

  public RoutingTableEntry[] getDistanceVector() {
    return Arrays.copyOf(table[router], length);
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
