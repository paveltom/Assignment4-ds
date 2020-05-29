import java.util.Arrays;
import java.util.Comparator;

@SuppressWarnings("unchecked")
public class BTree<T extends Comparable<T>> {

    // Default to 2-3 Tree
    private int minKeySize = 1;
    private int minChildrenSize = minKeySize + 1; // 2
    private int maxKeySize = 2 * minKeySize; // 2
    private int maxChildrenSize = maxKeySize + 1; // 3

    private Node<T> root = null;
    private int size = 0;

    /**
     * Constructor for B-Tree which defaults to a 2-3 B-Tree.
     */
    public BTree() {
    }

    /**
     * Constructor for B-Tree of ordered parameter. Order here means minimum
     * number of keys in a non-root node.
     *
     * @param order of the B-Tree.
     */
    public BTree(int order) {
        this.minKeySize = order - 1; //changed: '-1' added
        this.minChildrenSize = order;
        this.maxKeySize = (2 * order) - 1; //changed: '-1' added
        this.maxChildrenSize = 2 * order;
    }


    //Task 2.1
    public boolean insert(T value) {
        if (root == null) { //in case the tree is empty
            root = new Node<T>(null, maxKeySize, maxChildrenSize);
            root.addKey(value);
        } else {
            Node<T> tempRoot = this.root; //a temporal node which receives a nodes on the path

            if (tempRoot.keysSize == this.maxKeySize) { //split curr
//                Node<T> newRoot = new Node<T>(null, this.maxKeySize, this.maxChildrenSize);
//                this.root = newRoot;
//                newRoot.addChild(tempRoot);
                this.split(tempRoot);
                this.insertNonFull(this.root, value);
            } else this.insertNonFull(tempRoot, value);
        }
        return true;
    }

    private void insertNonFull(Node<T> curr, T value) {
        if (curr == null) return;

        int i = curr.keysSize - 1;

        if (curr.childrenSize == 0) { //in case 'curr' is a leaf
            if (curr.keysSize == maxKeySize) {
                Node<T> tempNode = curr.parent;
                this.split(curr);
                this.insertNonFull(tempNode, value);
            } else {
                while (i >= 0 && value.compareTo(curr.getKey(i)) < 0) { // sign '=' removed from the second condition
                    curr.keys[i + 1] = curr.keys[i];
                    i--;
                }
                curr.keys[i + 1] = value;
                curr.keysSize = curr.keysSize + 1; //manual update of the field
            }
        } else { //in case 'curr' is an inner node
            while (i >= 0 && value.compareTo(curr.keys[i]) <= 0)
                i--; //finding the index of the node which is smaller thant 'curr'

            i++; //the index to insert 'curr'
            Node<T> tempCurrChild = curr.getChild(i);

            if (tempCurrChild != null && tempCurrChild.childrenSize == this.maxChildrenSize) {
                this.split(tempCurrChild);
                if (value.compareTo(curr.getKey(i)) > 0)
                    i++; //in case the splat child inserted a key into its parent keys[] that is smaller than 'value'
            }

            this.insertNonFull(curr.getChild(i), value);
        }
    }


//    public T delete(T value) { //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//        Object[] searchResult = this.Search(this.root, value);
//        if (searchResult == null || searchResult[0] == null) return null;
//        Node<T> toDelete = (Node<T>)searchResult[0];
//        int i = (int)searchResult[1]; //index of toDelete in its parent.children array
//
//        if (toDelete.parent == null){ //in case of root
//
//            // add case the root is of size 1
//            if (toDelete.keysSize == 1 && toDelete.childrenSize > 0){
//
//
//                //if both childs are minimal
//
//                //if one of the childs is not minimal
//
//                //consider childs could have childs
//            }
//
//
//            if (toDelete.childrenSize == 0){
//                toDelete.removeKey(value);
//            }
//            else {
//                if (toDelete.children[i].keysSize > this.minKeySize){
//                    if(toDelete.children[i].childrenSize == 0) toDelete.keys[i] = toDelete.children[i].removeKey(toDelete.children[i].childrenSize-1);
//                    // else - child is not a leaf
//                }
//                else if(toDelete.children[i + 1].keysSize > this.minKeySize) {
//                    if (toDelete.children[i+1].childrenSize == 0) toDelete.keys[i] = toDelete.children[i + 1].removeKey(0);
//                    // else - child is not a leaf
//                }
//                else {
//                    // merging of two childs and removing the value from todelete
//                    Node<T> merged = toDelete.children[i];
//                    for (int k = 0; k < toDelete.children[i+1].keysSize; k++) merged.addKey(toDelete.children[i+1].keys[k]);
//                    toDelete.removeChild(i+1);
//                    toDelete.removeKey(i);
//                }
//            }
//
//        }
//
//        if(toDelete.childrenSize == 0) { // in case toDelete is a leaf
//            if (toDelete.numberOfKeys() > this.minKeySize) toDelete.removeKey(value);
//            else if(removeFromBro(toDelete)) toDelete.removeKey(value);
//                else {
//                toDelete = merge(toDelete, i); //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
//                toDelete.removeKey(value);
//                 }
//            return value;
//        }
//		return null;
//    }


