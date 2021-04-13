package projects.opticalNet.nodes.nodeImplementations;

import sinalgo.configuration.WrongConfigurationException;
import sinalgo.nodes.Node;

public abstract class SynchronizerLayer extends Node {

    private int MAX_TIMESLOT = 3;
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
        this.timeslot = 0;
    }

    @Override
    public void postStep() {
        
        switch (this.timeslot) {
        case 0:
            updateState();
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

    public void posRound() {
        
    }

    public void nodeStep() {

    }
    
	@Override
	public void preStep() { }
	
	@Override
	public void neighborhoodChange() { }
	
	@Override
	public void checkRequirements() throws WrongConfigurationException { }

}