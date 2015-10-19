package edu.csupomona.cs.cs241.prog_assgmnt_2;

import java.util.ArrayList;

/**
 * Assignement 2
 * Red Black Tree
 * CS 241 - Edwin Rodriguez
 *
 *
 * Created by Eric Rensel on 5/29/2015.
 * Finally got this working! I think if it can handle 800000 random keys I think it can handle anything!
 * This is a Red Black Tree that holds nodes with keys and values. The tree will always have a good degree of balance.
 *
 */
public class RB_Tree <K extends Comparable<K>, V> {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_WHITE = "\u001B[37m";

    protected RB_Node<K, V> root;

    protected int count = 0;

    /**
     * Default constructor, initializes null tree
     */
    public RB_Tree() {
        root = null;
        count = 0;
    }

    /**
     * adds a node to the tree with the key and value entered.
     * Calls fixInsert() to perform rotations when nodes are added to ensure that the tree remanains balanced.
     * @param key
     * @param val
     */
    public void insert(K key, V val) {
        RB_Node<K, V> newNode = new RB_Node<>(key, val);
        newNode.setLeftChild(new RB_Node<>(newNode));
        newNode.setRightChild(new RB_Node<>(newNode));
        newNode.setColor(Tree.RB_Color.RED);
        //CASE 0
        if (root == null) {
            root = newNode;
            root.setColor(Tree.RB_Color.BLACK);
            count++;
        } else {
            RB_Node<K, V> currNode = root;
            boolean added = false;
            while (!added) {
                if (key.compareTo(currNode.key) < 0) {
                    if (currNode.getLeftChild().key == null) {
                        currNode.setLeftChild(newNode);
                        currNode.getLeftChild().setParent(currNode);
                        added = true;
                        count++;
                    } else
                        currNode = currNode.getLeftChild();
                } else {
                    if (currNode.getRightChild().key == null) {
                        currNode.setRightChild(newNode);
                        currNode.getRightChild().setParent(currNode);
                        added = true;
                        count++;
                    } else
                        currNode = currNode.getRightChild();
                }
            }
            fixInsert(newNode);

        }
    }

    /**
     * rotates the tree in various ways and performs recolerings to ensure that the tree ramians balanced
     * @param newNode - the node to be fixed
     */
    private void fixInsert(RB_Node<K, V> newNode) {
        //RB_Node<K,V> uncle = newNode.getUncle();
        RB_Node<K, V> parent = newNode.getParent();
        RB_Node<K, V> grandParent = parent.getParent();
        //case 1, parent of node added is black
        if (newNode.getParent().color == Tree.RB_Color.BLACK) {
            return;
        }
        //case 2, parent of node is red, uncle is red
        RB_Node<K, V> uncle = newNode.getUncle();
        if (parent.color == Tree.RB_Color.RED && uncle.color == Tree.RB_Color.RED) {
            parent.setColor(Tree.RB_Color.BLACK);
            uncle.setColor(Tree.RB_Color.BLACK);
            grandParent.setColor(Tree.RB_Color.RED);
            //make sure that you havent broken invariant 4 "red nodes cant have red children"
            if (grandParent.getParent() != null) {
                if (grandParent.getParent().color == Tree.RB_Color.RED) {
                    fixInsert(grandParent);
                }
            }
        }
        //case 3, parent is red, uncle is black
        else if (parent.color == Tree.RB_Color.RED && uncle.color == Tree.RB_Color.BLACK) {
            if (newNode.isInner()) {
                //this is the case that the node is an inner node and on the right side of the subtree
                if (newNode.isLeftChild()) {
                    rotateRight(newNode.getParent());
                    rotateLeft(newNode.getParent());
                    newNode.setColor(Tree.RB_Color.BLACK);
                    newNode.getLeftChild().setColor(Tree.RB_Color.RED);
                }
                //this is the case that the node is inner and on the left side of the subtree
                else {
                    rotateLeft(newNode.getParent());
                    rotateRight(newNode.getParent());
                    newNode.setColor(Tree.RB_Color.BLACK);
                    newNode.getRightChild().setColor(Tree.RB_Color.RED);
                }
            }
            //if its an outer node
            else {
                //this is the case that the node is on the left side of the subtree
                if (newNode.isLeftChild()) {
                    rotateRight(newNode.getParent().getParent());
                    //now that newNode's parent is the "root" of the subtree
                    newNode.getParent().setColor(Tree.RB_Color.BLACK);
                    newNode.getParent().getRightChild().setColor(Tree.RB_Color.RED);
                }
                //this is the case that the node is on the right side of the subtree
                else {
                    rotateLeft(newNode.getParent().getParent());
                    //now that newNode's parent is the "root" of the subtree
                    newNode.getParent().setColor(Tree.RB_Color.BLACK);
                    newNode.getParent().getLeftChild().setColor(Tree.RB_Color.RED);
                }
            }
        }
        //make sure the root stays black
        root.setColor(Tree.RB_Color.BLACK);
    }

