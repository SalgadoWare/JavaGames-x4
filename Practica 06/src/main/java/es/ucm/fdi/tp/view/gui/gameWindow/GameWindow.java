package es.ucm.fdi.tp.view.gui.gameWindow;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.mvc.GameEvent;
import es.ucm.fdi.tp.mvc.GameEvent.EventType;
import es.ucm.fdi.tp.mvc.GameObservable;
import es.ucm.fdi.tp.mvc.GameObserver;
import es.ucm.fdi.tp.mvc.GameTable;
import es.ucm.fdi.tp.view.gui.GameController;
import es.ucm.fdi.tp.view.gui.gameView.GameView;
import es.ucm.fdi.tp.view.gui.settings.Settings;
import es.ucm.fdi.tp.view.gui.settings.SettingsUse;

@SuppressWarnings("rawtypes")
public class GameWindow<S extends GameState<S, A>, A extends GameAction<S, A>> extends JFrame
		implements GameObserver, GameWindowController {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private GameObservable<?, ?> game;
	private GameController<?, ?> gameCtrl;
	private int playerId;
	private GameView<?, ?> gameView;
	private SettingsUse settings;
	private GamePlayer randPlayer, smartPlayer;
	private S state; // current state of the game table

	private boolean enable;
	private PlayerMode playerMode;

	public static ArrayList<GameState> all_states;

	private Thread smartThread;

	public enum PlayerMode {
		MANUAL("manual"), RANDOM("random"), SMART("smart");
		private String name;

		PlayerMode(String name) {
			this.name = name;
		}

		public String toString() {
			return name;
		}
	}

	@SuppressWarnings("unchecked")
	public GameWindow(int playerId, GameView<?, ?> gameView, GameController<?, ?> gameCtrl, GameObservable<?, ?> game,
			GamePlayer randPlayer, GamePlayer smartPlayer, ArrayList<GameState> states) {

		super(((GameTable<?, ?>) game).getState().getGameDescription() + " (Player " + playerId + ") ");
		this.playerId = playerId;
		this.gameView = gameView;
		this.gameCtrl = gameCtrl;
		this.game = game;
		this.randPlayer = randPlayer;
		this.smartPlayer = smartPlayer;
		this.gameView.setGameViewCtrl(this.gameCtrl);
		this.settings = new Settings(this);

		all_states = states;

		initGui();

		this.game.addObserver(this);
		this.enable = false;

		// agreement
		this.playerMode = PlayerMode.MANUAL;

	}

	private void initGui() {
		this.getContentPane().setLayout(new BorderLayout());
		this.add((Component) settings, BorderLayout.PAGE_START);
		this.getContentPane().add(gameView, BorderLayout.CENTER);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setVisible(true);
	}

	public void setEnabled(boolean enable) {
		gameView.setEnabled(enable);
		settings.setEnabled(enable);
	}

	@SuppressWarnings("unchecked")
	private void handleEvent(GameEvent<?, ?> e) {
		state = (S) e.getState();
		int cturn = e.getState().getTurn();
		enable = playerId == cturn;

		gameView.clearPoints();

		if (enable) {
			shinePossibleActions();
			setEnabled(true);
		} else {
			setEnabled(false);
		}

		// more cases? remember stop button uses stop even type
		if (playerId == 1 && e.getType() != EventType.Error) {
			all_states.add(state);
		}

		switch (e.getType()) {

		case Change:
			gameView.update(e.getState());
			gameView.showInfoMessage(e.toString());

			// keeping order in the move thanks to the enable field
			if (enable) {
				// automatic moves
				switch (playerMode.name) {

				case "random":
					randPlayerMove();
					break;

				case "smart":
					smartPlayerMove();
					break;
				default:
					break;
				}
			}
			gameView.showInfoMessage("Turn for player " + cturn + "\n");
			break;

		case Error:
			if (this.playerId == e.getAction().getPlayerNumber())
				gameView.showInfoMessage(e.toString() + e.getError().getMessage());
			break;

		case Info:
			gameView.showInfoMessage(e.toString());
			break;

		case Start:
			if (playerId == cturn) {
				setLocation(80, 80);
			} else {
				setLocation(875, 80);
			}

			gameView.clearMessages();
			gameView.update(e.getState());
			gameView.showInfoMessage(
					"Ai message: Available cores: " + Runtime.getRuntime().availableProcessors() + '\n');
			gameView.showInfoMessage(e.toString() + "Turn for player 0\n");
			break;

		case Stop:
			gameView.update((GameState) e.getState());
			gameView.showInfoMessage(e.toString());
			setEnabled(false);
		default:
			break;
		}
	}

	private void shinePossibleActions() {
		for (A act : state.validActions(state.getTurn())) {
			gameView.actionToShine((GameAction<?, ?>) act);
		}

		gameView.repaint();
	}

	@Override
	public void notifyEvent(final GameEvent e) {
		try {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					handleEvent(e);
				}
			});
		} catch (Exception e1) {
			gameView.showInfoMessage("SWING EDT ERROR 1");
		}
	}

	@Override
	public void restartGame() {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gameCtrl.startGame();
			}
		});

		all_states.clear();
	}

	@Override
	public void randPlayerMove() {
		if (enable) {
			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					gameCtrl.makeAmove(randPlayer.requestAction(state));

					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						gameView.showInfoMessage("SLEEPING THREAD ERROR");
					}
				}
			});
		}
	}

	@Override
	public void smartPlayerMove() {
		if (enable) {
			setEnabled(false);
			settings.IAbuttonWasPressed();
			smartThread = new Thread(new Runnable() {

				@Override
				public void run() {
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							//change JPanel color
							settings.setAIthought(true);
						}
					});

					//casting is needed because the interface GamePlayer hasn't this methods
					((ConcurrentAiPlayer) smartPlayer).setMaxThreads(settings.getThreadsValue());
					((ConcurrentAiPlayer) smartPlayer).setTimeout(settings.getTimerValue());
					
					long time0 = System.currentTimeMillis();
					final GameAction action = smartPlayer.requestAction(state);
					long time1 = System.currentTimeMillis();

					if (action != null) {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								settings.setAIthought(false);
								gameCtrl.makeAmove(action);
							}
						});
						
						int totalNodes = ((ConcurrentAiPlayer) smartPlayer).getEvaluationCount();
						long totalTime = (time1 - time0);
						int p = (int) (totalNodes / totalTime);
						gameView.showInfoMessage(totalNodes + " nodes in " + totalTime + "ms.  " + p + "n/ms. Value: "
								+ ((ConcurrentAiPlayer) smartPlayer).getValue() + '\n');
						
					} else {
						SwingUtilities.invokeLater(new Runnable() {
							@Override
							public void run() {
								settings.setAIthought(false);
							}
						});
						gameView.showInfoMessage("AI Message: Time Out!\n");
					}
				}
			});
			smartThread.start();
		}
	}

	@Override
	public void changePlayerMode(PlayerMode newPlayer) {
		gameView.showInfoMessage("Now yow play like " + newPlayer.toString() + " player\n");
		playerMode = newPlayer;
	}

	@Override
	public void cancelAImove() {

		setEnabled(true);
		if (smartThread != null) {

			smartThread.interrupt();

			SwingUtilities.invokeLater(new Runnable() {
				@Override
				public void run() {
					settings.setAIthought(false);
				}
			});
			gameView.showInfoMessage("Smart move cancelled!!\n");
		} else {
			gameView.showInfoMessage("There arent any smart thread!!\n");
		}
	}

	@Override
	public void setStateToComeBack(int state_n) {
		if (state_n != -1) {
			GameState newState = (GameState) all_states.get(state_n);
			int total_states = all_states.size();
			int count = total_states - state_n;
			for (int i = 0; i < count; i++) {
				all_states.remove(state_n);
			}
			setState(newState);
		} else
			gameView.showInfoMessage("You cant come back those states");
	}

	private void setState(GameState newState) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				gameCtrl.setState(newState);
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public void avoidTurn() {
		S new_state = (S) this.state.clone();
		new_state.setTurn((state.getTurn() + 1) % 2);
		setState(new_state);
	}
}
