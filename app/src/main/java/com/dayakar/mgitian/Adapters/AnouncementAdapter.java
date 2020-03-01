package com.dayakar.mgitian.Adapters;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dayakar.mgitian.Activities.WinnouActivity;
import com.dayakar.mgitian.R;

import java.util.ArrayList;

public class AnouncementAdapter extends RecyclerView.Adapter<AnouncementAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<String> titles=new ArrayList<>();
    private ArrayList<String> links =new ArrayList<>();

    public AnouncementAdapter(Context context, ArrayList<String> titles, ArrayList<String> descs) {
        mContext = context;
        this.titles = titles;
        this.links = descs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.announcemets_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.titile.setText(titles.get(position));

    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView titile;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titile=itemView.findViewById(R.id.announcement);
               itemView.setOnClickListener(new View.OnClickListener() {
                   @Override
                   public void onClick(View v) {
                       Intent intent=new Intent(mContext, WinnouActivity.class);
                       intent.putExtra("page","Announcement");
                       String urlLink="http://drive.google.com/viewerng/viewer?embedded=true&url="+links.get(getAdapterPosition());
                       intent.putExtra("pageURL",urlLink);
                       mContext.startActivity(intent);

                   }
               });
        }

    }





}