    public T delete(T value) {
        Node<T> toDelete = this.getNode(value);
        //if toDelete is a root

        if (toDelete.childrenSize == 0) { // in case toDelete is a leaf
            if (toDelete.numberOfKeys() > this.minKeySize) toDelete.removeKey(value);
            else {
                this.combined(toDelete);
                toDelete.removeKey(value);
            }
            return value;



        }

        //case 1: toDelete has a pred or as succ
        if (toDelete.getChild(toDelete.indexOf(value)).keysSize == this.minKeySize && toDelete.getChild(toDelete.indexOf(value) + 1).keysSize == this.minKeySize) {
            Node<T> pred = this.predecessor(toDelete.getChild(toDelete.indexOf(value)));
            Node<T> succ;
            if (pred.keysSize == this.minKeySize) {
                succ = this.successor(toDelete.getChild(toDelete.indexOf(value) + 1));
                if (succ.keysSize > minKeySize) {
                    toDelete.addKey(succ.removeKey(0));
                    toDelete.removeKey(value);
                    return value;
                }
            } else {
                toDelete.addKey(pred.removeKey(0));
                toDelete.removeKey(value);
                return value;
            }
        }




        //case 2: toDelete does not have presd/succ => downmerge (not important if toDelete is a root or an inner node)

        return value;
    }

    private Node<T> predecessor (Node<T> curr){ //receives left child of the 'value' to find its predecessor
        while (curr.childrenSize != 0){
            curr = curr.children[curr.childrenSize-1];
        }
        return curr;
    }

    private Node<T> successor (Node<T> curr){ //receives right child of the 'value' to find its successor
        while (curr.childrenSize != 0){
            curr = curr.children[0];
        }
        return curr;
    }


    /*
        else if (toDelete == this.root && toDelete.keysSize == 1){
        Node<T> leftChild = toDelete.children[0];
        Node<T> rightChild = toDelete.children[1];

        if (leftChild.keysSize == minKeySize && rightChild.keysSize == minKeySize){
            for (int i = 0; i < rightChild.keysSize; i++)
                leftChild.addKey(rightChild.keys[i]);
            for(int i = 0; i < rightChild.childrenSize; i++)
                leftChild.addChild(rightChild.children[i]);
            this.root = leftChild;
            leftChild.parent = null;
        }
        else {
            if (leftChild.keysSize > this.minKeySize){

            }
            else

        }
    }
     */

