package edu.csupomona.cs.cs241.prog_assgmnt_2;

/**
 * Created by Eric on 5/24/2015.
 */


/**
 * This class creates a node that is meant to be used in a binary tree structure.
 * It has a pointer to the parent of the node which allows for traversal of the tree upwards.
 * It is written to hold any type of object as the data of the node.
 *
 * @author Eric Rensel
 * Created by Eric on 4/20/2015.
 */
public class RB_Node<K extends Comparable<K>, V> {

    /**
     * data holds a generic object in the node
     */
    protected K key;
    protected V data;
    /**
     * this enum defines the colors
     */
    protected Tree.RB_Color color;
    /**
     * these three fields hold pointers to the children and parent of the current node
     */
    private RB_Node<K,V> rightChild;
    private RB_Node<K,V> leftChild;
    private RB_Node<K,V> parent;

    /**
     * default constructor for a nod
     */
    public RB_Node(){
        this.data = null;
        this.key = null;
        rightChild = null;
        leftChild = null;
        parent = null;
        color = color.BLACK;
    }

    /**
     * constructor for a node that is given a key a value
     * @param key
     * @param data
     */
    public RB_Node(K key,V data){
        this.data = data;
        this.key = key;
        rightChild = null;
        leftChild = null;
        parent = null;
        color = color.BLACK;
    }

    /**
     * constructor for a node that is null but is provided a parent
     * @param p
     */
    public RB_Node(RB_Node p){
        this.data = null;
        this.key = null;
        rightChild = null;
        leftChild = null;
        parent = p;
        color = color.BLACK;
    }

    /**
     * returns the right child of the node
     * @return
     */
    public RB_Node<K,V> getRightChild(){
        return rightChild;
    }

    /**
     * Returns the left child of the node
     * @return of type {@link RB_Node}
     */
    public RB_Node<K,V> getLeftChild(){
        return leftChild;
    }

    /**
     * Returns the parent of the node
     * @return of type {@link RB_Node}
     */
    public RB_Node<K,V> getParent(){
        return parent;
    }

    /**
     * returns the uncle of the node provided
     * @return
     */
    public RB_Node<K,V> getUncle(){
        RB_Node<K,V> parent = this.getParent();
        RB_Node<K,V> temp = parent.getParent();
        if(temp.getLeftChild() == parent)
            temp = temp.getRightChild();
        else
            temp = temp.getLeftChild();
        return temp;
    }

    /**
     * returns the sibling of the node provided
     * @return
     */
    public RB_Node<K,V> getSibling(){
        if(this.parent == null){
            return null;
        }
        else if(this.isLeftChild()){
            return parent.getRightChild();
        }
        else{
            return parent.getLeftChild();
        }
    }

    /**
     * Sets the right child of the node
     * @param in of type {@link RB_Node}
     */
    public void setRightChild(RB_Node<K,V> in){
        rightChild = in;
    }

    /**
     * Sets the left child of the node
     * @param in of type {@link RB_Node}
     */
    public void setLeftChild(RB_Node<K,V> in){
        leftChild = in;
    }

    /**
     * Sets the parent of the node
     * @param in of type {@link RB_Node}
     */
    public void setParent(RB_Node<K,V> in){
        parent = in;
    }

    /**
     * sets the color of the node
     * @param in
     */
    public void setColor(Tree.RB_Color in){
        color = in;
    }

    /**
     * returns true if node is a left child, false if right child
     * @return
     */
    public boolean isLeftChild(){
        if(this.getParent() == null)
            return false;
        RB_Node<K,V> parent = getParent();
        if(parent.getLeftChild() == this)
            return true;
        else return false;
    }

    /**
     * returns true if the node is an inner node relative to its grandparent.
     * @return
     */
    public boolean isInner(){
        if(this.isLeftChild() && this.getParent().isLeftChild()){
            return false;
        }
        if(!this.isLeftChild() && !this.getParent().isLeftChild()){
            return false;
        }
        else
            return true;
    }

    /**
     * returns true if the node has any non-null(key) children
     * @return
     */
    public boolean hasChild(){
        if(this.key == null)
            return false;
        else if(this.getLeftChild().key != null || this.getRightChild().key != null){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * returns true if the nod has any red children
     * @return
     */
    public boolean hasRedChild(){
        if(this.key == null) {
            return false;
        }
        else if(this.getLeftChild().color == Tree.RB_Color.RED || this.getRightChild().color == Tree.RB_Color.RED) {
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * returns true if the node is red
     * @return
     */
    public boolean isRed(){
        if(this.color == Tree.RB_Color.RED){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * returns true if the node has a null key
     * @return
     */
    public boolean isNill(){
        if(this.key == null) {
            return true;
        }
        else{
            return false;
        }
    }
    //to check if a node is an locally left inner node, it must be a right child and be an inner node.
    //if it is a right inner node, it must be a left child and be an inner node.
    //outer nodes will return false on is inner, if it is a left child its on the left side of the tree, etc.

    /**
     * makes the node into a null black node
     */
    public void makeNull(){
        this.key = null;
        this.data = null;
        this.rightChild = null;
        this.leftChild = null;
        this.color = Tree.RB_Color.BLACK;
    }

    /**
     * returns a letter representing the color of the node
     * R for red, B for black.
     * @return
     */
    public String colorString(){
        String result = "";
        if(color == Tree.RB_Color.BLACK)
            result = "B";
        else
            result = "R";
        return result;

    }

}

