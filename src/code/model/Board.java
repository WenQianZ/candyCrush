package code.model;

import java.awt.Point;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
/**
 * Candy Crush - board
 * @author Wenqian Zhao
 */
public class Board {
	private ArrayList<ArrayList<String>> _board;
	private ArrayList<String> _colorFileNames;
	private Random _rand;
	private static int MAX_COLORS = 6; // max possible is 6

	public Board(int rows, int cols) {
		_board = new ArrayList<ArrayList<String>>();
		_rand = new Random();
		_colorFileNames = new ArrayList<String>();
		for (int i=0; i<MAX_COLORS; i=i+1) {
			_colorFileNames.add("Images/Tile-"+i+".png");
		}
		for (int r=0; r<rows; r=r+1) {
			ArrayList<String> row = new ArrayList<String>();
			for (int c=0; c<cols; c=c+1) {
				row.add(_colorFileNames.get(_rand.nextInt(_colorFileNames.size())));
			}
			_board.add(row);
			
			if(_board.size()==rows) { //board is complete
				boolean istrue = false;
				if(match(3).size()>0 || end()){ //(1)there are match //(2)there is no valid move
					istrue = true;
				}
				if (istrue) {
					_board.clear();		// if istrue clear the board
					r=-1;				// restart; r=-1, at the end of loop will add 1
				}	
			}
		}		
	}
	
	public int rows() { return _board.size(); }
	public int cols() { return _board.get(0).size(); }

	public String get(Point p) {
		return _board.get(p.x).get(p.y);
	}

	private String set(Point p, String s) {
		return _board.get(p.x).set(p.y, s);
	}

	public void exchange(Point p, Point q) {
		String temp = get(p);
		set(p, get(q));
		set(q, temp);
	}
	
	public void legalChange(Point p, Point q) {
		exchange(p,q);
		if(!haveMatch()){ 
			exchange(p,q); //Exchange back to original if there's no match
		}
	}
	
	public void response() {
		if(haveMatch()) {
			HashSet<Point> points = match(3);
			for(Point p: points) { 
				set(p,null);			//set match points to null
			}
			replace();    //replace the null 
		}
	}
	
	public ArrayList<Point> nullPoint() {  //Get all the point with null
		ArrayList<Point> points = new ArrayList<Point>();
		for (int r=0; r<rows(); r=r+1) {
			for (int c=0; c<cols(); c=c+1) {
				Point p = new Point(r,c);
				if(get(p)==null) {
					points.add(p);
				}
			}
		}
		return points;
	}
	
	public void replace() {  //replace the point with null
		ArrayList<Point> points = nullPoint();
		while(0 < points.size()) {
			Point p = points.get(0); //only get the first one 
			if(p.x==0) {      //if p is in first column
				set(p,_colorFileNames.get(_rand.nextInt(_colorFileNames.size())));   //set a new string
			}
			else {        
				exchange(p, new Point(p.x-1,p.y));   //exchange with the string above
			}
			points = nullPoint(); //renew the arraylist, will exclude the fist point
		} //end loop when there are is no null point
	}
	
	private HashSet<Point> match(int runLength) {
		HashSet<Point> matches = verticalMatch(runLength);
		matches.addAll(horizontalMatch(runLength));
		return matches;
	}

	public boolean haveMatch(){
		return match(3).size()>0;
	}

	private HashSet<Point> horizontalMatch(int runLength) {
		HashSet<Point> matches = new HashSet<Point>();
		int minCol = 0;
		int maxCol = cols() - runLength;
		for (int r = 0; r < rows(); r = r + 1) {
			for (int c = minCol; c <= maxCol; c = c + 1) {  // The cols we can START checking in
				HashSet<String> values = new HashSet<String>();
				HashSet<Point> points = new HashSet<Point>();
				for (int offset = 0; offset < runLength; offset = offset + 1) {
					Point p = new Point(r,c+offset);
					points.add(p);
					String s = get(p);
					values.add(s);
				}
				if (values.size() == 1) { matches.addAll(points); }
			}
		}
		return matches;
	}

	private HashSet<Point> verticalMatch(int runLength) {
		HashSet<Point> matches = new HashSet<Point>();
		int minRow = 0;
		int maxRow = rows() - runLength;
		for (int c = 0; c < cols(); c = c + 1) {
			for (int r = minRow; r <= maxRow; r = r + 1) {  // The rows we can START checking in
				HashSet<String> values = new HashSet<String>();
				HashSet<Point> points = new HashSet<Point>();
				for (int offset = 0; offset < runLength; offset = offset + 1) {
					Point p = new Point(r+offset,c);
					points.add(p);
					String s = get(p);
					values.add(s);
				}
				if (values.size() == 1) { matches.addAll(points); }
			}
		}
		return matches;
	}
	
