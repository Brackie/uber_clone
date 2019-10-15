package swoop.com.swoop.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import swoop.com.swoop.R;
import swoop.com.swoop.adapters.HistoryAdapter;
import swoop.com.swoop.models.HistoryModel;

/**
 * A simple {@link Fragment} subclass.
 */
public class History extends Fragment {


    View view;
    private RecyclerView historyList;
    private HistoryAdapter historyAdapter;
    private ArrayList<HistoryModel> historyArrayList;

    public History() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       view =  inflater.inflate(R.layout.fragment_history, container, false);

        //initialize variables
        historyList = (RecyclerView)view.findViewById(R.id.history_list);
        historyArrayList = new ArrayList<HistoryModel>();

        //update ui
        populateList(10);
        historyAdapter = new HistoryAdapter(getContext(),historyArrayList);
        historyList.setLayoutManager(new LinearLayoutManager(getContext()));
        historyList.setAdapter(historyAdapter);

        return view;
    }

    private void populateList(int n){

        for (int i = 1; i <= n;i++)
        {
            String points = "Point A" + i + " to Point B" + i;
            String payment = "Payment " + i;
            String date = "";
            if (i < 10)
            {
                date = "0" + i + "/07/2018";
            }
            else
            {
                date = i + "/07/2018";
            }
            String id = String.valueOf(i);
            double amount = 100 * i;
            String rating = String.valueOf(0.5 * i);

            historyArrayList.add(new HistoryModel(points,payment,date,id,amount,rating));
        }

    }

}
