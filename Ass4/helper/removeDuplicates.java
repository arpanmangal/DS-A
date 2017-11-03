import java.util.*;
import java.io.*;
import java.util.ArrayList;

public class removeDuplicates {
    public static void main(String argsp[]) {
    Scanner s = new Scanner(System.in);
    int size = s.nextInt();
    int newSize = 0;
    String in1 = s.next();
    System.out.println(in1);
    newSize++;
    String in2;
    for (int i = 1; i < size; i++) {
        in2 = s.next();
        if (in1.equals(in2) || in1.length() > 12){
            // ignore
        } else {
            System.out.println(in1);
        }
        newSize++;
        in1 = in2;
        
    }
    System.out.println(in1);
    System.out.println(newSize);
    }
}