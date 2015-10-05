package myapps.drawingboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;

/**
 * Activity for selecting the stroke
 * width of the 'brush'.
 */
public class DBStrokeWidth extends ActionBarActivity {
    float prevWidth, newWidth;
    Bitmap previewImg;
    ImageView previewView;
    Canvas cv;
    Paint paint;
    SeekBar width_seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        prevWidth = intent.getFloatExtra(DrawingBoard.EXTA_STROKE_WIDTH, 0);
        newWidth = prevWidth;

        setContentView(R.layout.activity_stroke_width);
        previewImg = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888);
        cv = new Canvas(previewImg);
        cv.drawColor(Color.WHITE);
        paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(newWidth);
        cv.drawLine(10, 128, 246, 128, paint);
        previewView = (ImageView) findViewById(R.id.width_preview);
        previewView.setImageBitmap(previewImg);
        previewView.invalidate();
        width_seekbar = (SeekBar) findViewById(R.id.width_seek_bar);
        width_seekbar.setProgress((int)newWidth);
        width_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                       @Override
                       public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                           newWidth = (float)progress;
                           paint.setStrokeWidth(newWidth);
                           cv.drawColor(Color.WHITE);
                           cv.drawLine(10, 128, 246, 128, paint);
                           previewView.invalidate();
                       }

                       @Override
                       public void onStartTrackingTouch(SeekBar seekBar) {
                       }

                       @Override
                       public void onStopTrackingTouch(SeekBar seekBar) {
                       }
                   }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_color_picker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case android.R.id.home:
                Intent intent = new Intent();
                intent.putExtra(DrawingBoard.EXTA_STROKE_WIDTH, newWidth);
                setResult(RESULT_OK, intent);
                finish();
                return true;
            case R.id.color_picker_cancel:
                setResult(RESULT_CANCELED);
                finish();
                return true;
            default:
                return false;
        }
    }
}
