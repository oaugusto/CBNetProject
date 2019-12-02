package projects.flatnet.nodes.nodeImplementations;

import java.util.HashMap;
import java.util.HashSet;

import projects.flatnet.nodes.messages.FlatNetMessage;
import projects.flatnet.nodes.tableEntry.FlatInfo;

/**
 * RotationLayer
 */
public abstract class RotationLayer extends ClusterLayer {

    private boolean rotating;
    private boolean routing;

    private FlatNetMessage flatnetMessage;


    @Override
    public void init() {
        super.init();

        this.rotating = false;
        this.routing = false;
        this.flatnetMessage = null;
    }

    @Override
    public void timeslot0() {
        this.tryOperation();
    }

    public void tryOperation() {

        if (this.hasFlatNetMessage()) {
            // System.out.println("Node (" + ID + "): has new flatnet message in buffer");

            this.flatnetMessage = this.getTopFlatNetMessage();
            if(this.ID == this.flatnetMessage.getDst()){
                return;
            }

            if (this.getDirection(flatnetMessage.getDst()) != 0) {
                this.sendRequestClusterTopDown(ID, flatnetMessage.getSrc(), flatnetMessage.getDst(),
                    flatnetMessage.getPriority());
            } else {
                this.sendRequestClusterBottomUp(ID, flatnetMessage.getSrc(), flatnetMessage.getDst(), 
                    flatnetMessage.getPriority());
            }
        }

    }

    @Override
    public void clusterCompletedBottomUp(HashMap<String, FlatInfo> cluster) {

        this.removeTopFlatNetMesssage();
        this.rotateBottomUp(cluster);
        // System.out.println("Cluster formed bottom up at node " + ID);
    }

    @Override
    public void clusterCompletedTopDown(HashMap<String, FlatInfo> cluster) {

        this.removeTopFlatNetMesssage();
        this.rotateTopDown(cluster);
        // System.out.println("Cluster formed top down at node " + ID);
    }

    @Override
    public void targetNodeFound(FlatInfo target) {

        this.removeTopFlatNetMesssage();
        this.flatnetMessage.incrementRouting(); // DATA LOG
        this.forwardFlatNetMessage(target.getNode().ID, this.flatnetMessage);
        // System.out.println("Cluster target found at node " + ID);
    }

    private void rotateBottomUp(HashMap<String, FlatInfo> cluster) {
        FlatInfo xInfo = cluster.get("x");
        FlatInfo yInfo = cluster.get("y");
        FlatInfo zInfo = cluster.get("z");
        
        this.rotating = true;

        this.flatnetMessage.incrementRotations();

        if (cluster.size() == 3) {
            this.zigBottomUp(xInfo, yInfo, zInfo);
        } else {
            this.flatnetMessage.incrementRotations();
            FlatInfo wInfo = cluster.get("w");
            FlatTreeLayer x = (FlatTreeLayer) xInfo.getNode();
            FlatTreeLayer y = (FlatTreeLayer) yInfo.getNode();

            if ((x == yInfo.getLeftChild() && y == zInfo.getLeftChild())
                    || (x == yInfo.getRightChild() && y == zInfo.getRightChild())) {
                this.zigZigBottomUp(xInfo, yInfo, zInfo, wInfo);
            } else {
                this.zigZagBottomUp(xInfo, yInfo, zInfo, wInfo);
            }
        }

    }

