package com.dayakar.mgitian.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dayakar.mgitian.Data.NotificationData;
import com.dayakar.mgitian.R;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {
   private Context mContext;
   private ArrayList<String> titles=new ArrayList<>();
    private ArrayList<String> descs=new ArrayList<>();
    private ArrayList<String> timestamp=new ArrayList<>();
    private ArrayList<NotificationData> mData=new ArrayList<>();


    public NotificationAdapter(Context context, ArrayList<NotificationData> data) {
        mContext = context;
        mData = data;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.notification_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         NotificationData data=mData.get(position);

        holder.notifiaction_title.setText(data.getTitle());
        holder.notification_desc.setText(data.getDesc());
        holder.timestamp.setText(data.getTime());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView notifiaction_title,notification_desc,timestamp;
        ImageView mImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            notifiaction_title=itemView.findViewById(R.id.notification_title);
            notification_desc=itemView.findViewById(R.id.notification_desc);
            mImageView=itemView.findViewById(R.id.image_notification);
            timestamp=itemView.findViewById(R.id.notification_time);
        }
    }
}

