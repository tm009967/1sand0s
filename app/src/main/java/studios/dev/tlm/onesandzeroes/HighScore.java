package studios.dev.tlm.onesandzeroes;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Created by tm009967 on 8/2/2014.
 * Major rework 2022-06-01: online high scores
 */
public class HighScore {
    private String name;
    private int numberOfRows;
    private int score;

    public HighScore() {}

    public HighScore(String name, int numberOfRows, int score) {
        this.name = name;
        this.numberOfRows = numberOfRows;
        this.score = score;
    }

    public String getName() {
        return name;
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getScore() {
        return score;
    }
}
