package col106.a3;

import java.util.List;
import java.util.*;

// raw type read => https://stackoverflow.com/a/2770692/7116413

public class BTree<Key extends Comparable<Key>,Value> implements DuplicateBTree<Key,Value> {

    private int b; // BTree parameter
    private BNode<Key, Value> root; // pointer to root node
    private int size; // number of key-value pairs

    public BTree(int b) throws bNotEvenException {  /* Initializes an empty b-tree. Assume b is even. */
        if (b % 2 == 1) throw new bNotEvenException();

        this.b = b;
        root = null;
        size = 0;
    }

    @Override
    public boolean isEmpty() {
        return (root == null);
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public int height() {
        if (root == null) return -1;
        return root.height();
    }

    @Override
    public List<Value> search(Key key) throws IllegalKeyException {
        throw new RuntimeException("Not Implemented");
    }

    @Override
    public void insert(Key key, Value val) {
        // iterative method to insert in the node
        boolean inserted = false;
        BNode<Key, Value> currentNode = root;
        if (root == null) {
            // have to initialise the tree
            root = new BNode<Key, Value>(b);
            root.insert(key, val, 0);
            inserted = true;
        }

        while(!inserted) {
            if (currentNode == null) {
                break; // cannot be inserted
            }
            // check if the node is to be divided
            if (currentNode.size() >= b - 1) {
                // break the node
                currentNode = currentNode.breakNode();
            }
            int location = currentNode.searchVal(key);
            if (currentNode.hasChild()) {
                // inset into the child
                currentNode = currentNode.getChild(location);
            } else {
                // insert right away
                //@SuppressWarnings("unchecked") // Just for this one statement
                currentNode.insert(key, val, location);
            }
        }
        if (inserted) size++;
    }

    @Override
    public void delete(Key key) throws IllegalKeyException {
        throw new RuntimeException("Not Implemented");
    }
}
