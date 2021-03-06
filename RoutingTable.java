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

  public void receiveDistanceVector(RoutingTableEntry[] dvOrig, int fromRouter) {
    RoutingTableEntry[] distanceVector = Arrays.copyOf(dvOrig, dvOrig.length);
    if (DistanceVectorRouting.binaryFlag == 2) {
      System.out.print((fromRouter + 1) + " sends to " + (router + 1) + ": ");
    }

    // only receive if router that sent DV is a neighbor or itself from edge change
    if (neighborCosts[fromRouter] != 0) {
      // copy everything
      for (int i = 0; i < distanceVector.length; i++) {
        RoutingTableEntry cur = distanceVector[i];

        // poison control/split horizon
        if (cur != null && i != router) {
          int nextHop = cur.getNextHop();
          if (nextHop == router) {
            if (DistanceVectorRouting.variation == 1) {
              // does not advertise
              distanceVector[i] = null;
            } else if (DistanceVectorRouting.variation == 2) {
              // advertises infinity
              distanceVector[i] = null;
            }
          }
        }

        if (DistanceVectorRouting.binaryFlag == 2) {
          System.out.print(cur == null ? " inf" : " " + cur.getCost());
        }

        // copies value to table
        table[fromRouter][i] = distanceVector[i];
      }
      if (DistanceVectorRouting.binaryFlag == 2) {
        System.out.println();
      }
    }
  }

  public void update() throws Exception {

    RoutingTableEntry[] sendDV = this.getDistanceVector();

    boolean changed = false;

    // recalculate everything
    for (int i = 0; i < table[router].length; i++) {
      int dest = i;

      // ignore path to itself
      if (dest != router) {
        int min = Integer.MAX_VALUE;
        int newNextHop = -1;
        int newNumHops = -1;

        RoutingTableEntry oldMin = table[router][dest];

        if (oldMin != null && oldMin.getCost() == 0) {
          continue;
        }

        for (int j = 0; j < neighborCosts.length; j++) {
          int startToMid = neighborCosts[j];
          RoutingTableEntry midToDest = table[j][dest];

          if (startToMid > 0 && midToDest != null) {

            int newCost = startToMid + midToDest.getCost();
            if (newCost < min) {
              if (midToDest.getNumHops() + 1 > 100) {
                System.out.println("Count to infinity detected at round " + RunDVR.round);
                throw new InfinityException();
              } else {
                min = newCost;
                newNextHop = j;
                newNumHops = midToDest.getNumHops() + 1;
              }
            }
          }
        }

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
  }

  public RoutingTableEntry[] getDistanceVector() {
    return Arrays.copyOf(table[router], length);
  }

  public void print() {
    if (DistanceVectorRouting.binaryFlag == 2) {
      System.out.printf("|%8s|", "From/To");
      for (int i = 0; i < table.length; i++) {
        System.out.printf("%3d |", i + 1);
      }
      System.out.println();
      for (int i = 0; i < table.length; i++) {
        System.out.printf("|%8d|", i + 1);
        for (int j = 0; j < table.length; j++) {
          RoutingTableEntry entry = table[i][j];
          System.out.printf("%3d, ", entry == null ? -1 : entry.getCost());
          System.out.printf(" %2d |", entry == null ? -1 : entry.getNextHop()+1);
        }
        System.out.println();
      }
    }
  }

  public void print(int round) {
      FileIO.print.println();
      FileIO.print.printf("|%8d|", round + 1);
      for (int j = 0; j < table.length; j++) {
        RoutingTableEntry entry = table[router][j];
        FileIO.print.printf("%2d,", entry == null ? -1 : entry.getNextHop()+1);
        FileIO.print.printf("%3d |", entry == null ? -1 : entry.getNumHops());
      }

    // for (int i = 0; i < table.length; i++) {
    //   System.out.printf("|%7d|", i + 1);
    //   for (int j = 0; j < table.length; j++) {
    //     RoutingTableEntry entry = table[routerNum][j];
    //     System.out.printf("%2d,", entry == null ? -1 : entry.getNextHop()+1);
    //     System.out.printf("%2d |", entry == null ? -1 : entry.getNumHops());
    //   }
    //   System.out.println();
    // }
  }
}
