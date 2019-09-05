package projects.cbnet.nodes.nodeImplementations;

import projects.cbnet.CustomGlobal;
import sinalgo.tools.logging.Logging;

/**
 * SynchronizerLayer
 */
public abstract class SynchronizerLayer extends CBTreeLayer {

    // LOG
    public Logging numCluster = Logging.getLogger("cluster.txt");

    private int MAX_TIMESLOT = 11;
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
            updateState();
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

        case 8:
            timeslot8();
            break;

        case 9:
            timeslot9();
            break;

        case 10:
            timeslot10();
            roundLog();
            break;

        default:
            break;
        }
        this.timeslot = (this.timeslot + 1) % MAX_TIMESLOT;

        this.nodeStep();
    }

    public void updateState() {

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

    public void timeslot8() {

    }

    public void timeslot9() {

    } 

    public void timeslot10() {

    }

    // LOG
    public void roundLog() {
        if (ID == 1) {
            numCluster.logln("" + CustomGlobal.numberClusters);
        }
    }

    public abstract void nodeStep();

    // @Override
    // public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
    //     String text = "" + ID;
    //     super.drawNodeAsDiskWithText(g, pt, highlight, text, 30, Color.WHITE);
    // }
}