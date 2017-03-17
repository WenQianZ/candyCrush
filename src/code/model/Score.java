package code.model;
/**
 * Candy Crush - Score
 * @author Wenqian Zhao
 */
public class Score {

	private int _score;
	private int _levelscore;
	private int _level;
	
	public Score() {
		_score = 0;   	 //Score to begin
		_levelscore = 0;  //Score require for level 1
		_level = 1;  	 //Start with level 1
	}
	
	public int score(Board board) {
		if(board.haveMatch()) {
			int n = board.score();
			_score = n + _score;
		}
		return _score;
	}

	public int level() {
		return _level;
	}
	
	public boolean newlevel(Board _board) {
		if(_score >= _board.rows() * 10 + _levelscore) {
			_levelscore = _board.rows() * 10 + _levelscore; //Update score require
			_level = _level + 1;
			return true;
		}
		return false;
	}
}
