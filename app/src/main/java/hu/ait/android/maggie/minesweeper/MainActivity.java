package hu.ait.android.maggie.minesweeper;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import hu.ait.android.maggie.minesweeper.views.GameView;


public class MainActivity extends ActionBarActivity {

    private final MinesweeperModel game = MinesweeperModel.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mineBtn = (Button) findViewById(R.id.mineBtn);
        Button expandBtn = (Button) findViewById(R.id.expandBtn);
        final GameView gameBoard = (GameView) findViewById(R.id.board);

        mineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Point selectedSquare = gameBoard.getSelectedSquare();
                if (selectedSquare != null) {
                    gameBoard.toggleMine(selectedSquare);
                    game.toggleMine(selectedSquare.y, selectedSquare.x);
                    checkForWin();
                }
            }
        });

        expandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Point selectedSquare = gameBoard.getSelectedSquare();
                //If this square has already been expanded, do nothing!
                //Game needs row and column, not x and y, so pass swapped
                if(selectedSquare != null && !game.expanded(selectedSquare.y, selectedSquare.x)) {
                    game.expand(selectedSquare.y, selectedSquare.x);
                    gameBoard.showExpansion(game.getGrid());
                }
            }
        });
    }

    private void checkForWin(){
        if(game.allMinesMarked()){
            Toast.makeText(MainActivity.this, "You win!", Toast.LENGTH_LONG).show();
        }
    }
}
