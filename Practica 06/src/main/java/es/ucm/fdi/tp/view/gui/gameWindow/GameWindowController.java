package es.ucm.fdi.tp.view.gui.gameWindow;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.view.gui.gameWindow.GameWindow.PlayerMode;

/**
 * Used like a bridge between the frame (GameWindow) and Settings
 * @author alex
 *
 */
public interface GameWindowController<S extends GameState<S, A>, A extends GameAction<S, A>> {
	
	public void changePlayerMode(PlayerMode newPlayer);

	public void randPlayerMove();

	public void smartPlayerMove();

	public void restartGame();
	
	public void cancelAImove();
	
	public void setStateToComeBack(int n);	
	
	public void avoidTurn();
}
