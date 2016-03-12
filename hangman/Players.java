package hangman;
import java.io.Serializable;

public class Players implements Serializable{
	private String name;
	private int scores;

	public Players(String name, int scores) {
		this.name = name;
		this.scores = scores;
	}

	public String getName() {
		return name; // returns player's name
	}

	public int getScores() {
		return scores; // returns player's scores
	}
}
