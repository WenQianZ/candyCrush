package code.model;

import java.awt.Point;
/**
 * Candy Crush - Selector
 * @author Wenqian Zhao
 */
public class Selector {

	private Point _selectedFirst;
	private Point _selectedSecond;
	private Board _board;
	private Score _score;
	
	public Selector(Board b,Score s) {
		_score = s;
		_board = b;
		clearSelections();
	}
	
	public void select(Point p) {
		if (_selectedFirst == null) {
			_selectedFirst = p;
		}
		else {
			_selectedSecond = p;
			if (adjacent(_selectedFirst, _selectedSecond)) {
				_board.legalChange(_selectedFirst, _selectedSecond);
				while(_board.haveMatch()) {    //Have match
					_score.score(_board);     //Count score first
					_board.response();		 //Then response - clear the match -replace the match
				}   //If new created tiles are match, repeat the loop

			}
			clearSelections();
		}
	}

	public Point selectedFirst() {
		return _selectedFirst;
	}

	private boolean adjacent(Point p, Point q) {
		return Math.abs(p.x-q.x) + Math.abs(p.y-q.y) == 1;
	}

	private void clearSelections() {
		_selectedFirst = null;
		_selectedSecond = null;
	}

}
