package col106.a3;

// a class used to store a pair
public class pair<Key extends Comparable<Key>, Value> {
    public BNode <Key, Value> node;
    public int val;

    // constructor
    public pair (BNode<Key, Value> node, int val) {
        this.node = node;
        this.val = val;
    }
}