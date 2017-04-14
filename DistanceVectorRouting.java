import java.io.BufferedReader;
import java.io.FileReader;

public class DistanceVectorRouting {

  public static void main(String[] args) {
    if (args.length != 3 || !args[0].contains(".txt") ||
      !args[1].contains(".txt") || (!args[2].equals("0") && !args[2].equals("1"))) {
      System.out.println("Usage: java textfile.txt eventFile.txt binaryFlag");
      System.exit(1);
    }

    String initFile = args[0];
    String eventFile = args[1];
    int binaryFlag = Integer.valueOf(args[2]);

    try (BufferedReader br = new BufferedReader(new FileReader(initFile))) {
      String line;
      line = br.readLine();
      int numRouters = Integer.valueOf(line);
      GlobalMatrix globalMatrix = new GlobalMatrix(numRouters);
      while ((line = br.readLine()) != null) {
        int from = Integer.valueOf(line.split(" ")[0]);
        int to = Integer.valueOf(line.split(" ")[1]);
        int distance = Integer.valueOf(line.split(" ")[2]);
        globalMatrix.setEdge(from, to, distance);
      }
      globalMatrix.printMatrix();
    } catch (Exception e) {
      System.out.println(e.getMessage());
      System.out.println(e.getClass().getCanonicalName());
    }
  }
}
