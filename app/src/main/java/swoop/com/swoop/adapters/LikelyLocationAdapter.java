package swoop.com.swoop.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import swoop.com.swoop.R;
import swoop.com.swoop.models.MyPlace;

/**
 * Created by HP on 6/24/2018.
 */

public class LikelyLocationAdapter extends BaseAdapter {

    Context context;
    ArrayList<MyPlace> likelyLocations;
    LayoutInflater inflater;

    public LikelyLocationAdapter(Context context, ArrayList<MyPlace> likelyLocations) {
        this.context = context;
        this.likelyLocations = likelyLocations;
    }

    @Override
    public int getCount() {
        return likelyLocations.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //inflate the view
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.place_layout,viewGroup,false);

        ViewHolder holder = new ViewHolder();
        holder.placeName = (TextView)view.findViewById(R.id.place_name);
        holder.placeAddress = (TextView)view.findViewById(R.id.place_address);
        view.setTag(holder);

        holder.placeName.setText(likelyLocations.get(i).getPlaceName());
        holder.placeAddress.setText(likelyLocations.get(i).getPlaceAddress());

        return view;
    }

    static class ViewHolder {
        TextView placeName;
        TextView placeAddress;
        int position;
    }

}
