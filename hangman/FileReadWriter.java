package hangman;

import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class FileReadWriter {
	private ObjectOutputStream output;
	private ObjectInputStream input;
	ArrayList<Players> myArr = new ArrayList<>();

	public void openFileToWrite() {
		try {
			output = new ObjectOutputStream(new FileOutputStream("players.ser", true)); // open file
		} catch (IOException ioException) {
			printErr("Opening file.");
		}
	}

	// add records to file
	public void addRecords(int scores, String name) {
		Players players = new Players(name, scores); // object to be written to file

		try { // output values to file
			output.writeObject(players); // output players
		} catch (IOException ioException) {
			printErr("Writing to file.");
		}
	}

	public void closeFileFromWriting() {
		try {
			if (output != null){
			  output.close(); // close file
			}
		} catch (IOException ioException) {
			printErr("Closing file.");
			System.exit(1); // exit program
		}
	}

	public void openFileToRead() {
		try {
			input = new ObjectInputStream(new FileInputStream("players.ser")); // open file
		} catch (IOException ioException) {
			printErr("Opening file.");
		}
	}

	public void printErr(String msg) {
		System.err.println("Error: " + msg); // print error
	}

	public void readRecords() {
		Players records;
		Object obj;

		try { // input the values from the file
			obj = input.readObject();
			if (obj instanceof Players) {
				records = (Players) obj;
				myArr.add(records);
				System.out.printf("DEBUG: %-10d%-12s\n", records.getScores(), records.getName());
			}
		}

		// print correct errors for different catch instances
		catch (ClassNotFoundException classNotFoundException) {
			System.err.println("Unable to create object.");
		} catch (IOException ioException) {
			printErr("During reading from file.");
		}
	}

	public void closeFileFromReading() {
		try {
			if (input != null) input.close(); // close file
			System.exit(0);
		} catch (IOException ioException) {
			printErr("Closing file.");
			System.exit(1); // exit program
		}
	}

	public void printAndSortScoreBoard() {
		Players temp = null;
		int n = myArr.size();
		int pass = 1;
		while (pass < n) {
			temp = setPlayer(temp, n, pass);
			pass++;
		}

		System.out.println("Scoreboard:");
		for (int i = 0; i < myArr.size(); i++) {
			System.out.printf("%d. %s ----> %d", i, myArr.get(i).getName(), myArr.get(i).getScores());
		}
	}

	public Players setPlayer(Players temp, int n, int pass) {
		for (int i = 0; i < n - pass; i++) {
			if (myArr.get(i).getScores() > myArr.get(i + 1).getScores()) {
				temp = myArr.get(i);
				myArr.set(i, myArr.get(i + 1));
				myArr.set(i + 1, temp);
			}
		}
		return temp;
	}
}