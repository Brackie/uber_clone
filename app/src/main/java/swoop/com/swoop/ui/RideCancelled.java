package swoop.com.swoop.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import swoop.com.swoop.R;
import swoop.com.swoop.adapters.CancellationReasonsAdapter;
import swoop.com.swoop.models.Reason;

public class RideCancelled extends AppCompatActivity implements CancellationReasonsAdapter.OnItemClickListener{

    private RecyclerView reasonsList;
    private CancellationReasonsAdapter reasonsAdapter;
    private ArrayList<Reason> reasonsForCancelling;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ride_cancelled);

        //setup toolbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //initialize variables
        reasonsList = (RecyclerView)findViewById(R.id.reasons_list);
        reasonsForCancelling = new ArrayList<Reason>();

        //update ui
        reasonsForCancelling.add(new Reason("Lost Items",false));
        reasonsForCancelling.add(new Reason("Fare Issues",false));
        reasonsForCancelling.add(new Reason("Route feedback",false));
        reasonsForCancelling.add(new Reason("Driver feedback",false));
        reasonsForCancelling.add(new Reason("Vehicle feedback",false));
        reasonsForCancelling.add(new Reason("Receipts and Payment",false));
        reasonsForCancelling.add(new Reason("Promotions",false));
        reasonsForCancelling.add(new Reason("I was involved in an Accident",false));
        reasonsForCancelling.add(new Reason("Cash trips",false));
        reasonsAdapter = new CancellationReasonsAdapter(this,reasonsForCancelling);
        reasonsList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        reasonsList.setAdapter(reasonsAdapter);
        reasonsAdapter.SetItemClickListener(this);

    }

    @Override
    public void OnClick(View view, int position) {
        //handle item click listener
        if (reasonsForCancelling.get(position).isSelected())
        {
            reasonsForCancelling.get(position).setSelected(false);
            reasonsAdapter.notifyDataSetChanged();
        }
        else
        {
            reasonsForCancelling.get(position).setSelected(true);
            reasonsAdapter.notifyDataSetChanged();
        }

    }

    //handle click events on options menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home)
        {
            finish();
        }

        return true;
    }
}
