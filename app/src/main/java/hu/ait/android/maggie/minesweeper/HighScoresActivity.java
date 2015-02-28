package hu.ait.android.maggie.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;


public class HighScoresActivity extends ActionBarActivity {

    public static final int SCORE_COUNT = 10;
    public static final String EMPTY_TIME = "--:--";

    private String[] times;
    private TextView scoresText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        times = loadTimes();

        String newScore = getIntent().getStringExtra(MainActivity.NEW_SCORE);
        if(newScore != null){
            insertNewScore(newScore);
        }

        scoresText = (TextView) findViewById(R.id.scoresText);
        scoresText.setText(createScoreString());
    }

    private void insertNewScore(String newScore) {
        for(int i = 0; i < times.length; i++){
            if(EMPTY_TIME.equals(times[i]) || times[i].compareTo(newScore) >= 0){
                String temp = times[i];
                times[i] = newScore;
                for(int j = i + 1; j < times.length; j++){
                    String temp2 = times[j];
                    times[j] = temp;
                    temp = temp2;
                }
                return;
            }
        }
    }

    private String createScoreString() {
        StringBuilder scoreString = new StringBuilder();
        for (int i = 0; i < times.length; i++) {
            scoreString.append((i + 1) + ". " + times[i] + "\n");
        }
        return scoreString.toString();
    }

    private void resetScores() {
        Arrays.fill(times, EMPTY_TIME);
        scoresText.setText(createScoreString());
        scoresText.invalidate();
    }

    private String[] loadTimes() {
        String[] timesFromFile = new String[SCORE_COUNT];
        try {
            FileInputStream in = openFileInput("high_scores.txt");
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
            if(i == 0){ //File was empty
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
    protected void onStop() {
        String dir = getFilesDir().getAbsolutePath();
        File highScores = new File(dir, "high_scores.txt");
        highScores.delete();
        try {
            FileOutputStream out = openFileOutput("high_scores.txt", MODE_PRIVATE);
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
