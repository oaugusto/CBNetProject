package projects.randomdisplaynetAdapt.nodes.nodeImplementations;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;

import projects.randomdisplaynetAdapt.CustomGlobal;
import projects.randomdisplaynetAdapt.nodes.messages.CBNetMessage;
import projects.randomdisplaynetAdapt.nodes.messages.CompletionMessage;
import sinalgo.nodes.messages.Message;
import sinalgo.tools.Tools;

/**
 * CBNetLayer
 */
public abstract class CBNetLayer extends RPCLayer {
	
	// to decide upon the type of the message (only rotation or only routing)
    private Random rand = Tools.getRandomNumberGenerator();
    
    private double p = 0.5; // probability of splaying the message
    private int previousDestinationId = -1;
    
	private boolean recvCBNetMessage;
	private int sourceID;
	private boolean recvAckCBNetMessage;
	private ArrayList<CBNetMessage> ackMessageReceived;
    private PriorityQueue<CBNetMessage> cbnetQueue;

    @Override
    public void init() {
        super.init();

        this.recvCBNetMessage = false;
        this.sourceID = -1;
        this.recvAckCBNetMessage = false;
        this.ackMessageReceived = new ArrayList<CBNetMessage>();
        this.cbnetQueue = new PriorityQueue<>();
    }

    public boolean hasCBNetMessage() {
        return !this.cbnetQueue.isEmpty();
    }

    public CBNetMessage getTopCBNetMessage() {
        return this.cbnetQueue.peek();
    }

    public void removeTopCBNetMesssage() {
        this.cbnetQueue.poll();
    }

    public void sendCBNetMessage(int dst, double priority) {
    	this.incrementCounter(); // increment local counter
    	if (this.previousDestinationId != dst) {
    		this.previousDestinationId = dst;
    		p = Math.max(1/(double)512, p/2.0);
    		// System.out.println("First: " + 1/(double)512 + " p= " + p);
    	} else {
    		// System.out.println("P value reseted");
    		p = 1;
    	}
        CBNetMessage msg = new CBNetMessage(ID, dst, priority, rand.nextDouble() <= p /*onlyRotation*/);
        msg.initialTime = this.getCurrentRound();
        this.cbnetQueue.add(msg);
    }

    public void forwardCBNetMessage(int dst, CBNetMessage msg) {
        this.sendForwardMessage(dst, msg);
    }

    @Override
    public void receiveMessage(Message msg) {
        super.receiveMessage(msg);

        if (msg instanceof CBNetMessage) {
            CBNetMessage cbmsg = (CBNetMessage) msg;

            if (ID == cbmsg.getDst()) {
                this.receivedCBNetMessage(cbmsg);
                this.sourceID = cbmsg.getSrc();
                this.recvCBNetMessage = true;

                // send ack message
                this.sendDirect(new CompletionMessage(cbmsg), Tools.getNodeByID(cbmsg.getSrc()));

            } else {
                this.cbnetQueue.add(cbmsg);
            }

            return;
        } else if (msg instanceof CompletionMessage) {
            CompletionMessage completionMessage = (CompletionMessage) msg;
            this.recvAckCBNetMessage = true;
            this.ackMessageReceived.add(completionMessage.getCbnetMessage());
            return;
        }
    }
    
    @Override
    public void timeslot11() {
    	super.timeslot11();
    	//if future improvements allow to 
    	//receive multiples messages at same time
    	//this procedure must be changed 
    	if (this.recvCBNetMessage == true) {
    		this.recvCBNetMessage = false;
    		this.incrementCounter(); // increment local counter
    		this.incrementWeight();
    		this.updateWeights(ID, this.sourceID); //TODO
    	}
    	
    	if (this.recvAckCBNetMessage == true) {
    		this.recvAckCBNetMessage = false;
    		Iterator<CBNetMessage> it = this.ackMessageReceived.iterator();
    		while (it.hasNext()) {
    			this.ackCBNetMessageReceived(it.next());
    		}
    	}
    	
    	this.ackMessageReceived.clear();
    }

    public abstract void receivedCBNetMessage(CBNetMessage msg);

    
    public abstract void ackCBNetMessageReceived(CBNetMessage msg);
    
}