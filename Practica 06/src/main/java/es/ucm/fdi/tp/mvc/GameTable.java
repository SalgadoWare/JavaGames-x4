package es.ucm.fdi.tp.mvc;

import java.util.ArrayList;
import java.util.List;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameError;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;

/**
 * An event-driven game engine. Keeps a list of players and a state, and
 * notifies observers of any changes to the game.
 */
public class GameTable<S extends GameState<S, A>, A extends GameAction<S, A>> implements GameObservable<S, A> {

	protected S currentState;
	protected S initialState;
	protected boolean active;
	protected int numPlayers;

	@SuppressWarnings("rawtypes")
	protected List<GameObserver> my_observers;

	@SuppressWarnings("rawtypes")
	public GameTable(S initState) {

		if (initState == null) {
			throw new GameError("Initial State is null\n");
		}
		initialState = initState;
		currentState = initState;
		active = false;
		my_observers = new ArrayList<GameObserver>();
		// agreement numPlayers = 2;
	}

	public void start() {
		this.currentState = this.initialState;
		active = true;
		notifyObservers(new GameEvent<S, A>(EventType.Start, null, currentState, null, "The game has started\n"));
	}

	public void stop() {
		if (active) {
			active = false;
			notifyObservers(new GameEvent<S, A>(EventType.Stop, null, currentState, null, "The game has stopped\n"));
		}
	}

	public void execute(A action) {

		// first steep, check general errors
		if (active && !currentState.isFinished() && action.getPlayerNumber() == currentState.getTurn()) {
			List<A> validActions = currentState.validActions(action.getPlayerNumber());

			// second steep, check the action
			if (validActions.contains(action)) {
				currentState = action.applyTo(currentState);

				if (currentState.isFinished()) {
					stop();

					//third, check draws, winners...
					if (currentState.getWinner() >= 0) {
						notifyObservers(new GameEvent<S, A>(EventType.Stop, action, currentState, null,
								"PLAYER " + action.getPlayerNumber() + " HAS WON !!\n"));
					} else {
						notifyObservers(
								new GameEvent<S, A>(EventType.Stop, action, currentState, null, "The are a draw\n "));
					}

				} else
					notifyObservers(new GameEvent<S, A>(EventType.Change, action, currentState, null,
							"The game state has changed\n"));
			} else {
				GameError err = new GameError("Cannot perform action\n");
				notifyObservers(new GameEvent<S, A>(EventType.Error, action, currentState, err, "Invalid action!\n"));
			}
		} else {
			GameError err = new GameError("Cannot perform action\n");
			notifyObservers(new GameEvent<S, A>(EventType.Error, action, currentState, err, "Invalid player!\n"));
		}
	}

	public S getState() {
		return currentState;
	}

	public void addObserver(GameObserver<S, A> o) {
		my_observers.add(o);
		if (active)
			notifyObservers(new GameEvent<>(EventType.Info, null, currentState, null, "A player has join!\n"));
		// new possible implementations:// numPlayers++;
	}

	public void removeObserver(GameObserver<S, A> o) {
		my_observers.remove(o);
	}

	@SuppressWarnings("unchecked")
	private void notifyObservers(GameEvent<S, A> event) {
		for (GameObserver<S, A> o : my_observers) {
			o.notifyEvent(event);
		}
	}

	public boolean isActive() {
		return active;
	}

	public void setState(S state) {
		currentState = state;
		notifyObservers(new GameEvent<>(EventType.Change, null, currentState, null, "The game state has changed\n"));
	}
}
