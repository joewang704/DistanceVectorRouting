package dvr;

public class RoutingTableEntry {
  private int nextHop;
  private int cost;
  private int numHops;

  public RoutingTableEntry(int nextHop, int cost, int numHops) {
    this.nextHop = nextHop;
    this.cost = cost;
    this.numHops = numHops;
  }

  public void updateNextHop(int newHop) {
    this.nextHop = newHop;
  }

  public void updateCost(int newCost) {
    this.cost = newCost;
  }

  public void updateNumHops(int newHop) {
    this.numHops = numHops;
  }

  public int getCost() { return cost; }

  public int getNextHop() { return nextHop; }

  public int getNumHops() { return numHops; }
}
