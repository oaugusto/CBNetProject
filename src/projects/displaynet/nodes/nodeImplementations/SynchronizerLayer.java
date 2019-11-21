package projects.displaynet.nodes.nodeImplementations;

import java.awt.Color;
import java.awt.Graphics;
import projects.defaultProject.nodes.nodeImplementations.BinarySearchTreeLayer;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.tools.Tools;

/**
 * SynchronizerNode Implements timeslots system to coordenate operations. The node initiate the
 * timeslot in 0
 */
public class SynchronizerLayer extends BinarySearchTreeLayer {

  private int MAX_TIMESLOT = 16;
  public int timeslot;
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
        handShakeStep();
        break;

      case 1:
        handShakeStep();
        break;

      case 2:
        handShakeStep();
        break;

      case 3:
        handShakeStep();
        break;

      case 4:
        handShakeStep();
        break;

      case 5:
        controlStep();
        break;

      case 8:
        clusterPhaseOne();
        break;

      case 11:
        clusterPhaseTwo();
        break;

      case 14:
        rotationStep();
        break;

      case 15:
        posRound();

        this.round++;
        break;

      default:
        break;
    }

    this.timeslot = (this.timeslot + 1) % MAX_TIMESLOT;
  }

  public void handShakeStep() {}

  public void controlStep() {}

  public void clusterPhaseOne() {}

  public void clusterPhaseTwo() {}

  public void rotationStep() {}

  public void posRound() {}

}