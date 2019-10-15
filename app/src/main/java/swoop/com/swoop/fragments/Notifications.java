package swoop.com.swoop.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import swoop.com.swoop.R;
import swoop.com.swoop.adapters.NotificationAdapter;
import swoop.com.swoop.models.NotificationModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notifications extends Fragment {


    private View view;
    private RecyclerView notificationList;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationModel> notifications;

    public Notifications() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_notifications, container, false);

        //initialize variables
        notificationList = (RecyclerView)view.findViewById(R.id.notification_list);
        notifications = new ArrayList<NotificationModel>();

        //update ui
        populateList(10);
        notificationAdapter = new NotificationAdapter(getContext(),notifications);
        notificationList.setLayoutManager(new LinearLayoutManager(getContext()));
        notificationList.setAdapter(notificationAdapter);

        return view;
    }

    private void populateList(int size){

        for (int i = 1; i <= size;i++)
        {
            String date = "";
            if (i < 10)
            {
                date = "0" + i + "/07/2018";
            }
            else
            {
                date = i + "/07/2018";
            }
            notifications.add(new NotificationModel("Title " + i,"Sample Information " + i,date));
        }

    }

}