    /* DONE
                  z                     z
                 /                    /
                y                   *x
              /   \               /   \
            *x     c     ->      a     y
            / \                       / \
           a   b                     b   c
	 
	 */
    private void zigBottomUp(FlatInfo xInfo, FlatInfo yInfo, FlatInfo zInfo) {
        // System.out.println("Node (" + ID + "): doing zigBottomUp");

        FlatTreeLayer x = (FlatTreeLayer) xInfo.getNode();
        FlatTreeLayer y = (FlatTreeLayer) yInfo.getNode();
        FlatTreeLayer z = (FlatTreeLayer) zInfo.getNode();
        FlatTreeLayer dfChild = (FlatTreeLayer) x.getDeferredChild();

        if(yInfo.getLeftChild() == x){
            this.requestRPCTo(y.ID, "changeLeftChildTo", dfChild);
            if(x.getLeftChild() == dfChild){
                this.requestRPCTo(x.ID, "changeLeftChildTo", y);
                this.updateDescendants("setLeftDescendants", yInfo.getRightDescendants(), x.getLeftDescendants(), y.ID);
                this.requestRPCTo(y.ID, "setLeftDescendants", x.getLeftDescendants());
            }else{
                this.requestRPCTo(x.ID, "changeRightChildTo", y);
                this.updateDescendants("setRightDescendants", yInfo.getRightDescendants(), x.getRightDescendants(), y.ID);
                this.requestRPCTo(y.ID, "setLeftDescendants", x.getRightDescendants());
            }
        }else{
            this.requestRPCTo(y.ID, "changeRightChildTo", dfChild);
            if(x.getLeftChild() == dfChild){
                this.requestRPCTo(x.ID, "changeLeftChildTo", y);
                this.updateDescendants("setLeftDescendants", yInfo.getLeftDescendants(), x.getLeftDescendants(), y.ID);                
                this.requestRPCTo(y.ID, "setRightDescendants", x.getLeftDescendants());
            }else{
                this.requestRPCTo(x.ID, "changeRightChildTo", y);
                this.updateDescendants("setRightDescendants", yInfo.getLeftDescendants(), x.getRightDescendants(), y.ID);
                this.requestRPCTo(y.ID, "setRightDescendants", x.getRightDescendants());
            }
        }
        if(zInfo.getLeftChild() == y){
            this.requestRPCTo(z.ID, "updateLeftChildTo", x);
        }else { 
            this.requestRPCTo(z.ID, "updateRightChildTo", x);
        }
        this.shiftDefChild();

        this.forwardFlatNetMessage(x.ID, this.flatnetMessage);
    }
    /* DONE       w 
                 /
	            z                    w
     	       / \                  /
     		  y   d                *x
	         / \       ->        /   \
           *x   c               y     z
	       / \                 / \   /  \
	      a   b               a   c b    d
	*/
    private void zigZigBottomUp(FlatInfo xInfo, FlatInfo yInfo, FlatInfo zInfo, FlatInfo wInfo) {
        // System.out.println("Node (" + ID + "): doing zigZigBottomUp");

        FlatTreeLayer x = (FlatTreeLayer) xInfo.getNode();
        FlatTreeLayer y = (FlatTreeLayer) yInfo.getNode();
        FlatTreeLayer z = (FlatTreeLayer) zInfo.getNode();
        FlatTreeLayer w = (FlatTreeLayer) wInfo.getNode();
        FlatTreeLayer dfChild = (FlatTreeLayer) x.getDeferredChild();
        FlatTreeLayer pfChild = (FlatTreeLayer) x.getPreferredChild();
        String changeChild, setDescendants;

        // System.out.println(xInfo.getNode().ID + " is doing a BTU ZigZig rotation");

        if(y.getLeftChild() == x){
            changeChild = "changeLeftChildTo";
            setDescendants = "setLeftDescendants";
        }else{
            changeChild = "changeRightChildTo";
            setDescendants = "setRightDescendants";
        }
        this.requestRPCTo(y.ID, changeChild, dfChild);
        this.requestRPCTo(z.ID, changeChild, pfChild);

        if(dfChild == x.getLeftChild()){
            this.requestRPCTo(y.ID, setDescendants, x.getLeftDescendants());
            this.requestRPCTo(z.ID, setDescendants, x.getRightDescendants());
            this.requestRPCTo(x.ID, "changeLeftChildTo", y);
            this.requestRPCTo(x.ID, "changeRightChildTo", z);

            if(y.getLeftChild() == x){
                this.updateDescendants("setLeftDescendants", yInfo.getRightDescendants(), x.getLeftDescendants(), y.ID);
                this.updateDescendants("setRightDescendants", zInfo.getRightDescendants(), x.getRightDescendants(), z.ID);
            }else{
                this.updateDescendants("setLeftDescendants", yInfo.getLeftDescendants(), x.getLeftDescendants(), y.ID);
                this.updateDescendants("setRightDescendants", zInfo.getLeftDescendants(), x.getRightDescendants(), z.ID);
            }

        }else{
            this.requestRPCTo(y.ID, setDescendants, x.getRightDescendants());
            this.requestRPCTo(z.ID, setDescendants, x.getLeftDescendants());
            this.requestRPCTo(x.ID, "changeLeftChildTo", z);
            this.requestRPCTo(x.ID, "changeRightChildTo", y);

            if(y.getLeftChild() == x){
                this.updateDescendants("setRightDescendants", yInfo.getRightDescendants(), x.getRightDescendants(), y.ID);
                this.updateDescendants("setLeftDescendants", zInfo.getRightDescendants(), x.getLeftDescendants(), z.ID);
            }else{
                this.updateDescendants("setRightDescendants", yInfo.getLeftDescendants(), x.getRightDescendants(), y.ID);
                this.updateDescendants("setLeftDescendants", zInfo.getLeftDescendants(), x.getLeftDescendants(), z.ID);
            }
        }

        if(wInfo.getLeftChild() == z){
            this.requestRPCTo(w.ID, "updateLeftChildTo", x);
        }else { 
            this.requestRPCTo(w.ID, "updateRightChildTo", x);
        }

        this.forwardFlatNetMessage(x.ID, this.flatnetMessage);
    }

