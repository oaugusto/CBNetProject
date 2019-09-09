package projects.displaynet.nodes.nodeImplementations;

/**
 * SynchronizerNode Implements timeslots system to coordenate operations. The
 * node initiate the timeslot in 0 and go ...
 */
public abstract class SynchronizerLayer extends BinaryTreeLayer {

    private int MAX_TIMESLOT = 13;
    private int timeslot;
    private long round;

    public int getCurrentTimeSlot() {
        return this.timeslot;
    }

    public long getCurrentRound() {
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
            nodeStep();
            break;

        case 1:
            nodeStep();
            break;

        case 2:
            nodeStep();
            break;

        case 3:
            updateState();
            break;

        case 4:
            timeslot1();
            break;

        case 5:
            timeslot2();
            break;

        case 6:
            timeslot3();
            break;

        case 7:
            timeslot4();
            break;

        case 8:
            timeslot5();
            break;

        case 9:
            timeslot6();
            break;

        case 10:
            timeslot7();
            break;

        case 11:
            timeslot8();
            break;

        case 12:
            timeslot9();
            posRound();

            this.round++;
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

    public void posRound() {

    }

    public void nodeStep() {

    }

}