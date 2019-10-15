package swoop.com.swoop.models;

public class Reason {

    private String reason;
    private boolean selected;

    public Reason(String reason,boolean selected) {
        this.reason = reason;
        this.selected = selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getReason() {
        return reason;
    }

    public boolean isSelected() {
        return selected;
    }
}
