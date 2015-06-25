package andiag.coru.es.welegends.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import andiag.coru.es.welegends.R;

/**
 * Created by Andy on 26/06/2015.
 */
public class ActivitySplashScreen extends Activity {

    private static final long SPLASH_SCREEN_DELAY = 2000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Get actual time
        long t = Calendar.getInstance().getTimeInMillis();

        super.onCreate(savedInstanceState);

        //Poner pantalla vertical
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_splash_screen);

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                //Iniciar actividad principal y finalizar splash
                Intent mainIntent = new Intent().setClass(ActivitySplashScreen.this, ActivitySummoner.class);
                startActivity(mainIntent);
                finish();
            }
        };

        //Set the new delay time
        long delay = SPLASH_SCREEN_DELAY - (Calendar.getInstance().getTimeInMillis() - t);
        if (delay > 0) {
            Timer timer = new Timer();
            timer.schedule(task, delay);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_splash_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