    private void downMerge(Node<T> source, T value) {

        int indexOfLeftChild = source.indexOf(value);
        Node<T> leftChild = source.getChild(indexOfLeftChild);
        Node<T> rightBro = source.getChild(indexOfLeftChild + 1);
        leftChild.addKey(source.removeKey(indexOfLeftChild));
        for (int i = 0; i < rightBro.keysSize; i++) leftChild.addKey(rightBro.keys[i]); //toDelete receives the values of right brother
        source.removeChild(rightBro); //right brother is removed

    }

    private Node<T> merge(Node<T> toDelete, int indexInParent){ //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Node<T> bro;
        if (indexInParent > 0){ // in case of merge with left brother
            bro = toDelete.parent.children[indexInParent-1]; // getChild(toDelete.parent.indexOf(toDelete)-1); //left brother
            bro.addKey(bro.parent.removeKey(indexInParent-1)); //left brother receives the value of medial parent
            for (int i = 0; i < toDelete.keysSize; i++)
                bro.addKey(toDelete.keys[i]); //left brother receives the values of toDelete
            bro.parent.removeChild(toDelete); //toDelete is removed
            return bro;
        }
        else if(indexInParent < (toDelete.parent.children.length-1)){ // in case of merge with right brother
            bro = toDelete.parent.children[indexInParent+1];
            toDelete.addKey(bro.parent.removeKey(indexInParent)); //toDelete receives the value of medial parent
            for (int i = 0; i < bro.keysSize; i++)
                toDelete.addKey(bro.keys[i]); //toDelete receives the values of left brother
            toDelete.parent.removeChild(bro); //left brother is removed
            return toDelete;
        }
        return toDelete;
    }

    private Object[] Search(Node<T> curr, T value){// !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        // consider the case if the root and its children don't have enough keys!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        int i = 0;
        while (i < curr.numberOfKeys() && (value.compareTo(curr.keys[i])>0)) i++;
        if (value.equals(curr.keys[i])) return new Object[]{curr, i};
        if (curr.childrenSize == 0) return null;
        else {
            if (curr.children[i].numberOfKeys() == this.minKeySize) return Search(increaseNumOfKeys(curr.children[i], i), value);
            else return Search(curr.children[i], value);
        }
    }

    private Node<T> increaseNumOfKeys(Node<T> toIncrease, int indexInParent){ //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        if (removeFromBro(toIncrease)) return toIncrease;
        return merge(toIncrease, indexInParent);
    }

    private boolean removeFromBro(Node<T> toDelete){
        Node<T> parent = toDelete.parent;
        Node<T> broNode;
        int i = 0;
        while (!parent.children[i].equals(toDelete)) i++;
        if(i > 0 && parent.children[i-1].numberOfKeys() > this.minKeySize){ //remove from left bro
            broNode = parent.children[i-1];
            toDelete.addKey(parent.keys[i-1]);
            int broLength = broNode.keysSize;
            parent.keys[i-1] = broNode.keys[broLength-1];
            broNode.removeKey(broLength-1);
            return true;
        }
        else if(i < (parent.children.length-1) && parent.children[i+1].numberOfKeys() > this.minKeySize){ //remove from right bro
            broNode = parent.children[i+1];
            toDelete.addKey(parent.keys[i]);
            parent.keys[i] = broNode.keys[0];
            broNode.removeKey(0);
            return true;
        }
        return false;
    }

    
	//Task 2.2
    public boolean insert2pass(T value) {
    	// TODO: implement your code here
		return false;
    }
    
