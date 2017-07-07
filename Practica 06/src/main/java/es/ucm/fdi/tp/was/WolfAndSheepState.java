package es.ucm.fdi.tp.was;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.ucm.fdi.tp.base.model.GameState;

/**
 * A WolfAndSheep state. Describes a board of WolfAndSheep that is either being
 * played or is already finished.
 */
public class WolfAndSheepState extends GameState<WolfAndSheepState, WolfAndSheepAction> {

	private static final long serialVersionUID = -2641387354190472377L;

	private int turn;
	private final boolean finished;
	private final int[][] board;
	private final int winner;
	public static final int dim = 8;
	// Constant atributes used to simplify the code
	final static int EMPTY = -1;
	public final static int WOLF = 0;
	public final static int SHEEP = 1;

	/**
	 * Default constructor creates the board and set the initial position of the
	 * players
	 */
	public WolfAndSheepState() {
		super(2);
		// Initial empty board of the game
		board = new int[dim][];
		for (int i = 0; i < dim; i++) {
			board[i] = new int[dim];
			for (int j = 0; j < dim; j++)
				board[i][j] = EMPTY;
		}
		// Initial position of the players
		board[dim - 1][0] = WOLF;
		for (int i = 0; i < dim; i++) {
			if (i % 2 == 1)
				board[0][i] = SHEEP;
		}
		// Set the initial turn
		this.turn = WOLF;
		this.winner = -1;
		this.finished = false;
	}

	/**
	 * Alternative constructor copies previous state and set next turn
	 * 
	 * @param prev
	 *            contains the previous state of the game
	 * @param board
	 *            is the board for the new state
	 * @param finished
	 *            sets if game is end
	 * @param winner
	 *            sets who wins the game
	 */
	public WolfAndSheepState(WolfAndSheepState prev, int[][] board, boolean finished, int winner) {
		super(2);
		this.board = board;
		this.turn = (prev.turn + 1) % 2;
		this.finished = finished;
		this.winner = winner;
	}

	/**
	 * Determines if a position (x, y) of the board is empty
	 * 
	 * @param row
	 *            associated to the coordinate 'x' of the board
	 * @param col
	 *            associated to the coordinate 'y' of the board
	 * @return true if the position is empty, false if not
	 */
	public boolean isValid(int row, int col) {
		// Check if the game is finished
		if (isFinished())
			return false;
		// Check if the position is inside the board
		if (row < 0 || row >= dim || col < 0 || col >= dim)
			return false;
		return at(row, col) == EMPTY;
	}

	/**
	 * Fill a list with the valid actions for the actual player
	 * 
	 * @param playerNumber
	 *            is the actual player
	 * @return list with the valid actions
	 */
	public List<WolfAndSheepAction> validActions(int playerNumber) {
		ArrayList<WolfAndSheepAction> valid = new ArrayList<>();
		// Search for the actual player on the board
		for (int i = 0; i < dim; i++) {
			for (int j = 0; j < dim; j++) {
				if (at(i, j) == playerNumber) {
					// Fill the list with the valid actions depending on the
					// player type
					if (playerNumber == WOLF) {
						for (WolfMovement m : WolfMovement.values()) {
							if (isValid(i + m.getRow(), j + m.getCol()))
								valid.add(new WolfAndSheepAction(playerNumber, i, j, i + m.getRow(), j + m.getCol()));
						}
					} else {
						for (SheepMovement m : SheepMovement.values()) {
							if (isValid(i + m.getRow(), j + m.getCol()))
								valid.add(new WolfAndSheepAction(playerNumber, i, j, i + m.getRow(), j + m.getCol()));
						}
					}
				}
			}
		}
		return valid;
	}

	/**
	 * Determines if a player from a specific position on the board can make a
	 * movement
	 * 
	 * @param board
	 *            of the game
	 * @param playerNumber
	 *            is the actual player
	 * @param x
	 *            is a row from the board
	 * @param y
	 *            is a column from the board
	 * @return true if the actual player can make any move, false if not
	 */
	private static boolean isWinner(int[][] board, int playerNumber, int x, int y) {
		int row, col;
		// Check the valid actions depending on the player type
		if (playerNumber == WOLF) {
			for (WolfMovement m : WolfMovement.values()) {
				row = x + m.getRow();
				col = y + m.getCol();
				if (row >= 0 && row < board.length && col >= 0 && col < board[row].length) {
					if (board[row][col] == EMPTY)
						return false;
				}
			}
		} else {
			for (SheepMovement m : SheepMovement.values()) {
				row = x + m.getRow();
				col = y + m.getCol();
				if (row >= 0 && row < board.length && col >= 0 && col < board[row].length) {
					if (board[row][col] == EMPTY)
						return false;
				}
			}
		}
		return true;
	}

	/**
	 * Determines if a player has won the game
	 * 
	 * @param board
	 *            of the game
	 * @param playerNumber
	 *            is the actual player
	 * @return true if the actual player has won, false if not
	 */
	public static boolean isWinner(int[][] board, int playerNumber) {
		// Check the possible winning causes depending on the player type
		if (playerNumber == WOLF) {
			// Check if the wolf has reached the upper row
			for (int j = 0; j < board.length; j++) {
				if (board[0][j] == WOLF)
					return true;
			}
			// Check if the sheep can make any move
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					if (board[i][j] == SHEEP) {
						if (!isWinner(board, 1, i, j))
							return false;
					}
				}
			}
			return true;
		} else {
			// Check if the wolf can make any move
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					if (board[i][j] == WOLF)
						return isWinner(board, 0, i, j);
				}
			}
		}
		return false;
	}

	/**
	 * Gives the content from a position (x, y) of the board
	 * 
	 * @param row
	 *            associated to the coordinate 'x' of the board
	 * @param col
	 *            associated to the coordinate 'y' of the board
	 * @return the content from the determined position
	 */
	public int at(int row, int col) {
		return board[row][col];
	}

	/**
	 * Getter of turn
	 * 
	 * @return turn
	 */
	public int getTurn() {
		return turn;
	}

	/**
	 * Getter of finished
	 * 
	 * @return finished
	 */
	public boolean isFinished() {
		return finished;
	}

	/**
	 * Getter of winner
	 * 
	 * @return winner
	 */
	public int getWinner() {
		return winner;
	}

	/**
	 * Makes a duplicate of the board
	 * 
	 * @return a copy of the board
	 */
	public int[][] getBoard() {
		int[][] copy = new int[board.length][];
		for (int i = 0; i < board.length; i++)
			copy[i] = board[i].clone();
		return copy;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < board.length; i++) {
			sb.append("|");
			for (int j = 0; j < board.length; j++)
				sb.append(board[i][j] == WOLF ? " W |"
						: board[i][j] == SHEEP ? " S |" : board[i][j] == EMPTY && i % 2 == j % 2 ? " # |" : "   |");
			sb.append("\n");
		}
		return sb.toString();
	}

	@Override
	public int getDimension() {
		return dim;
	}

	public String getGameDescription() {
		return "Wolf & Sheeps";
	}

	@Override
	public boolean equals(Object o) {
		WolfAndSheepState s = (WolfAndSheepState) o;
		return this.finished == s.finished && this.winner == s.winner && this.turn == s.turn
				&& Arrays.equals(this.board, s.board);
	}

	@Override
	public void setTurn(int turn) {
	this.turn = turn;
	}
	
	@Override
	public Object clone() {
		return new WolfAndSheepState(this, board, finished, winner);
	}

}