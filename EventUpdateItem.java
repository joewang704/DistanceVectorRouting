package dvr;

public class EventUpdateItem {
  public int round;
  public int fromRouter;
  public int toRouter;
  public int cost;
  public EventUpdateItem(int round, int fromRouter, int toRouter, int cost) {
    this.round = round;
    this.fromRouter = fromRouter;
    this.toRouter = toRouter;
    this.cost = cost;
  }
}
