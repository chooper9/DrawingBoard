package myapps.drawingboard;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;

/**
 * Custom View class that handles drawing
 * from MotionEvent inputs.
 */
public class DBView extends View {
    Paint paint = new Paint();
    Path tmpPath = new Path();
    ArrayList<Paint> paints = new ArrayList<Paint>();
    ArrayList<Path> paths = new ArrayList<Path>();
    Deque<Paint> redoPaints = new ArrayDeque<>();
    Deque<Path> redoPaths = new ArrayDeque<>();
    float mX, mY, strokeWidth;
    int mColor;

    public DBView (Context context, AttributeSet attrs) {
        super(context, attrs);
        mColor = Color.BLACK;
        strokeWidth = 20.0f;

        paint.setColor(mColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                tmpPath.moveTo(x, y);
                tmpPath.lineTo(x, y);
                mX = x;
                mY = y;
                return true;
            case MotionEvent.ACTION_MOVE:
                tmpPath.quadTo(mX, mY, (mX+x)/2, (mY+y)/2);
                mX = x;
                mY = y;
                invalidate();
                return true;
            case MotionEvent.ACTION_UP:
                tmpPath.quadTo(mX, mY, (mX+x)/2, (mY+y)/2);
                paths.add(tmpPath);
                paints.add(paint);
                redoPaths.clear();
                redoPaints.clear();
                tmpPath = new Path ();
                invalidate();
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(int i = 0; i < paths.size(); i++) {
            canvas.drawPath(paths.get(i), paints.get(i));
        }
        canvas.drawPath(tmpPath, paint);
    }

    public void setColor (int color) {
        mColor = color;
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
    }

    public void setWidth (float w) {
        strokeWidth = w;
        paint = new Paint();
        paint.setColor(mColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(w);
    }

    public void undo () {
        if (paths.size() > 0) {
            redoPaths.push(paths.remove(paths.size() - 1));
            redoPaints.push(paints.remove(paints.size() - 1));
            invalidate();
        }
    }

    public void redo () {
        if (redoPaths.size() > 0) {
            paths.add(redoPaths.pop());
            paints.add(redoPaints.pop());
            invalidate();
        }
    }
}
