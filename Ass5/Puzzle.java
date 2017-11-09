import java.util.*;

public class Puzzle {

    // most important function => but remove it
    private void print(String s) {
        System.out.println(s);
    }
    private class Node {
        // a container which stores the adjacent edges of a node and their corresponding costs
        // cost[i] corresponding to Adj[i], and in terms of 1-8 which shifts
        ArrayList<String> Adj;
        ArrayList<Integer> Cost;
        int distance; // distance from the source vertex, to be used in Dijkstra

        int heapIndex; // index at which it is stored in the heap
        Node previous; // pointer to previous node
        public Node() {
            // constructor
            Adj = new ArrayList<>(4);
            Cost = new ArrayList<>(4);
        }
    }

    /************ HEAP ****************/

    private class heap {
        // maintains a min heap of nodes in order of their distances

    }
    // heap for maintaing nodes & distances from source node
    private class stringContainer {
        // for passing strings by reference
        String str;
    }
    private Node[] heap;
    private int heapSize;

    private void buildHeap(String start) {
        heapSize = 362880; // 362880 nodes
        Node tmp;
        int source = -1; // initialisation to satisfy compiler
        for (int i = 0, j = 1; i < heapSize; i++, j++) { // j = i + 1
            tmp = graph.get(Permutations.get(i));
            tmp.distance = Integer.MAX_VALUE;
            tmp.previous = null;
            tmp.heapIndex = j;
            if (Permutations.get(i).equals(start)) {
                // it's the source
                tmp.distance = 0;
                source = j;
            }
            heap[j] = tmp;
        }
        // System.out.println(source+" "+Permutations.get(source - 1));
        percolateUp(source, heap[source]);
        // percolateDown(1, heap[1]);
        // for (int i = 1; i <= heapSize; i++) {
        //     if (heap[i].distance == 0) {
        //         print(""+i);
        //     }
        // }
    }

    private void percolateDown(int i, Node node) {
        // heapSize is # elements, i is the hole,
        // node.distance is the value to insert
        
        int childInx = 2 * i;
        int j;
        if (childInx > heapSize) {
            heap[i] = node;
            heap[i].heapIndex = i;
        } else if (childInx == heapSize) {
            if (heap[childInx].distance < node.distance) {
                heap[i] = heap[childInx];
                heap[i].heapIndex = i;
                heap[childInx] = node;
                heap[childInx].heapIndex = childInx;
            } else {
                heap[i] = node;
                heap[i].heapIndex = i;
            }
        } else {
            if (heap[childInx].distance < heap[childInx + 1].distance) {
                j = childInx;
            } else {
                j = childInx + 1;
            }
            if (heap[j].distance < node.distance) {
                heap[i] = heap[j];
                heap[i].heapIndex = i;
                percolateDown(j, node);
            } else {
                heap[i] = node;
                heap[i].heapIndex = i;
            }
        }
    }
    private void percolateUp(int i, Node node) {
        // print("called with "+i);
        if (i < 1) {
            System.out.println("error!!!");
        }
        int prtIdx = i / 2;
        if (i > 1 && heap[prtIdx].distance > node.distance) {
            // percolate up
            heap[i] = heap[prtIdx];
            heap[i].heapIndex = i;
            percolateUp(prtIdx, node);
        } else {
            // done percolation
            heap[i] = node;
            heap[i].heapIndex = i;
        }
    }

    // Edge table storing edge weights
    int Edges[];

    // Hashmap storing all nodes and corresponding edges
    private HashMap<String, Node> graph;
    private HashMap<String, Integer> g;
    public Puzzle() {
        // constructor
        graph = new HashMap<>();
        heap = new Node[362881]; // 362881 + 1

        // generatePermutations();
        Permutations = new ArrayList<>();
        permutation("123456780");
        // generate the graph
        for (int ix = 0; ix < Permutations.size(); ix++) {
            // System.out.println(Permutations.get(ix));
            // if (!g.containsKey(Permutations.get(ix))) {
                // g.add(Permutations.get(ix));
            // }
            generateComponent(Permutations.get(ix));
        }
        System.out.println(graph.size());
        // if (!graph.containsKey("123456780")) generateComponent("123456780");
        // if (!graph.containsKey("123456708")) generateComponent("123456708");
    }

    // temporary variables which can be used in the below function
    // private int tmp1;
    // private int tmp2;
    // private int tmp3;

    ArrayList<String> Permutations;
        public  void permutation(String str) { 
        permutation("", str); 
    }