	public ArrayList<Point> validMoves() {
		ArrayList<Point> valids = new ArrayList<Point>();
		valids.addAll(verticalValidMoves());
		valids.addAll(horizontalValidMoves());
		return valids;		//add all the valid point
	}
	
	private ArrayList<Point> horizontalValidMoves(){
		ArrayList<Point> points = new ArrayList<Point>();
		for(int r = 0; r<rows(); r++) {
			for(int c = 0; c<cols()-1; c++) {
					exchange(new Point(r,c),new Point(r,c+1));
					if(match(3).size()>0){
						points.add(new Point(r,c+1));
						points.add(new Point(r,c));   //add the points when exchange its match 
					}
					exchange(new Point(r,c),new Point(r,c+1));	
			}
		}
		return points;
	}
	
	private ArrayList<Point> verticalValidMoves(){
		ArrayList<Point> points = new ArrayList<Point>();
		for(int r = 0; r<rows()-1; r++) {
			for(int c = 0; c<cols(); c++) {
					exchange(new Point(r,c),new Point(r+1,c));
					if(match(3).size()>0){
						points.add(new Point(r+1,c));
						points.add(new Point(r,c));    //add the points when exchange its match 
					}
					exchange(new Point(r,c),new Point(r+1,c));
			}
		}
		return points;
	}
	
	private HashSet<Point> maximalMatchedRegion(Point p, ArrayList<ArrayList<String>> board) {
		HashSet<Point> candidates = new HashSet<Point>();
		HashSet<Point> matches = new HashSet<Point>();
		HashSet<Point> region = new HashSet<Point>();
		if(p!=null) {
			candidates.add(p);
			while(!candidates.isEmpty()) { 
				for(Point point: candidates) {
					matches.addAll(adjacentAndMatching(point,board));
					region.add(point);
				}
				candidates.addAll(matches);
				candidates.removeAll(region);
				matches.clear();
			}
		}
		return region;
	}
	
	public HashSet<HashSet<Point>> partition(ArrayList<ArrayList<String>> board) {
		HashSet<HashSet<Point>> points = new HashSet<HashSet<Point>>();
		ArrayList<Point> boardp = new ArrayList<Point>();
		if (board!=null) {
			for(int r = 0; r<board.size();r++) {
				for(int c = 0; c<board.get(0).size(); c++) {
					boardp.add(new Point(r,c));
				}
			}
			for(int i = 0; i < boardp.size();) {
				HashSet<Point> point = maximalMatchedRegion(boardp.get(0), board);
				points.add(point);
				boardp.removeAll(point);
			}
		}
		return points;
	}
	
	private HashSet<Point> trimmedRegion(HashSet<Point> in) {
		HashSet<Point> out = new HashSet<Point>();
		for(Point p: in) {
			if(match(3).contains(p)) { //compare the point with match
				out.add(p);
			}
		}
		return out;
	}
	
	private HashSet<HashSet<Point>> matchRegion(HashSet<HashSet<Point>> partition) {
		HashSet<HashSet<Point>> matchRegions = new HashSet<HashSet<Point>>();
		for(HashSet<Point> points : partition) {
			HashSet<Point> region = trimmedRegion(points);
			if(!region.isEmpty()) {
				matchRegions.add(region);
			}
		}
		return matchRegions;
	}
	
	public int score() {   //All match region
		int score = 0;
		for(HashSet<Point> match : matchRegion(partition(_board))) {
			int n = match.size();
			score = 3 + (n-3)*(n-3) + score;            //Add score for each match region
		}
		return score;
	}

	private HashSet<Point> adjacentAndMatching(Point p, ArrayList<ArrayList<String>> board) {
		HashSet<Point> result = new HashSet<Point>();
		Point check;
		check = new Point(p.x-1,p.y); if (inBounds(check, board) && matches(p,check,board)) { result.add(check); }
		check = new Point(p.x+1,p.y); if (inBounds(check, board) && matches(p,check,board)) { result.add(check); }
		check = new Point(p.x,p.y-1); if (inBounds(check, board) && matches(p,check,board)) { result.add(check); }
		check = new Point(p.x,p.y+1); if (inBounds(check, board) && matches(p,check,board)) { result.add(check); }
		return result;
	}

	private boolean matches(Point p, Point q, ArrayList<ArrayList<String>> _board) {return _board.get(p.x).get(p.y).equals(_board.get(q.x).get(q.y));}
	
	private boolean inBounds(Point p, ArrayList<ArrayList<String>> _board) {return p.x >=0 && p.x < _board.size() && p.y >= 0 && p.y < _board.get(0).size();}
	
	public boolean end() {
		return validMoves().size()==0;   //no valid move
	}
}
