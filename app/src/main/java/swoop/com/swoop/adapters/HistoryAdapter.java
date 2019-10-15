package swoop.com.swoop.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import swoop.com.swoop.R;
import swoop.com.swoop.models.HistoryModel;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    Context context;
    ArrayList<HistoryModel> historyList;
    LayoutInflater inflater;

    public HistoryAdapter(Context context, ArrayList<HistoryModel> historyList) {
        this.context = context;
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.history_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.points.setText(historyList.get(i).getPoints());
        viewHolder.paymentMethod.setText(historyList.get(i).getPaymentMethod());
        viewHolder.amount.setText("Ksh. " + String.valueOf(historyList.get(i).getAmount()));
        viewHolder.date.setText(historyList.get(i).getDate());
        viewHolder.tripRating.setText(historyList.get(i).getRating());

    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView points,paymentMethod,amount,date,tripRating;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            points = (TextView)itemView.findViewById(R.id.points);
            paymentMethod = (TextView)itemView.findViewById(R.id.payment_method);
            amount = (TextView)itemView.findViewById(R.id.amount);
            date = (TextView)itemView.findViewById(R.id.date);
            tripRating = (TextView)itemView.findViewById(R.id.trip_rating);

        }
    }
}
