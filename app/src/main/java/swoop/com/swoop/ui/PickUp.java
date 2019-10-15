package swoop.com.swoop.ui;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.gcm.Task;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.android.PolyUtil;
import com.google.maps.errors.ApiException;
import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.joda.time.DateTime;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import swoop.com.swoop.R;
import swoop.com.swoop.utils.DirectionsJSONParser;

public class PickUp extends AppCompatActivity implements OnMapReadyCallback{

    private MapView map;
    private LinearLayout bottomSheet;
    private BottomSheetBehavior bottomSheetBehavior;
    private ImageView toggleSheet;
    private TextView destination,currentLocation,paymentMethod,changePaymentMethod,fareEstimate,promoCode;
    private Button confirmBooking,cancelRide;
    private ProgressDialog progressDialog;
    private final static int CHANGE_PAYMENT_REQUEST_CODE = 3;
    private String destinationName,myLocation,payment;
    private double destinationLatitude,destinationLongitude,myLocationLatitude,myLocationLongitude;
    private GoogleMap googleMapGlobal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_up);

        //get information from intent
        destinationName  = getIntent().getExtras().getString("Destination");
        myLocation  = getIntent().getExtras().getString("currentLocation");
        destinationLatitude = getIntent().getExtras().getDouble("destinationLatitude");
        destinationLongitude = getIntent().getExtras().getDouble("destinationLongitude");
        myLocationLatitude = getIntent().getExtras().getDouble("myLocationLatitude");
        myLocationLongitude = getIntent().getExtras().getDouble("myLocationLongitude");

        //set up action bar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initializing variables
        map = (MapView)findViewById(R.id.map);
        bottomSheet = (LinearLayout)findViewById(R.id.bottom_sheet);
        toggleSheet = (ImageView)findViewById(R.id.toggle_sheet);
        destination = (TextView)findViewById(R.id.destination);
        currentLocation = (TextView)findViewById(R.id.current_location);
        paymentMethod = (TextView)findViewById(R.id.payment_method);
        changePaymentMethod = (TextView)findViewById(R.id.change_payment_method);
        fareEstimate = (TextView)findViewById(R.id.fare_estimate);
        promoCode = (TextView)findViewById(R.id.promo_code);
        confirmBooking = (Button)findViewById(R.id.confirm_booking);
        cancelRide = (Button)findViewById(R.id.cancel_booking);
        progressDialog = new ProgressDialog(this);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        //setting up map
        if (map != null)
        {
            map.onCreate(null);
            map.onResume();
            map.getMapAsync(this);
        }

        //update ui
        currentLocation.setText("Current Location: " + myLocation);
        destination.setText("Destination: " + destinationName);
        bottomSheetBehavior.setPeekHeight(339);
        bottomSheetBehavior.setHideable(false);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState)
                {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        toggleSheet.setImageResource(R.drawable.ic_action_drop_down);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        toggleSheet.setImageResource(R.drawable.ic_action_pull_up);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        //handle click events
        changePaymentMethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent change = new Intent(PickUp.this,ChangePaymentMethod.class);
                startActivityForResult(change,CHANGE_PAYMENT_REQUEST_CODE);
            }
        });

        toggleSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED)
                {
                    toggleSheet.setImageResource(R.drawable.ic_action_drop_down);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else
                {
                    toggleSheet.setImageResource(R.drawable.ic_action_pull_up);
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        confirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent review = new Intent(PickUp.this,ReviewRide.class);
                review.putExtra("pick_up",myLocation);
                review.putExtra("drop_off",destinationName);
                startActivity(review);
                finish();
            }
        });

        cancelRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PickUp.this,RideCancelled.class));
                finish();
            }
        });


    }

    //method to handle map events when ready
    @Override
    public void onMapReady(GoogleMap googleMap) {

        LatLng current = new LatLng(myLocationLatitude,myLocationLongitude);
        LatLng destination = new LatLng(destinationLatitude,destinationLongitude);

        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMapGlobal = googleMap;
        googleMap.addMarker(new MarkerOptions().position(current).title(myLocation));
        googleMap.addMarker(new MarkerOptions().position(destination).title(destinationName));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocationLatitude,myLocationLongitude),12));

        String origin = String.valueOf(myLocationLatitude) + "," + String.valueOf(myLocationLongitude);
        String destiny = String.valueOf(destinationLatitude) + "," + String.valueOf(destinationLongitude);

        DirectionsResult directionsResult = getDirectionsResult(origin,destiny);
        List<LatLng> points = PolyUtil.decode(directionsResult.routes[0].overviewPolyline.getEncodedPath());
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(points);
        polylineOptions.width(18);
        polylineOptions.color(Color.BLUE);
        polylineOptions.geodesic(true);
        googleMap.addPolyline(polylineOptions);

    }

    private GeoApiContext getGeoApiContext(){
        GeoApiContext geoApiContext = new GeoApiContext();
        geoApiContext.setApiKey(getString(R.string.google_maps_key))
                .setConnectTimeout(1, TimeUnit.SECONDS)
                .setReadTimeout(1, TimeUnit.SECONDS)
                .setWriteTimeout(1, TimeUnit.SECONDS);
        return geoApiContext.setQueryRateLimit(3);
    }

    private DirectionsResult getDirectionsResult(String origin,String destination){
        progressDialog.setMessage("Getting Routes..");
        progressDialog.show();
        DirectionsResult result = null;
        DateTime now = new DateTime();
        try {
            result = DirectionsApi.newRequest(getGeoApiContext())
                    .mode(TravelMode.DRIVING)
                    .origin(origin)
                    .destination(destination)
                    .departureTime(now)
                    .await();
        } catch (ApiException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            progressDialog.dismiss();
        }
        return result;
    }

    //get data from activities started for result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHANGE_PAYMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK)
        {
            payment = data.getStringExtra("payment_method");
            paymentMethod.setText(payment);
        }

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
