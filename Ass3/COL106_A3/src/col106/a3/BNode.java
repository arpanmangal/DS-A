package col106.a3;

import java.util.*;

public class BNode<Key extends Comparable<Key>,Value> {
    private int numKeys;
    private int t;
    private Vector<Key> Keys;
    private Vector<Value> Values;
    private Vector<BNode<Key, Value>> children;
    private boolean haveChildren; // 1 for children, 0 for no children
    private BNode<Key, Value> parentNode;
    private int parentIndex; // varies from 0 to size, Me is parentNode(parentIndex)

    public BNode(int b) {
        if (b % 2 == 1);

        t = b / 2;
        numKeys = 0;
        Keys = new Vector<Key>(b - 1);
        Values = new Vector<Value>(b - 1);
        children = new Vector<BNode<Key, Value>>(0);
    }

    public int getNodeParameter() {return t;};
    public int size() {return numKeys;};

    public BNode<Key, Value> getParent() {return parentNode;};
    public int getParentIndex() {return parentIndex;};
    public void setParent(BNode<Key, Value> parent) {parentNode = parent;};
    public void setParentIndex(int index) {parentIndex = index;};

    // functions giving child information
    public BNode<Key, Value> getChild(int index) {
        if (!haveChildren || index > numKeys + 1) {
            return null;
        } else {
            return children.get(index);
        }
    }
    public boolean hasChild() {return haveChildren;};

    public int height() {
        // returns height of the node
        int h = 0;
        BNode<Key, Value> currentNode = this;
        while(currentNode.hasChild()) {
            h++;
            currentNode = currentNode.children.get(0);
        }
        return h;
    }

    // functions used in inserting 
    public int searchVal(Key key) {
        // implements binary search to find val
        // returns index of immediate successor in whose left child do the insert
        return 0;
    }

    public BNode<Key, Value> breakNode() {
        // the node is full, so send it to its parent
        // t - 1 is the mid-value break there
        return this;
    }

    public void insert(Key key, Value val, int index) {

    }
}