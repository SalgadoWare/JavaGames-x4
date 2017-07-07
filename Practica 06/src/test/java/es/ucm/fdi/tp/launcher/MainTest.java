package es.ucm.fdi.tp.launcher;

import static org.junit.Assert.*;

import org.junit.Test;

public class MainTest {

	/**
	 * Test if the application can handle too many arguments when creating the game
	 */
	@Test
	public void testTooManyArguments() {
		System.out.println("Too Many Arguments Test:\n");
		try {
			// Create an input with too many arguments
			String input = "was rand rand rand";
			String[] args = input.split(" +");
			// Execute the application with the given input
			MainP4.main(args);
		}
		catch (IllegalArgumentException e) {
			// Test failed if the exception was not handled
			fail("Too many arguments was not considered an exception");
		}
	}
	
	/**
	 * Test if the application can handle very few arguments when creating the game
	 */
	@Test
	public void testVeryFewArguments() {
		System.out.println("Very Few Arguments Test:\n");
		try {
			// Create an input with very few arguments
			String input = "was rand";
			String[] args = input.split(" +");
			// Execute the application with the given input
			MainP4.main(args);
		}
		catch (IllegalArgumentException e) {
			// Test failed if the exception was not handled
			fail("Too many arguments was not considered an exception");
		}
	}
	
	/**
	 * Test if the application can handle the creation of a game not defined
	 */
	@Test
	public void testGameNotDefined() {
		System.out.println("Game Not Defined Test:\n");
		try {
			// Create an input with a game not defined
			String input = "chess rand rand";
			String[] args = input.split(" +");
			// Execute the application with the given input
			MainP4.main(args);
		}
		catch (IllegalArgumentException e) {
			// Test failed if the exception was not handled
			fail("A game not defined was not considered an exception");
		}
	}

}
