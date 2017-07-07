package es.ucm.fdi.tp.view.gui.gameView;

import javax.swing.JPanel;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.view.gui.GameController;

/**
 * Used like a bridge between the frame (GameWindow), and the board and the
 * InfoArea
 * 
 * @author alex
 *
 * @param <S>
 * @param <A>
 */
public abstract class GameView<S extends GameState<S, A>, A extends GameAction<S, A>> extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected int idPlayer;
	protected GameController<?, ?> gameCtrl;

	public abstract void setGameViewCtrl(GameController<?, ?> control);

	/**
	 * Better than two methods enable() and disable()
	 */
	public abstract void setEnabled(boolean enable);

	/**
	 * 
	 * @param state
	 *            from GameEvent
	 */
	public abstract void update(GameState<?, ?> state);

	/**
	 * for send messages to InfoArea
	 * 
	 * @param message
	 */
	public abstract void showInfoMessage(String message);

	/**
	 * clear messages to InfoArea
	 */
	public abstract void clearMessages();
	
	
	
	//extra
	public abstract void actionToShine(GameAction<?, ?> act) ;
	
	public abstract void clearPoints();
}
