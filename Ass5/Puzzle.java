import java.util.*;

public class Puzzle {

    private class Node {
        // a container which stores the adjacent edges of a node and their corresponding costs
        // cost[i] corresponding to Adj[i], and in terms of 1-8 which shifts
        ArrayList<String> Adj;
        ArrayList<Integer> Cost;
        int distance; // distance from the source vertex, to be used in Dijkstra

        public Node() {
            // constructor
            Adj = new ArrayList<>();
            Cost = new ArrayList<>();
        }
    }

    private class heap {
        // maintains a min heap of nodes in order of their distances

    }

    // Hashmap storing all nodes and corresponding edges
    private HashMap<String, Node> graph;

    public Puzzle() {
        // constructor
        graph = new HashMap<>();

        // generate the graph
        generateComponent("123456780");
        generateComponent("123456708");
    }

    void generateComponent(String key) {
        // generate the neighbours of the key node
        int index = key.indexOf('0'); // index where '0' is present
    }

    public static void main(String args[]) {
        if (args.length != 2) {
            // check input
            System.out.println("java Puzzle someinputfilename.txt someoutputfilename.txt");
            System.exit(1);
        }

        Scanner input = new Scanner(System.in); // change it later
		// try
	    // {
        //     input = new Scanner(new File(args[0])); // input.txt
	    // }
	    // catch (IOException e)  
	    // {
	    //     System.out.println(e);
	    //     System.exit(1); // exit
        // }

        // make the class
        Puzzle puzzle = new Puzzle();

    }
}