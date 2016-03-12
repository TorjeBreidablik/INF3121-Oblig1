package hangman;

import java.util.Random;
import java.util.Scanner;

public class Game {
	// the words that can be guessed. The program picks one at random every time
	private static final String[] wordForGuessing = { "computer", "programmer",
			"software", "debugger", "compiler", "developer", "algorithm",
			"array", "method", "variable" };

	private String guessWord;
	private StringBuffer dashedWord;
	private FileReadWriter filerw;

	public Game(boolean autoStart) {
		guessWord = getRandWord();
		dashedWord = getW(guessWord);
		filerw = new FileReadWriter();
		if(autoStart) displayMenu();
	}

	private String getRandWord() {
		Random rand = new Random();
		return wordForGuessing[rand.nextInt(9)]; // returns a random word from String[] wordForGuessing
	}

	public void displayMenu() {
		System.out.println("Welcome to �Hangman� game. Please, try to guess my secret word.\n"
						+ "Use 'TOP' to view the top scoreboard, 'RESTART' to start a new game,"
						+ "'HELP' to cheat and 'EXIT' to quit the game.");

		findLetterAndPrintIt(); // starts a new sequence of guessing a word
	}

	private void findLetterAndPrintIt() {
		boolean isHelpUsed = false;
		String letter;
		StringBuffer dashBuff = new StringBuffer(dashedWord);
		int mistakes = 0;

		do { // execute this until guessWord is found
			System.out.println("The secret word is: " + printDashes(dashBuff));
			System.out.println("DEBUG " + guessWord);
			do { // execute this while input is not letter (a-z)
				System.out.println("Enter your guess (1 letter allowed): ");
				letter = (new Scanner(System.in)).next();

				if (letter.equalsIgnoreCase("help")) { // executes this if player wants to cheat
					isHelpUsed = true;
					int i = 0, j = 0;
					while (j < 1) {
						if (dashBuff.charAt(i) == '_') {
							dashBuff.setCharAt(i, guessWord.charAt(i));
							++j;
						}
						++i;
					}
					System.out.println("The secret word is: " + printDashes(dashBuff));
				} // end if
				menu(letter);

			} while (!letter.matches("[a-z]"));


			int counter = 0;
			for (int i = 0; i < guessWord.length(); i++) {
				String currentLetter = Character.toString(guessWord.charAt(i));
				if (letter.equals(currentLetter)) {
					++counter;
					dashBuff.setCharAt(i, letter.charAt(0));
				}
			}

			if (counter == 0) {
				++mistakes;
				System.out.printf("Sorry! There are no unrevealed letters \'%s\'. \n", letter);
			} else System.out.printf("Good job! You revealed %d letter(s).\n", counter);

		} while (!dashBuff.toString().equals(guessWord));

		if (!isHelpUsed) { // executes this if player didn't cheat
			System.out.println("You won with " + mistakes + " mistake(s).");
			System.out.println("The secret word is: " + printDashes(dashBuff));

			System.out.println("Please enter your name for the top scoreboard:");
			String playerName = (new Scanner(System.in)).next(); // takes input for registering player on scoreboard

			filerw.openFileToWrite();
			filerw.addRecords(mistakes, playerName);
			filerw.closeFileFromWriting();
			filerw.openFileToRead();
			filerw.readRecords();
			filerw.closeFileFromReading();
			filerw.printAndSortScoreBoard();
		} else { // executes this if player cheated
			System.out
					.println("You won with "
							+ mistakes
							+ " mistake(s). but you have cheated. You are not allowed to enter into the scoreboard.");
			System.out.println("The secret word is: " + printDashes(dashBuff));
		}
		new Game(true); // restarts the game
	}

	private void menu(String letter) {
		if (letter.equalsIgnoreCase("restart")) // starts a new game
			new Game(true);
		else if (letter.equalsIgnoreCase("top")) { // shows scoreboard
			filerw.openFileToRead();
			filerw.readRecords();
			filerw.closeFileFromReading();
			filerw.printAndSortScoreBoard();
			new Game(true);
		} else if (letter.equalsIgnoreCase("exit")) // exits program
			System.exit(1);
	}

	private StringBuffer getW(String word) {
		StringBuffer dashes = new StringBuffer("");
		for (int i = 0; i < word.length(); i++) {
			dashes.append("_");
		}
		return dashes;
	}

	private String printDashes(StringBuffer word) { // formats string with spaces between each letter
		String toDashes = "";
		for (int i = 0; i < word.length(); i++) {
			toDashes += (" " + word.charAt(i));
		}
		return toDashes;
	}
}
