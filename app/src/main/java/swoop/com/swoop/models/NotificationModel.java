package swoop.com.swoop.models;

public class NotificationModel {

    private String title;
    private String info;
    private String date;

    public NotificationModel(String title, String info, String date) {
        this.title = title;
        this.info = info;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public String getDate() {
        return date;
    }
}
