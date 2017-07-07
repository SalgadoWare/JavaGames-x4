package es.ucm.fdi.tp.view.console;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameObservable;
import es.ucm.fdi.tp.mvc.GameObserver;

public class ConsoleView<S extends GameState<S, A>, A extends GameAction<S, A>> implements GameObserver<S, A> {

	public ConsoleView(GameObservable<S, A> gameTable) {
		gameTable.addObserver(this);
	}

	@Override
	public void notifyEvent(GameEvent<S, A> e) {
		System.out.println("Current state:\n" + e.getState());

		if (e.getState().isFinished()) {

			String endText = "The game has ended: ";
			int winner = e.getState().getWinner();

			if (winner == -1) {
				endText += "there is a draw!";
			} else {
				endText += "player " + winner + " has won!";
			}
			System.out.println(endText);

		} else {
			System.out.println("Turn for player: " + e.getState().getTurn());
		}
	}
}
