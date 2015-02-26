package hu.ait.android.maggie.minesweeper;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
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

        scoresText = (TextView) findViewById(R.id.scoresText);
        scoresText.setText(createScoreString());
    }

    private String createScoreString(){
        StringBuilder scoreString = new StringBuilder();
        for(int i = 0; i < times.length; i++){
            scoreString.append((i + 1) + ". " + times[i] + "\n");
        }
        return scoreString.toString();
    }

    private void resetScores(){
        Arrays.fill(times, EMPTY_TIME);
        scoresText.setText(createScoreString());
        scoresText.invalidate();
    }

    private String[] loadTimes() {
        String[] timesFromFile = new String[SCORE_COUNT];
        try{
            FileInputStream in = openFileInput("high_scores.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            int i = 0;
            while((line = reader.readLine()) != null){
                if(i == SCORE_COUNT){ //array is full, quit even if the file has more lines
                    return timesFromFile;
                }
                timesFromFile[i] = line;
                i++;
            }
            in.close();
        } catch(Exception e){
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
        try {
            FileOutputStream out = openFileOutput("high_scores.txt", MODE_PRIVATE);
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));
            for(int i = 0; i < times.length; i++){
                writer.append(times[i]);
            }
            writer.flush();
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
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
