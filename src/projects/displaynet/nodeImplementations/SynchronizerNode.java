package projects.displaynet.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;

import sinalgo.gui.transformation.PositionTransformation;

/**
 * SynchronizerNode Implements timeslots system to coordenate operations. The
 * node initiate the timeslot in 0 and go till 14
 */
public abstract class SynchronizerNode extends BinaryTreeNode {

    private int MAX_TIMESLOT = 15;
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
        // this.nodeStep();
        switch (this.timeslot) {
        case 0:
            timeslotZero();
            break;

        case 1:
            timeslotOne();
            break;

        case 2:
            timeslotTwo();
            break;

        case 5:
            timeslotFive();
            break;

        default:
            break;
        }
        this.timeslot = (this.timeslot + 1) % MAX_TIMESLOT;
    }

    // public abstract void nodeStep();
    public void timeslotZero() {
        // System.out.println("timeslot zero");
    }

    public void timeslotOne() {
        // System.out.println("timeslot one");
    }
    
    public void timeslotTwo() {
        // System.out.println("timeslot two");
    }

    public void timeslotFive() {
        // System.out.println("timeslot five");
    }

    @Override
    public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
        String text = "timeslot: " + this.getCurrentTimeSlot();
        super.drawNodeAsDiskWithText(g, pt, highlight, text, 20, Color.WHITE);
    }

}