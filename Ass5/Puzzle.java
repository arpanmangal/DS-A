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
        ArrayList<Character> Direction; // Direction of neighbours w.r.t to me L U R D, how will I reach my neighbours
        ArrayList<Integer> Cost; // stores cost in form of edge label
        int distance; // distance from the source vertex, to be used in Dijkstra

        int heapIndex; // index at which it is stored in the heap
        Node previous; // pointer to previous node
        char direcPrev; // my direction w.r.t. previous, how would I reach from previous
        int edgePrev;
        public Node() {
            // constructor
            Adj = new ArrayList<>(4);
            Direction = new ArrayList<>(4);
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

    private void buildHeap() {
        // could be optimised by building just once and making all INF
        // heapSize = 362880; // 362880 nodes
        // Node tmp;
        // int source = -1; // initialisation to satisfy compiler
        // for (int i = 0, j = 1; i < heapSize; i++, j++) { // j = i + 1
        //     // tmp = graph.get(Permutations.get(i));
        //     // tmp.distance = Integer.MAX_VALUE;
        //     // tmp.previous = null;
        //     // tmp.heapIndex = j;
        //     // if (Permutations.get(i).equals(start)) {
        //     //     // it's the source
        //     //     tmp.distance = 0;
        //     //     source = j;
        //     // }
        //     heap[j].heapIndex = j;
        // }
        int i = 1;
        for (Node value : graph.values()) {
            // iterate over all nodes in the graph
            heap[i] = value;
            // value.heapIndex = i;
            i++;
        }
        // System.out.println(source+" "+Permutations.get(source - 1));
        // percolateUp(source, heap[source]);
        // print(""+heapSize);
        // deleteMin();
        // print(""+heapSize);
        // percolateDown(1, heap[1]);
        // for (int i = 1; i <= heapSize; i++) {
        //     if (heap[i].distance == 0) {
        //         print(""+i);
        //     }
        // }
    }

    private void heapify(String start) {
        heapSize = 362880; // 362880 nodes
        Node tmp;
        // int source = -1; // initialisation to satisfy compiler
        for (int i = 0, j = 1; i < heapSize; i++, j++) { // j = i + 1
            // tmp = graph.get(Permutations.get(i));
            heap[j].distance = Integer.MAX_VALUE;
            heap[j].previous = null;
            heap[j].heapIndex = j;
            // tmp.heapIndex = j;
            // if (Permutations.get(i).equals(start)) {
            //     // it's the source
            //     tmp.distance = 0;
            //     source = j;
            // }
            // heap[j] = tmp;
        }
        // for (int i = 0, j = 1; i < heapSize; i++, j++) {
        //     if (heap[j] == graph.get(start)) print("got it @ "+j);
        // }
        graph.get(start).distance = 0;
        // print(start);
        // System.out.println(source+" "+Permutations.get(source - 1));
        // print("calling percolate with "+graph.get(start).heapIndex+graph.get(start).previous);
        percolateUp(graph.get(start).heapIndex, graph.get(start));
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
    private Node getMin() {
        return heap[1];
    }
    private Node deleteMin() {
        Node min = heap[1];
        heap[1] = heap[heapSize]; // exchange with last element
        heap[heapSize] = min;
        heapSize--;
        percolateDown(1, heap[1]);
        min.heapIndex = -1; // to denote that it's not a part of heap but that of cloud
        return min;
    }

    // Edge table storing edge weights
    int Edges[];

    // Hashmap storing all nodes and corresponding edges
    private HashMap<String, Node> graph;
    // private HashMap<String, Integer> g;
    private Puzzle() {
        // constructor
        graph = new HashMap<>();
        bakTrk = new ArrayList<>(1000); // used for backTracking

        // generatePermutations();
        Permutations = new ArrayList<>();
        permutation("123456780");

        // initialise heap
        heapSize = 362880;
        heap = new Node[362881]; // 362881 + 1

        // generate the graph
        for (int ix = 0; ix < Permutations.size(); ix++) {
            // System.out.println(Permutations.get(ix));
            // if (!g.containsKey(Permutations.get(ix))) {
                // g.add(Permutations.get(ix));
            // }
            generateComponent(Permutations.get(ix));
        }
        System.out.println(graph.size());
    }
    private void solvePuzzle(String start, String end) {
        // print(end);
        Node min;
        Node neigh;
        int dist;

        // get min dist from source for all
        while(heapSize > 181441) {
            // while List is not empty, or better one of the component is not empty
            // remove min
            // System.out.println(end + " " + (graph.get(end) == null));
            if (graph.get(end).heapIndex == 1) {
                // our string has been found
                // found a path
                break;
            }
            min = deleteMin(); // v := min
            // print("got min");
            for (i = 0; i < min.Adj.size(); i++) {
                // for each neighbour
                neigh = graph.get(min.Adj.get(i)); // neighbour node
                if (neigh.heapIndex == -1) {
                    // part of cloud
                    continue;
                }
                // !!! possible opimisation => store -1 from begining
                // print(""+min.Cost.get(i));
                dist = min.distance + Edges[min.Cost.get(i) - 1]; // v's distance + edge(v, n)'s weight
                if (dist < neigh.distance) {
                    // update it
                    neigh.distance = dist;
                    neigh.previous = min;
                    neigh.direcPrev = min.Direction.get(i);
                    neigh.edgePrev = min.Cost.get(i);
                    percolateUp(neigh.heapIndex, neigh);
                }
            } 
            // print("adjusted neigh");
            // print(end);
        }
    }
    ArrayList<String> bakTrk;
    private void backtrack(String end) {
        bakTrk.clear(); // code could be optimised here
        int steps = 0;
        // after completing dikstra,
        if (graph.get(end).distance == Integer.MAX_VALUE) {
            // it's a different component
            System.out.println("-1 -1\n");
            return;
        }
        
        Node bckTrak = graph.get(end);
        while(bckTrak.previous != null) {
            steps++;
            // System.out.printf("%d%c ",bckTrak.edgePrev, bckTrak.direcPrev);
            bakTrk.add(""+bckTrak.edgePrev+bckTrak.direcPrev+" ");
            bckTrak = bckTrak.previous;
        }
        System.out.println(steps + " " + graph.get(end).distance);
        printBckTrk();
        System.out.println();
    }
    private void printBckTrk() {
        // prints the ArrayList bckTrack in reverse order
        for (int i = bakTrk.size() - 1; i >= 0; i--) {
            System.out.printf("%s", bakTrk.get(i));
        }
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
        // swapChar only returns a new string and does not change key
        if (i < 6) {
            // swap with index + 3, and add to node
            node.Adj.add(swapChar(key, i, i + 3));
            node.Cost.add(Character.getNumericValue(key.charAt(i + 3)));
            node.Direction.add('U');
        }
        if (i >= 3) {
            // swap with index - 3, and add to node
            node.Adj.add(swapChar(key, i, i - 3));
            node.Cost.add(Character.getNumericValue(key.charAt(i - 3)));
            node.Direction.add('D');
        }
        if (i % 3 < 2) {
            // swap with index + 1, and add to node
            node.Adj.add(swapChar(key, i, i + 1));
            node.Cost.add(Character.getNumericValue(key.charAt(i + 1)));
            node.Direction.add('L');
        }
        if (i % 3 > 0) {
            // swap with index - 1, and add to node
            node.Adj.add(swapChar(key, i, i - 1));
            node.Cost.add(Character.getNumericValue(key.charAt(i - 1)));
            node.Direction.add('R');
        }

        // add our node to the graph and heap
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

        // build heap
        puzzle.buildHeap();

        puzzle.Edges = new int[8];
        int t = input.nextInt();
        String start, end;

        long oneTime=System.currentTimeMillis()-startTime;
        for (int test = 0; test < t; test++) {
            start = input.next();
            end = input.next();

            // make G as 0 for convienence
            start = start.replace('G', '0');
            end = end.replace('G', '0');
            for (int e = 0; e < 8; e++) {
                puzzle.Edges[e] = input.nextInt();
            }
            // puzzle.print(end);
            // buildHeap
            puzzle.heapify(start);
            puzzle.solvePuzzle(start, end);
            puzzle.backtrack(end);
        }

        long solveTime=System.currentTimeMillis()-startTime-oneTime;
        System.out.println("oneTime: "+ oneTime + " millis and solveTime: "+solveTime+" millis");
    }
}