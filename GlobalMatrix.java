package dvr;

public class GlobalMatrix {
  int[][] globalMatrix;

  public GlobalMatrix(int numRouters) {
    globalMatrix = new int[numRouters][numRouters];
  }

  public void setEdge(int from, int to, int distance) {
    int f = from;
    int t = to;

    globalMatrix[f][t] = distance;
    globalMatrix[t][f] = distance;
  }

  public void printMatrix() {
    for (int i = 0; i < globalMatrix.length; i++) {
      for (int j = 0; j < globalMatrix.length; j++) {
        System.out.println((i+1) + " " + (j+1) + " " + globalMatrix[i][j]);
      }
    }
  }
}
