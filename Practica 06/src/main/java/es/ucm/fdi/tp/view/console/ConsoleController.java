package es.ucm.fdi.tp.view.console;

import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameTable;
import es.ucm.fdi.tp.view.gui.GameController;

public class ConsoleController<S extends GameState<S, A>, A extends GameAction<S, A>> extends GameController<S, A>
		implements Runnable {

	protected List<GamePlayer> players;
	protected boolean stop;

	public ConsoleController(List<GamePlayer> players, GameTable<S, A> game) {
		super(game);

		//check possible errors
		for (int i = 0; i < players.size(); i++) {
			if (players.get(i).getPlayerNumber() != i) {
				throw new GameError("Not correct list of players");
			}
		}

		this.players = players;
		this.stop = true;
	}

	private void playGame() {
		super.startGame();
		
		//game main loop
		while (!game.getState().isFinished() && !this.stop) {
			A action = players.get(game.getState().getTurn()).requestAction(game.getState());
			super.makeAmove(action);
		}
	}

	/**
	 * implementing the runnable
	 */
	public void run() {
		this.stop = false;
		this.playGame();
		this.stop = true;
	}
}
