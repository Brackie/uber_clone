package swoop.com.swoop.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import swoop.com.swoop.R;
import swoop.com.swoop.models.MyPlace;
import swoop.com.swoop.models.Reason;

/**
 * Created by HP on 6/24/2018.
 */

public class CancellationReasonsAdapter extends RecyclerView.Adapter<CancellationReasonsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Reason> reasonsForCancelling;
    private LayoutInflater inflater;
    private OnItemClickListener itemClickListener;

    public CancellationReasonsAdapter(Context context, ArrayList<Reason> reasonsForCancelling) {
        this.context = context;
        this.reasonsForCancelling = reasonsForCancelling;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //inflate the view
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.cancelling_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        viewHolder.reason.setText(reasonsForCancelling.get(i).getReason());
        if (reasonsForCancelling.get(i).isSelected())
        {
            viewHolder.selectReason.setVisibility(View.VISIBLE);
        }
        else
        {
            viewHolder.selectReason.setVisibility(View.GONE);
        }

    }
    @Override
    public int getItemCount() {
        return reasonsForCancelling.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView reason;
        ImageView selectReason;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            reason = (TextView) itemView.findViewById(R.id.place_name);
            selectReason = (ImageView) itemView.findViewById(R.id.select_reason);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (itemClickListener != null)
            {
                itemClickListener.OnClick(view,getAdapterPosition());
            }
        }
    }

    public void SetItemClickListener(OnItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface OnItemClickListener{
        void OnClick(View view,int position);
    }

}
