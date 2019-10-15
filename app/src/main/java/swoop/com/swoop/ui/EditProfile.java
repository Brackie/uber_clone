package swoop.com.swoop.ui;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import swoop.com.swoop.R;
import swoop.com.swoop.adapters.PlacesAdapter;
import swoop.com.swoop.models.MyPlace;
import swoop.com.swoop.utils.PlacesSQLDBHelper;

public class EditProfile extends AppCompatActivity implements PlacesAdapter.OnItemClickListener{

    private Spinner countryCodes;
    private PlacesSQLDBHelper placeSqlDBHelper;
    private PlacesAdapter placesAdapter;
    private RecyclerView placesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        //setup toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initializing variables
        countryCodes = (Spinner)findViewById(R.id.country_codes);
        placesList = (RecyclerView) findViewById(R.id.places_list);
        placeSqlDBHelper = new PlacesSQLDBHelper(this);
        placesAdapter = new PlacesAdapter(getPlaces(),this);

        //method call
        inflateCountryCodesSpinner();

        //update ui
        placesList.setAdapter(placesAdapter);
        placesList.setLayoutManager(new LinearLayoutManager(this));
        placesAdapter.setItemClickListener(this);

    }

    //handle click events on options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            finish();
        }

        return true;
    }

    //handle click event on place list item is selected
    @Override
    public void OnPlaceSelected(View v, int position) {

        int id = v.getId();

        if (id == R.id.places_layout)
        {
            Toast.makeText(this, "MyPlace:" + getPlaces().get(position).getPlaceName(), Toast.LENGTH_SHORT).show();
        }
        else if(id == R.id.delete_place)
        {
            getPlaces().remove(position);
            placeSqlDBHelper.deleteRow(String.valueOf(getPlaces().get(position).getId()));
            notifyAll();
        }

    }

    //make list of country codes and inflate to spinner
    private void inflateCountryCodesSpinner() {
        String[] codes = {"+254","+1","+255","+45","+253","+237","+78","+89","+98"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,codes);
        countryCodes.setAdapter(adapter);
    }

    //read places data from sqlite database
    private ArrayList<MyPlace> getPlaces(){
        ArrayList<MyPlace> places = new ArrayList<MyPlace>();
        Cursor c = placeSqlDBHelper.readAllData();

        while (c.moveToNext())
        {
            int id = c.getInt(0);
            String placeName = c.getString(1);
            String placeAddress = c.getString(2);
            double longitude = Double.parseDouble(c.getString(3));
            double latitude = Double.parseDouble(c.getString(4));

            MyPlace place = new MyPlace(placeName,placeAddress,latitude,longitude);
            place.setId(id);

            places.add(place);
        }

        return places;
    }

}
