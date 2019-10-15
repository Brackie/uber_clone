package swoop.com.swoop.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import swoop.com.swoop.R;

public class ReviewRide extends AppCompatActivity {

    String pick_up,drop_off;
    TextView pickUp,dropOff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_ride);

        //set up tool bar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get extras from intent
        pick_up = getIntent().getExtras().getString("pick_up");
        drop_off = getIntent().getExtras().getString("drop_off");

        //initialize variables
        pickUp = (TextView)findViewById(R.id.pick_up);
        dropOff = (TextView)findViewById(R.id.drop_off);

        //update ui
        pickUp.setText("From : " + pick_up);
        dropOff.setText("To : " + drop_off);


    }

    //handle click events on option menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            finish();
        }

        return true;
    }

}
