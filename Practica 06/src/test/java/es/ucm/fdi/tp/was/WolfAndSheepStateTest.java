package es.ucm.fdi.tp.was;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.junit.Test;

import es.ucm.fdi.tp.base.model.GamePlayer;
import es.ucm.fdi.tp.base.player.RandomPlayer;
import es.ucm.fdi.tp.base.player.ConcurrentAiPlayer;

public class WolfAndSheepStateTest {

	/**
	 * Test if the wolf gets trapped, the sheeps have won
	 */
	@Test
	public void testSheepIsWinner() {
		System.out.println("Sheep Winning Test:\n");
		// Create a list with a smart sheep and a not very smart wolf
		List<GamePlayer> players = new ArrayList<GamePlayer>();
		players.add(new RandomPlayer("Wolf"));
		players.add(new ConcurrentAiPlayer("Sheep"));
		int playerCount = 0;
		for (GamePlayer p : players)
			p.join(playerCount++);
		// Initialize the first state of the game
		WolfAndSheepState state = new WolfAndSheepState();
		// Loop until the game is finished
		while (!state.isFinished()) {
			WolfAndSheepAction action = players.get(state.getTurn()).requestAction(state);
			state = action.applyTo(state);
			System.out.println("After action:\n" + state);
		}
		System.out.println("The game ended: player " + players.get(state.getWinner()).getName() + " won!");
		// Check if the winner is the sheep
		if (state.getWinner() == 1) {
			// Check if the sheeps have won because the wolf can not make any move
			if (!WolfAndSheepState.isWinner(state.getBoard(), state.getWinner()))
				// Test failed if the wolf can make any move
				fail("Sheep have not won yet");
		}
	}
	
	/**
	 * Test if the wolf gets to the upper row, it has won
	 */
	@Test
	public void testWolfIsWinner() {
		System.out.println("Wolf Winning Test:\n");
		// Create a list with a smart wolf and a not very smart sheep
		List<GamePlayer> players = new ArrayList<GamePlayer>();
		players.add(new ConcurrentAiPlayer("Wolf"));
		players.add(new RandomPlayer("Sheep"));
		int playerCount = 0;
		for (GamePlayer p : players)
			p.join(playerCount++);
		// Initialize the first state of the game
		WolfAndSheepState state = new WolfAndSheepState();
		// Loop until the game is finished
		while (!state.isFinished()) {
			WolfAndSheepAction action = players.get(state.getTurn()).requestAction(state);
			state = action.applyTo(state);
			System.out.println("After action:\n" + state);
		}
		System.out.println("The game ended: player " + players.get(state.getWinner()).getName() + " won!");
		// Check if the winner is the wolf
		if (state.getWinner() == 0) {
			// Check if the wolf has won because the it is on the upper row
			if (!WolfAndSheepState.isWinner(state.getBoard(), state.getWinner()))
				// Test failed if the wolf is not on the upper row
				fail("Wolf has not won yet");
		}
	}
	
	/**
	 * Test if the wolf has only 1 valid action on his first movement, and 4 valid actions on his second movement
	 */
	@Test
	public void testWolfMovement() {
		System.out.println("Wolf Movement Test:\n");
		// Initialize the first state of the game
		WolfAndSheepState state = new WolfAndSheepState();
		// Create a list with the valid actions for the wolf
		List<WolfAndSheepAction> actions = state.validActions(state.getTurn());
		System.out.println(actions);
		// Check if the wolf has only 1 valid action
		if (actions.size() == 1) {
			// Play as the wolf and create the next state
			state = actions.get(0).applyTo(state);
			// Fill the list with the valid actions for the sheep
			actions = state.validActions(state.getTurn());
			// Play as the sheep and create the next state
			state = actions.get(new Random().nextInt(actions.size())).applyTo(state);
			// Fill the list with the valid actions for the wolf
			actions = state.validActions(state.getTurn());
			System.out.println(actions);
			if (actions.size() != 4) fail("Wolf has not 4 valid actions on next turn");
			// Test failed if the wolf has not 4 valid actions on his second movement
		}
		else fail("Wolf has more than one valid action on initial position");
		// Test failed if the wolf has more than 1 valid action on his first movement
	}
	
	/**
	 * Test if the sheep has 2 valid actions on his first movement, and 1 valid action on a side of the board
	 */
	@Test
	public void testSheepMovement() {
		System.out.println("Sheep Movement Test:\n");
		// Initialize the first state of the game
		WolfAndSheepState state = new WolfAndSheepState();
		// Create a list with the valid actions for the wolf
		List<WolfAndSheepAction> actions = state.validActions(state.getTurn());
		// Play as the wolf and create the next state
		state = actions.get(new Random().nextInt(actions.size())).applyTo(state);
		// Fill the list with the valid actions for the sheep
		actions = state.validActions(state.getTurn());
		System.out.println(actions);
		// Fill an array with the number of possible initial movements of each column
		int[] movements = new int[state.getDimension()];
		for (int i = 0; i < actions.size(); i++) movements[actions.get(i).getIniCol()]++;
		for (int m : movements) {
			// Check if each column has either 0 or 2 possible movements, and 0 or 1 on a side of the board
			if (m > 0 && m < movements.length - 1) {
				if (movements[m] != 0 && movements[m] != 2) fail("Sheep has not 2 valid actions on initial position");
				// Test failed if the sheep has not 2 valid actions on initial position
			}
			else {
				if (movements[m] != 0 && movements[m] != 1) fail("Sheep has not 1 valid action on a side");
				// Test failed if the sheep has not 1 valid actions on a side of the board
			}
		}
	}
	
}
