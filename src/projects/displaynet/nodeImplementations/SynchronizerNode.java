package projects.displaynet.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;

import sinalgo.gui.transformation.PositionTransformation;

/**
 * SynchronizerNode Implements timeslots system to coordenate operations. The
 * node initiate the timeslot in 0 and go ...
 */
public abstract class SynchronizerNode extends BinaryTreeNode {

    private int MAX_TIMESLOT = 8;
    private int timeslot;

    public int getCurrentTimeSlot() {
        return this.timeslot;
    }

    @Override
    public void init() {
        super.init();
        this.timeslot = 0;
    }

    @Override
    public void postStep() {
        
        switch (this.timeslot) {
        case 0:
            timeslotZero();
            break;

        case 3:
            timeslotThree();
            break;

        case 6:
            timeslotSix();
            break;

        case 7:
            timeslotSeven();
            break;

        default:
            break;
        }
        this.timeslot = (this.timeslot + 1) % MAX_TIMESLOT;
    }

    // public abstract void nodeStep();
    public abstract void timeslotZero(); //send request cluster
    
    public abstract void timeslotThree();//all nodes receive request cluster message

    public abstract void timeslotSix();  //all nodes acknowledge one request cluster and the winner node rotate

    public abstract void timeslotSeven();//check finish splay

    @Override
    public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
        String text = "" + ID;
        super.drawNodeAsDiskWithText(g, pt, highlight, text, 30, Color.WHITE);
    }

}