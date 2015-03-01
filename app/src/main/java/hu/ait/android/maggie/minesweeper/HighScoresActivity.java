package hu.ait.android.maggie.minesweeper;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;

import hu.ait.android.maggie.minesweeper.views.GameView;


public class HighScoresActivity extends ActionBarActivity {

    public static final int SCORE_COUNT = 10;
    public static final String EMPTY_TIME = "--:--";
    public static final String HIGH_SCORES_FILE = "high_scores.txt";

    private String[] times;
    private TextView[] scoresTexts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        times = loadTimes();

        String newScore = getIntent().getStringExtra(MainActivity.NEW_SCORE);
        int newScoreIndex = -1;
        if (newScore != null) {
            newScoreIndex = insertNewScore(newScore);
        }

        scoresTexts = new TextView[SCORE_COUNT];
        final LinearLayout layoutContainer = (LinearLayout) findViewById(R.id.layoutContainer);
        for (int i = 0; i < SCORE_COUNT; i++) {
            scoresTexts[i] = new TextView(HighScoresActivity.this);
            scoresTexts[i].setText((i + 1) + ". " + times[i]);
            scoresTexts[i].setTextSize(TypedValue.COMPLEX_UNIT_SP, 27);
            if((newScoreIndex != -1) && (i == newScoreIndex)){
                scoresTexts[i].setTextColor(GameView.ORANGE);
            }
            LinearLayout.LayoutParams params =  new LinearLayout.LayoutParams(ViewGroup
                    .LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            scoresTexts[i].setGravity(Gravity.CENTER_HORIZONTAL);
            layoutContainer.addView(scoresTexts[i], params);
        }
    }

    /**
     * Inserts the new score into the correct location in previous high scores array.
     * Returns the index of the inserted score.
     */
    private int insertNewScore(String newScore) {
        for (int i = 0; i < times.length; i++) {
            if (EMPTY_TIME.equals(times[i]) || times[i].compareTo(newScore) >= 0) {
                String temp = times[i];
                times[i] = newScore;
                for (int j = i + 1; j < times.length; j++) {
                    String temp2 = times[j];
                    times[j] = temp;
                    temp = temp2;
                }
                return i;
            }
        }
        return -1;
    }

    /** Sets all score entries back to EMPTY_SCORE. */
    private void resetScores() {
        Arrays.fill(times, EMPTY_TIME);
        for (int i = 0; i < SCORE_COUNT; i++) {
            scoresTexts[i].setText(times[i]);
            scoresTexts[i].invalidate();
        }
    }

    /** Load saved high scores from file. */
    private String[] loadTimes() {
        String[] timesFromFile = new String[SCORE_COUNT];
        try {
            FileInputStream in = openFileInput(HIGH_SCORES_FILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            int i = 0;
            while ((line = reader.readLine()) != null) {
                if (i == SCORE_COUNT) { //array is full, quit even if the file has more lines
                    return timesFromFile;
                }
                timesFromFile[i] = line;
                i++;
            }
            if (i == 0) { //File was empty
                Arrays.fill(timesFromFile, EMPTY_TIME);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            timesFromFile = new String[SCORE_COUNT];
            Arrays.fill(timesFromFile, EMPTY_TIME);
        }
        return timesFromFile;
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, GameSetupActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_high_scores, menu);
        return true;
    }

    @Override
    /** Save the displayed scores to file. */
    protected void onStop() {
        String dir = getFilesDir().getAbsolutePath();
        File highScores = new File(dir, HIGH_SCORES_FILE);
        highScores.delete();
        try {
            FileOutputStream out = openFileOutput(HIGH_SCORES_FILE, MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            for (int i = 0; i < times.length; i++) {
                writer.write(times[i] + '\n');
                writer.flush();
            }
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            super.onStop();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.new_game_action:
                startActivity(new Intent(this, GameSetupActivity.class));
                return true;
            case R.id.clear_action:
                resetScores();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
