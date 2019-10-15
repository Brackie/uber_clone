package swoop.com.swoop.fragments;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

import swoop.com.swoop.R;
import swoop.com.swoop.adapters.ChooseVehicleClassAdapter;
import swoop.com.swoop.adapters.LikelyLocationAdapter;
import swoop.com.swoop.adapters.PlacesAdapter;
import swoop.com.swoop.models.MyPlace;
import swoop.com.swoop.models.VehicleClassModel;
import swoop.com.swoop.ui.PickUp;
import swoop.com.swoop.utils.Constants;
import swoop.com.swoop.utils.PlacesSQLDBHelper;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainMap extends Fragment implements OnMapReadyCallback, ChooseVehicleClassAdapter.OnItemClickListener ,PlacesAdapter.OnItemClickListener{

    private View view;
    private MapView mapView;
    private GoogleMap googleMapGlobal;
    private MarkerOptions myMarker;
    private Marker destMarker;
    private EditText searchPickupLocation;
    private FrameLayout mainLayout;
    private LinearLayout chooseCurrentLocation,searchingDriverLayout;
    private ImageView closeChooseCurrentLocationLayout;
    private ListView likelyLocations;
    private RecyclerView chooseVehicleClass;
    private RecyclerView myPlacesList;
    private Button requestRide,cancelSearchingDriver;
    private PlacesAdapter placesAdapter;
    private ChooseVehicleClassAdapter vehicleClassAdapter;
    private LikelyLocationAdapter likelyLocationAdapter;
    private static final int PLACE_PICKER_INTENT = 2;
    private double destinationLatitude,destinationLongitude,myLocationLatitude,myLocationLongitude;
    private boolean chooseVehicleClassIsOpen = false;
    private boolean myPlacesListIsOpen = false;
    private boolean searchingDriver = false;
    private String destinationName,destinationAddress,currentLocation,currentLocationAddress,vehicleClass;
    private ArrayList<MyPlace> likelyLocationsList;
    private ArrayList<MyPlace> places;
    private ArrayList<VehicleClassModel> vehicleClassModels;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private PlacesSQLDBHelper placeSqlDBHelper;

    public MainMap() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_main_map, container, false);

        //initializing variables
        mapView = (MapView)view.findViewById(R.id.map);
        searchPickupLocation = (EditText) view.findViewById(R.id.search_pickup_location);
        likelyLocations = (ListView) view.findViewById(R.id.likely_locations);
        chooseVehicleClass = (RecyclerView) view.findViewById(R.id.vehicle_classes);
        myPlacesList = (RecyclerView) view.findViewById(R.id.my_places_list);
        mainLayout = (FrameLayout) view.findViewById(R.id.fragment_home);
        chooseCurrentLocation = (LinearLayout)view.findViewById(R.id.choose_current_location);
        searchingDriverLayout = (LinearLayout)view.findViewById(R.id.searching_driver_layout);
        closeChooseCurrentLocationLayout = (ImageView)view.findViewById(R.id.close_choose_my_location_layout);
        requestRide = (Button) view.findViewById(R.id.request_ride);
        cancelSearchingDriver = (Button) view.findViewById(R.id.cancel_getting_driver);
        likelyLocationsList = new ArrayList<MyPlace>();
        places = new ArrayList<MyPlace>();
        vehicleClassModels = new ArrayList<VehicleClassModel>();
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.HORIZONTAL);
        placeSqlDBHelper = new PlacesSQLDBHelper(getContext());

        //setting up map
        if (mapView != null)
        {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

        //update ui
        getMyPlaces();
        getCurrentLocation();
        getVehicleClasses();

        //handle click events
        searchPickupLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePlaceAnimate();
            }
        });

        closeChooseCurrentLocationLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseCurrentLocationAnimate(true);
            }
        });

        likelyLocations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                chooseCurrentLocationAnimate(true);
                currentLocation = likelyLocationsList.get(i).getPlaceName();
                currentLocationAddress = likelyLocationsList.get(i).getPlaceAddress();
                myLocationLatitude = likelyLocationsList.get(i).getLatitude();
                myLocationLongitude = likelyLocationsList.get(i).getLongitude();
                showAddToMyPlaces(currentLocation,currentLocationAddress,myLocationLatitude,myLocationLongitude);
                if (googleMapGlobal != null)
                {
                    myMarker = new MarkerOptions().position(new LatLng(myLocationLatitude,myLocationLongitude)).title("Current Location");
                    googleMapGlobal.addMarker(myMarker);
                    googleMapGlobal.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocationLatitude,myLocationLongitude),12));
                }
            }
        });

        requestRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentLocation == null)
                {
                    chooseCurrentLocationAnimate(false);
                }
                else if (destinationName == null)
                {
                    pickDestination();
                    Toast.makeText(getContext(), "Choose Destination", Toast.LENGTH_LONG).show();
                }
                else
                {
                    chooseVehicleClassAnimate();
                }
            }
        });

        cancelSearchingDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateSearchingDriverLayout();
                chooseVehicleClassAnimate();
                Intent intent = new Intent(getActivity(),PickUp.class);
                intent.putExtra("Destination",destinationName);
                intent.putExtra("currentLocation",currentLocation);
                intent.putExtra("destinationLatitude",destinationLatitude);
                intent.putExtra("destinationLongitude",destinationLongitude);
                intent.putExtra("myLocationLatitude",myLocationLatitude);
                intent.putExtra("myLocationLongitude",myLocationLongitude);
                startActivity(intent);
            }
        });

        return view;
    }

    //method to handle map events when ready
    @Override
    public void onMapReady(GoogleMap googleMap) {

        googleMapGlobal = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        chooseCurrentLocationAnimate(false);

        googleMap.addMarker(new MarkerOptions().position(new LatLng(-1.2609932,36.7997173)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_driver)));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(-1.2642987,36.8005026)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_driver)));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(-1.256632,36.8011453)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_driver)));
        googleMap.addMarker(new MarkerOptions().position(new LatLng(-1.266519,36.798018)).icon(BitmapDescriptorFactory.fromResource(R.mipmap.marker_driver)));

        googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                View v = getActivity().getLayoutInflater().inflate(R.layout.marker_driver_details,null);
                return v;

            }
        });

    }

    //get data form explicit intent
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PLACE_PICKER_INTENT && resultCode == Activity.RESULT_OK)
        {
            Place place = PlacePicker.getPlace(data,getActivity());
            LatLng position = place.getLatLng();
            destinationLatitude = position.latitude;
            destinationLongitude = position.longitude;
            destinationName = String.format("%s",place.getName());
            destinationAddress = String.format("%s",place.getAddress());
            searchPickupLocation.setText("To : " + destinationName);
            if (googleMapGlobal != null)
            {
                if (destMarker != null)
                {
                   destMarker.remove();
                   destMarker = googleMapGlobal.addMarker(new MarkerOptions().position(position));
                   googleMapGlobal.moveCamera(CameraUpdateFactory.newLatLngZoom(position,12));
                }
                else
                {
                    destMarker = googleMapGlobal.addMarker(new MarkerOptions().position(position));
                    googleMapGlobal.moveCamera(CameraUpdateFactory.newLatLngZoom(position,12));
                }
            }
            showAddToMyPlaces(destinationName,destinationAddress,destinationLatitude,destinationLongitude);
        }
    }

    //handle click event on vehicle class list item is selected
    @Override
    public void OnChooseVehicle(View v, int position) {
        vehicleClass = vehicleClassModels.get(position).getClassName();
        if(vehicleClassModels.get(position).isSelected())
        {
            vehicleClassModels.get(position).setSelected(false);
            vehicleClassAdapter.notifyDataSetChanged();
        }
        else
        {
            vehicleClassModels.get(position).setSelected(true);
            vehicleClassAdapter.notifyDataSetChanged();
            animateSearchingDriverLayout();
            chooseVehicleClassAnimate();
        }
    }

    //handle click event on place list item is selected
    @Override
    public void OnPlaceSelected(View v, int position) {

        int id = v.getId();

        if (places.get(position).getPlaceName().equals("More..."))
        {
            pickDestination();
            choosePlaceAnimate();
        }
        else if (id == R.id.places_layout)
        {
            destinationLatitude = places.get(position).getLatitude();
            destinationLongitude = places.get(position).getLongitude();
            destinationName = places.get(position).getPlaceName();
            destinationAddress = places.get(position).getPlaceAddress();
            searchPickupLocation.setText("To : " + destinationName);
            LatLng place = new LatLng(destinationLatitude,destinationLongitude);
            if (destMarker != null)
            {
                destMarker.remove();
                destMarker = googleMapGlobal.addMarker(new MarkerOptions().position(place));
                googleMapGlobal.moveCamera(CameraUpdateFactory.newLatLngZoom(place,12));
            }
            else
            {
                destMarker = googleMapGlobal.addMarker(new MarkerOptions().position(place));
                googleMapGlobal.moveCamera(CameraUpdateFactory.newLatLngZoom(place,12));
            }
            choosePlaceAnimate();
        }
        else if (id == R.id.delete_place)
        {
            places.remove(position);
            placeSqlDBHelper.deleteRow(String.valueOf(places.get(position).getId()));
            placesAdapter.notifyDataSetChanged();
        }

    }

    //check for locally stored user preferred locations in sqllitedatabase
    private void getMyPlaces(){

        places.clear();
        Cursor c = placeSqlDBHelper.readAllData();
        places.add(0,new MyPlace("More...","Find more places",0.0,0.0));
        while (c.moveToNext())
        {
            int id = c.getInt(0);
            String placeName = c.getString(1);
            String placeAddress = c.getString(2);
            double latitude = Double.parseDouble(c.getString(3));
            double longitude = Double.parseDouble(c.getString(4));

            MyPlace place = new MyPlace(placeName,placeAddress,latitude,longitude);
            place.setId(id);

            places.add(place);
        }

        placesAdapter = new PlacesAdapter(places,getContext());
        myPlacesList.setAdapter(placesAdapter);
        myPlacesList.setLayoutManager(new LinearLayoutManager(getContext()));
        placesAdapter.setItemClickListener(this);

    }

    //get likely current location
    private void getCurrentLocation(){

        //check if location permissions are allowed
        int permission = ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        if (getActivity() != null && permission == PackageManager.PERMISSION_GRANTED)
        {
            PlaceDetectionClient mPlaceDetectionClient = Places.getPlaceDetectionClient(getActivity());
            Task<PlaceLikelihoodBufferResponse> placeResult = mPlaceDetectionClient.getCurrentPlace(null);
            placeResult.addOnCompleteListener(new OnCompleteListener<PlaceLikelihoodBufferResponse>() {
                @SuppressLint("RestrictedApi")
                @Override
                public void onComplete(@NonNull Task<PlaceLikelihoodBufferResponse> task) {
                    view.findViewById(R.id.load_likely_locations).setVisibility(View.GONE);
                    PlaceLikelihoodBufferResponse likelyPlaces = task.getResult();
                    for (PlaceLikelihood placeLikelihood : likelyPlaces) {
                        String placeName = placeLikelihood.getPlace().getName().toString();
                        String address = placeLikelihood.getPlace().getAddress().toString();
                        double latitude = placeLikelihood.getPlace().getLatLng().latitude;
                        double longitude = placeLikelihood.getPlace().getLatLng().longitude;
                        likelyLocationsList.add(new MyPlace(placeName,address,latitude,longitude));
                    }
                    likelyPlaces.release();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    view.findViewById(R.id.load_likely_locations).setVisibility(View.GONE);
                    Toast.makeText(getContext(), "Could not get your current location.\nTurn on location and restart app ", Toast.LENGTH_LONG).show();
                }
            });
        }

        likelyLocationAdapter = new LikelyLocationAdapter(getContext(),likelyLocationsList);
        likelyLocations.setAdapter(likelyLocationAdapter);

    }

    //create list of vehicle classes and add to recycler view list
    private void getVehicleClasses(){

        vehicleClassModels.add(new VehicleClassModel("Mini",R.drawable.taxi_mini,false));
        vehicleClassModels.add(new VehicleClassModel("Sedan",R.drawable.taxi_sedan,false));
        vehicleClassModels.add(new VehicleClassModel("Luxury",R.drawable.taxi_luxury,false));
        vehicleClassModels.add(new VehicleClassModel("Nanny",R.drawable.ic_nanny_cab,false));
        vehicleClassAdapter = new ChooseVehicleClassAdapter(vehicleClassModels,getContext());
        chooseVehicleClass.setLayoutManager(staggeredGridLayoutManager);
        chooseVehicleClass.setAdapter(vehicleClassAdapter);
        vehicleClassAdapter.setItemClickListener(this);

    }

    //launch place picker intent
    private void pickDestination(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(getActivity()),PLACE_PICKER_INTENT);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    //display ui to choose current location
    private void chooseCurrentLocationAnimate(boolean open){

        if (open)
        {
            Animation fadeOut = AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_out);
            chooseCurrentLocation.startAnimation(fadeOut);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    chooseCurrentLocation.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        else
        {
            chooseCurrentLocation.setVisibility(View.VISIBLE);
            Animation fadeIn = AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_in);
            chooseCurrentLocation.startAnimation(fadeIn);
        }
    }

    //display ui to choose vehicle class
    private void chooseVehicleClassAnimate(){
        if (chooseVehicleClassIsOpen)
        {
            Animation slideDown = AnimationUtils.loadAnimation(getContext(),R.anim.slide_down);
            chooseVehicleClass.startAnimation(slideDown);
            slideDown.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    chooseVehicleClass.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            chooseVehicleClassIsOpen = false;
        }
        else
        {
            chooseVehicleClass.setVisibility(View.VISIBLE);
            Animation slideUp = AnimationUtils.loadAnimation(getContext(),R.anim.slide_up);
            chooseVehicleClass.startAnimation(slideUp);
            chooseVehicleClassIsOpen = true;
        }
    }

    //display ui to choose locally saved location
    private void choosePlaceAnimate(){
        if (myPlacesListIsOpen)
        {
            Animation fadeOut = AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_out);
            myPlacesList.startAnimation(fadeOut);
            fadeOut.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    myPlacesList.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            myPlacesListIsOpen = false;
        }
        else
        {
            myPlacesList.setVisibility(View.VISIBLE);
            Animation fadeIn = AnimationUtils.loadAnimation(getContext(),android.R.anim.fade_in);
            myPlacesList.startAnimation(fadeIn);
            myPlacesListIsOpen = true;
        }
    }

    //create circular reveal for searching driver layout
    private void animateSearchingDriverLayout(){

        if (searchingDriver)
        {
            int x = searchingDriverLayout.getLeft();
            int y = searchingDriverLayout.getTop();

            int startRadius = (int)Math.hypot(mainLayout.getWidth(),mainLayout.getHeight());
            int endRadius = 0;

            Animator circularReveal = ViewAnimationUtils.createCircularReveal(searchingDriverLayout,x,y,startRadius,endRadius);
            circularReveal.start();
            circularReveal.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    searchingDriverLayout.setVisibility(View.GONE);
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            searchingDriver = false;
        }
        else
        {
            int x = searchingDriverLayout.getRight();
            int y = searchingDriverLayout.getBottom();

            int startRadius = 0;
            int endRadius = (int)Math.hypot(mainLayout.getWidth(),mainLayout.getHeight());

            Animator circularReveal = ViewAnimationUtils.createCircularReveal(searchingDriverLayout,x,y,startRadius,endRadius);
            searchingDriverLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
            searchingDriver = true;
        }

    }

    //show dialog to confirm addition of location to local storage
    private void showAddToMyPlaces(final String name, final String address, final double latitude, final double longitude){

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setTitle("Add to my places")
                .setMessage("Would you like to add this location to your list of places?")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(Constants.TB1_ROW1,name);
                        contentValues.put(Constants.TB1_ROW2,address);
                        contentValues.put(Constants.TB1_ROW3,latitude);
                        contentValues.put(Constants.TB1_ROW4,longitude);
                        boolean result = placeSqlDBHelper.insertData(contentValues);
                        if (result)
                        {
                            Toast.makeText(getContext(), "Location Added", Toast.LENGTH_SHORT).show();
                            places.add(new MyPlace(name,address,latitude,longitude));
                            placesAdapter.notifyDataSetChanged();
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Sorry..!.Location Not Added", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();

    }

}
