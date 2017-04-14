public class RoutingTableEntry {
  private int nextHop;
  private int cost;

  public RoutingTableEntry(int nextHop, int cost) {
    this.nextHop = nextHop;
    this.cost = cost;
  }

  public void updateNextHop(int newHop) {
    this.nextHop = newHop;
  }

  public void updateCost(int newCost) {
    this.cost = newCost;
  }
}
