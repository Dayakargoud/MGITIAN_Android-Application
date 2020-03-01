package com.dayakar.mgitian.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dayakar.mgitian.Data.Transport_Data;
import com.dayakar.mgitian.R;

import java.util.ArrayList;

public class TransportAdapter extends RecyclerView.Adapter<TransportAdapter.Holder> {
    private ArrayList<Transport_Data> mTransport_data=new ArrayList<>();
    private Context mContext;



    public TransportAdapter(ArrayList<Transport_Data> transport_data, Context context) {
        this.mTransport_data = transport_data;
        this.mContext = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.transport_single_item,parent,false);


        return new Holder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        Transport_Data data=mTransport_data.get(position);
        holder.route_no.setText(data.getRoute_no());
        holder.route_name.setText(data.getRoute_name());
        holder.stops.setText(data.getStops());

    }

    @Override
    public String toString() {
        return "TransportAdapter{" +
                "mTransport_data=" + mTransport_data +
                ", mContext=" + mContext +
                '}';
    }

    @Override
    public int getItemCount() {
        return mTransport_data.size();
    }

    public class Holder extends RecyclerView.ViewHolder{
        TextView route_no,route_name,stops;

        public Holder(@NonNull View itemView) {
            super(itemView);
            route_name=itemView.findViewById(R.id.route_name_value);
            route_no=itemView.findViewById(R.id.route_no_value);
            stops=itemView.findViewById(R.id.route_stops_values);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }
}
