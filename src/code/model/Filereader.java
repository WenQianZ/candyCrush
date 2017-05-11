package code.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
/**
 * Candy Crush - Filereader
 * @author Wenqian Zhao
 */
public class Filereader implements Iterator<String> {
	private Scanner _scan;
	private ArrayList<Integer> list;
	
	public Filereader(String filename) {
		try {
			_scan = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			try {
	            File file = new File(filename);
	            BufferedWriter output = new BufferedWriter(new FileWriter(file));
	            output.close();
	        } catch ( IOException e1 ) {
	            e1.printStackTrace();
	         
	        }
//			System.out.println("There is no file named \""+filename+"\" found.");
		}
	}
	
	public boolean hasNext() {
		return _scan.hasNext();
	}
	
	public String next() {
		return _scan.nextLine();
	}
	
	public void close() {
		_scan.close();
	}
	
	
	public void compare(String filename) {
		try(
			FileWriter fw = new FileWriter(filename, false);)
		{
			for(int n: list) {
				fw.write(n+"\n"); // Add number from list and & next line
			}
		    fw.close();
		
		} catch (IOException e) {
			e.getStackTrace();
		}
	}
	
	public String highestScore() {
		String s = ""+0;
		if(hasNext()) {  
			s = next();  //next is string
		}
		return s;
	}
	
	public void updatetxt(int n) {
		list = new ArrayList<Integer>();
		HashSet<Integer> list2 = new HashSet<Integer>(); //Have no repeat score on list
		list2.add(n);			
		while(hasNext()){
			list2.add(Integer.parseInt(next()));   //Convert string to Integer and & into list2
		}
		list.addAll(list2);
		Collections.sort(list);     //Sort from smallest to greatest
		Collections.reverse(list);  //Reverse
	}
	


}
