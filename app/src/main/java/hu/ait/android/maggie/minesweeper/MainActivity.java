package hu.ait.android.maggie.minesweeper;

import android.graphics.Point;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;

import hu.ait.android.maggie.minesweeper.views.GameView;


public class MainActivity extends ActionBarActivity {

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
                    gameBoard.markMine(selectedSquare);
                    //Add guess to game logic
                }
            }
        });

        expandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Point selectedSquare = gameBoard.getSelectedSquare();
                //If this square has already been expanded, do nothing!
                if(selectedSquare != null) {
                    gameBoard.showExpansion(new int[][]
                           {{-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                            {0,1,0,1,2,3,0,0,0,0},
                            {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                            {0,1,0,1,2,3,0,0,0,0},
                            {0,1,0,1,2,3,0,0,0,0},
                            {0,1,0,1,2,3,0,0,0,0},
                            {-1,-1,-1,-1,-1,-1,-1,-1,-1,-1},
                            {0,1,0,1,2,3,0,0,0,0},
                            {0,1,0,1,2,3,0,0,0,0},
                            {0,1,0,1,2,3,0,0,0,0}});
                }
            }
        });
    }
}
