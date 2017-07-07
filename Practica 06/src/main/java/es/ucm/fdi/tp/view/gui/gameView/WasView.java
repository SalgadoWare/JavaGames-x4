package es.ucm.fdi.tp.view.gui.gameView;

import java.awt.event.KeyEvent;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.was.WolfAndSheepAction;

public class WasView<S extends GameState<S, A>, A extends GameAction<S, A>> extends RectBoardGameView<S, A> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// control mouse
	private Integer lastClickedRow;
	private Integer lastClickedCol;

	public WasView(int idPlayer) {
		super.idPlayer = idPlayer;
		lastClickedCol = null;
		lastClickedRow = null;
	}

	@Override
	protected int getNumCols() {
		return super.state == null ? -1 : super.state.getDimension();
	}

	@Override
	protected int getNumRows() {
		return super.state == null ? -1 : super.state.getDimension();
	}

	@Override
	protected Integer getPosition(int row, int col) {
		return super.state == null ? null : super.state.at(row, col);
	}

	@Override
	protected void mouseClicked(int row, int col, int clickCount, int mouseButton) {

		if (clickCount == 1 && mouseButton == 1) {
			if (lastClickedCol == null) {
				lastClickedCol = col;
				lastClickedRow = row;
			} else {
				int iniRow = (int) lastClickedRow;
				int iniCol = (int) lastClickedCol;

				GameAction<?, ?> action = new WolfAndSheepAction(super.idPlayer, iniRow, iniCol, row, col);
				super.gameCtrl.makeAmove(action);

				lastClickedCol = null;
				lastClickedRow = null;
			}
		}
	}

	@Override
	protected void keyTyped(int keyCode) {
		if (super.idPlayer == super.state.getTurn() && keyCode == KeyEvent.VK_ESCAPE) {
			lastClickedCol = null;
			lastClickedRow = null;
		}
	}
}