    private  void permutation(String prefix, String str) {
        int n = str.length();
        if (n == 0) Permutations.add(prefix);
        else {
            for (int i = 0; i < n; i++)
                permutation(prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
        }
    }
    // An iterative function to print all permutations of s.
    void generatePermutations()
    {
        // from http://www.geeksforgeeks.org/permutations-string-using-iteration/
        // generates all permutations of "123456780";
        
        Permutations = new ArrayList<>(362881);
        // String s = "123456780";
        char[] s = "123456780".toCharArray();
        int n = 9;
        int fc = 362880; // = 9!
    
        // Point j to the 2nd position
        int j = 1;
    
        // To store position of character to be fixed next.
        // m is used as in index in s[].
        int m = 0;
        
        char tmp; // used for swapping

        // Iterate while permutation count is
        // smaller than n! which fc
        for (int perm_c = 0; perm_c < fc; )
        {
            // Store perm as current permutation
            char[] perm = s;
    
            // Fix the first position and iterate (n-1)
            // characters upto (n-1)!
            // k is number of iterations for current first
            // character.
            int k = 0;
            while (k != fc/n)
            {
                // Swap jth value till it reaches the end position
                while (j != n-1)
                {
                    // Print current permutation
                    Permutations.add(new String(perm));
    
                    // Swap perm[j] with next character
                    tmp = perm[j];
                    perm[j] = perm[j + 1];
                    perm[j + 1] = tmp;
                    // swap(perm[j], perm[j+1]);
    
                    // Increment count of permutations for this
                    // cycle.
                    k++;
    
                    // Increment permutation count
                    perm_c++;
    
                    // Increment 'j' to swap with next character
                    j++;
                }
    
                // Again point j to the 2nd position
                j = 1;
            }
    
            // Move to next character to be fixed in s[]
            m++;
    
            // If all characters have been placed at
            if (m == n)
               break;
    
            // Move next character to first position
            tmp = s[0];
            s[0] = s[m];
            s[m] = tmp;
            // swap(s[0], s[m]);
        }
    }
    private int i;
    void generateComponent(String key) {
        // generate the neighbours of the key node
        // if (graph.containsKey(key)) {
        //     System.out.println("it already contains");
        //     return;
        // }
        // System.out.println("called with key: "+key);
        // if (key.length() != 9) {
        //     System.out.println("error!!!");
        //     return;
        // }
        i = key.indexOf('0'); // index where '0' is present
        Node node = new Node();
        if (i < 6) {
            // swap with index + 3, and add to node
            node.Adj.add(swapChar(key, i, i + 3));
            node.Cost.add(Character.getNumericValue(key.charAt(i)));
        }
        if (i >= 3) {
            // swap with index + 3, and add to node
            node.Adj.add(swapChar(key, i, i - 3));
            node.Cost.add(Character.getNumericValue(key.charAt(i)));
        }
        if (i % 3 < 2) {
            // swap with index + 3, and add to node
            node.Adj.add(swapChar(key, i, i + 1));
            node.Cost.add(Character.getNumericValue(key.charAt(i)));
        }
        if (i % 3 > 0) {
            // swap with index + 3, and add to node
            node.Adj.add(swapChar(key, i, i - 1));
            node.Cost.add(Character.getNumericValue(key.charAt(i)));
        }

        // add our node to the graph
        graph.put(key, node);

        // System.out.println(graph.size());
        // recurse on all edges
        // for (int ix = 0; ix < node.Adj.size(); ix++) {
        //     if (!graph.containsKey(node.Adj.get(ix))) {
        //         // add its edges
        //         // generateComponent(node.Adj.get(ix));
        //     }
        // }

        return;
    }

    String swapChar(String s, int s1, int s2) {
        char[] str = s.toCharArray();
        char tmp = str[s1];
        str[s1] = str[s2];
        str[s2] = tmp;
        return new String(str);
    }

    public static void main(String args[]) {
        long startTime=System.currentTimeMillis();

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

        puzzle.Edges = new int[8];
        int t = input.nextInt();
        String start, end;
        for (int test = 0; test < t; test++) {
            start = input.next();
            end = input.next();
            for (int e = 0; e < 8; e++) {
                puzzle.Edges[e] = input.nextInt();
            }
            
            // buildHeap
            puzzle.buildHeap(start.replace('G', '0'));
        }

        long makeTime=System.currentTimeMillis()-startTime;
        System.out.println(makeTime + " millis");
    }
}