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

    // setter functions
    // public void setSize(int n) {numKeys = n;};
    // public void setParent(BNode<Key, Value> parent) {parentNode = parent;};
    // public void setParentIndex(int index) {parentIndex = index;};

    // access functions
    public int getNodeParameter() {return t;};
    public int size() {return numKeys;};

    // public BNode<Key, Value> getParent() {return parentNode;};
    // public int getParentIndex() {return parentIndex;};

    // functions giving child information
    public BNode<Key, Value> getChild(int index) {
        if (!haveChildren || index > numKeys) { // changed form + 1
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
        // System.out.println("in searchVal in node " + this.Keys.get(0) + this.Values.get(0));
        // implements binary search to find val
        // returns index of immediate successor in whose left child do the insert
        return search(key, 0, this.numKeys - 1);
    }
    private int search(Key key, int start, int end) {
        // System.out.println("in search");
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
        // System.out.println("in breaknode");
        // the node is full, so do a rotate or break it
        // t - 1 is the mid element
        if (this.parentNode == null) {
            // we are the full root
            // System.out.println("Breaking the root!");
            BNode<Key, Value> root = new BNode<Key, Value>(2 * t);
            root.Keys.add(this.Keys.get(t - 1));
            root.Values.add(this.Values.get(t - 1));
            root.numKeys = 1;
            root.haveChildren = true;
            root.parentNode = null; // since I am the root
            root.parentIndex = -1;
            // root.parentNode = this.parentNode;
            // if (root.parentNode != null) {
            //     // repair connections

            // }

            // make a new node with right half
            // System.out.println("Making right half");
            BNode<Key, Value> rightHalf = new BNode<Key, Value>(2 * t);
            for (int i = 0, lim = t - 1; i < lim; i++) { // last half t - 1 keys
                rightHalf.Keys.add(this.Keys.get(t + i));
                rightHalf.Values.add(this.Values.get(t + i));
                if (this.haveChildren) {
                    rightHalf.children.add(this.children.get(t + i));
                    rightHalf.children.get(i).parentNode = rightHalf;
                    rightHalf.children.get(i).parentIndex = i;
                }
            }
            // System.out.println("adding last child");
            if (this.haveChildren) {
                rightHalf.children.add(this.children.get(2 * t - 1));
                rightHalf.children.get(t - 1).parentNode = rightHalf;
                rightHalf.children.get(t - 1).parentIndex = t - 1;
            }
            rightHalf.numKeys = t - 1;
            rightHalf.haveChildren = this.haveChildren;
            rightHalf.parentNode = root;
            rightHalf.parentIndex = 1;

            // System.out.println("adjusting self");
            this.numKeys = t - 1; // just decrease the size, remaining values remain as garbage <= this is problem
            this.parentNode = root;
            this.parentIndex = 0;

            root.children.add(this);
            root.children.add(rightHalf);

            // System.out.println("Final Configuration: root = " + root.Keys.get(0) + root.children.get(0).Keys.get(0) + root.children.get(1).Keys.get(0));
            return root;
        }

        // send the mid-value to parent node
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
        return this.parentNode;

        // BNode<Key, Value> leftSib;
        // BNode<Key, Value> rightSib;
        // if(parentIndex == 0) {
        //     // check right sibling
        //     rightSib = parentNode.children.get(1);
        //     if (rightSib.numKeys > t - 1) {
        //         // do a left rotate
        //         Keys.add(this.parentNode)
        //     }
        // }
        // t - 1 is the mid-value break there
    }

    public void insert(Key key, Value val, int index) {
        // System.out.println("in insert");
        // try {
        //     System.out.println("this = " + this.Keys.get(0) + " insert " + key + " @ index " + index);
        // } catch(ArrayIndexOutOfBoundsException e) {
        //     System.out.println("PL Butwhy? " + e);
        // }
        this.Keys.add(index, key);
        this.Values.add(index, val);
        this.numKeys++;
    }


    public List<Value> searchKey(Key key) {
        // traverse the node and get the key-value pairs for key = key
        List<Value> data = new ArrayList<Value>();
        int status = 0;
        int comp;
        for (int i = 0; i < numKeys; i++) {
            comp = key.compareTo(this.Keys.get(i));
            if (comp == 0) {
                // key = val
                status = 1;
                if (this.haveChildren) {
                    data.addAll(this.children.get(i).searchKey(key));
                    // System.out.println("Going left of " + this.Keys.get(i) + this.Values.get(i));
                }
                data.add(this.Values.get(i));
                // System.out.println("adding " + this.Values.get(i));
            } else if (comp < 0) {
                // key > val
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
                // System.out.println("Going right of " + this.Keys.get(numKeys - 1) + this.Values.get(numKeys - 1));
            }
        }

        return data;
    }

    // deleting a node
    private void boomCell(BNode<Key, Value> node, int index) {
        System.out.println("Booming the cell: ");
        // boom it, don't care about rotation etc.
        if (node == null) return; // no node to boom
        System.out.println("" + node.Keys.get(index) + node.Values.get(index));
        if (!node.haveChildren) {
            // leaf node
            node.numKeys--;
            node.Keys.remove(index);
            node.Values.remove(index);
            // for (int i = index; i < node.numKeys; i++) {
            //     Keys.
            // }
        } else {
            // find inorder successor and swap with that
            BNode<Key, Value> currentNode = node.children.get(index + 1);
            while(currentNode.haveChildren) currentNode = currentNode.children.get(0);

            // swap and delete it
            node.Keys.set(index, currentNode.Keys.get(0));
            node.Values.set(index, currentNode.Values.get(0));
            
            currentNode.Keys.remove(0);
            currentNode.Values.remove(0);
            currentNode.numKeys--;
        }
    }

    public BNode<Key, Value> removeKey(Key key) {
        // booms all required key-value pair inside itself and gives pointer to next node to process, can be itself
        System.out.println("removing the key: " + key + " from node with " + this.Keys.get(0) + this.Values.get(0));
        if (this.parentNode != null && this.numKeys < t) {
            System.out.println("Node with insufficient Keys, Me: " + this.Keys.get(0) + this.Values.get(0) + this.parentNode.Keys.get(0) + this.parentNode.Values.get(0));
            // do something to increase the keys
            // check with nearby siblings
            // control has reached me => neither my parents nor my siblings are free of the key
            BNode<Key, Value> parent = this.parentNode;
            if (this.parentIndex > 0 && parent.children.get(this.parentIndex - 1).numKeys >= t) {
                // left sibling => do a right rotate
                System.out.println("doing a right rotate");
                BNode<Key, Value> leftSib = parent.children.get(this.parentIndex - 1);
                this.Keys.add(0, parent.Keys.get(this.parentIndex - 1));
                this.Values.add(0, parent.Values.get(this.parentIndex - 1));
                this.children.add(0, leftSib.children.get(leftSib.numKeys));
                this.numKeys++;

                parent.Keys.set(this.parentIndex - 1, leftSib.Keys.get(leftSib.numKeys - 1));
                parent.Values.set(this.parentIndex - 1, leftSib.Values.get(leftSib.numKeys - 1));
                leftSib.Keys.remove(numKeys - 1);
                leftSib.Values.remove(numKeys - 1);
                leftSib.children.remove(numKeys - 1);
                leftSib.numKeys--;

                return this;
            } else if ((this.parentIndex < 2 * t - 1 && parent.children.get(this.parentIndex).numKeys >= t)) {
                // right sibling => do a left rotate
                System.out.println("doing a left rotate");
                BNode<Key, Value> rightSib = parent.children.get(this.parentIndex + 1);
                this.Keys.add(0, parent.Keys.get(this.parentIndex));
                this.Values.add(0, parent.Values.get(this.parentIndex));
                this.children.add(0, rightSib.children.get(0));
                this.numKeys++;

                parent.Keys.set(this.parentIndex, rightSib.Keys.get(0));
                parent.Values.set(this.parentIndex, rightSib.Values.get(0));
                rightSib.Keys.remove(0);
                rightSib.Values.remove(0);
                rightSib.children.remove(0);
                rightSib.numKeys--;

                return this;
            } else if (this.parentIndex > 0) {
                // merge with left sibling
                System.out.println("doing merging with left sibling, my: "+  this.Keys.get(0) + this.Values.get(0));
                BNode<Key, Value> leftSib = parent.children.get(this.parentIndex - 1);
                System.out.println("parent: " + parent.Keys.get(0) + leftSib.Keys.get(0));
                leftSib.Keys.add(parent.Keys.get(this.parentIndex - 1));
                leftSib.Values.add(parent.Values.get(this.parentIndex - 1));
                for (int i = 0; i < this.numKeys; i++) {
                    leftSib.Keys.add(this.Keys.get(i));
                    leftSib.Values.add(this.Values.get(i));
                    if (this.haveChildren) leftSib.children.add(this.children.get(i));
                }
                if (this.haveChildren) leftSib.children.add(this.children.get(this.numKeys));
                leftSib.numKeys = 2 * t - 1;
                leftSib.parentNode = this.parentNode;
                leftSib.parentIndex = this.parentIndex - 1;

                this.numKeys = 0;
                this.Keys.removeAllElements();
                this.Values.removeAllElements();
                this.children.removeAllElements();

                parent.Keys.remove(this.parentIndex - 1);
                parent.Values.remove(this.parentIndex - 1);
                parent.children.remove(this.parentIndex);

                System.out.println("new node with first val: " + leftSib.Keys.get(0) + leftSib.Values.get(0) +
                " mid-val " + leftSib.Keys.get(t - 1) + leftSib.Values.get(t - 1) +  " last val " + leftSib.Keys.get(2 * t - 2) + leftSib.Values.get(2 * t - 2));
                return leftSib;
            } else {
                // merge with right sibling
                System.out.println("doing merging with right sibling");
                BNode<Key, Value> rightSib = parent.children.get(this.parentIndex + 1);
                this.Keys.add(parent.Keys.get(this.parentIndex));
                this.Values.add(parent.Values.get(this.parentIndex));
                for (int i = 0; i < rightSib.numKeys; i++) {
                    this.Keys.add(rightSib.Keys.get(i));
                    this.Values.add(rightSib.Values.get(i));
                    if (this.haveChildren) this.children.add(rightSib.children.get(i));
                }
                if (this.haveChildren) this.children.add(rightSib.children.get(rightSib.numKeys));
                this.numKeys = 2 * t - 1;
                this.parentNode = rightSib.parentNode;
                this.parentIndex = rightSib.parentIndex - 1;
                
                rightSib.numKeys = 0;
                rightSib.Keys.removeAllElements();
                rightSib.Values.removeAllElements();
                rightSib.children.removeAllElements();

                parent.Keys.remove(this.parentIndex);
                parent.Values.remove(this.parentIndex);
                parent.children.remove(this.parentIndex + 1);

                return this;
            }
        } else {
            System.out.println("root or a t key node to delete");
            // root or a t key node, find and boom
            int succ = this.searchVal(key);
            System.out.println("delete at index: " + succ +"- 1");
            if (succ == 0) {
                // not to be found in me
                System.out.println("not in me -> see my children(0)");
                if (this.haveChildren) {
                    System.out.println(" going to child(0)");
                    return this.children.get(0);
                } else {
                    // nothing to delete
                    System.out.println("returning null");
                    return null;
                }
            } else {
                System.out.println(this.Keys.get(succ - 1) + " " + key + " " + (this.Keys.get(succ - 1).equals(key)) + " " + key.getClass().getName() + this.Keys.get(succ - 1).getClass().getName());
                if (this.Keys.get(succ - 1).equals(key)) {
                    // delete it
                    System.out.println("deleting inside me @ " + succ +"- 1");
                    boomCell(this, succ - 1);
                    if (this.parentNode == null && numKeys == 0) {
                        // root node made empty
                        return null; // done
                    }
                    return this; // as more could have been left
                } else {
                    // no one in this node
                    System.out.println("Go to my children");
                    if (this.haveChildren) {
                        return this.children.get(succ);
                    } else {
                        // nothing to delete
                        return null;
                    }
                }
            }
        }
    }

    public BNode<Key, Value> newRoot() {
        System.out.println("Assigning the new root");
        if (this.haveChildren) {
            return this.children.get(0);
        } else {
            // I am empty and I have no children
            return null;
        }
    }
    public String toString() {
        // from https://www.javatpoint.com/StringBuilder-class
        // System.out.println("root = " + this.Keys.get(0) + this.numKeys);
        StringBuilder s = new StringBuilder("[");
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
}