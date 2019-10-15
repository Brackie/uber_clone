package swoop.com.swoop.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import swoop.com.swoop.R;

public class Register extends AppCompatActivity {

    Spinner countryCodes;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //setting up toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initializing variables
        countryCodes = (Spinner)findViewById(R.id.country_codes);
        next = (Button)findViewById(R.id.next);

        //method call
        inflateCountryCodesSpinner();

        //handle click events
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,PhoneVerification.class));
            }
        });


    }

    //make list of country codes and inflate to spinner
    private void inflateCountryCodesSpinner() {
        String[] codes = {"+254","+1","+255","+45","+253","+237","+78","+89","+98"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,codes);
        countryCodes.setAdapter(adapter);
    }

    //handle item clicks on options menu
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
