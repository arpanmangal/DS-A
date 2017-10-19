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
        haveChildren = false;
        parentNode = null;
        parentIndex = -1;
    }

    // access functions
    public int getNodeParameter() {return t;};
    public int size() {return numKeys;};


    // compute height
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


    // insert functions
    // functions giving child information
    public BNode<Key, Value> getChild(int index) {
        if (!haveChildren || index > numKeys) { // changed form + 1
            return null;
        } else {
            return children.get(index);
        }
    }
    public boolean hasChild() {return haveChildren;};

    public int searchVal(Key key) {
        // implements binary search to find val
        // returns index of immediate successor in whose left child do the insert
        if (numKeys == 0) return -1; // not possible to find
        return search(key, 0, this.numKeys - 1);
    }
    private int search(Key key, int start, int end) {
        // helper function for searchVal, implements binary search
        int mid = (start + end) / 2;

        int comp = key.compareTo(this.Keys.get(mid));
        if (comp >= 0) {
            // key >= mid => in right half
            if (mid == end) {
                // last elem
                return end + 1;
            } else {
                return search(key, mid + 1, end);
            }
        } else{
            // key < mid
            if (mid == start) {
                // first elem
                return start;
            } else {
                return search(key, start, mid - 1);
            }
        }
    }
    public BNode<Key, Value> breakNode() {
        // the node is full, so do a rotate or break it
        // t - 1 is the mid element
        if (this.parentNode == null) {
            // we are the full root
            BNode<Key, Value> root = new BNode<Key, Value>(2 * t);
            root.Keys.add(root.numKeys, this.Keys.get(t - 1));
            root.Values.add(root.numKeys, this.Values.get(t - 1));
            root.numKeys = 1;
            root.haveChildren = true;
            root.parentNode = null; // since I am the root
            root.parentIndex = -1;

            // make a new node with right half
            BNode<Key, Value> rightHalf = new BNode<Key, Value>(2 * t);
            for (int i = 0, lim = t - 1; i < lim; i++) { // last half t - 1 keys
                rightHalf.Keys.add(this.Keys.get(t + i));
                rightHalf.Values.add(this.Values.get(t + i));
                if (this.haveChildren) {
                    rightHalf.children.add(this.children.get(t + i));
                    rightHalf.children.get(i).parentNode = rightHalf;
                    rightHalf.children.get(i).parentIndex = i;
                }
                rightHalf.numKeys++;
            }
            if (this.haveChildren) {
                rightHalf.children.add(this.children.get(2 * t - 1));
                rightHalf.children.get(t - 1).parentNode = rightHalf;
                rightHalf.children.get(t - 1).parentIndex = t - 1;
            }
            // rightHalf.numKeys = t - 1;
            rightHalf.haveChildren = this.haveChildren;
            rightHalf.parentNode = root;
            rightHalf.parentIndex = 1;

            this.numKeys = t - 1; // just decrease the size, remaining values remain as garbage <= this is problem
            this.parentNode = root;
            this.parentIndex = 0;

            root.children.add(this);
            root.children.add(rightHalf);

            return root;
        }

        this.parentNode.Keys.add(this.parentIndex, this.Keys.get(t - 1));
        this.parentNode.Values.add(this.parentIndex, this.Values.get(t - 1));
        this.parentNode.numKeys++;

        // divide the current node
        // make a new node with right half
        BNode<Key, Value> rightHalf = new BNode<Key, Value>(2 * t);
        for (int i = 0, lim = t - 1; i < lim; i++) { // last half t - 1 keys
            rightHalf.Keys.add(this.Keys.get(t + i));
            rightHalf.Values.add(this.Values.get(t + i));
            if (this.haveChildren) {
                rightHalf.children.add(this.children.get(t + i));
                rightHalf.children.get(i).parentNode = rightHalf;
                rightHalf.children.get(i).parentIndex = i;
            }
            rightHalf.numKeys++;
        }
        if (this.haveChildren) {
            rightHalf.children.add(this.children.get(2 * t - 1));
            rightHalf.children.get(t - 1).parentNode = rightHalf;
            rightHalf.children.get(t - 1).parentIndex = t - 1;
        }
        rightHalf.numKeys = t - 1;
        rightHalf.haveChildren = this.haveChildren;
        rightHalf.parentNode = this.parentNode;
        rightHalf.parentIndex = this.parentIndex + 1;

        this.numKeys = t - 1; // just decrease the size, remaining values remain as garbage
        this.parentNode = this.parentNode;
        this.parentIndex = this.parentIndex;
        this.parentNode.children.add(this.parentIndex + 1, rightHalf);

        // update all parent indices
        for (int i = this.parentIndex + 1; i <= this.parentNode.numKeys; i++) {
            this.parentNode.children.get(i).parentIndex = i;
        }
        return this.parentNode;
    }

    public void insert(Key key, Value val, int index) {
        // function for inserting in a leaf node
        this.Keys.add(index, key);
        this.Values.add(index, val);
        this.numKeys++;
    }


    // search function
    public List<Value> searchKey(Key key) {
        // function for searching the key in the node, implements linear search
        // traverse the node and get the key-value pairs for key = key
        List<Value> data = new ArrayList<Value>();
        int status = 0;
        int comp;
        if (this.numKeys == 0) return data; // nothing to search
        for (int i = 0; i < numKeys; i++) {
            comp = key.compareTo(this.Keys.get(i));
            if (comp == 0) {
                // key = val
                status = 1;
                if (this.haveChildren) {
                    data.addAll(this.children.get(i).searchKey(key));
                }
                data.add(this.Values.get(i));
            } else if (comp < 0) {
                // key < val
                if (status != 2) {
                    if (this.haveChildren) {
                        data.addAll(this.children.get(i).searchKey(key));
                    }
                }
                status = 2;
            }
        }
        // take care of last child
        comp = key.compareTo(this.Keys.get(numKeys - 1));
        if (comp >= 0) {
            // key >= val
            if (this.haveChildren) {
                data.addAll(this.children.get(numKeys).searchKey(key));
            }
        }

        return data;
    }

    // deletion functions
    // deleting a node cell
    private pair<Key, Value> boomCell(BNode<Key, Value> node, int index) {
        // return the modified version of node after booming the value
        // if a root, numKeys can be < t, o.w. >= t
        if (node == null) return new pair<Key, Value>(node, 0); // no node to boom
        if (index >= node.numKeys) {
            // cannot delete
            return new pair<Key, Value>(node, 0);
        }
        if (!node.haveChildren) {
            // leaf node
            node.numKeys--;
            node.Keys.remove(index);
            node.Values.remove(index);
            return new pair<Key, Value>(node, 1);
        } else {
            // find inorder successor and swap with that
            BNode<Key, Value> currentNode = node.children.get(index + 1);
            if (currentNode.numKeys < t) {
                // it's an underflow
                currentNode = removeUnderflow(currentNode);
                if (node.numKeys != 0) {
                    // either the toDelete key is absorbed down or is there
                    return new pair<Key, Value>(node, 0); // future iterations will look after further deletion
                } else {
                    // the node has become empty, it must have been a root
                    return new pair<Key, Value>(currentNode, 0);
                }
                 
            }
            // no probs if told to delete from root
            while(currentNode.haveChildren) {
                currentNode = currentNode.children.get(0);
                if (currentNode.numKeys < t) {
                    // remove the underflow
                    currentNode = removeUnderflow(currentNode);
                }
            }
            // swap and delete it
            node.Keys.set(index, currentNode.Keys.get(0));
            node.Values.set(index, currentNode.Values.get(0));
            
            currentNode.Keys.remove(0);
            currentNode.Values.remove(0);
            currentNode.numKeys--;
            return new pair<Key, Value>(node, 1);
        }
    }

    public pair<Key, Value> removeKey(Key key) {
        // booms all required key-value pair inside itself and gives pointer to next node to process, can be itself
        if (this.numKeys == 0) {
            // nothing in here
            return new pair<Key, Value>(null, 0);
        }
        if (this.parentNode != null && this.numKeys < t) {
            return new pair<Key, Value>(removeUnderflow(this), 0);
        } else {
            // root or a t key node, find and boom
            int succ = this.searchVal(key);
            if (succ == 0) {
                // not to be found in me
                if (this.haveChildren) {
                    return new pair<Key, Value>(this.children.get(0), 0);
                } else {
                    // nothing to delete
                    return new pair<Key, Value>(null, 0);
                }
            } else {
                if (this.Keys.get(succ - 1).equals(key)) {
                    // delete it
                    return boomCell(this, succ - 1); // as more could have been left
                } else {
                    // no one in this node
                    if (this.haveChildren) {
                        return new pair<Key, Value>(this.children.get(succ), 0);
                    } else {
                        // nothing to delete
                        return new pair<Key, Value>(null, 0);
                    }
                }
            }
        }
    }

    private BNode<Key, Value> removeUnderflow(BNode<Key, Value> node) {
        // the node has underflow, remove it
        if (node == null) {
            // error checking
            return node;
        }
        if (node.numKeys >= t) {
            // not an underflow
            return node;
        }
        BNode<Key, Value> parent = node.parentNode;
        // check method resolution of underflow
        if (parent == null) {
            // it is the root, it can have underflow
            return node;
        } else if ((node.parentIndex > 0) && (parent.children.get(node.parentIndex - 1).numKeys >= t)) {
            // have left sibling, which has extra keys
            // do a right rotate, parent not affected
            BNode<Key, Value> leftSib = parent.children.get(node.parentIndex - 1);
            node.Keys.add(0, parent.Keys.get(node.parentIndex - 1));
            node.Values.add(0, parent.Values.get(node.parentIndex - 1));
            node.numKeys++;
            if (node.haveChildren) {
                node.children.add(0, leftSib.children.get(leftSib.numKeys));
                // update the child's parent
                node.children.get(0).parentNode = node;
                // adjust the indices of all the siblings
                for (int i = 0; i <= node.numKeys; i++) {
                    node.children.get(i).parentIndex = i;
                }
            }

            parent.Keys.set(node.parentIndex - 1, leftSib.Keys.get(leftSib.numKeys - 1));
            parent.Values.set(node.parentIndex - 1, leftSib.Values.get(leftSib.numKeys - 1));
            leftSib.Keys.remove(leftSib.numKeys - 1);
            leftSib.Values.remove(leftSib.numKeys - 1);
            if(leftSib.haveChildren) leftSib.children.remove(leftSib.numKeys);
            leftSib.numKeys--;

            return node;
        } else if ((node.parentIndex < parent.numKeys) && (node.parentNode.children.get(node.parentIndex + 1).numKeys >= t)) {
            // have right sibling, which has extra keys
            // do a left rotate
            BNode<Key, Value> rightSib = parent.children.get(node.parentIndex + 1);
            node.Keys.add(node.numKeys, parent.Keys.get(node.parentIndex));
            node.Values.add(node.numKeys, parent.Values.get(node.parentIndex));
            node.numKeys++;
            if (node.haveChildren) {
                node.children.add(node.numKeys, rightSib.children.get(0));
                // update parent of this child
                node.children.get(node.numKeys).parentNode = node;
                node.children.get(node.numKeys).parentIndex = node.numKeys;
            }
            
            parent.Keys.set(node.parentIndex, rightSib.Keys.get(0));
            parent.Values.set(node.parentIndex, rightSib.Values.get(0));
            rightSib.Keys.remove(0);
            rightSib.Values.remove(0);
            rightSib.numKeys--;
            if (rightSib.haveChildren) {
                rightSib.children.remove(0);
                for (int i = 0; i <= rightSib.numKeys; i++) {
                    rightSib.children.get(i).parentIndex = i;
                }
            }

            return node;
        } else if(node.parentIndex > 0) {
            // have left sibling, merge it
            // merge with left sibling
            BNode<Key, Value> leftSib = parent.children.get(node.parentIndex - 1);
            leftSib.Keys.add(leftSib.numKeys, parent.Keys.get(node.parentIndex - 1));
            leftSib.Values.add(leftSib.numKeys, parent.Values.get(node.parentIndex - 1));
            leftSib.numKeys++;
            for (int i = 0; i < node.numKeys; i++) {
                leftSib.Keys.add(leftSib.numKeys, node.Keys.get(i));
                leftSib.Values.add(leftSib.numKeys, node.Values.get(i));
                if (node.haveChildren) {
                    leftSib.children.add(leftSib.numKeys, node.children.get(i));
                    // update parent of this child
                    leftSib.children.get(leftSib.numKeys).parentNode = leftSib;
                    leftSib.children.get(leftSib.numKeys).parentIndex = leftSib.numKeys;
                }
                leftSib.numKeys++;
            }
            if (node.haveChildren) {
                leftSib.children.add(leftSib.numKeys, node.children.get(node.numKeys));
                // update parent of this child
                leftSib.children.get(leftSib.numKeys).parentNode = leftSib;
                leftSib.children.get(leftSib.numKeys).parentIndex = leftSib.numKeys;
            }

            //  leftSib.numKeys = 2 * t - 1;
            leftSib.parentNode = node.parentNode;
            leftSib.parentIndex = node.parentIndex - 1;

            node.numKeys = 0;
            node.Keys.removeAllElements();
            node.Values.removeAllElements();
            if(node.haveChildren) node.children.removeAllElements();

            parent.Keys.remove(node.parentIndex - 1);
            parent.Values.remove(node.parentIndex - 1);
            parent.children.remove(node.parentIndex);
            parent.numKeys--;

            if (parent.numKeys == 0) {
                // it was the root which has become empty!!!
                leftSib.parentNode = null;
                leftSib.parentIndex = -1;
            } else {
                // adjust the indices of all children of the parent
                for (int i = leftSib.parentIndex; i <= leftSib.parentNode.numKeys; i++) {
                    leftSib.parentNode.children.get(i).parentIndex = i;
                }
            }
            
            return leftSib;
        } else {
            // have right sibling, merge it
            // merge with right sibling
            BNode<Key, Value> rightSib = parent.children.get(node.parentIndex + 1);
            node.Keys.add(node.numKeys, parent.Keys.get(node.parentIndex));
            node.Values.add(node.numKeys, parent.Values.get(node.parentIndex));
            node.numKeys++;

            for (int i = 0; i < rightSib.numKeys; i++) {
                node.Keys.add(node.numKeys, rightSib.Keys.get(i));
                node.Values.add(node.numKeys, rightSib.Values.get(i));
                if (node.haveChildren) {
                    node.children.add(node.numKeys, rightSib.children.get(i));
                    node.children.get(node.numKeys).parentNode = node;
                    node.children.get(node.numKeys).parentIndex = node.numKeys;
                }
                node.numKeys++;
            }
            if (node.haveChildren) {
                node.children.add(node.numKeys, rightSib.children.get(rightSib.numKeys));
                node.children.get(node.numKeys).parentNode = node;
                node.children.get(node.numKeys).parentIndex = node.numKeys;
            }
            node.numKeys = 2 * t - 1;
            node.parentNode = node.parentNode; // same
            node.parentIndex = node.parentIndex; // same
                
            rightSib.numKeys = 0;
            rightSib.Keys.removeAllElements();
            rightSib.Values.removeAllElements();
            if(rightSib.haveChildren) rightSib.children.removeAllElements();

            parent.Keys.remove(node.parentIndex);
            parent.Values.remove(node.parentIndex);
            parent.children.remove(node.parentIndex + 1);
            parent.numKeys--;

            if (parent.numKeys == 0) {
                // it was the root which has become empty!!!
                node.parentNode = null;
                node.parentIndex = -1;
            } else {
                // adjust the indices of all children of the parent
                for (int i = node.parentIndex; i <= node.parentNode.numKeys; i++) {
                    node.parentNode.children.get(i).parentIndex = i;
                }
            }
            return node;
        }
    }

    public BNode<Key, Value> newRoot() {
        if (this.haveChildren) {
            return this.children.get(0);
        } else {
            // I am empty and I have no children
            return null;
        }
    }
    public String toString() {
        // uses String Builder (https://www.javatpoint.com/StringBuilder-class)
        StringBuilder s = new StringBuilder("[");
        if (this == null || this.numKeys == 0) return "[]";
        for (int i = 0; i < numKeys; i++) {
            if (this.haveChildren) {
                s.append(this.children.get(i).toString());
                s.append(", ");
            }
            s.append(this.Keys.get(i));
            s.append("=");
            s.append(this.Values.get(i));
            s.append(", ");
        }
        if (this.haveChildren) {
            s.append(this.children.get(this.numKeys).toString());
            s.append("]");
            return s.substring(0);
        } else {
            s.replace(s.length() - 2, s.length(), "]");
            return s.substring(0);
        }
    }

    public boolean myassert() {
        // checks that each child has corresponding parent
        boolean status = true;
        if (!this.haveChildren) {
            return true;
        } else {
            for (int i = 0; i <= this.numKeys; i++) {
                if (!status) return status;
                if (this.children.get(i).parentIndex != i) {
                    return false;
                }
                status = this.children.get(i).myassert();
            }
        }
        return status;
    }
}