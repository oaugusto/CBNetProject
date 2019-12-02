package projects.optnet;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

import projects.defaultProject.DataCollection;
import projects.defaultProject.RequestQueue;
import projects.defaultProject.OptTreeConstructor;
import projects.defaultProject.nodes.nodeImplementations.BinarySearchTreeLayer;
import projects.optnet.nodes.nodeImplementations.OptNode;
import projects.simplenet.nodes.nodeImplementations.CBNetApp;
import projects.simplenet.nodes.nodeImplementations.CBNetNode;
import sinalgo.configuration.Configuration;
import sinalgo.gui.transformation.PositionTransformation;
import sinalgo.runtime.AbstractCustomGlobal;
import sinalgo.tools.Tools;
import sinalgo.tools.Tuple;

public class CustomGlobal extends AbstractCustomGlobal {

  // final condition
  public long MAX_REQ;

  // simulation
  public int numNodes = 30;
  public ArrayList<BinarySearchTreeLayer> tree = null;
  public BinarySearchTreeLayer controlNode = null;
  public OptTreeConstructor treeTopology = null;
  public RequestQueue requestQueue;

  public Random random = Tools.getRandomNumberGenerator();
  public double lambda = 0.15;

  // LOG
  DataCollection data = DataCollection.getInstance();

  @Override
  public boolean hasTerminated() {
    if (this.data.getCompletedRequests() >= MAX_REQ) {
      OptNode node = (OptNode) Tools.getNodeByID(1);
      this.data.printRotationData();
      this.data.printRoutingData();
      return true;
    }
    return false;
  }

  @Override
  public void preRun() {

    String input = "";
    String output = "";

    try {

      if (Configuration.hasParameter("input")) {
        input = Configuration.getStringParameter("input");
      }

      if (Configuration.hasParameter("output")) {
        output = Configuration.getStringParameter("output");
      }

    } catch (Exception e) {
      e.printStackTrace();
      System.out.println("Missing configuration parameters");
    }

    // Set Log Path
    this.data.setPath(output);

    /*
     * read input data and configure the simulation
     */
    this.requestQueue = new RequestQueue(input);
    this.numNodes = this.requestQueue.getNumberOfNodes();
    MAX_REQ = this.requestQueue.getNumberOfRequests();

    /*
     * create the nodes and constructs the tree topology
     */
    this.tree = new ArrayList<BinarySearchTreeLayer>();

    for (int i = 0; i < numNodes; i++) {
      OptNode n = new OptNode();
//      CBNetApp n = new CBNetApp();
      n.finishInitializationWithDefaultModels(true);
      this.tree.add(n);
    }

    this.controlNode = new CBNetNode() {
      public void draw(Graphics g, PositionTransformation pt, boolean highlight) {
        String text = "ControlNode";
        super.drawNodeAsDiskWithText(g, pt, highlight, text, 10, Color.YELLOW);
      }
    };
    this.controlNode.finishInitializationWithDefaultModels(true);

    CommunicationMatrix mtx = new CommunicationMatrix(input);
    this.treeTopology = new OptTreeConstructor(controlNode, this.tree, mtx.getFrequencyMatrix());

    this.treeTopology.buildTree();
    System.out.println("opt tree finished");
    this.treeTopology.setPositions();

    /*
     * initiate sigma buffers with message
     */
    while (this.requestQueue.hasNextRequest()) {
      Tuple<Integer, Integer> r = this.requestQueue.getNextRequest();
      OptNode node = (OptNode) Tools.getNodeByID(r.first);
//      CBNetApp node = (CBNetApp) Tools.getNodeByID(r.first);
      node.newMessage(r.second);
    }

  }

  @Override
  public void preRound() {
    this.treeTopology.setPositions();
  }

}
