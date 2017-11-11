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
        int backTrackLength;
        public Node() {
            // constructor
            Adj = new ArrayList<>(4);
            Direction = new ArrayList<>(4);
            Cost = new ArrayList<>(4);
        }
    }

    /************ HEAP ****************/
    // heap for maintaing nodes & distances from source node

    private Node[] heap;
    private int heapSize;

    private void buildHeap() {
        // initialise heap
        heapSize = 362880;
        heap = new Node[362881]; // 362881 + 1

        int i = 1;
        for (Node value : graph.values()) {
            // iterate over all nodes in the graph
            heap[i] = value;
            // value.heapIndex = i;
            i++;
        }
    }

    private void heapify(String start) {
        heapSize = 362880; // 362880 nodes
        for (int i = 0, j = 1; i < heapSize; i++, j++) { // j = i + 1
            // tmp = graph.get(Permutations.get(i));
            heap[j].distance = Integer.MAX_VALUE;
            heap[j].previous = null;
            heap[j].backTrackLength = Integer.MAX_VALUE;
            heap[j].heapIndex = j;
        }

        Node source = graph.get(start);
        source.distance = 0;
        source.backTrackLength = 0;
        percolateUp(source.heapIndex, source);
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

/******************** END HEAP **********************/

/***************** GENERATE PERMUTATIONS ************/
    // generate permutations
    ArrayList<String> Permutations;

    void permute(char[] str,int i) { 
        if (i == 9) {// 9 is the length of perms
            // printArray(array,length);
            Permutations.add(new String(str));
            return;
        }
        int j = i;
        char tmp;
        for (j = i; j < 9; j++) { 
            // swap(array+i,array+j);
            tmp = str[i];
            str[i] = str[j];
            str[j] = tmp;
            
            permute(str,i+1);
            // swap(array+i,array+j);
            tmp = str[i];
            str[i] = str[j];
            str[j] = tmp;
        }
        return;
    }

    String swapChar(String s, int s1, int s2) {
        char[] str = s.toCharArray();
        char tmp = str[s1];
        str[s1] = str[s2];
        str[s2] = tmp;
        return new String(str);
    }

/***************** PERMUTATIONS GENERATED ************/


/*********GENERATE GRAPH AND SOLVE PUZZLE *************/
    // Edge table storing edge weights
    int Edges[];

    // Hashmap storing all nodes and corresponding edges
    private HashMap<String, Node> graph;
    private Puzzle() {
        // constructor
        graph = new HashMap<>(1000000);
        bkTrkPath = new ArrayList<>(1000); // used for backTracking

        // generatePermutations();
        Permutations = new ArrayList<>(400000);
        // permutation("123456780");
        long start = System.currentTimeMillis();
        permute("123456780".toCharArray(), 0);
        System.out.println("time to make permutations: "+(System.currentTimeMillis()-start)+" millis");

        // generate the graph
         start=System.currentTimeMillis();
        for (int ix = 0; ix < Permutations.size(); ix++) {
            // System.out.println(Permutations.get(ix));
            // if (!g.containsKey(Permutations.get(ix))) {
                // g.add(Permutations.get(ix));
            // }
            generateGraph(Permutations.get(ix));
        }
        System.out.println("time to make graph: "+(System.currentTimeMillis()-start)+" millis");
        // System.out.println(graph.size());
    }
    private int i;
    void generateGraph(String key) {
        // generate the neighbours of the key node
        
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

        return;
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
                if (dist < neigh.distance || dist == neigh.distance && min.backTrackLength < neigh.backTrackLength) {
                    // // My neighbour has a shorter path through me
                    neigh.distance = dist;
                    neigh.previous = min;
                    neigh.direcPrev = min.Direction.get(i);
                    neigh.edgePrev = min.Cost.get(i);
                    neigh.backTrackLength = min.backTrackLength + 1; // one step more
                    percolateUp(neigh.heapIndex, neigh);
                }
            } 
            // print("adjusted neigh");
            // print(end);
        }
    }
    ArrayList<String> bkTrkPath;
    private void backtrack(String end) {
        bkTrkPath.clear(); // code could be optimised here
        // int steps = 0;
        // after completing dikstra,
        if (graph.get(end).distance == Integer.MAX_VALUE) {
            // it's a different component
            System.out.println("-1 -1\n");
            return;
        }
        
        Node bckTrak = graph.get(end);
        while(bckTrak.previous != null) {
            // steps++;
            // System.out.printf("%d%c ",bckTrak.edgePrev, bckTrak.direcPrev);
            bkTrkPath.add(""+bckTrak.edgePrev+bckTrak.direcPrev+" ");
            bckTrak = bckTrak.previous;
        }
        System.out.println(graph.get(end).backTrackLength + " " + graph.get(end).distance);
        printBckTrk();
        System.out.println();
    }
    private void printBckTrk() {
        // prints the ArrayList bckTrack in reverse order
        for (int i = bkTrkPath.size() - 1; i >= 0; i--) {
            System.out.printf("%s", bkTrkPath.get(i));
        }
    }

/******************* PUZZLE SOLVED *******************/

/******************* PLAY GAME ***********************/

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