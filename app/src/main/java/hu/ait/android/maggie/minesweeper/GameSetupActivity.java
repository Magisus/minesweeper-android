package hu.ait.android.maggie.minesweeper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class GameSetupActivity extends ActionBarActivity {

    public static final String EXTRA_MINE_COUNT = "hu.ait.android.maggie.minesweeper.MINE_COUNT";

    private TextView mineCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_setup);

        mineCount = (TextView) findViewById(R.id.mineCount);

        Button startBtn = (Button) findViewById(R.id.startBtn);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startGameIntent = new Intent(GameSetupActivity.this, MainActivity.class);
                startGameIntent.putExtra(EXTRA_MINE_COUNT, Integer.parseInt(mineCount.getText()
                        .toString()));
                startActivity(startGameIntent);
            }
        });

        Button addMinesBtn = (Button) findViewById(R.id.addMinesBtn);
        addMinesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = Integer.parseInt(mineCount.getText().toString());
                if(current < 15){
                    mineCount.setText(Integer.toString(current + 1));
                }
            }
        });

        Button removeMinesBtn = (Button) findViewById(R.id.removeMinesBtn);
        removeMinesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = Integer.parseInt(mineCount.getText().toString());
                if(current > 3) {
                    mineCount.setText(Integer.toString(current - 1));
                }
            }
        });
    }
}
