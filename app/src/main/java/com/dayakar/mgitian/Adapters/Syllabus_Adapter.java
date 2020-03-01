package com.dayakar.mgitian.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dayakar.mgitian.R;
import java.util.ArrayList;

public class Syllabus_Adapter extends RecyclerView.Adapter<Syllabus_Adapter.ViewHolder> {
   private ArrayList<String> list=new ArrayList<>();
   private Context mContext;

    public Syllabus_Adapter(ArrayList<String> list, Context mContext) {
        this.list = list;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.syllabus_list_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        int unit_number=position+1;
        holder.details.setText(list.get(position));

        if(position==(list.size()-1)){
            holder.unit.setText("Reference Books");
        }else {
            holder.unit.setText("Unit-"+unit_number);
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
          TextView unit;
          TextView details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            unit= itemView.findViewById(R.id.tvsbUnit);
            details= itemView.findViewById(R.id.tvsybs);
        }
    }
}
