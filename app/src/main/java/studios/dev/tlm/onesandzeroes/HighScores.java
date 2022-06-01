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
 */
public class HighScores {
    public class HighScore implements Comparable<HighScore> {
        public final String name;
        public final int numberOfRows;
        public final int score;

        public HighScore(String name, int numberOfRows, int score) {
            this.name = name;
            this.numberOfRows = numberOfRows;
            this.score = score;
        }

        public int compareTo(HighScore h) {
            Integer tmpScore = this.score;
            Integer tmpOtherScore = h.score;
            return tmpScore.compareTo(tmpOtherScore);
        }
    }

    private static String HIGH_SCORES_PREFS_KEY = "studios.dev.tlm.onesandzeroes.HIGH_SCORE_PREFS_KEY";
    private TreeSet<HighScore> setOfHighScores;
    private Context context;

    public HighScores(Context context) {
        this.setOfHighScores = new TreeSet<HighScore>();
        this.context = context;
        loadHighScores();
    }

    private Set<String> generateHighScoresStringSet() {
        Set<String> highScoresStringSet = new HashSet<String>();
        for (HighScore highScore : setOfHighScores) {
            highScoresStringSet.add(Integer.toString(highScore.score) + " " + Integer.toString(highScore.numberOfRows) + " " + highScore.name);
        }
        return highScoresStringSet;
    }

    public void saveHighScores() {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putStringSet(HIGH_SCORES_PREFS_KEY, generateHighScoresStringSet());
        editor.commit();
    }

    private void loadHighScoresFromStringSet(Set<String> highScoresStringSet) {
        this.setOfHighScores = new TreeSet<HighScore>();
        for (String str : highScoresStringSet) {
            String[] pieces = str.split(" ", 3);
            this.setOfHighScores.add(new HighScore(pieces[2], Integer.parseInt(pieces[1]), Integer.parseInt(pieces[0])));
        }
    }

    private Set<String> makeDefaultHighScoresStringSet() {
        Set<String> defaultScoresStringSet = new HashSet<String>();
        defaultScoresStringSet.add("9000 4 _Alice");
        defaultScoresStringSet.add("7000 4 _Bob");
        defaultScoresStringSet.add("5000 4 _Charlie");
        defaultScoresStringSet.add("4000 4 _Dave");
        defaultScoresStringSet.add("3100 4 _Eve");
        defaultScoresStringSet.add("2400 4 _Freckle");
        defaultScoresStringSet.add("1700 3 _Gloop");
        defaultScoresStringSet.add("1000 2 _Hal");
        defaultScoresStringSet.add("750 2 _Ivy");
        defaultScoresStringSet.add("500 1 _Julius");

        return defaultScoresStringSet;
    }

    public void loadHighScores() {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        Set<String> highScoresStringSet = sharedPref.getStringSet(HIGH_SCORES_PREFS_KEY, makeDefaultHighScoresStringSet());
        loadHighScoresFromStringSet(highScoresStringSet);
    }

    public void resetHighScores() {
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.remove(HIGH_SCORES_PREFS_KEY);
        editor.commit();

        loadHighScores();
    }

    public boolean isNewHighScore(int newScore) {
        HighScore lowestHighScore = setOfHighScores.first();
        return (newScore > lowestHighScore.score);
    }

    private void add(HighScore highScore) {
        // adds new high score and removes lowest old high score
        setOfHighScores.add(highScore);
        setOfHighScores.pollFirst();
        saveHighScores();
    }

    public void addHighScore(String name, int numberOfRows, int score) {
        this.add(new HighScore(name, numberOfRows, score));
    }

    public HighScore[] getHighScoresArray() {
        // returns highest-first
        return setOfHighScores.descendingSet().toArray(new HighScore[setOfHighScores.size()]);
    }
}