    /* DONE
                   w                  w
                  /                  /
                 z				   *x
                / \               /   \
               y   d             y     z
              / \		  ->    / \   / \
             a   x*            a   b c   d
                / \
               b   c 
	 */
    private void zigZagBottomUp(FlatInfo xInfo, FlatInfo yInfo, FlatInfo zInfo, FlatInfo wInfo) {
        // System.out.println("Node (" + ID + "): doing zigZagBottomUp");

        FlatTreeLayer x = (FlatTreeLayer) xInfo.getNode();
        FlatTreeLayer y = (FlatTreeLayer) yInfo.getNode();
        FlatTreeLayer z = (FlatTreeLayer) zInfo.getNode();
        FlatTreeLayer w = (FlatTreeLayer) wInfo.getNode();
        FlatTreeLayer dfChild = (FlatTreeLayer) x.getDeferredChild();
        FlatTreeLayer pfChild = (FlatTreeLayer) x.getPreferredChild();
        String yChangeChild, ySetDescendants;
        String zChangeChild, zSetDescendants;

        if(y.getLeftChild() == x){
            yChangeChild = "changeLeftChildTo";
            ySetDescendants = "setLeftDescendants";
            zChangeChild = "changeRightChildTo";
            zSetDescendants = "setRightDescendants";
        }else{
            zChangeChild = "changeLeftChildTo";
            zSetDescendants = "setLeftDescendants";
            yChangeChild = "changeRightChildTo";
            ySetDescendants = "setRightDescendants";
        }

        this.requestRPCTo(y.ID, yChangeChild, dfChild);
        this.requestRPCTo(z.ID, zChangeChild, pfChild);

        if(dfChild == x.getLeftChild()){
            this.requestRPCTo(y.ID, ySetDescendants, x.getLeftDescendants());
            this.requestRPCTo(z.ID, zSetDescendants, x.getRightDescendants());
            if(y.getLeftChild() == x){
                this.requestRPCTo(x.ID, "changeLeftChildTo", y);
                this.requestRPCTo(x.ID, "changeRightChildTo", z);
                this.updateDescendants("setLeftDescendants", yInfo.getRightDescendants(), x.getLeftDescendants(), y.ID);
                this.updateDescendants("setRightDescendants", zInfo.getLeftDescendants(), x.getRightDescendants(), z.ID);
            }else{
                this.requestRPCTo(x.ID, "changeLeftChildTo", y);
                this.requestRPCTo(x.ID, "changeRightChildTo", z);
                this.updateDescendants("setLeftDescendants", yInfo.getLeftDescendants(), x.getLeftDescendants(), y.ID);
                this.updateDescendants("setRightDescendants", zInfo.getRightDescendants(), x.getRightDescendants(), z.ID);
            }
        }else{
            this.requestRPCTo(y.ID, ySetDescendants, x.getRightDescendants());
            this.requestRPCTo(z.ID, zSetDescendants, x.getLeftDescendants());
            if(y.getLeftChild() == x){
                this.requestRPCTo(x.ID, "changeLeftChildTo", y);
                this.requestRPCTo(x.ID, "changeRightChildTo", z);
                this.updateDescendants("setLeftDescendants", yInfo.getRightDescendants(), x.getRightDescendants(), y.ID);
                this.updateDescendants("setRightDescendants", zInfo.getLeftDescendants(), x.getLeftDescendants(), z.ID);
            }else{
                this.requestRPCTo(x.ID, "changeLeftChildTo", y);
                this.requestRPCTo(x.ID, "changeRightChildTo", z);
                this.updateDescendants("setLeftDescendants", yInfo.getLeftDescendants(), x.getRightDescendants(), y.ID);
                this.updateDescendants("setRightDescendants", zInfo.getRightDescendants(), x.getLeftDescendants(), z.ID);
            }
        }

        if(wInfo.getLeftChild() == z){
            this.requestRPCTo(w.ID, "updateLeftChildTo", x);
        }else { 
            this.requestRPCTo(w.ID, "updateRightChildTo", x);
        }
        
        this.forwardFlatNetMessage(x.ID, this.flatnetMessage);
    }

    public void updateDescendants(String command, HashSet<Integer> a, HashSet<Integer> b, int id){
        HashSet<Integer> newTable = new HashSet<Integer>();
        newTable.addAll(a);
        newTable.addAll(b);
        newTable.add(id);
        this.requestRPCTo(this.ID, command, newTable);
    }

