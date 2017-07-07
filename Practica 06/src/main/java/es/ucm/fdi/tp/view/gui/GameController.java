package es.ucm.fdi.tp.view.gui;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameTable;

public class GameController<S extends GameState<S, A>, A extends GameAction<S, A>> {

	protected GameTable<S, A> game;

	public GameController(GameTable<S, A> game2) {
		this.game = game2;
	}

	public void stopGame() {
		this.game.stop();
	}

	public void run() {
		this.startGame();
	}

	public void startGame() {
		game.start();
	}

	/*
	 * I have had problems with he parameter S action, so I have implemented it better with GameAction type
	 */
	@SuppressWarnings("unchecked")
	public void makeAmove(GameAction<?, ?> action) {
		if (game.isActive())
			game.execute((A) action);
	}
	
	@SuppressWarnings("unchecked")
	public void setState(GameState<?,?> state) {
		game.setState((S)state);
	}
}