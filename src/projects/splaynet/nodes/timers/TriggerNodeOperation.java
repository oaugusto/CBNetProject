package projects.splaynet.nodes.timers;

import projects.defaultProject.nodes.messages.ApplicationMessage;
import projects.splaynet.CustomGlobal;
import sinalgo.nodes.timers.Timer;
import sinalgo.tools.Tools;

/**
 * TimerExponential
 */
public class TriggerNodeOperation extends Timer {

  @Override
  public void fire() {
    CustomGlobal.mustGenerateSplay = true;
  }

}
