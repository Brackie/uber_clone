package swoop.com.swoop.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import swoop.com.swoop.R;
import swoop.com.swoop.models.VehicleClassModel;

/**
 * Created by HP on 7/6/2018.
 */

public class ChooseVehicleClassAdapter extends RecyclerView.Adapter<ChooseVehicleClassAdapter.ViewHolder>{

    Context context;
    ArrayList<VehicleClassModel> vehicleClassModels;
    LayoutInflater inflater;
    OnItemClickListener itemClickListener;

    public ChooseVehicleClassAdapter(ArrayList<VehicleClassModel> vehicleClassModels, Context context) {
        this.vehicleClassModels = vehicleClassModels;
        this.context = context;
    }

    @NonNull
    @Override
    public ChooseVehicleClassAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.choose_vehicle_class_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseVehicleClassAdapter.ViewHolder holder, int position) {

        holder.vehicleIcon.setImageResource(vehicleClassModels.get(position).getVehicleIcon());
        holder.className.setText(vehicleClassModels.get(position).getClassName());

        if(vehicleClassModels.get(position).isSelected())
        {
            holder.vehicleIcon.setBackgroundResource(R.drawable.round_background_color_primary);
            holder.unselectClass.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.vehicleIcon.setBackgroundResource(R.drawable.round_background_grey);
            holder.unselectClass.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return vehicleClassModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView vehicleIcon,unselectClass;
        TextView className;

        public ViewHolder(View itemView) {
            super(itemView);

            //initialize variables
            vehicleIcon = (ImageView)itemView.findViewById(R.id.vehicle_icon);
            unselectClass = (ImageView)itemView.findViewById(R.id.un_select_class);
            className = (TextView) itemView.findViewById(R.id.class_name);

            //add on click listeners
            vehicleIcon.setOnClickListener(this);

        }

        //handle click events
        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
            {
                itemClickListener.OnChooseVehicle(view,getAdapterPosition());
            }
        }
    }

    public void setItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void OnChooseVehicle(View v,int position);
    }

}
