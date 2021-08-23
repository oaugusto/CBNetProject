package projects.opticalNet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import projects.opticalNet.nodes.messages.NetworkMessage;
import projects.opticalNet.nodes.infrastructureImplementations.InputNode;
import sinalgo.nodes.messages.Inbox;
import sinalgo.nodes.messages.Message;
import sinalgo.gui.transformation.PositionTransformation;

public class NetworkNode extends SynchronizerLayer {
    private int weights = 0;
    private ArrayList<InputNode> interfaces = new ArrayList<>();

    private InputNode parent = null;
    private InputNode leftChild = null;
    private InputNode rightChild = null;

    private int minIdInSubtree = 0;
    private int maxIdInSubtree = 0;

    public NetworkNode() {
    	this.minIdInSubtree = this.ID;
    	this.maxIdInSubtree = this.ID;
    }
    
    public void connectToInputNode(InputNode node) {
        this.interfaces.add(node);
        this.addConnectionTo(node);
    }

    public void setParent(InputNode node) {
        this.parent = node;
    }

    public InputNode getParent() {
        return this.parent;
    }

    public void setChild (InputNode node, int subtreeId) {
        if (this.ID < node.getIndex())
            this.setRightChild(node, subtreeId);
        else
            this.setLeftChild(node, subtreeId);
    }

    public void setLeftChild(InputNode node, int minId) {
        this.leftChild = node;
        this.setMinIdInSubtree(minId);
    }

    public InputNode getLeftChild() {
        return this.leftChild;
    }

    public void setRightChild(InputNode node, int maxId) {
        this.rightChild = node;
        this.setMaxIdInSubtree(maxId);
    }

    public void setMinIdInSubtree(int value) {
    	if (value == -1) {
    		this.minIdInSubtree = this.ID;
    	} else {
    		this.minIdInSubtree = value;
    	}
    }

    public int getMinIdInSubtree() {
        return this.minIdInSubtree;
    }

    public void setMaxIdInSubtree(int value) {
    	if (value == -1) {
    		this.maxIdInSubtree = this.ID;
    	} else {
    		this.maxIdInSubtree = value;
    	}
    }

    public int getMaxIdInSubtree() {
        return this.maxIdInSubtree;
    }

    public int getWeight() {
        return this.weights;
    }

    public int getId() {
        return this.ID;
    }

    @Override
    public void init() {
        super.init();
    }

    public void sendMsg(int to) {
    	NetworkMessage netmsg = new NetworkMessage(this.ID, to);
    	if (to == this.ID) {
        	this.weights++;
            System.out.println("Message received from node " + to);
            return;
        }

        if (this.minIdInSubtree <= to && to < this.ID) {
            this.send(netmsg, this.leftChild);
        } else if (this.ID < to && to <= this.maxIdInSubtree) {
            this.send(netmsg, this.rightChild);
        } else {
            this.send(netmsg, this.parent);
        }
    }
    
    @Override
    public void handleMessages(Inbox inbox) {
        while (inbox.hasNext()) {
            Message msg = inbox.next();
            if (!(msg instanceof NetworkMessage)) {
                continue;
            }
            NetworkMessage cbmsg = (NetworkMessage) msg;
            if (cbmsg.getDst() == this.ID) {
            	this.weights++;
                System.out.println("Message received from node " + cbmsg.getSrc());
                continue;
            }
            // forward message in the network
            if (this.minIdInSubtree <= cbmsg.getDst() && cbmsg.getDst() < this.ID) {
                this.send(cbmsg, this.leftChild);
            } else if (this.ID < cbmsg.getDst() && cbmsg.getDst() <= this.maxIdInSubtree) {
                this.send(cbmsg, this.rightChild);
            } else {
                this.send(cbmsg, this.parent);
            }
        }
    }

    @Override
    public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
        String text = "" + ID;
        // draw the node as a circle with the text inside
        super.drawNodeAsDiskWithText(g, pt, highlight, text, 12, Color.YELLOW);
    }
}
