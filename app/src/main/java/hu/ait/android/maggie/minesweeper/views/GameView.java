package hu.ait.android.maggie.minesweeper.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magisus on 2/12/2015.
 */
public class GameView extends View {

    private Paint paintBackground;
    private Paint paintGrid;

    private List<PointF> touchCoords;

    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);

        touchCoords = new ArrayList<>();

        paintBackground = new Paint();
        paintBackground.setColor(Color.DKGRAY);
        paintBackground.setStyle(Paint.Style.FILL);

        paintGrid = new Paint();
        paintGrid.setColor(Color.LTGRAY);
        paintGrid.setStyle(Paint.Style.STROKE);
        paintGrid.setStrokeWidth(3);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchCoords.add(new PointF(event.getX(), event.getY()));
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

        int interval = getWidth() / 10;
        for(int i = 0; i < 11; i++){
            canvas.drawLine(i * interval, 0, i * interval, getHeight(), paintGrid);
            canvas.drawLine(0, i * interval, getWidth(),i * interval, paintGrid);
        }
    }
}
