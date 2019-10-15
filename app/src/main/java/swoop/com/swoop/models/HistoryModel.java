package swoop.com.swoop.models;

public class HistoryModel {

    private String points;
    private String paymentMethod;
    private String date;
    private String paymentId;
    private double amount;
    private String rating;

    public HistoryModel(String points, String paymentMethod, String date, String paymentId, double amount, String rating) {
        this.points = points;
        this.paymentMethod = paymentMethod;
        this.date = date;
        this.paymentId = paymentId;
        this.amount = amount;
        this.rating = rating;
    }

    public String getPoints() {
        return points;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public String getDate() {
        return date;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public double getAmount() {
        return amount;
    }

    public String getRating() {
        return rating;
    }
}
