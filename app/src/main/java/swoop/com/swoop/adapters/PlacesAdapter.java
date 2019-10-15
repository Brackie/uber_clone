package swoop.com.swoop.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import swoop.com.swoop.R;
import swoop.com.swoop.models.MyPlace;

/**
 * Created by HP on 7/6/2018.
 */

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder>{

    Context context;
    ArrayList<MyPlace> myPlaces;
    LayoutInflater inflater;
    OnItemClickListener itemClickListener;

    public PlacesAdapter(ArrayList<MyPlace> myPlaces, Context context) {
        this.myPlaces = myPlaces;
        this.context = context;
    }

    @NonNull
    @Override
    public PlacesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_place_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacesAdapter.ViewHolder holder, int position) {

        holder.placeName.setText(myPlaces.get(position).getPlaceName());
        holder.placeAddress.setText(myPlaces.get(position).getPlaceAddress());

        if (myPlaces.get(position).getPlaceName().equals("More..."))
        {
            holder.deletePlace.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return myPlaces.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        LinearLayout placesLayout;
        ImageView deletePlace;
        TextView placeName,placeAddress;

        public ViewHolder(View itemView) {
            super(itemView);

            //initialize variables
            placeName = (TextView) itemView.findViewById(R.id.place_name);
            placeAddress = (TextView) itemView.findViewById(R.id.place_address);
            deletePlace = (ImageView) itemView.findViewById(R.id.delete_place);
            placesLayout = (LinearLayout) itemView.findViewById(R.id.places_layout);

            //add on click listeners
            placesLayout.setOnClickListener(this);
            deletePlace.setOnClickListener(this);

        }

        //handle click events
        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
            {
                itemClickListener.OnPlaceSelected(view,getAdapterPosition());
            }
        }

    }

    public void setItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void OnPlaceSelected(View v,int position);
    }

}