    /**
     * lookup finds a node if it matches the key provided. It will find the return the
     * value of the first matching node found. If no node is found, it returns null.
     * @param rKey
     * @return
     */
    public V lookup(K rKey) {
        V temp = null;
        boolean found = false;
        //case 0
        if (root == null) {
            temp = null;
        } else {
            RB_Node<K, V> ptr = root;
            while (ptr.key != null) {
                if (ptr.key.compareTo(rKey) == 0) {
                    found = true;
                    break;
                } else if (ptr.key.compareTo(rKey) < 0) {
                    ptr = ptr.getRightChild();
                } else {
                    ptr = ptr.getLeftChild();
                }
            }
            if (found) {
                temp = ptr.data;
                //find inorder successor
                RB_Node<K, V> successor = ptr;
                if (successor.getRightChild().key != null) {
                    successor = successor.getRightChild();
                    while (successor.getLeftChild().key != null) {
                        successor = successor.getLeftChild();
                    }
                } else if (successor.getLeftChild().key != null) { //no right children, but left child
                    successor = successor.getLeftChild();
                }
            } else {
                //node not found
                return null;
            }
        }
        return temp;
    }

    /**
     * remove deletes an element of the tree the same way that a binary search tree does and then
     * but calls a method called safeDelete() to safely remove the node without breaking the invariants
     * of the red black tree.
     * @param rKey
     * @return
     */
    public V remove(K rKey) {
        V temp = null;
        boolean found = false;
        //case 0
        if (root == null) {
            temp = null;
        } else if (count == 1) {
            temp = root.data;
            root = null;
            //count--;
        } else if (count > 1) {
            RB_Node<K, V> ptr = root;
            while (ptr.key != null) {
                if (ptr.key.compareTo(rKey) == 0) {
                    found = true;
                    break;
                } else if (ptr.key.compareTo(rKey) < 0) {
                    ptr = ptr.getRightChild();
                } else {
                    ptr = ptr.getLeftChild();
                }
            }
            if (found) {
                temp = ptr.data;
                //find inorder successor
                RB_Node<K, V> successor = ptr;
                if (successor.getRightChild().key != null) {
                    successor = successor.getRightChild();
                    while (successor.getLeftChild().key != null) {
                        successor = successor.getLeftChild();
                    }
                    swap(ptr, successor);
                } else if (successor.getLeftChild().key != null) { //no right children, but left child
                    successor = successor.getLeftChild();
                    swap(ptr, successor);
                }
                safeDelete(successor);
                count--;
            } else {
                //node not found
                return null;
            }
        }
        return temp;
    }

    /**
     * this method deletes the node safely, which means that it will handle the case that
     * a black node was removed from the tree. If a black node was removed then the method
     * fixDoubleBlack() will be called to fix the error.
     * @param node
     */
    private void safeDelete(RB_Node<K, V> node) {
        RB_Node<K, V> parent = node.getParent();
        //node is red and has no children
        if (node.isRed() && !node.hasChild()) {
            node.makeNull();
            //count--;
        }
        //black and no children
        else if (!node.isRed() && !node.hasChild()) {
            node.makeNull();
            fixDoubleBlack(node);
            //count--;
        }

        //has a non null child
        else if (node.hasChild()) {
            //left child is null
            if (node.getLeftChild().isNill()) {
                RB_Node<K, V> newNode = node.getRightChild();
                //node is a left child
                if (node.isLeftChild()) {
                    parent.setLeftChild(newNode);
                    newNode.setParent(node.getParent());
                    //count--;
                }
                //node is a right child
                else {
                    parent.setRightChild(newNode);
                    newNode.setParent(node.getParent());
                    //count--;
                }
                if (!node.isRed()) {
                    fixDoubleBlack(newNode);
                }
            }
            //right child is null
            else {
                RB_Node<K, V> newNode = node.getLeftChild();
                //node is a left child
                if (node.isLeftChild()) {
                    parent.setLeftChild(newNode);
                    newNode.setParent(node.getParent());
                    //count--;
                }
                //node is a right child
                else {
                    parent.setRightChild(newNode);
                    newNode.setParent(node.getParent());
                    //count--;
                }
                if (!node.isRed()) {
                    fixDoubleBlack(newNode);
                } else {
                    newNode.setColor(Tree.RB_Color.BLACK);
                }
            }

        }

        //decrement count after deletion
        //count--;
    }

