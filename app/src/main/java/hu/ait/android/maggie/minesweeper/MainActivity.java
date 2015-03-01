package hu.ait.android.maggie.minesweeper;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import hu.ait.android.maggie.minesweeper.model.MinesweeperModel;
import hu.ait.android.maggie.minesweeper.views.GameView;

import static hu.ait.android.maggie.minesweeper.GameSetupActivity.EXTRA_MINE_COUNT;


public class MainActivity extends ActionBarActivity {

    public static final String NEW_SCORE = "hu.ait.android.maggie.minesweeper.NEW_SCORE";

    private final MinesweeperModel game = MinesweeperModel.getInstance();

    private Chronometer timer;
    private Button mineBtn;
    private Button expandBtn;
    private GameView gameBoard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mineBtn = (Button) findViewById(R.id.mineBtn);
        expandBtn = (Button) findViewById(R.id.expandBtn);
        gameBoard = (GameView) findViewById(R.id.board);

        game.setMineCount(getIntent().getIntExtra(EXTRA_MINE_COUNT, 10));

        timer = (Chronometer) findViewById(R.id.timer);

        mineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Point selectedSquare = gameBoard.getSelectedSquare();
                if (selectedSquare != null) {
                    gameBoard.toggleMine(selectedSquare);
                    game.toggleMine(selectedSquare.y, selectedSquare.x);
                    if (checkForWin()) {
                        displayMessage(getString(R.string.win_message));
                        endRound();
                        goToHighScores(true);
                    }
                }
            }
        });

        expandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Point selectedSquare = gameBoard.getSelectedSquare();
                //If this square has already been expanded, do nothing!
                //Game needs row and column, not x and y, so pass swapped
                if (selectedSquare != null) {
                    if (game.isMine(selectedSquare.y, selectedSquare.x)) {
                        displayMessage(getString(R.string.lose_message));
                        endRound();
                        goToHighScores(false);
                    } else if (!game.expanded(selectedSquare.y, selectedSquare.x)) {
                        game.expand(selectedSquare.y, selectedSquare.x);
                        gameBoard.showExpansion(game.getGrid());
                    }
                }
            }
        });
    }

    /**
     * Opens high score activity after a short delay. If the game was won,
     * submits the new potential high score.
     */
    private void goToHighScores(final boolean gameWon) {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent highScoresIntent = new Intent(MainActivity.this, HighScoresActivity.class);
                if (gameWon) {
                    highScoresIntent.putExtra(NEW_SCORE, timer.getText());
                }
                startActivity(highScoresIntent);
            }
        }, 2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.new_game_action:
                startActivity(new Intent(this, GameSetupActivity.class));
                return true;
            case R.id.high_scores_action:
                startActivity(new Intent(this, HighScoresActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Chronometer timer = (Chronometer) findViewById(R.id.timer);
        timer.start();
    }

    private void endRound() {
        expandBtn.setEnabled(false);
        mineBtn.setEnabled(false);
        gameBoard.clearSelectedSquare();
        timer.stop();
    }

    private void displayMessage(String text) {
        Toast winMsg = Toast.makeText(MainActivity.this, text, Toast.LENGTH_SHORT);
        winMsg.setGravity(Gravity.CENTER_VERTICAL, 0, -15);
        winMsg.show();
    }

    private boolean checkForWin() {
        return game.allMinesMarked();
    }
}
