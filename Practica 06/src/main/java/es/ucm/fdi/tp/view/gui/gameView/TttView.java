package es.ucm.fdi.tp.view.gui.gameView;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.ttt.TttAction;
import es.ucm.fdi.tp.ttt.TttState;

public class TttView<S extends GameState<S, A>, A extends GameAction<S, A>> extends RectBoardGameView<S, A> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TttView(int idPlayer) {
		super.idPlayer = idPlayer;
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
			GameAction<TttState, TttAction> action = new TttAction(idPlayer, row, col);
			super.gameCtrl.makeAmove(action);
		}
	}

	@Override
	protected void keyTyped(int keyCode) {
	}
}