    private void rotateTopDown(HashMap<String, FlatInfo> cluster) {

        FlatInfo xInfo = cluster.get("x");
        FlatInfo yInfo = cluster.get("y");

        FlatTreeLayer y = (FlatTreeLayer) yInfo.getNode();
        FlatTreeLayer nxt = (FlatTreeLayer) y.getNextNode(this.flatnetMessage.getDst());

        this.routing = true;
        if(nxt == null){
            return;
        }

        this.rotating = true;
        this.flatnetMessage.incrementRotations();
        
        if ((y == xInfo.getLeftChild() && nxt == yInfo.getLeftChild())
        || (y == xInfo.getRightChild() && nxt == yInfo.getRightChild())) {
            this.zigZigTopDown(xInfo, yInfo, nxt);
        } else {
            this.zigZagTopDown(xInfo, yInfo, nxt);
        }
    }

     /* DONE
	      	    *x                    x
		        / \                 /   \
		       y   d      ->     *nxt    y
		      / \                 / \   / \
		    nxt   c              a   b d   c
		    / \
		   a   b    
	*/
    private void zigZigTopDown(FlatInfo xInfo, FlatInfo yInfo, FlatTreeLayer nxt) {
        // System.out.println("Node (" + ID + "): doing zigZigTopDown");

        FlatTreeLayer x = (FlatTreeLayer) xInfo.getNode();
        FlatTreeLayer y = (FlatTreeLayer) yInfo.getNode();
        FlatTreeLayer other;
        String xChangeChild;
        String yChangeChild;

        if(xInfo.getLeftChild() == y){
            other = (FlatTreeLayer) xInfo.getRightChild();
            xChangeChild = "changeRightChildTo";
            yChangeChild = "changeLeftChildTo";
            this.requestRPCTo(y.ID, "setLeftDescendants", x.getRightDescendants());
            this.requestRPCTo(x.ID, "setRightDescendants", y.getLeftDescendants());
            this.updateDescendants("setLeftDescendants", y.getRightDescendants(), x.getRightDescendants(), y.ID);
        }else{
            other = (FlatTreeLayer) xInfo.getLeftChild();
            xChangeChild = "changeLeftChildTo";
            yChangeChild = "changeRightChildTo";
            this.requestRPCTo(x.ID, "setLeftDescendants", y.getRightDescendants());
            this.requestRPCTo(y.ID, "setRightDescendants", x.getLeftDescendants());
            this.updateDescendants("setRightDescendants", y.getLeftDescendants(), x.getLeftDescendants(), y.ID);
        }

        this.requestRPCTo(y.ID, yChangeChild, other);
        this.requestRPCTo(x.ID, xChangeChild, nxt);

        this.flatnetMessage.incrementRouting();
        this.forwardFlatNetMessage(nxt.ID, this.flatnetMessage);
    }

    /* DONE
	         *z                     x
	         / \        ->        /   \
	        y   d                y     z
	       / \                  / \   / \
	      a   x                a  *b *c  d
	         / \
	        b   c
	*/
    private void zigZagTopDown(FlatInfo xInfo, FlatInfo yInfo, FlatTreeLayer nxt) {
        // System.out.println("Node (" + ID + "): doing zigZagTopDown");

        FlatTreeLayer x = (FlatTreeLayer) xInfo.getNode();
        FlatTreeLayer y = (FlatTreeLayer) yInfo.getNode();
        FlatTreeLayer other;
        String changeChild;

        if(xInfo.getLeftChild() == y){
            other = (FlatTreeLayer) xInfo.getRightChild();
            changeChild = "changeRightChildTo";
            this.requestRPCTo(y.ID, "setRightDescendants", x.getRightDescendants());
            this.requestRPCTo(x.ID, "setRightDescendants", y.getRightDescendants());
            this.updateDescendants("setLeftDescendants", y.getLeftDescendants(), x.getRightDescendants(), y.ID);
        }else{
            other = (FlatTreeLayer) xInfo.getLeftChild();
            changeChild = "changeLeftChildTo";
            this.requestRPCTo(x.ID, "setLeftDescendants", y.getLeftDescendants());
            this.requestRPCTo(y.ID, "setLeftDescendants", x.getLeftDescendants());
            this.updateDescendants("setRightDescendants", y.getRightDescendants(), x.getLeftDescendants(), y.ID);
        }

        this.requestRPCTo(y.ID, changeChild, other);
        this.requestRPCTo(x.ID, changeChild, nxt);

        this.flatnetMessage.incrementRouting();
        this.forwardFlatNetMessage(nxt.ID, this.flatnetMessage);
    }

    /*
              y                   x
            /   \               /   \
           x     c     ->      a     y
          / \                       / \
         a   b                     b   c
    */

    @Override
    public void timeslot10() {
    }

    @Override
    public void timeslot11() {

        this.flatnetMessage = null;

        if (this.rotating) {

            this.rotationCompleted();
            this.rotating = false;

        } 
        if (this.routing) {
            this.forwardCompleted();
            this.routing = false;
        }

    }

    @Override
    public void timeslot12() {

        this.executeAllRPC();
    }

    public void rotationCompleted() {

    }

    public void forwardCompleted() {

    }

}