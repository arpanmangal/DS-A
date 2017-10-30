import java.util.*;
import java.io.*;
import java.util.ArrayList;

public class timeTest {
    public static void main(String args[]) {
                long startTime=System.currentTimeMillis();
        for (int i = 0; i < 25000; i++) {
            int u = 3;
            int v = u - 3;
        }
                long loadTime=System.currentTimeMillis()-startTime;
        System.out.println("loadTime: "+loadTime+" millis");// and findTime: "+findTime+" millis and totalTime: "+(loadTime+findTime)+" milis");
    }
}