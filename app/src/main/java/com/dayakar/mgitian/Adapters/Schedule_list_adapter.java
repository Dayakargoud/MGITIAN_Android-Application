package com.dayakar.mgitian.Adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dayakar.mgitian.Activities.DayDetailsActivity;
import com.dayakar.mgitian.R;

import java.util.ArrayList;
import java.util.List;

public class Schedule_list_adapter extends RecyclerView.Adapter<Schedule_list_adapter.ViewHolder> {
    private Context mContext;
    private List<String> items =new ArrayList<String>();
    private List<Integer> colors=new ArrayList<Integer>();
    private String NextActivity="";

    public Schedule_list_adapter(Context mContext, List<String> items,List<Integer> colors) {
        this.mContext = mContext;
        this.items = items;
        this.colors=colors;
    }

    public void activityName(String name){
        NextActivity=name;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.schedule_list_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.subject_name.setText(items.get(position));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            holder.imgage.setForeground(mContext.getDrawable(colors.get(position)));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView subject_name;
        private ImageView imgage;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgage=itemView.findViewById(R.id.hoeizontal_Image);

            subject_name= itemView.findViewById(R.id.horizontal_item_textView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String itmText=items.get(getAdapterPosition());
                    final Intent intent=new Intent(mContext, DayDetailsActivity.class);

                    Log.i("Schedule ",mContext.toString());
                    int positon=getAdapterPosition();
                    intent.putExtra("key",NextActivity);
                    intent.putExtra("activity_title",itmText);
                    mContext.startActivity(intent);


                }
            });

        }

    }

}
