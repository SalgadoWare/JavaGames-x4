package es.ucm.fdi.tp.was;

import es.ucm.fdi.tp.base.model.GameAction;

/**
 * An action for WolfAndSheep.
 */
public class WolfAndSheepAction implements GameAction<WolfAndSheepState, WolfAndSheepAction> {

	private static final long serialVersionUID = -8491198872908329925L;

	private int player;
	private int iniRow;
	private int iniCol;
	private int finRow;
	private int finCol;

	/**
	 * Constructor creates the action with the given parameters
	 * 
	 * @param player
	 *            is the actual player
	 * @param iniRow
	 *            is the initial row of the action
	 * @param iniCol
	 *            is the initial column of the action
	 * @param finRow
	 *            is the final row of the action
	 * @param finCol
	 *            is the final column of the action
	 */
	public WolfAndSheepAction(int player, int iniRow, int iniCol, int finRow, int finCol) {
		this.player = player;
		this.iniRow = iniRow;
		this.iniCol = iniCol;
		this.finRow = finRow;
		this.finCol = finCol;
	}

	public boolean equals(Object action_o) {
		WolfAndSheepAction action = (WolfAndSheepAction) action_o;
		return action.finCol == this.finCol && action.finRow == this.finRow && action.iniCol == this.iniCol
				&& action.iniRow == this.iniRow && this.player == action.player;
	}

	/**
	 * Getter of player
	 * 
	 * @return player
	 */
	public int getPlayerNumber() {
		return player;
	}

	/**
	 * Makes a move of the player on a state of the game and creates the next
	 * state
	 * 
	 * @return the next state of the game
	 */
	public WolfAndSheepState applyTo(WolfAndSheepState state) {
		// Check if it is the turn of the player
		if (player != state.getTurn()) {
			throw new IllegalArgumentException("Not the turn of this player");
		}
		// Make a move on the board
		int[][] board = state.getBoard();
		board[iniRow][iniCol] = WolfAndSheepState.EMPTY;
		board[finRow][finCol] = player;
		// Create and update the next state of the game
		WolfAndSheepState next;
		if (WolfAndSheepState.isWinner(board, state.getTurn())) {
			next = new WolfAndSheepState(state, board, true, state.getTurn());
		} else {
			next = new WolfAndSheepState(state, board, false, -1);
		}
		return next;
	}

	/**
	 * Getter of initial row
	 * 
	 * @return initial row
	 */
	public int getIniRow() {
		return iniRow;
	}

	/**
	 * Getter of initial column
	 * 
	 * @return initial column
	 */
	public int getIniCol() {
		return iniCol;
	}

	/**
	 * Getter of the final row
	 * 
	 * @return final row
	 */
	public int getFinRow() {
		return finRow;
	}

	/**
	 * Getter of the final column
	 * 
	 * @return final column
	 */
	public int getFinCol() {
		return finCol;
	}

	public String toString() {
		String player;
		if (this.player == 0)
			player = "W";
		else
			player = "S (" + iniRow + ", " + iniCol + ")";
		return "place " + player + " at (" + finRow + ", " + finCol + ")";
	}

}
