package dvr;

public class UpdateItem {
  public RoutingTableEntry[] distanceVector;
  public int fromRouter;
  public UpdateItem(RoutingTableEntry[] distanceVector, int fromRouter) {
    this.distanceVector = distanceVector;
    this.fromRouter = fromRouter;
  }
}
