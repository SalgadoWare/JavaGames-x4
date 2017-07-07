package es.ucm.fdi.tp.launcher;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import es.ucm.fdi.tp.base.model.GameAction;
import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.model.GameState;
import es.ucm.fdi.tp.base.console.ConsolePlayer;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.ttt.TttState;
import es.ucm.fdi.tp.was.WolfAndSheepState;

public class MainP4 {
	
	// Console input used for playing the games
	public static Scanner in = new Scanner(System.in);
	
	/**
	 * Creates a new game and plays until it is finished
	 * @param initialState of the game
	 * @param players of the game
	 * @return winner of the game
	 */
	public static <S extends GameState<S, A>, A extends GameAction<S, A>> int playGame(GameState<S, A> initialState, List<GamePlayer> players) {
		int playerCount = 0;
		// Assign players to the game
		for (GamePlayer p : players)
			p.join(playerCount++); // welcome each player, and assign playerNumber
		@SuppressWarnings("unchecked")
		S currentState = (S) initialState;
		// Loop until the game is finished
		while (!currentState.isFinished()) {
			// Request a move to the actual player
			A action = players.get(currentState.getTurn()).requestAction(currentState);
			// Apply a move to the current state
			currentState = action.applyTo(currentState);
			System.out.println("After action:\n" + currentState);
			// Check if the game is finished to print the final message
			if (currentState.isFinished()) {
				String endText = "The game ended: ";
				int winner = currentState.getWinner();
				if (winner == -1) {
					endText += "draw!";
				} else {
					endText += "player " + (winner + 1) + " (" + players.get(winner).getName() + ") won!";
				}
				System.out.println(endText);
			}
		}
		return currentState.getWinner();
	}
	
	/**
	 * Creates the initial state of the game
	 * @param gameName is the name of the game
	 * @return the initial state of the given game
	 */
	public static GameState<?,?> createInitialState(String gameName)
			throws IllegalArgumentException {
		if (gameName.equalsIgnoreCase("ttt")) return new TttState(3);
		else if (gameName.equalsIgnoreCase("was")) return new WolfAndSheepState();
		else throw new IllegalArgumentException("Error: game " + gameName + " is not defined");
	}
	
	/**
	 * Creates a player for a game
	 * @param gameName is the name of the game
	 * @param playerType is the type of player
	 * @param playerName is the name of the player
	 * @return a new player of the given type for the given name
	 */
	public static GamePlayer createPlayer(String gameName, String playerType, String playerName)
			throws IllegalArgumentException {
		if (playerType.equalsIgnoreCase("console")) return new ConsolePlayer(playerName, in);
		else if (playerType.equalsIgnoreCase("rand")) return new RandomPlayer(playerName);
		else if (playerType.equalsIgnoreCase("smart")) return new ConcurrentAiPlayer(playerName);
		else throw new IllegalArgumentException("Error: player " + playerType + " is not defined");
	}

	/**
	 * Main of the application: Creates and plays a game
	 * @param args contains the parameters to initialize the game
	 */
	public static void main(String[] args) {
		String []words;
		// Create the argument line with program arguments or console input
		if (args.length > 0) words = args;
		else {
			String line = in.nextLine();
			line = line.trim();
			words = line.split(" +");
		}
		try {
			if (words.length == 3) {
				// Create the initial state of the indicated game
				GameState<?, ?> game = createInitialState(words[0]);
				// Create the list with the indicated players
				List<GamePlayer> players = new ArrayList<GamePlayer>();
				players.add(createPlayer(words[0], words[1], "Diego"));
				players.add(createPlayer(words[0], words[2], "Alejandro"));
				// Execute the game
				playGame(game, players);
				in.close();
			}
			else if (words.length > 3) throw new IllegalArgumentException("Error: too many players for this game");
			else throw new IllegalArgumentException("Error: not enough arguments for this game");
		}
		catch (IllegalArgumentException e) {
			System.out.println(e);
		}
	}

}
