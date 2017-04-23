package dvr;

import java.io.*;

public class FileIO {
  public static FileOutputStream out = null;
  public static PrintStream print;

  public static void start() {
    try {
      if (DistanceVectorRouting.binaryFlag == 0) {
        if (DistanceVectorRouting.variation == 0) {
          out = new FileOutputStream("output-basic.txt");
          print = new PrintStream(out);
        } else if (DistanceVectorRouting.variation == 1) {
          out = new FileOutputStream("output-split-horizon.txt");
          print = new PrintStream(out);
        } else if (DistanceVectorRouting.variation == 2) {
          out = new FileOutputStream("output-poison-reverse.txt");
          print = new PrintStream(out);
        }
      } else if (DistanceVectorRouting.binaryFlag == 1) {
        if (DistanceVectorRouting.variation == 0) {
          out = new FileOutputStream("output-detailed-basic.txt");
          print = new PrintStream(out);
        } else if (DistanceVectorRouting.variation == 1) {
          out = new FileOutputStream("output-detailed-split-horizon.txt");
          print = new PrintStream(out);
        } else if (DistanceVectorRouting.variation == 2) {
          out = new FileOutputStream("output-detailed-poison-reverse.txt");
          print = new PrintStream(out);
        }
      }
    } catch (Exception e) {
    }
  }

  public static void close() {
    try {
      print.flush();
      out.close();
    } catch (Exception e) {
    }
  }
}
