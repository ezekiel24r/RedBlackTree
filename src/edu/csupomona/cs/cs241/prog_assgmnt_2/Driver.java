package edu.csupomona.cs.cs241.prog_assgmnt_2;

import java.util.Random;

/**
 * Created by Eric on 5/29/2015.
 * A simple driver program that adds random numbers to the RBT and deletes them.
 * the numbers can be easily changed so that thousand of values can be added and removed.
 * Tested up to 800000 and still works
 */



public class Driver {

    //change these values to add an remove a large amount of values.
    static public int addedVals = 800; //the amount to be added
    static public int subVals = 790; //the amount to then be removed

    public static void main(String[] args) {
        RB_Tree<Integer, Integer> rbt = new RB_Tree<>();
        Random rand = new Random();
        rand.setSeed(System.currentTimeMillis());
        Integer [] nArr = new Integer [addedVals];
        for(int i = 0; i<addedVals; i++){
            Integer k = rand.nextInt(63 + 1);
            nArr[i] = k;
            rbt.insert(k, rand.nextInt(99)+1);
        }
        for(int i = 0; i<subVals; i++){
            rbt.remove(nArr[i]);
        }


        System.out.println(rbt.toPrettyString());
        System.out.println();
        System.out.println("Count: " + rbt.count);

        rbt.insert(50, 99);
        System.out.println(rbt.toPrettyString());
        System.out.println("removing node with key 50 returns: " + rbt.remove(50));
        System.out.println();
        System.out.println(rbt.toPrettyString());


        //BTreePrinter.printNode(rbt.root);
        //System.out.println(rbt.verify(rbt.root));
        //rbt.myPString();
    }



    //END OF CODE WRITTEN BY ME
    //daniels error checking code
    //used to check if a tree is valid
    //THIS IS NOT USED BY THE RB_TREE CLASS AT ALL! ONLY OUTSIDE OF IT BY THE DRIVER PROGRAM TO TEST
    //WRITTEN BY DANIEL GAMBOA
    public static int verify(RB_Node node) { // node will typically refer to the current node we are on

        int lh, rh; // left height & right height

        if (node.key == null) {
            return 1;
        } else {
            RB_Node ln = node.getLeftChild();
            RB_Node rn = node.getRightChild();

            if (node.color == Tree.RB_Color.RED) { // Consecutive red nodes
                if ((ln.color == Tree.RB_Color.RED) || (rn.color == Tree.RB_Color.RED)) {
                    System.out.println("Red Violation (#4: Children of a red node are black)");
                    return 0;
                }
            }

            lh = verify(ln);
            rh = verify(rn);

            if ((ln.key != null && ln.key.compareTo(node.key) > 0) || (rn.key != null && rn.key.compareTo(node.key) < 0)) {	// Invalid binary search tree
                System.out.println("Binary Tree Violation");
                return 0;
            }

            if (lh != 0 && rh != 0 && lh != rh) { // Black height mismatch
                System.out.println("Black Violation (#5: Any path from root to leaf contains the same number of black nodes.");
                return 0;
            }

            // Only count black links
            if (lh != 0 && rh != 0) {
                // Additional Note: Subtract 1 from the number returned because NIL's color is black
                return (node.color == Tree.RB_Color.RED) ? lh : lh + 1; // FIXME maybe I should remove the +1
            } else {
                System.out.println("Invalid");
                return 0;
            }
        }
    }
}
