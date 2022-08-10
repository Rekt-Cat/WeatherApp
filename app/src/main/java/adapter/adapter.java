package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.maid.weatherapp.R;

import java.util.ArrayList;

public class adapter extends RecyclerView.Adapter<adapter.viewHolder> {
    ArrayList<model> list;
    Context context;
    public adapter(ArrayList<model> list,Context context){ // constructor
        this.list=list;
        this.context = context;
    }

    @NonNull
    @Override
    public adapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull adapter.viewHolder holder, int position) {
        model model =list.get(position);
        holder.time.setText(model.gettime());
        holder.temp1.setText(model.gettemp1());
        holder.windspeed.setText(model.getWindspeed());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class  viewHolder extends RecyclerView.ViewHolder{
        TextView time;
        TextView temp1;
        TextView windspeed;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.currTime);
            temp1 = itemView.findViewById(R.id.currTemp1);
            windspeed = itemView.findViewById(R.id.windSpeed);
        }
    }
}

