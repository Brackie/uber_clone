package swoop.com.swoop.models;

/**
 * Created by HP on 6/24/2018.
 */

public class MyPlace {

    private String placeName;
    private String placeAddress;
    private double latitude;
    private double longitude;
    private int id;

    public MyPlace(String placeName, String placeAddress, double latitude, double longitude) {
        this.placeName = placeName;
        this.placeAddress = placeAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlaceName() {
        return placeName;
    }

    public String getPlaceAddress() {
        return placeAddress;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getId() {
        return id;
    }

}