    /**
     * the name of this method stems from the fact that the node being fixed would need
     * to be counted twice for the tree to be balanced. The method performs recolorings and
     * rotations to add a black node to the double-black node's side, or adds a black node to the
     * siblings side.
     * @param node
     */
    private void fixDoubleBlack(RB_Node<K, V> node) {
        if (node == root) {
            node.color = Tree.RB_Color.BLACK;
            return;
        }
        RB_Node<K, V> sibling = node.getSibling();
        //sibling is black and has black children
        if (!sibling.isRed() && !sibling.hasRedChild()) {
            sibling.color = Tree.RB_Color.RED;
            if (node.getParent().color == Tree.RB_Color.BLACK) {
                fixDoubleBlack(node.getParent());
            } else
                node.getParent().color = Tree.RB_Color.BLACK;
        }
        //sibling is red
        else if (sibling.isRed()) {
            if (node.isLeftChild()) {
                rotateLeft(node.getParent());
            } else
                rotateRight(node.getParent());
            sibling.setColor(Tree.RB_Color.BLACK);
            node.getParent().setColor(Tree.RB_Color.RED);
            fixDoubleBlack(node);
        }
        //sibling is black and has red child
        else {
            //node is left child
            if (node.isLeftChild()) {
                //sibling has right (outer) red child
                if (node.getSibling().getRightChild().isRed()) {
                    sibling.color = node.getParent().color;
                    node.getParent().color = Tree.RB_Color.BLACK;
                    sibling.getRightChild().color = Tree.RB_Color.BLACK;
                    rotateLeft(node.getParent());
                } else if (node.getSibling().getLeftChild().isRed()) { //sibling has left red child
                    sibling.getLeftChild().color = Tree.RB_Color.BLACK;
                    sibling.color = Tree.RB_Color.RED;
                    rotateRight(sibling);
                    fixDoubleBlack(node);
                }
            }
            //node is right child
            else {
                //sibling has left (outer) red right child
                if (node.getSibling().getLeftChild().isRed()) {
                    sibling.color = node.getParent().color;
                    node.getParent().color = Tree.RB_Color.BLACK;
                    sibling.getLeftChild().color = Tree.RB_Color.BLACK;
                    rotateRight(node.getParent());
                } else if (node.getSibling().getRightChild().isRed()) { //sibling has left red child
                    sibling.getRightChild().color = Tree.RB_Color.BLACK;
                    sibling.color = Tree.RB_Color.RED;
                    rotateLeft(sibling);
                    fixDoubleBlack(node);
                }
            }
        }
    }

    /**
     * Performs a left rotation on the node that is provided. The "pivot" in a visual sense would
     * actually be underneath the node that rotate is called on. "grandpivot" is the parent
     * of the pivot.
     * @param pivot
     */
    private void rotateLeft(RB_Node<K, V> pivot) {
        RB_Node<K, V> grandPivot = pivot.getParent();
        RB_Node<K, V> newPivot = pivot.getRightChild();
        RB_Node<K, V> adopted = newPivot.getLeftChild();
        //the right child gets pivots parent as a parent
        newPivot.setParent(grandPivot);
        //if the grand pivot is null, the old pivot must be the root, so set the root as the newpivot
        //if pivot is a left child, set its right child(new pivot) as the left child of the pivot parent
        //else set the new pivot as a right child
        if (grandPivot == null) {
            root = newPivot;
        } else if (pivot.isLeftChild()) {
            grandPivot.setLeftChild(newPivot);
        } else {
            grandPivot.setRightChild(newPivot);
        }
        //the old pivot gets the new pivot as a parent
        pivot.setParent(newPivot);
        //the old pivot gets the new pivots old left child
        pivot.setRightChild(adopted);
        adopted.setParent(pivot);
        //the new pivots left child is set to the old pivot
        newPivot.setLeftChild(pivot);
    }

