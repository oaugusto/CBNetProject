package projects.displaynet.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;

import sinalgo.gui.transformation.PositionTransformation;

/**
 * SynchronizerNode Implements timeslots system to coordenate operations. The
 * node initiate the timeslot in 0 and go ...
 */
public abstract class SynchronizerLayer extends RPCLayer {

    private int MAX_TIMESLOT = 8;
    private int timeslot;
    private int round;

    public int getCurrentTimeSlot() {
        return this.timeslot;
    }

    public int getCurrentRound() {
        return this.round;
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
            this.round++; // update round
            timeslot0();
            break;

        case 1:
            timeslot1();
            break;

        case 2:
            timeslot2();
            break;

        case 3:
            timeslot3();
            break;

        case 4:
            timeslot4();
            break;

        case 5:
            timeslot5();
            break;

        case 6:
            timeslot6();
            break;

        case 7:
            timeslot7();
            break;

        default:
            break;
        }
        this.timeslot = (this.timeslot + 1) % MAX_TIMESLOT;
    }

    public void timeslot0() {

    }

    public void timeslot1() {

    } 
    
    public void timeslot2() {

    } 

    public void timeslot3() {

    } 

    public void timeslot4() {

    } 
    
    public void timeslot5() {

    }

    public void timeslot6() {

    } 

    public void timeslot7() {

    } 

    @Override
    public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
        String text = "" + ID;
        super.drawNodeAsDiskWithText(g, pt, highlight, text, 30, Color.WHITE);
    }

}