package projects.optnet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * CommunicationMatrix
 * read the communication sequence and build the matrix
 */
public class CommunicationMatrix {

    private int length; //the size of communication matrix
    private int[][] communicationMatrix;
    private int totalMatrix; // the sum of entire matrix

    public CommunicationMatrix(String file) {

        try {
            BufferedReader br = new BufferedReader(new FileReader(new File(file)));
            String line = null;

            // read header
			if ((line = br.readLine()) != null) {
				String[] fields = line.split(" ");
				this.length = Integer.parseInt(fields[0]);
				this.totalMatrix = Integer.parseInt(fields[1]);
            }

            // create matrix and initialize with 0
            this.communicationMatrix = new int[this.length][this.length];
            for (int i = 0; i < this.length; i++) {
                for (int j = 0; j < this.length; j++) {
                    this.communicationMatrix[i][j] = 0;
                }
            }

            while ((line = br.readLine()) != null) {
				String[] fields = line.split(" ");
				int src = Integer.parseInt(fields[0]); 
                int dst = Integer.parseInt(fields[1]);
                this.communicationMatrix[src][dst]++;
                this.communicationMatrix[dst][src]++;
			}
            
            br.close();

        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }

    }

    public int getLenght() {
        return this.length;
    }
    
    public int getTotalMatrix() {
        return this.totalMatrix;
    }

    public int getValueAt(int i, int j) {
        return this.communicationMatrix[i][j];
    }

    public double[][] getFrequencyMatrix() {
        double[][] frequencyMatrix = new double[this.length][this.length];

        for (int i = 0; i < this.length; i++) {
            for (int j = 0; j < this.length; j++) {
                frequencyMatrix[i][j] = this.communicationMatrix[i][j] / (double) this.totalMatrix;
            }
        }

        return frequencyMatrix;
    }
}