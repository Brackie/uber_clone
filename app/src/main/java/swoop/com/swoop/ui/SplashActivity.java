package swoop.com.swoop.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import swoop.com.swoop.R;

public class SplashActivity extends AppCompatActivity {

    TextView logo;
    ImageView taxi;
    private final static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //initializing variables
        logo = (TextView)findViewById(R.id.swoop);
        taxi = (ImageView) findViewById(R.id.taxi_icon);

        //method call
        animateLogo();

        //switch between different interface depending on authentication state

        if (isSaved() && locationIsOn())
        {
            startActivity(new Intent(SplashActivity.this,Home.class));
        }
        else if(locationIsOn())
        {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,Login.class));
                    finish();
                }
            };
            handler.postDelayed(runnable,SPLASH_TIME_OUT);
        }

    }

    //check if location is turned on
    private boolean locationIsOn(){
        try {
            int location_mode = Settings.Secure.getInt(getContentResolver(),Settings.Secure.LOCATION_MODE);
            if (location_mode == 0)
            {
                Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(onGPS);
                Toast.makeText(this, "You need to turn on location to proceed using this app", Toast.LENGTH_LONG).show();
                return false;
            }
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
        return true;
    }

    //check if user details are saved to phone
    private boolean isSaved(){
        SharedPreferences userDetails = this.getSharedPreferences("AppUserDetails",this.MODE_PRIVATE);
        String userName = userDetails.getString("username","");
        if (!userName.equals(""))
        {
            return true;
        }
        return false;
    }

    //animate logo
    private void animateLogo(){

        Animation slideInLeft = AnimationUtils.loadAnimation(this,android.R.anim.slide_in_left);
        Animation fadeIn = AnimationUtils.loadAnimation(this,android.R.anim.fade_in);
        logo.startAnimation(fadeIn);
        taxi.startAnimation(slideInLeft);

    }

    private void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
        builder.setTitle("Location Off")
                .setMessage("Location services is off...You need to turn it on to proceed using this app")
                .setPositiveButton("Turn On", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent onGPS = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(onGPS);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .show();

    }

    @Override
    protected void onResume() {
        super.onResume();
        animateLogo();
        if(locationIsOn())
        {
            Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    startActivity(new Intent(SplashActivity.this,Login.class));
                    finish();
                }
            };
            handler.postDelayed(runnable,SPLASH_TIME_OUT);
        }
    }

}
