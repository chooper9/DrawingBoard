package myapps.drawingboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.SeekBar;

/**
 * Activity for selecting a color using
 * three seekbars (RBG).
 */
public class DBColorPicker extends ActionBarActivity {
    int prevColor;
    int newColor;
    Bitmap previewImg;
    ImageView previewView;
    Canvas cv;
    SeekBar red_seekbar, green_seekbar, blue_seekbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        prevColor = intent.getIntExtra(DrawingBoard.EXTRA_COLOR, 0);

        setContentView(R.layout.activity_color_picker);

        newColor = prevColor;
        previewImg = Bitmap.createBitmap(128, 128, Bitmap.Config.ARGB_8888);
        cv = new Canvas(previewImg);
        cv.drawColor(newColor);
        previewView = (ImageView) findViewById(R.id.color_preview);
        previewView.setImageBitmap(previewImg);
        previewView.invalidate();
        red_seekbar = (SeekBar) findViewById(R.id.red_seek_bar);
        green_seekbar = (SeekBar) findViewById(R.id.green_seek_bar);
        blue_seekbar = (SeekBar) findViewById(R.id.blue_seek_bar);
        red_seekbar.setProgress((newColor & 0x00ff0000) >> 16);
        green_seekbar.setProgress((newColor & 0x0000ff00) >> 8);
        blue_seekbar.setProgress(newColor & 0x000000ff);
        red_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                   @Override
                   public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                       newColor = (newColor & 0xff00ffff) | ((progress & 0xff) << 16);
                       cv.drawColor(newColor);
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
        green_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                   @Override
                   public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                       newColor = (newColor & 0xffff00ff) | ((progress & 0xff) << 8);
                       cv.drawColor(newColor);
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

        blue_seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                   @Override
                   public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                       newColor = (newColor & 0xffffff00) | (progress & 0xff);
                       cv.drawColor(newColor);
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
                intent.putExtra(DrawingBoard.EXTRA_COLOR, newColor);
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