    /**
     * Performs a right rotation on the node that is provided. The "pivot" in a visual sense would
     * actually be underneath the node that rotate is called on. "grandpivot" is the parent
     * of the pivot.
     *
     * @param pivot
     */
    private void rotateRight(RB_Node<K, V> pivot) {
        RB_Node<K, V> grandPivot = pivot.getParent();
        RB_Node<K, V> newPivot = pivot.getLeftChild();
        RB_Node<K, V> adopted = newPivot.getRightChild();
        //the right child gets pivots parent as a parent
        newPivot.setParent(grandPivot);
        //if the grand pivot is null, the old pivot must be the root, so set the root as the newpivot
        //if pivot is a left child, set its right child(new pivot) as the left child of the pivot parent
        //else set the new pivot as a right child
        if (grandPivot == null) {
            root = newPivot;
        } else if (pivot.isLeftChild()) {
            grandPivot.setLeftChild(newPivot);
        } else {
            grandPivot.setRightChild(newPivot);
        }
        //the old pivot gets the new pivot as a parent
        pivot.setParent(newPivot);
        //the old pivot gets the new pivots old right child
        pivot.setLeftChild(adopted);
        adopted.setParent(pivot);
        //the new pivots left child is set to the old pivot
        newPivot.setRightChild(pivot);
    }

    /**
     * swaps keys between the nodes entered as parameters. I eleced to do this instead of
     * just copying the key needed so I could debug more easily.
     * @param n1
     * @param n2
     */
    private void swap(RB_Node<K, V> n1, RB_Node<K, V> n2) {
        K temp = n1.key;
        n1.key = n2.key;
        n2.key = temp;
    }

    /**
     * Finds the max depth of the tree, useful for printing purposes.
     * @param node
     * @return
     */
    public int maxDepth(RB_Node<K, V> node) {
        if (node.key == null) {
            return 1;
        } else {
            int leftH = maxDepth(node.getLeftChild());
            int rightH = maxDepth(node.getRightChild());
            if (leftH > rightH)
                return leftH + 1;
            else
                return rightH + 1;
        }
    }

    /**
     * returns the current row that the node would be on if it was printed visually
     *
     * @param node
     * @return
     */
    public int getBreadthRow(RB_Node<K, V> node) {
        int row = 0;
        while (node.getParent() != null) {
            row++;
            node = node.getParent();
        }
        return row;
    }

    /**
     * returns a string that shows the tree in pyramid style. It keeps track of null
     * black nodes and adds null children to that node. If a null node has a null parent
     * it will print as an empty space. This creates the correct gaps in the tree when nodes
     * are not present, so that nodes are always under the correct parent. R and B represent
     * red and black respectively in the output,
     *
     * @return
     */
    public String toPrettyString() {
        String result = "";
        ArrayList<RB_Node> queue = new ArrayList<RB_Node>(255);
        ArrayList<Integer> rowQueue = new ArrayList<>(255);
        RB_Node<K, V> node;
        int depth = maxDepth(root);
        int currPad = depth * 10;
        int prevPad = currPad;
        queue.add(root);
        int currRow = getBreadthRow(root);
        int prevRow = currRow;
        int nodes = 0;
        //rowQueue.add(getBreadthRow(root));
        while (!queue.isEmpty()) {
            node = queue.remove(0);
            nodes++;
            currRow = getBreadthRow(node);
            if (currRow != prevRow) {
                result += "\n";
                prevPad = currPad;
                currPad /= (int) 2;
            }
            if (!node.isNill()) {
                queue.add(node.getLeftChild());
                queue.add(node.getRightChild());
            } else if (currRow != maxDepth(root) - 1) {
                queue.add(new RB_Node(node));
                queue.add(new RB_Node(node));
            }
            if (currRow != prevRow) {
                result += addPadding(currPad);
            } else {
                result += addPadding(prevPad);
            }
            if (node.isNill()) {
                if (node.getParent().isNill()) {
                    result += " ";
                } else {
                    result += "X";
                }
            } else {
                result += node.key + node.colorString();
            }

            prevRow = currRow;
        }
        return result;
    }

    /**
     * add padding is used by prettyprint to add mltiple spaces easily.
     * @param n
     * @return
     */
    private String addPadding(int n) {
        String result = "";
        for (int i = 0; i < n; i++) {
            result += " ";
        }
        return result;
    }
}
