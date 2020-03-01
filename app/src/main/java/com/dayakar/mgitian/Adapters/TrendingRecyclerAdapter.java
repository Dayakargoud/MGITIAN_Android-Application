package com.dayakar.mgitian.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.dayakar.mgitian.Interfaces.OnImageDownloadListener;
import com.dayakar.mgitian.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrendingRecyclerAdapter extends RecyclerView.Adapter<TrendingRecyclerAdapter.AlleventHolder> {
    private Context mContext;
    private ArrayList<String> allEvents;
    private OnImageDownloadListener mOnImageDownloadListener;

    public TrendingRecyclerAdapter(Context mContext, ArrayList<String> allEvents,OnImageDownloadListener mListener) {
        this.mContext = mContext;
        this.allEvents=allEvents;
        this.mOnImageDownloadListener=mListener;
    }

    @NonNull
    @Override
    public AlleventHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.trendingreycliersingleitem, parent, false);
        AlleventHolder eventHolder = new AlleventHolder(view);


        return eventHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AlleventHolder holder, int position) {


      //  holder.eventTitles.setText(events.getTitle());
        Picasso.get().load(allEvents.get(position)).into(holder.eventimages, new Callback() {
            @Override
            public void onSuccess() {
                mOnImageDownloadListener.onImageLoad();
            }

            @Override
            public void onError(Exception e) {

            }
        });


    }

    @Override
    public int getItemCount() {
       return allEvents.size();
    }


    public class AlleventHolder extends RecyclerView.ViewHolder{

        public ImageView eventimages;
        public TextView eventTitles;


        public AlleventHolder(View itemView) {
            super(itemView);
            eventimages = itemView.findViewById(R.id.eventImage);
         //   eventTitles = (TextView) itemView.findViewById(R.id.eventtitle);

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Event ev=allEvents.get(getAdapterPosition());
//                    final  Intent intent=new Intent(mContext, Event_Details_Activity.class);
//                    intent.putExtra("image",ev.getImage());
//                    intent.putExtra("desc",ev.getDesc());
//                    intent.putExtra("title",ev.getTitle());
//                    intent.putExtra("contact",ev.getContact_info());
//                    intent.putExtra("branch",ev.getBranch());
//                    intent.putExtra("reg_fee",ev.getReg_fee());
//                    intent.putExtra("venue",ev.getVenue());
//                    intent.putExtra("postId",ev.getPostId());
//                    intent.putExtra("position",getAdapterPosition());
//
//
//                    //applying shared element transition
//                    Pair<View, String> p1 = Pair.create((View)eventimages, ViewCompat.getTransitionName(eventimages));
//                    Pair<View, String> p2 = Pair.create((View)eventTitles, ViewCompat.getTransitionName(eventTitles));
//                    ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)mContext,p1,p2);
//
//
//                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.JELLY_BEAN){
//                        mContext.startActivity(intent,options.toBundle());}
//                    else{
//                        mContext.startActivity(intent);
//                    }
//
//                }
//            });



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
