package swoop.com.swoop.models;

/**
 * Created by HP on 7/6/2018.
 */

public class VehicleClassModel {

    private String className;
    private int vehicleIcon;
    boolean isSelected;

    public VehicleClassModel(String className, int vehicleIcon,boolean isSelected) {
        this.className = className;
        this.vehicleIcon = vehicleIcon;
        this.isSelected = isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getClassName() {
        return className;
    }

    public int getVehicleIcon() {
        return vehicleIcon;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