    /**
     * {@inheritDoc}
     */
    public boolean add(T value) {
        if (root == null) {
            root = new Node<T>(null, maxKeySize, maxChildrenSize);
            root.addKey(value);
        } else {
            Node<T> node = root;
            while (node != null) {
                if (node.numberOfChildren() == 0) {
                    node.addKey(value);
                    if (node.numberOfKeys() <= maxKeySize) {
                        // A-OK
                        break;
                    }                         
                    // Need to split up
                    split(node);
                    break;
                }
                // Navigate

                // Lesser or equal
                T lesser = node.getKey(0);
                if (value.compareTo(lesser) <= 0) {
                    node = node.getChild(0);
                    continue;
                }

                // Greater
                int numberOfKeys = node.numberOfKeys();
                int last = numberOfKeys - 1;
                T greater = node.getKey(last);
                if (value.compareTo(greater) > 0) {
                    node = node.getChild(numberOfKeys);
                    continue;
                }

                // Search internal nodes
                for (int i = 1; i < node.numberOfKeys(); i++) {
                    T prev = node.getKey(i - 1);
                    T next = node.getKey(i);
                    if (value.compareTo(prev) > 0 && value.compareTo(next) <= 0) {
                        node = node.getChild(i);
                        break;
                    }
                }
            }
        }

        size++;

        return true;
    }

    /**
     * The node's key size is greater than maxKeySize, split down the middle.
     * 
     * @param nodeToSplit
     *            to split.
     */
    private void split(Node<T> nodeToSplit) { //changed
        Node<T> node = nodeToSplit;
        int numberOfKeys = node.numberOfKeys();
        int medianIndex = numberOfKeys / 2;
        T medianValue = node.getKey(medianIndex);

        Node<T> left = new Node<T>(null, maxKeySize, maxChildrenSize);
        for (int i = 0; i < medianIndex; i++) {
            left.addKey(node.getKey(i));
        }
        if (node.numberOfChildren() > 0) {
            for (int j = 0; j <= medianIndex; j++) {
                Node<T> c = node.getChild(j);
                left.addChild(c);
            }
        }

        Node<T> right = new Node<T>(null, maxKeySize, maxChildrenSize);
        for (int i = medianIndex + 1; i < numberOfKeys; i++) {
            right.addKey(node.getKey(i));
        }
        if (node.numberOfChildren() > 0) {
            for (int j = medianIndex + 1; j < node.numberOfChildren(); j++) {
                Node<T> c = node.getChild(j);
                right.addChild(c);
            }
        }

        if (node.parent == null) {
            // new root, height of tree is increased
            Node<T> newRoot = new Node<T>(null, maxKeySize, maxChildrenSize);
            newRoot.addKey(medianValue);
            this.root = newRoot;
            newRoot.addChild(left);
            newRoot.addChild(right);
        } else {
            // Move the median value up to the parent
            Node<T> parent = node.parent;
            parent.addKey(medianValue);
            parent.removeChild(node);
            parent.addChild(left);
            parent.addChild(right);
        }
    }

    /**
     * {@inheritDoc}
     */
    public T remove(T value) {
        T removed = null;
        Node<T> node = this.getNode(value);
        removed = remove(value,node);
        return removed;
    }

    /**
     * Remove the value from the Node and check invariants
     * 
     * @param value
     *            T to remove from the tree
     * @param node
     *            Node to remove value from
     * @return True if value was removed from the tree.
     */
    private T remove(T value, Node<T> node) {
        if (node == null) return null;

        T removed = null;
        int index = node.indexOf(value);
        removed = node.removeKey(value);
        if (node.numberOfChildren() == 0) {
            // leaf node
            if (node.parent != null && node.numberOfKeys() < minKeySize) {
                this.combined(node);
            } else if (node.parent == null && node.numberOfKeys() == 0) {
                // Removing root node with no keys or children
                root = null;
            }
        } else {
            // internal node
            Node<T> lesser = node.getChild(index);
            Node<T> greatest = this.getGreatestNode(lesser);
            T replaceValue = this.removeGreatestValue(greatest);
            node.addKey(replaceValue);
            if (greatest.parent != null && greatest.numberOfKeys() < minKeySize) {
                this.combined(greatest);
            }
            if (greatest.numberOfChildren() > maxChildrenSize) {
                this.split(greatest);
            }
        }

        size--;

        return removed;
    }

