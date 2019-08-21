package projects.displaynet;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import sinalgo.tools.Tuple;

public class RequestQueue {
	
	private Queue<Tuple<Integer, Integer>> queue = new LinkedList<>();
		
	public void setDataFromFile(String path) {
		queue.clear();
		
		BufferedReader reader; 
		
		try {
			
			String line;
			reader = new BufferedReader(new FileReader(path));
					
			while ((line = reader.readLine()) != null) {
				String[] fields = line.split(",");
				Tuple<Integer, Integer> r = new Tuple<>(Integer.valueOf(fields[0]) 
						+ 1, Integer.valueOf(fields[1]) + 1);
				queue.add(r);
			}
			
			reader.close();
			
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		} 
		
	}
	
	public Tuple<Integer, Integer> getNextRequest() {
		return this.queue.poll();
	}
	
	public boolean hasNextRequest() {
		return !queue.isEmpty();
	}
	
}