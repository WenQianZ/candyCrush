package code.model;

import java.awt.Point;
import java.util.ArrayList;
import code.ui.UI;
/**
 * Candy Crush - model
 * @author Wenqian Zhao
 */
public class Model {
	
	private UI _observer;
	private Board _board;
	private Selector _selector;
	private Filereader _filereader;
	private Score _score;
	private int _level;
	
	public Model() {
		_level = 1;  // Start with level 1
		_board = new Board(_level + 4, _level + 4);		// Set up the board
		_score = new Score();
		_selector = new Selector(_board,_score);
		_filereader = new Filereader("Score/highScore.txt");
	}
	
	public void addObserver(UI ui) {
		_observer = ui;
		_observer.update();
	}
	
	public int rows() { return _board.rows(); }
	public int cols() { return _board.cols(); }

	public String get(Point p) {
		return _board.get(p);
	}
	
	public Point selectedFirst() {
		return _selector.selectedFirst();
	}

	public void select(int r, int c) {
		_selector.select(new Point(r,c));
		_observer.update();
	}
	
	public ArrayList<Point> hints() {
		return _board.validMoves();
	}
	public int score() {
		return _score.score(_board);
	}
	
	public String highestScore() {
		_filereader = new Filereader("Score/highScore.txt");
		return _filereader.highestScore();
	}
	public void updatetxt(String filename, int i) {
		_filereader = new Filereader(filename);
		_filereader.updatetxt(i);
		_filereader.compare(filename);
	}
	
	public int level() {
		return _score.level();
	}
	public Board newBoard() {
		_level = level();
		_board = new Board(_level + 4, _level + 4);              //create a new board 
		_selector = new Selector(_board,_score); //create a new selector to the new board 
		return _board;
	}
	
	public boolean newLevel() {
		return _score.newlevel(_board);
	}
	
	public boolean gameOver() {
			return _board.end();
		
	}
	

}
