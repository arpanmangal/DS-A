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
        // //System.out.println("in searchVal in node " + this.Keys.get(0) + this.Values.get(0));
        // implements binary search to find val
        // returns index of immediate successor in whose left child do the insert
        if (numKeys == 0) return -1; // not possible to find
        return search(key, 0, this.numKeys - 1);
    }
    private int search(Key key, int start, int end) {
        // //System.out.println("in search");
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
        //System.out.println("in breaknode");
        // the node is full, so do a rotate or break it
        // t - 1 is the mid element
        if (this.parentNode == null) {
            // we are the full root
            //System.out.println("Breaking the root!");
            BNode<Key, Value> root = new BNode<Key, Value>(2 * t);
            root.Keys.add(root.numKeys, this.Keys.get(t - 1));
            root.Values.add(root.numKeys, this.Values.get(t - 1));
            root.numKeys = 1;
            root.haveChildren = true;
            root.parentNode = null; // since I am the root
            root.parentIndex = -1;
            // root.parentNode = this.parentNode;
            // if (root.parentNode != null) {
            //     // repair connections

            // }

            // make a new node with right half
            //System.out.println("Making right half");
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
            //System.out.println("adding last child");
            if (this.haveChildren) {
                rightHalf.children.add(this.children.get(2 * t - 1));
                rightHalf.children.get(t - 1).parentNode = rightHalf;
                rightHalf.children.get(t - 1).parentIndex = t - 1;
            }
            // rightHalf.numKeys = t - 1;
            rightHalf.haveChildren = this.haveChildren;
            rightHalf.parentNode = root;
            rightHalf.parentIndex = 1;

            //System.out.println("adjusting self");
            this.numKeys = t - 1; // just decrease the size, remaining values remain as garbage <= this is problem
            this.parentNode = root;
            this.parentIndex = 0;

            //System.out.println(""+root.numKeys + root.children.size());
            root.children.add(this);
            root.children.add(rightHalf);

            //System.out.println("Final Configuration: root = " + root.Keys.get(0) + root.children.get(0).Keys.get(0) + root.children.get(1).Keys.get(0));
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
            rightHalf.numKeys++;
        }
        if (this.haveChildren) {
            rightHalf.children.add(this.children.get(2 * t - 1));
            rightHalf.children.get(t - 1).parentNode = rightHalf;
            rightHalf.children.get(t - 1).parentIndex = t - 1;
        }
        // rightHalf.numKeys = t - 1;
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
        // //System.out.println("in insert");
        // try {
        //     //System.out.println("this = " + this.Keys.get(0) + " insert " + key + " @ index " + index);
        // } catch(ArrayIndexOutOfBoundsException e) {
        //     //System.out.println("PL Butwhy? " + e);
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
        if (numKeys == 0) return data; // nothing to search
        for (int i = 0; i < numKeys; i++) {
            comp = key.compareTo(this.Keys.get(i));
            if (comp == 0) {
                // key = val
                status = 1;
                if (this.haveChildren) {
                    data.addAll(this.children.get(i).searchKey(key));
                    // //System.out.println("Going left of " + this.Keys.get(i) + this.Values.get(i));
                }
                data.add(this.Values.get(i));
                // //System.out.println("adding " + this.Values.get(i));
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
                // //System.out.println("Going right of " + this.Keys.get(numKeys - 1) + this.Values.get(numKeys - 1));
            }
        }

        return data;
    }

    // deleting a node
    private BNode<Key, Value> boomCell(BNode<Key, Value> node, int index) {
        // return the modified version of node after booming the value
        // if a root, numKeys can be < t, o.w. >= t
        //System.out.println("Booming the cell: ");
        // boom it, don't care about rotation etc.
        if (node == null) return node; // no node to boom
        if (index >= node.numKeys) {
            // cannot delete
            //System.out.println("illegal boom order");
            return node;
        }
        //System.out.println("" + node.Keys.get(index) + node.Values.get(index));
        if (!node.haveChildren) {
            // leaf node
            //System.out.println("leaf node in boom cell");
            node.numKeys--;
            node.Keys.remove(index);
            node.Values.remove(index);
            return node;
            // for (int i = index; i < node.numKeys; i++) {
            //     Keys.
            // }
        } else {
            // find inorder successor and swap with that
            //System.out.println("going to succ in boom cell");
            BNode<Key, Value> currentNode = node.children.get(index + 1);
            if (currentNode.numKeys < t) {
                //System.out.println("In boom cell right child has underflow!");
                // it's an underflow
                // int initialKeys = node.numKeys;
                currentNode = removeUnderflow(currentNode);
                // if (node.numKeys != initialKeys) {

                // }
                if (node.numKeys != 0) {
                    // either the toDelete key is absorbed down or is there
                    return node; // future iterations will look after further deletion
                } else {
                    // the node has become empty, it must have been a root
                    return currentNode;
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
            return node;
        }
    }

    public BNode<Key, Value> removeKey(Key key) {
        // booms all required key-value pair inside itself and gives pointer to next node to process, can be itself
        // //System.out.println("removing the key: " + key + " from node with " + this.Keys.get(0) + this.Values.get(0));
        //System.out.println("ho -> in this iteration: " + this.//toString() + " and my children's parent: ");
        if (this.haveChildren) {
            for (int i = 0; i <= this.numKeys; i++) {
                //System.out.println(this.children.get(i).parentNode.//toString());
            }
        }
        if (this.numKeys == 0) {
            // nothing in here
            //System.out.println("returning null!!");
            return null;
        }
        if (this.parentNode != null && this.numKeys < t) {
            //System.out.println("ha");
            //System.out.println("Node with insufficient Keys, Me: " + this.Keys.get(0) + this.Values.get(0) +" parent: "+ this.parentNode.Keys.get(0) + this.parentNode.Values.get(0));
            // do something to increase the keys
            // check with nearby siblings
            // control has reached me => neither my parents nor my siblings are free of the key
         /*   BNode<Key, Value> parent = this.parentNode;
            if (this.parentIndex > 0 && parent.children.get(this.parentIndex - 1).numKeys >= t) {
                // left sibling => do a right rotate
                //System.out.println("doing a right rotate");
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
                //System.out.println("doing a left rotate");
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
                //System.out.println("doing merging with left sibling, my: "+  this.Keys.get(0) + this.Values.get(0));
                BNode<Key, Value> leftSib = parent.children.get(this.parentIndex - 1);
                //System.out.println("parent: " + parent.Keys.get(0) + leftSib.Keys.get(0));
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

                //System.out.println("new node with first val: " + leftSib.Keys.get(0) + leftSib.Values.get(0) +
                " mid-val " + leftSib.Keys.get(t - 1) + leftSib.Values.get(t - 1) +  " last val " + leftSib.Keys.get(2 * t - 2) + leftSib.Values.get(2 * t - 2));
                return leftSib;
            } else {
                // merge with right sibling
                //System.out.println("doing merging with right sibling");
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
            } */
            return removeUnderflow(this);
            
        } else {
            //System.out.println("root or a t key node to delete, me: " + this.//toString() + this.numKeys + this.children.size());
            // root or a t key node, find and boom
            int succ = this.searchVal(key);
            //System.out.println("delete at index: " + (succ - 1));
            if (succ == 0) {
                // not to be found in me
                //System.out.println("not in me -> see my children(0)");
                if (this.haveChildren) {
                    //System.out.println(" going to child(0)");
                    return this.children.get(0);
                } else {
                    // nothing to delete
                    //System.out.println("returning null");
                    return null;
                }
            } else {
                //System.out.println(this.Keys.get(succ - 1) + " " + key + " " + (this.Keys.get(succ - 1).equals(key)) + " " + key.getClass().getName() + this.Keys.get(succ - 1).getClass().getName());
                if (this.Keys.get(succ - 1).equals(key)) {
                    // delete it
                    //System.out.println("deleting inside me @ " + succ +"- 1");
                    // boomCell(this, succ - 1);
                    /*if (this.parentNode == null && numKeys == 0) {
                        // root node made empty
                        return null; // done
                    }*/
                    return boomCell(this, succ - 1); // as more could have been left
                } else {
                    // no one in this node
                    //System.out.println("Go to my children");
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

    private BNode<Key, Value> removeUnderflow(BNode<Key, Value> node) {
        //System.out.println(" in underFlow nodeParent: " + node.parentNode.//toString());
        // the node has underflow, remove it
        if (node == null) {
            // error checking
            return node;
        }
        if (node.numKeys >= t) {
            // not an underflow
            return node;
        }
        //System.out.println("going to treat the underflow");
        BNode<Key, Value> parent = node.parentNode;
        //System.out.println("ok");
        // check method resolution of underflow
        //System.out.println("parentIndex: " + node.parentIndex);
        if (parent == null) {
            // it is the root, it can have underflow
            //System.out.println("it's the root");
            return node;
        } else if ((node.parentIndex > 0) && (parent.children.get(node.parentIndex - 1).numKeys >= t)) {
            // have left sibling, which has extra keys
            // do a right rotate, parent not affected
            //System.out.println("doing a right rotate");
            BNode<Key, Value> leftSib = parent.children.get(node.parentIndex - 1);
            node.Keys.add(0, parent.Keys.get(node.parentIndex - 1));
            node.Values.add(0, parent.Values.get(node.parentIndex - 1));
            if (node.haveChildren) {
                node.children.add(0, leftSib.children.get(leftSib.numKeys));
                // update the child's parent
                node.children.get(0).parentNode = node;
                node.children.get(0).parentIndex = 0;
            }
            node.numKeys++;

            parent.Keys.set(node.parentIndex - 1, leftSib.Keys.get(leftSib.numKeys - 1));
            parent.Values.set(node.parentIndex - 1, leftSib.Values.get(leftSib.numKeys - 1));
            leftSib.Keys.remove(leftSib.numKeys - 1);
            leftSib.Values.remove(leftSib.numKeys - 1);
            if(leftSib.haveChildren) leftSib.children.remove(leftSib.numKeys);
            leftSib.numKeys--;

            //System.out.println(" parent: "+node.parentNode.//toString());
            return node;
        } else if ((node.parentIndex < parent.numKeys) && (node.parentNode.children.get(node.parentIndex + 1).numKeys >= t)) {
            // have right sibling, which has extra keys
            // do a left rotate
            //System.out.println("doing a left rotate");
            BNode<Key, Value> rightSib = parent.children.get(node.parentIndex + 1);
            node.Keys.add(node.numKeys, parent.Keys.get(node.parentIndex));
            node.Values.add(node.numKeys, parent.Values.get(node.parentIndex));
            if (node.haveChildren) {
                node.children.add(node.numKeys+1, rightSib.children.get(0));
                // update parent of this child
                node.children.get(node.numKeys + 1).parentNode = node;
                node.children.get(node.numKeys + 1).parentIndex = node.numKeys + 1;
            }
            node.numKeys++;

            parent.Keys.set(node.parentIndex, rightSib.Keys.get(0));
            parent.Values.set(node.parentIndex, rightSib.Values.get(0));
            rightSib.Keys.remove(0);
            rightSib.Values.remove(0);
            if (rightSib.haveChildren) rightSib.children.remove(0);
            rightSib.numKeys--;

            //System.out.println(" parent: "+node.parentNode.//toString());
            return node;
        } else if(node.parentIndex > 0) {
            // have left sibling, merge it
            // merge with left sibling
            //System.out.println("left merge");
            //System.out.println("doing merging with left sibling, my: "+  node.Keys.get(0) + node.Values.get(0));
            BNode<Key, Value> leftSib = parent.children.get(node.parentIndex - 1);
            // //System.out.println("parent: " + parent.Keys.get(0) + " leftSib: " + leftSib.Keys.get(0));
            //System.out.println("parent: "+parent.//toString()+" me: "+node.//toString()+" leftSib "+leftSib.//toString());
            leftSib.Keys.add(leftSib.numKeys, parent.Keys.get(node.parentIndex - 1));
            leftSib.Values.add(leftSib.numKeys, parent.Values.get(node.parentIndex - 1));
            leftSib.numKeys++;
            // //System.out.println(" after left merge node: " + leftSib.//toString());
            // //System.out.println(" after left merge node: " + node.//toString());
            for (int i = 0; i < node.numKeys; i++) {
                leftSib.Keys.add(leftSib.numKeys, node.Keys.get(i));
                leftSib.Values.add(leftSib.numKeys, node.Values.get(i));
                // //System.out.println("adding "+node.Keys.get(i)+node.Values.get(i)+node.Keys.size()+leftSib.Keys.size());
                if (node.haveChildren) {
                    leftSib.children.add(leftSib.numKeys, node.children.get(i));
                    // update parent of this child
                    leftSib.children.get(leftSib.numKeys).parentNode = leftSib;
                    leftSib.children.get(leftSib.numKeys).parentIndex = leftSib.numKeys;
                }
                leftSib.numKeys++;
                // //System.out.println(" after left merge node: " + leftSib.//toString());
            }
            if (node.haveChildren) {
                leftSib.children.add(leftSib.numKeys, node.children.get(node.numKeys));
                // update parent of this child
                leftSib.children.get(leftSib.numKeys).parentNode = leftSib;
                leftSib.children.get(leftSib.numKeys).parentIndex = leftSib.numKeys;
            }

            //System.out.println(" after left merge node: " + leftSib.//toString());
            
            // //System.out.println(" after left merge node: " + leftSib.//toString());
            // //System.out.println("setting left sib");
        //    leftSib.numKeys = 2 * t - 1;
            leftSib.parentNode = node.parentNode;
            leftSib.parentIndex = node.parentIndex - 1;

            // //System.out.println(" after left merge node: " + leftSib.//toString());
            // //System.out.println("setting myself");
            node.numKeys = 0;
            node.Keys.removeAllElements();
            node.Values.removeAllElements();
            if(node.haveChildren) node.children.removeAllElements();

            // //System.out.println("setting parent in left merge");
            parent.Keys.remove(node.parentIndex - 1);
            parent.Values.remove(node.parentIndex - 1);
            parent.children.remove(node.parentIndex);
            parent.numKeys--;

            // //System.out.println(" after left merge node: " + leftSib.//toString());
            if (parent.numKeys == 0) {
                // it was the root which has become empty!!!
                //System.out.println("empty root afer merge");
                leftSib.parentNode = null;
                leftSib.parentIndex = -1;
            }
            // //System.out.println("ya ya");
            //System.out.println(" after left merge node: " + leftSib.//toString());
            // //System.out.println("new node with first val: " + leftSib.Keys.get(0) + leftSib.Values.get(0) +
            // " mid-val " + leftSib.Keys.get(t - 1) + leftSib.Values.get(t - 1) +  " last val " + leftSib.Keys.get(2 * t - 2) + leftSib.Values.get(2 * t - 2));
            //System.out.println("yo");
            return leftSib;
        } else {
            // have right sibling, merge it
            // merge with right sibling
            //System.out.println("doing merging with right sibling");
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
                //System.out.println("empty root afer merge");
                node.parentNode = null;
                node.parentIndex = -1;
            }
            return node;

        }

    }

    public BNode<Key, Value> newRoot() {
        //System.out.println("Assigning the new root");
        if (this.haveChildren) {
            return this.children.get(0);
        } else {
            // I am empty and I have no children
            //System.out.println("empty and no children");
            return null;
        }
    }
    public String toString() {
        // from https://www.javatpoint.com/StringBuilder-class
        // //System.out.println("root = " + this.Keys.get(0) + this.numKeys);
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




    // deletion functions
    
}