    /**
     * Remove greatest valued key from node.
     * 
     * @param node
     *            to remove greatest value from.
     * @return value removed;
     */
    private T removeGreatestValue(Node<T> node) {
        T value = null;
        if (node.numberOfKeys() > 0) {
            value = node.removeKey(node.numberOfKeys() - 1);
        }
        return value;
    }

    /**
     * {@inheritDoc}
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * {@inheritDoc}
     */
    public boolean contains(T value) {
        Node<T> node = getNode(value);
        return (node != null);
    }

    /**
     * Get the node with value.
     * 
     * @param value
     *            to find in the tree.
     * @return Node<T> with value.
     */
    private Node<T> getNode(T value) {
        Node<T> node = root;
        while (node != null) {
            T lesser = node.getKey(0);
            if (value.compareTo(lesser) < 0) {
                if (node.numberOfChildren() > 0)
                    node = node.getChild(0);
                else
                    node = null;
                continue;
            }

            int numberOfKeys = node.numberOfKeys();
            int last = numberOfKeys - 1;
            T greater = node.getKey(last);
            if (value.compareTo(greater) > 0) {
                if (node.numberOfChildren() > numberOfKeys)
                    node = node.getChild(numberOfKeys);
                else
                    node = null;
                continue;
            }

            for (int i = 0; i < numberOfKeys; i++) {
                T currentValue = node.getKey(i);
                if (currentValue.compareTo(value) == 0) {
                    return node;
                }

                int next = i + 1;
                if (next <= last) {
                    T nextValue = node.getKey(next);
                    if (currentValue.compareTo(value) < 0 && nextValue.compareTo(value) > 0) {
                        if (next < node.numberOfChildren()) {
                            node = node.getChild(next);
                            break;
                        }
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Get the greatest valued child from node.
     * 
     * @param nodeToGet
     *            child with the greatest value.
     * @return Node<T> child with greatest value.
     */
    private Node<T> getGreatestNode(Node<T> nodeToGet) {
        Node<T> node = nodeToGet;
        while (node.numberOfChildren() > 0) {
            node = node.getChild(node.numberOfChildren() - 1);
        }
        return node;
    }

    /**
     * Combined children keys with parent when size is less than minKeySize.
     * 
     * @param node
     *            with children to combined.
     * @return True if combined successfully.
     */
    private boolean combined(Node<T> node) {
        Node<T> parent = node.parent;
        int index = parent.indexOf(node);
        int indexOfLeftNeighbor = index - 1;
        int indexOfRightNeighbor = index + 1;

        Node<T> leftNeighbor = null;
        int leftNeighborSize = -minChildrenSize;
        if (indexOfLeftNeighbor >= 0) {
            leftNeighbor = parent.getChild(indexOfLeftNeighbor);
            leftNeighborSize = leftNeighbor.numberOfKeys();
        }

        // Try to borrow neighbor
        if (leftNeighbor != null && leftNeighborSize > minKeySize) {
            // Try to borrow from left neighbor
            T removeValue = leftNeighbor.getKey(leftNeighborSize-1);
            int prev = getIndexOfPreviousValue(parent, removeValue) + 1;
            if (parent.keysSize == 1) prev = 0;
            T parentValue = parent.removeKey(prev);
            T neighborValue = leftNeighbor.removeKey(leftNeighborSize-1);
            node.addKey(parentValue);
            parent.addKey(neighborValue);
            if (leftNeighbor.numberOfChildren() > 0) {
                node.addChild(leftNeighbor.removeChild(leftNeighbor.numberOfChildren() - 1));
            }
        } else {
            Node<T> rightNeighbor = null;
            int rightNeighborSize = -minChildrenSize;
            if (indexOfRightNeighbor < parent.numberOfChildren()) {
                rightNeighbor = parent.getChild(indexOfRightNeighbor);
                rightNeighborSize = rightNeighbor.numberOfKeys();
            }

            if (rightNeighbor != null && rightNeighborSize > minKeySize) {
                // Try to borrow from right neighbor
                T removeValue = rightNeighbor.getKey(0);
                int prev = getIndexOfNextValue(parent, removeValue);
                T parentValue = parent.removeKey(prev);
                T neighborValue = rightNeighbor.removeKey(0);
                node.addKey(parentValue);
                parent.addKey(neighborValue);
                if (rightNeighbor.numberOfChildren() > 0) {
                    node.addChild(rightNeighbor.removeChild(0));
                }
            } else if (leftNeighbor != null && parent.numberOfKeys() > 0) {
                // Can't borrow from neighbors, try to combined with left neighbor
                T removeValue = leftNeighbor.getKey(leftNeighbor.numberOfKeys()-1);
                int prev = getIndexOfPreviousValue(parent, removeValue) + 1;
                if (parent.keysSize == 1) prev = 0;
                T parentValue = parent.removeKey(prev);
                parent.removeChild(leftNeighbor);
                node.addKey(parentValue);
                for (int i = 0; i < leftNeighbor.keysSize; i++) {
                    T v = leftNeighbor.getKey(i);
                    node.addKey(v);
                }
                for (int i = 0; i < leftNeighbor.childrenSize; i++) {
                    Node<T> c = leftNeighbor.getChild(i);
                    node.addChild(c);
                }

                if (parent.parent != null && parent.numberOfKeys() < minKeySize) {
                    // removing key made parent too small, combined up tree
                    this.combined(parent);
                } else if (parent.numberOfKeys() == 0) {
                    // parent no longer has keys, make this node the new root
                    // which decreases the height of the tree
                    node.parent = null;
                    root = node;
                }
            } else if (rightNeighbor != null && parent.numberOfKeys() > 0) {
                // Can't borrow from neighbors, try to combined with right neighbor
                T removeValue = rightNeighbor.getKey(0);
                int prev = getIndexOfNextValue(parent, removeValue);
                T parentValue = parent.removeKey(prev);
                parent.removeChild(rightNeighbor);
                node.addKey(parentValue);
                for (int i = 0; i < rightNeighbor.keysSize; i++) {
                    T v = rightNeighbor.getKey(i);
                    node.addKey(v);
                }
                for (int i = 0; i < rightNeighbor.childrenSize; i++) {
                    Node<T> c = rightNeighbor.getChild(i);
                    node.addChild(c);
                }

                if (parent.parent != null && parent.numberOfKeys() < minKeySize) {
                    // removing key made parent too small, combined up tree
                    this.combined(parent);
                } else if (parent.numberOfKeys() == 0) {
                    // parent no longer has keys, make this node the new root
                    // which decreases the height of the tree
                    node.parent = null;
                    root = node;
                }
            }
        }

        return true;
    }

    /**
     * Get the index of previous key in node.
     * 
     * @param node
     *            to find the previous key in.
     * @param value
     *            to find a previous value for.
     * @return index of previous key or -1 if not found.
     */
    private int getIndexOfPreviousValue(Node<T> node, T value) {
        for (int i = 1; i < node.numberOfKeys(); i++) {
            T t = node.getKey(i);
            if (t.compareTo(value) >= 0)
                return i - 1;
        }
        return node.numberOfKeys() - 1;
    }

    /**
     * Get the index of next key in node.
     * 
     * @param node
     *            to find the next key in.
     * @param value
     *            to find a next value for.
     * @return index of next key or -1 if not found.
     */
    private int getIndexOfNextValue(Node<T> node, T value) {
        for (int i = 0; i < node.numberOfKeys(); i++) {
            T t = node.getKey(i);
            if (t.compareTo(value) >= 0)
                return i;
        }
        return node.numberOfKeys() - 1;
    }

    /**
     * {@inheritDoc}
     */
    public int size() {
        return size;
    }

    /**
     * {@inheritDoc}
     */
    public boolean validate() {
        if (root == null) return true;
        return validateNode(root);
    }

    /**
     * Validate the node according to the B-Tree invariants.
     * 
     * @param node
     *            to validate.
     * @return True if valid.
     */
    private boolean validateNode(Node<T> node) {
        int keySize = node.numberOfKeys();
        if (keySize > 1) {
            // Make sure the keys are sorted
            for (int i = 1; i < keySize; i++) {
                T p = node.getKey(i - 1);
                T n = node.getKey(i);
                if (p.compareTo(n) > 0)
                    return false;
            }
        }
        int childrenSize = node.numberOfChildren();
        if (node.parent == null) {
            // root
            if (keySize > maxKeySize) {
                // check max key size. root does not have a min key size
                return false;
            } else if (childrenSize == 0) {
                // if root, no children, and keys are valid
                return true;
            } else if (childrenSize < 2) {
                // root should have zero or at least two children
                return false;
            } else if (childrenSize > maxChildrenSize) {
                return false;
            }
        } else {
            // non-root
            if (keySize < minKeySize) {
                return false;
            } else if (keySize > maxKeySize) {
                return false;
            } else if (childrenSize == 0) {
                return true;
            } else if (keySize != (childrenSize - 1)) {
                // If there are chilren, there should be one more child then
                // keys
                return false;
            } else if (childrenSize < minChildrenSize) {
                return false;
            } else if (childrenSize > maxChildrenSize) {
                return false;
            }
        }

        Node<T> first = node.getChild(0);
        // The first child's last key should be less than the node's first key
        if (first.getKey(first.numberOfKeys() - 1).compareTo(node.getKey(0)) > 0)
            return false;

        Node<T> last = node.getChild(node.numberOfChildren() - 1);
        // The last child's first key should be greater than the node's last key
        if (last.getKey(0).compareTo(node.getKey(node.numberOfKeys() - 1)) < 0)
            return false;

        // Check that each node's first and last key holds it's invariance
        for (int i = 1; i < node.numberOfKeys(); i++) {
            T p = node.getKey(i - 1);
            T n = node.getKey(i);
            Node<T> c = node.getChild(i);
            if (p.compareTo(c.getKey(0)) > 0)
                return false;
            if (n.compareTo(c.getKey(c.numberOfKeys() - 1)) < 0)
                return false;
        }

        for (int i = 0; i < node.childrenSize; i++) {
            Node<T> c = node.getChild(i);
            boolean valid = this.validateNode(c);
            if (!valid)
                return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return TreePrinter.getString(this);
    }
    
    
    private static class Node<T extends Comparable<T>> {

        private T[] keys = null;
        private int keysSize = 0;
        private Node<T>[] children = null;
        private int childrenSize = 0;
        private Comparator<Node<T>> comparator = new Comparator<Node<T>>() {
            public int compare(Node<T> arg0, Node<T> arg1) {
                return arg0.getKey(0).compareTo(arg1.getKey(0));
            }
        };

        protected Node<T> parent = null;

        private Node(Node<T> parent, int maxKeySize, int maxChildrenSize) {
            this.parent = parent;
            this.keys = (T[]) new Comparable[maxKeySize+1]; // maybe [maxKeySize] only   ??????????????????????
            this.keysSize = 0;
            this.children = new Node[maxChildrenSize+1]; // maybe [maxChildrenSize] only  ??????????????????????
            this.childrenSize = 0;
        }

        private T getKey(int index) {
            return keys[index];
        }

        private int indexOf(T value) {
            for (int i = 0; i < keysSize; i++) {
                if (keys[i].equals(value)) return i;
            }
            return -1;
        }

        private void addKey(T value) {
            keys[keysSize++] = value;
            Arrays.sort(keys, 0, keysSize);
        }

        private T removeKey(T value) {
            T removed = null;
            boolean found = false;
            if (keysSize == 0) return null;
            for (int i = 0; i < keysSize; i++) {
                if (keys[i].equals(value)) {
                    found = true;
                    removed = keys[i];
                } else if (found) {
                    // shift the rest of the keys down
                    keys[i - 1] = keys[i];
                }
            }
            if (found) {
                keysSize--;
                keys[keysSize] = null;
            }
            return removed;
        }

        private T removeKey(int index) {
            if (index >= keysSize)
                return null;
            T value = keys[index];
            for (int i = index + 1; i < keysSize; i++) {
                // shift the rest of the keys down
                keys[i - 1] = keys[i];
            }
            keysSize--;
            keys[keysSize] = null;
            return value;
        }

        private int numberOfKeys() {
            return keysSize;
        }

        private Node<T> getChild(int index) {
            if (index >= childrenSize)
                return null;
            return children[index];
        }

        private int indexOf(Node<T> child) {
            for (int i = 0; i < childrenSize; i++) {
                if (children[i].equals(child))
                    return i;
            }
            return -1;
        }

        private boolean addChild(Node<T> child) {
            child.parent = this;
            children[childrenSize++] = child;
            Arrays.sort(children, 0, childrenSize, comparator);
            return true;
        }

        private boolean removeChild(Node<T> child) {
            boolean found = false;
            if (childrenSize == 0)
                return found;
            for (int i = 0; i < childrenSize; i++) {
                if (children[i].equals(child)) {
                    found = true;
                } else if (found) {
                    // shift the rest of the keys down
                    children[i - 1] = children[i];
                }
            }
            if (found) {
                childrenSize--;
                children[childrenSize] = null;
            }
            return found;
        }

        private Node<T> removeChild(int index) {
            if (index >= childrenSize)
                return null;
            Node<T> value = children[index];
            children[index] = null;
            for (int i = index + 1; i < childrenSize; i++) {
                // shift the rest of the keys down
                children[i - 1] = children[i];
            }
            childrenSize--;
            children[childrenSize] = null;
            return value;
        }

        private int numberOfChildren() {
            return childrenSize;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();

            builder.append("keys=[");
            for (int i = 0; i < numberOfKeys(); i++) {
                T value = getKey(i);
                builder.append(value);
                if (i < numberOfKeys() - 1)
                    builder.append(", ");
            }
            builder.append("]\n");

            if (parent != null) {
                builder.append("parent=[");
                for (int i = 0; i < parent.numberOfKeys(); i++) {
                    T value = parent.getKey(i);
                    builder.append(value);
                    if (i < parent.numberOfKeys() - 1)
                        builder.append(", ");
                }
                builder.append("]\n");
            }

            if (children != null) {
                builder.append("keySize=").append(numberOfKeys()).append(" children=").append(numberOfChildren()).append("\n");
            }

            return builder.toString();
        }
    }

    private static class TreePrinter {

        public static <T extends Comparable<T>> String getString(BTree<T> tree) {
            if (tree.root == null) return "Tree has no nodes.";
            return getString(tree.root, "", true);
        }

        private static <T extends Comparable<T>> String getString(Node<T> node, String prefix, boolean isTail) {
            StringBuilder builder = new StringBuilder();

            builder.append(prefix).append((isTail ? "└── " : "├── "));
            for (int i = 0; i < node.numberOfKeys(); i++) {
                T value = node.getKey(i);
                builder.append(value);
                if (i < node.numberOfKeys() - 1)
                    builder.append(", ");
            }
            builder.append("\n");

            if (node.children != null) {
                for (int i = 0; i < node.numberOfChildren() - 1; i++) {
                    Node<T> obj = node.getChild(i);
                    builder.append(getString(obj, prefix + (isTail ? "    " : "│   "), false));
                }
                if (node.numberOfChildren() >= 1) {
                    Node<T> obj = node.getChild(node.numberOfChildren() - 1);
                    builder.append(getString(obj, prefix + (isTail ? "    " : "│   "), true));
                }
            }

            return builder.toString();
        }
    }

}