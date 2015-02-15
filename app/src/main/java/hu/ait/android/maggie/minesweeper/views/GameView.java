package hu.ait.android.maggie.minesweeper.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static hu.ait.android.maggie.minesweeper.MinesweeperGame.GRID_WIDTH;

/**
 * Created by Magisus on 2/12/2015.
 */
public class GameView extends View {

    private Paint paintBackground;
    private Paint paintGrid;
    private Paint paintHighlight;
    private Paint paintMine;
    private Paint paintNoMine;

    private Point selectedSquare;
    private List<Point> mines;
    private int[][] mineCounts;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        selectedSquare = null;

        mines = new ArrayList<>();
        mineCounts = new int[GRID_WIDTH][GRID_WIDTH];
        for(int i = 0; i < GRID_WIDTH; i++){
            Arrays.fill(mineCounts[i], -1);
        }

        paintBackground = new Paint();
        paintBackground.setColor(Color.DKGRAY);
        paintBackground.setStyle(Paint.Style.FILL);

        paintGrid = new Paint();
        paintGrid.setColor(Color.LTGRAY);
        paintGrid.setStyle(Paint.Style.STROKE);
        paintGrid.setStrokeWidth(3);

        paintHighlight = new Paint();
        paintHighlight.setColor(Color.RED);
        paintHighlight.setStyle(Paint.Style.FILL);

        paintMine = new Paint();
        paintMine.setColor(Color.BLACK);
        paintMine.setStyle(Paint.Style.FILL);
        //paintMine.setTextSize(); Figure this out!

        paintNoMine = new Paint();
        paintNoMine.setColor(Color.WHITE);
        paintNoMine.setStyle(Paint.Style.FILL);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        selectedSquare = getSquare(event.getX(), event.getY());
        invalidate();
        return super.onTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
        setMeasuredDimension(d, d);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(0, 0, getWidth(), getHeight(), paintBackground);

        int interval = getWidth() / GRID_WIDTH;
        for (int i = 0; i < 11; i++) {
            canvas.drawLine(i * interval, 0, i * interval, getHeight(), paintGrid);
            canvas.drawLine(0, i * interval, getWidth(), i * interval, paintGrid);
        }

        //Highlight selected square
        if (selectedSquare != null) {
            canvas.drawRect(selectedSquare.x * interval + 2, selectedSquare.y * interval + 2,
                    (selectedSquare.x + 1) * interval - 1, (selectedSquare.y + 1) * interval - 1,
                    paintHighlight);
        }

        for (Point point : mines) {
            canvas.drawCircle(point.x * interval + (interval / 2) + 1,
                              point.y * interval + (interval / 2) + 1,
                              interval / 3,
                              paintMine);
        }

        for (int i = 0; i < GRID_WIDTH; i++) {
            for (int j = 0; j < GRID_WIDTH; j++) {
                if (mineCounts[i][j] >= 0) {
                    canvas.drawRect(j * interval + 2, i * interval + 2,
                            (j + 1) * interval - 1, (i + 1) * interval - 1,
                            paintNoMine);
                }
                if (mineCounts[i][j] > 0) {
                    canvas.drawText(Integer.toString(mineCounts[i][j]), j * interval + 19,
                            i * interval + 27, paintMine);
                }
            }
        }
    }

    private Point getSquare(float xIn, float yIn) {
        int interval = getWidth() / GRID_WIDTH;
        int x = (int) (xIn / interval);
        int y = (int) (yIn / interval);
        return new Point(x, y);
    }

    public Point getSelectedSquare() {
        return selectedSquare;
    }

    public void markMine(Point square) {
        mines.add(square);
        selectedSquare = null;
        invalidate();
    }

    public void showExpansion(int[][] newMineCounts) {
        mineCounts = newMineCounts;
        selectedSquare = null;
        invalidate();
    }
}
