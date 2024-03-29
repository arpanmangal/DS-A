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
        if (this.isEmpty()) return -1;
        return root.height();
    }

    @Override
    public List<Value> search(Key key) throws IllegalKeyException {
        if (!(key != null)) {
            // if key is of some other type, it will throw compiler error then and there
            throw new IllegalKeyException();
        }
        if (root == null) return new ArrayList<Value>(); // empty list
        return root.searchKey(key);
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
                // if the broken node is the root
                if (root == currentNode) {
                    root = currentNode = currentNode.breakNode();
                } else {
                    currentNode = currentNode.breakNode();
                }    
            }
            int location = currentNode.searchVal(key);
            if (currentNode.hasChild()) {
                // inset into the child
                currentNode = currentNode.getChild(location);
            } else {
                // insert right away
                currentNode.insert(key, val, location);
                inserted = true;
            }
        }
        if (inserted) size++;
    }

    @Override
    public void delete(Key key) throws IllegalKeyException {
        if (!(key != null)) {
            // if key is of some other type, it will throw compiler error then and there
            throw new IllegalKeyException();
        }
        pair<Key, Value> currentNode = new pair<Key, Value>(root, 0);
        int initialSize = size;
        while(currentNode.node != null) {
            // delete until all not deleted
                currentNode = currentNode.node.removeKey(key);
                size -= currentNode.val;
            if (root != null && root.size() == 0) {
                // root is empty (will be empty when its children absorb it)
                root = root.newRoot();
            }
        }
        if (size == initialSize) {
            // tried to delete an illegal key
            throw new IllegalKeyException();
        }
    }

    @Override
    public String toString() {
        if (this.isEmpty()) return "[]"; // to avoid null pointer exception
        return root.toString();
    }
}
