package es.ucm.fdi.tp.launcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import javax.swing.SwingUtilities;
import es.ucm.fdi.tp.base.console.ConsolePlayer;
import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.mvc.GameTable;
import es.ucm.fdi.tp.ttt.TttAction;
import es.ucm.fdi.tp.ttt.TttState;
import es.ucm.fdi.tp.view.console.ConsoleController;
import es.ucm.fdi.tp.view.console.ConsoleView;
import es.ucm.fdi.tp.view.gui.GameController;
import es.ucm.fdi.tp.view.gui.gameView.GameView;
import es.ucm.fdi.tp.view.gui.gameView.TttView;
import es.ucm.fdi.tp.view.gui.gameView.WasView;
import es.ucm.fdi.tp.view.gui.gameWindow.GameWindow;
import es.ucm.fdi.tp.was.WolfAndSheepAction;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public class Main {

	// Console input used for playing the games
	private static Scanner sin = new Scanner(System.in);
	@SuppressWarnings("rawtypes")
	private static ArrayList<GameState> states = new ArrayList<>();

	/**
	 * 
	 * @param gType
	 * @return GameTable or null
	 */
	private static GameTable<?, ?> createGame(String gType) {
		try {
			if (gType.trim().equalsIgnoreCase("ttt")) {
				return new GameTable<TttState, TttAction>(new TttState(/* DIM: */3));
			} else {
				if (gType.trim().equalsIgnoreCase("was"))
					return new GameTable<WolfAndSheepState, WolfAndSheepAction>(new WolfAndSheepState());
				return null;
			}
		} catch (Exception e) {
			return null;
		}
	}

	private static <S extends GameState<S, A>, A extends GameAction<S, A>> void startConsoleMode(GameTable<S, A> game,
			String playerModes[]) {

		// create the list of players as in assignment 4
		List<GamePlayer> players = new ArrayList<GamePlayer>();

		try {
			for (int i = 0; i < playerModes.length; i++) {
				GamePlayer p = createPlayer(playerModes[i], "Player(" + i + ")");
				p.join(i);
				players.add(p);
			}
		} catch (IllegalArgumentException e) {
			System.out.println(e);
		}

		new ConsoleView<S, A>(game);
		new ConsoleController<S, A>(players, game).run();
	}

	/**
	 * 
	 * @param game
	 * @param gType
	 */
	private static <S extends GameState<S, A>, A extends GameAction<S, A>> void startGUIMode(final GameTable<S, A> game,
			final String gType) {

		GameController<S, A> gameCtrl = new GameController<S, A>(game);

		for (int i = 0; i < game.getState().getPlayerCount(); ++i) {

			GamePlayer random = createPlayer("rand", "random player for player " + i);
			random.join(i);
			GamePlayer smart = createPlayer("smart", "IA for player " + i);
			smart.join(i);

			final int j = i;

			try {
				SwingUtilities.invokeAndWait(new Runnable() {

					@Override
					public void run() {
						new GameWindow<S, A>(j, createGameView(gType, j), gameCtrl, game, random, smart, states);
					}
				});
			} catch (Exception e) {
				System.out.println("Swing exception");
			}
		}

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				game.start();
			}
		});
	}

	/**
	 * Creates a player for a game
	 * 
	 * @param gameName
	 *            is the name of the game
	 * @param playerType
	 *            is the type of player
	 * @param playerName
	 *            is the name of the player
	 * @return a new player of the given type for the given name
	 */
	private static GamePlayer createPlayer(String playerType, String playerName) throws IllegalArgumentException {
		if (playerType.equalsIgnoreCase("manual"))
			return new ConsolePlayer(playerName, sin);
		else if (playerType.equalsIgnoreCase("rand"))
			return new RandomPlayer(playerName);
		else if (playerType.equalsIgnoreCase("smart"))
			return new ConcurrentAiPlayer(playerName);
		else
			throw new IllegalArgumentException("Error: player " + playerType + " is not defined");
	}

	private static void usage() {
		System.out.println("-Syntax Application Main:-\n");
		System.out.println("-game mode player1 player2 (where...)-\n");
		System.out.println("-game = ttt || was-\n");
		System.out.println("-mode = console || gui-\n");
		System.out.println("-player = manual || smart || rand-\n");
	}

	private static GameView<?, ?> createGameView(String gType, int idPlayer) {
		GameView<?, ?> gameView = null;

		switch (gType) {
		case "ttt":
			gameView = new TttView<TttState, TttAction>(idPlayer);
			break;

		case "was":
			gameView = new WasView<WolfAndSheepState, WolfAndSheepAction>(idPlayer);
			break;

		default:
			break;
		}

		return gameView;
	}

	public static void main(String[] args) {
		if (args.length < 2) {
			System.err.println("Invalid arguments number");
			usage();
			System.exit(1);
		}
		GameTable<?, ?> game = createGame(args[0]);
		if (game == null) {
			System.err.println("Invalid game\n");
			usage();
			System.exit(1);
		}
		String[] otherArgs = Arrays.copyOfRange(args, 2, args.length);
		switch (args[1]) {
		case "console":
			startConsoleMode(game, otherArgs);
			break;
		case "gui":
			startGUIMode(game, args[0]);
			break;
		default:
			System.err.println("Invalid view mode: " + args[1] + "\n");
			usage();
			System.exit(1);
			break;
		}
	}
}
