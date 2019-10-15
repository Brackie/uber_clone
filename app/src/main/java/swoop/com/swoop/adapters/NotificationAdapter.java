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
import swoop.com.swoop.models.NotificationModel;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    Context context;
    ArrayList<NotificationModel> notificationList;
    LayoutInflater inflater;

    public NotificationAdapter(Context context, ArrayList<NotificationModel> notificationList) {
        this.context = context;
        this.notificationList = notificationList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.notification_item_layout,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.title.setText(notificationList.get(i).getTitle());
        viewHolder.info.setText(notificationList.get(i).getInfo());
        viewHolder.date.setText(notificationList.get(i).getDate());

    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,info,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = (TextView)itemView.findViewById(R.id.notification_title);
            info = (TextView)itemView.findViewById(R.id.notificaton_info);
            date = (TextView)itemView.findViewById(R.id.notification_date);

        }
    }
}
