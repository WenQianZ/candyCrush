package code.ui;

import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;

import code.model.Model;
/**
 * Candy Crush - ui
 * @author Wenqian Zhao
 */
public class UI implements Runnable {

	private JFrame _frame; 
	private Model _model;
	private ArrayList<ArrayList<JButton>> _viewBoard; 	//Buttons of the board
	private ArrayList<JLabel> _scores;	// Show the Level/ Score / Highest Score
	private JPanel _board;	// Board for button
	
	public UI() {
		_model = new Model();
		_viewBoard = new ArrayList<ArrayList<JButton>>();
	}

	@Override
	public void run() {
		_frame = new JFrame("Wenqian Zhao's Lab 11");
		_board = new JPanel();
		JPanel scoreBoard = new JPanel();	
		scoreBoard.setLayout(new GridLayout(5,1));
		_scores = new ArrayList<JLabel>();
		for(int x = 0; x < 5; x++) {	//place _score on scoreBoard
			JLabel score = new JLabel();
			score.setBackground(new Color(50,50,50));
			score.setForeground(Color.WHITE);
			score.setOpaque(true);
			score.setFont(new Font("Consolas", Font.PLAIN, 18));
			score.setHorizontalAlignment(JLabel.CENTER);
			scoreBoard.add(score);	
			_scores.add(score);
		}
		_frame.getContentPane().setLayout(new BoxLayout(_frame.getContentPane(), BoxLayout.Y_AXIS));

		_frame.add(scoreBoard);
		_frame.add(_board);
		newBoard();
	
		_model.addObserver(this);	
		update();
		_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		_frame.pack();
		_frame.setVisible(true);
	}
	
	public void newBoard() {
		_frame.remove(_board);		// Remove the old _board(JPanel)
		_board = new JPanel();		// Renew the _board
		_board.setLayout(new GridLayout(_model.rows(), _model.cols()));
		_viewBoard = new ArrayList<ArrayList<JButton>>();
		for (int r=0; r<_model.rows(); r++) {
			_viewBoard.add(new ArrayList<JButton>());
			for (int c=0; c<_model.cols(); c++) {
				JButton button = new JButton();
				button.setOpaque(true);
				_viewBoard.get(r).add(button);
				_board.add(button);
				button.addActionListener(new EventHandler(_model, r, c));
			}
		}	
		_frame.add(_board);
	}

	public void update() {
		//NEW BOARD - if possible
		if(_model.newLevel()) {
			_model.newBoard();
			newBoard();
		}
		//UPDATE SCORES
		_scores.get(1).setText("Level: " + _model.level());						// Level
		_scores.get(2).setText("Score: " + _model.score());						// Score
		_scores.get(3).setText("Highest Score: " + _model.highestScore());		// Highest Score
		// UPDATE BOARD - redraw the whole thing
		for (int r=0; r<_model.rows(); r++) {
			for (int c=0; c<_model.cols(); c++) {
				JButton button = _viewBoard.get(r).get(c);
				button.setIcon(new ImageIcon(_model.get(new Point(r,c))));
				button.setBackground(new Color(50,50,50));
			}
		}
		//SHOW HINT
		ArrayList<Point> hint = _model.hints();
		if(hint.size() > 1) {
			_viewBoard.get(hint.get(0).x).get(hint.get(0).y).setBackground(Color.WHITE);
			_viewBoard.get(hint.get(1).x).get(hint.get(1).y).setBackground(Color.WHITE);
		}
		// MARK FIRST SELECTED - if applicable
		Point p = _model.selectedFirst();
		if (p != null) {
			_viewBoard.get(p.x).get(p.y).setBackground(Color.RED);
		}
		//GAME OVER
		if(_model.gameOver()) {
			System.out.println("Score:  " + _model.score());  //print out score
			_model.updatetxt("Score/highScore.txt", _model.score()); //update highscore.txt
			System.exit(0);
			
		}
		// REPAINT JFrame
		_frame.pack();
		_frame.repaint();
		
		
	
	}

}
