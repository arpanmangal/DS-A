package col106.a3;

// import col106.a3.BTree;
// import col106.a3.DuplicateBTree;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StressTest {
    public static void main(String argv[]) throws Exception {
        long startTime=System.currentTimeMillis();
        DuplicateBTree<Integer, Integer> graph = new BTree<>(6);
        int V = 1000;
        int E = 20000;
        ArrayList<ArrayList<Integer>> g = new ArrayList<>(V);
        Random r = new Random();
        for (int i = 0; i < V; i++)
            g.add(new ArrayList<>());
        for (int i = 0; i < E; i++) {
            int v1 = r.nextInt(V);
            int v2 = r.nextInt(V);
            if (v1 != v2) {
                g.get(v1).add(v2);
                graph.insert(v1, v2);
                // System.out.println("insert " + v1 + " " + v2);
            }
        }
        System.out.println("height: "+graph.height()+" size: "+graph.size()+" tree is ");
        for (int j = V - 1; j >= 3 * V / 4; j--) {
            // delete one by one and check all
            // System.out.println("deleting " + j + " from " + graph);
            g.get(j).clear();
            graph.delete(j);
            for (int i = 0; i < V; i++) {
                List<Integer> neighbourhood = graph.search(i);
                neighbourhood.sort(Integer::compareTo);
                ArrayList<Integer> correctAnswer = g.get(i);
                correctAnswer.sort(Integer::compareTo);
                if (!neighbourhood.equals(correctAnswer)) {
                    System.out.println("Incorrect search result for " + i);
                    System.out.println(correctAnswer);
                    System.out.println(neighbourhood);
                }
            }
        }
        for (int j = V / 4; j < 3 * V / 4; j++) {
            // delete one by one and check all
            // System.out.println("deleting " + j + " from " + graph);
            g.get(j).clear();
            graph.delete(j);
            for (int i = 0; i < V; i++) {
                List<Integer> neighbourhood = graph.search(i);
                neighbourhood.sort(Integer::compareTo);
                ArrayList<Integer> correctAnswer = g.get(i);
                correctAnswer.sort(Integer::compareTo);
                if (!neighbourhood.equals(correctAnswer)) {
                    System.out.println("Incorrect search result for " + i);
                    System.out.println(correctAnswer);
                    System.out.println(neighbourhood);
                }
            }
        }
        for (int j = V / 4 - 1; j >= 0; j--) {
            // delete one by one and check all
            // System.out.println("deleting " + j + " from " + graph);
            g.get(j).clear();
            graph.delete(j);
            for (int i = 0; i < V; i++) {
                List<Integer> neighbourhood = graph.search(i);
                neighbourhood.sort(Integer::compareTo);
                ArrayList<Integer> correctAnswer = g.get(i);
                correctAnswer.sort(Integer::compareTo);
                if (!neighbourhood.equals(correctAnswer)) {
                    System.out.println("Incorrect search result for " + i);
                    System.out.println(correctAnswer);
                    System.out.println(neighbourhood);
                }
            }
        }
        long time=System.currentTimeMillis()-startTime;
        System.out.println(graph.size() +" height: "+graph.height()+" graph "+graph);
        System.out.println("time: "+time+" millis");
    }
}
