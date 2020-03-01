package com.dayakar.mgitian.Adapters;

import android.app.Activity;
import android.content.Context;

import android.content.Intent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.dayakar.mgitian.Data.Event;
import com.dayakar.mgitian.Activities.Event_Details_Activity;
import com.dayakar.mgitian.R;
import com.squareup.picasso.Picasso;
import java.util.List;

public class TrendingEventsRecyclerAdapter extends RecyclerView.Adapter<TrendingEventsRecyclerAdapter.AlleventHolder> {
    private Context mContext;
    private List<Event> allEvents;

    public TrendingEventsRecyclerAdapter(Context mContext, List<Event> allEvents) {
        this.mContext = mContext;
        this.allEvents = allEvents;
    }

    @NonNull
    @Override
    public AlleventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.trending_event_item, parent, false);
        AlleventHolder eventHolder = new AlleventHolder(view);


        return eventHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlleventHolder holder, int position) {

        Event events = allEvents.get(position);
        holder.eventTitles.setText(events.getTitle());
        Picasso.get().load(events.getImage()).into(holder.eventimages);


    }

    @Override
    public int getItemCount() {
//         if(allEvents.size()==0){
//             return 2;
//         }else
        return 3;
    }


    public class AlleventHolder extends RecyclerView.ViewHolder{

        public ImageView eventimages;
        public TextView eventTitles;


        public AlleventHolder(View itemView) {
            super(itemView);
            eventimages = itemView.findViewById(R.id.eventImage);
            eventTitles = itemView.findViewById(R.id.eventtitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
               public void onClick(View v) {
                    Event ev=allEvents.get(getAdapterPosition());
                    final Intent intent=new Intent(mContext, Event_Details_Activity.class);
                    intent.putExtra("image",ev.getImage());
                    intent.putExtra("desc",ev.getDesc());
                    intent.putExtra("title",ev.getTitle());
                    intent.putExtra("contact",ev.getContact_info());
                    intent.putExtra("branch",ev.getBranch());
                    intent.putExtra("reg_fee",ev.getReg_fee());
                    intent.putExtra("venue",ev.getVenue());
                    intent.putExtra("postId",ev.getPostId());
                    intent.putExtra("position",getAdapterPosition());
                    intent.putExtra("time",ev.getTime());


                   //applying shared element transition
                    Pair<View, String> p1 = Pair.create(eventimages, ViewCompat.getTransitionName(eventimages));
                    Pair<View, String> p2 = Pair.create(eventTitles, ViewCompat.getTransitionName(eventTitles));
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)mContext,p1,p2);


                        mContext.startActivity(intent,options.toBundle());



                }
            });



            }

    }

    private void remove(int position) {
        allEvents.remove(position);
        notifyItemRemoved(position);
    }
    private int setCount(int n){

        return n;
    }
}
