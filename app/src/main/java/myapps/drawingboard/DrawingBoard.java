package myapps.drawingboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.graphics.Color;
import android.graphics.Canvas;

public class DrawingBoard extends ActionBarActivity {
    public static final String EXTRA_COLOR = "myapps.drawingboard.COLOR";
    public static final String EXTA_STROKE_WIDTH = "myapps.drawingboard.STROKE_WIDTH";
    public static final int PICK_COLOR_REQUEST = 1;
    public static final int STROKE_WIDTH_REQUEST = 2;

    int currentColor;
    float currentWidth;
    DBView canvas;
    MenuItem colorMenuItem;
    Bitmap colorImg;
    BitmapDrawable bmd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentColor = Color.BLACK;
        currentWidth = 20;
        setContentView(R.layout.activity_drawing_board);
        canvas = (DBView) findViewById(R.id.drawing_canvas);
        colorImg = Bitmap.createBitmap(42, 42, Bitmap.Config.ARGB_8888);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_drawing_board, menu);
        colorMenuItem = menu.findItem(R.id.action_quick_colors);
        updateColorImg();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.quick_color_black:
                currentColor = Color.BLACK;
                canvas.setColor(Color.BLACK);
                updateColorImg();
                return true;
            case R.id.quick_color_red:
                currentColor = Color.RED;
                canvas.setColor(Color.RED);
                updateColorImg();
                return true;
            case R.id.quick_color_green:
                currentColor = Color.GREEN;
                canvas.setColor(Color.GREEN);
                updateColorImg();
                return true;
            case R.id.quick_color_blue:
                currentColor = Color.BLUE;
                canvas.setColor(Color.BLUE);
                updateColorImg();
                return true;
            case R.id.quick_color_white:
                currentColor = Color.WHITE;
                canvas.setColor(Color.WHITE);
                updateColorImg();
                return true;
            case R.id.action_undo:
                canvas.undo();
                return true;
            case R.id.action_redo:
                canvas.redo();
                return true;
            case R.id.action_brush_size:
                Intent strokeIntent = new Intent(this, DBStrokeWidth.class);
                strokeIntent.putExtra(EXTA_STROKE_WIDTH, currentWidth);
                startActivityForResult(strokeIntent, STROKE_WIDTH_REQUEST);
                return true;
            case R.id.action_more_colors:
                Intent intent = new Intent(this, DBColorPicker.class);
                intent.putExtra(EXTRA_COLOR, currentColor);
                startActivityForResult(intent, PICK_COLOR_REQUEST);
                return true;
            default:
                return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == PICK_COLOR_REQUEST) {
            if(resultCode == RESULT_OK) {
                currentColor = data.getIntExtra(EXTRA_COLOR, Color.YELLOW);
                canvas.setColor(currentColor);
                updateColorImg();
            }
        } else if(requestCode == STROKE_WIDTH_REQUEST) {
            if(resultCode == RESULT_OK) {
                currentWidth = data.getFloatExtra(EXTA_STROKE_WIDTH, 10);
                canvas.setWidth(currentWidth);
            }
        }
    }

    /*
     * Updates the 'Quick Colors' menu
     * icon to the current color.
     */
    public void updateColorImg () {
        Canvas cv = new Canvas(colorImg);
        cv.drawColor(currentColor);

        bmd = new BitmapDrawable(getResources(), colorImg);
        colorMenuItem.setIcon(bmd);
        invalidateOptionsMenu();
    }
}
