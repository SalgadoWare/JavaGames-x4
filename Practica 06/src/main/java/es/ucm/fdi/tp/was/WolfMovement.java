package es.ucm.fdi.tp.was;

public enum WolfMovement {
	// List of possible movements of the wolf with the associated row and column shifts
	UPLEFT(-1, -1), UPRIGHT(-1, 1),
	DOWNLEFT(1, -1), DOWNRIGHT(1, 1);
	
	private int row;
	private int col;
	
	/**
	 * Sets the parameters of the enum
	 * @param row is the row shift
	 * @param col is the column shift
	 */
	WolfMovement(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	/**
	 * Getter of row shift
	 * @return row shift
	 */
	public int getRow() {
		return row;
	}
	
	/**
	 * Getter of column shift
	 * @return column shift
	 */
	public int getCol() {
		return col;
	}
	
}
