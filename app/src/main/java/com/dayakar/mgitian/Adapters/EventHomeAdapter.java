package com.dayakar.mgitian.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.dayakar.mgitian.Activities.EventBranchActivity;
import com.dayakar.mgitian.R;

import java.util.ArrayList;
import java.util.List;

public class EventHomeAdapter extends RecyclerView.Adapter<EventHomeAdapter.HomeViewHolder> {
   private List<String> items=new ArrayList<>();
   private Context mContext;
   private List<Integer> backgroundColors=new ArrayList<>();


    public EventHomeAdapter(List<String> items, List<Integer> backgroundcolors, Context mContext) {
        this.items = items;
        this.mContext = mContext;
        this.backgroundColors=backgroundcolors;
    }

    @NonNull
    @Override
    public HomeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.horizontal_recyclrview_single_item,viewGroup, false);
        HomeViewHolder hV=new HomeViewHolder(view,mContext);

        return hV;
    }

    @Override
    public void onBindViewHolder(@NonNull HomeViewHolder holder, int i) {

        holder.titleText.setText(items.get(i));
       // holder.imgage.setImageResource(backgroundColors.get(i));
        holder.mCardView.setBackgroundResource(backgroundColors.get(i));

     }


    @Override
    public int getItemCount() {

        return items.size();
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder{
         ImageView imgage;
         TextView titleText;
         RelativeLayout mCardView;


        public HomeViewHolder(@NonNull View itemView, final Context mContext) {
            super(itemView);
            imgage=itemView.findViewById(R.id.hoeizontal_Image);
            titleText= itemView.findViewById(R.id.horizontal_item_textView);
            mCardView=itemView.findViewById(R.id.clubCardView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String itmText=items.get(getAdapterPosition());
                    final Intent intent=new Intent(mContext, EventBranchActivity.class);
                    int positon=getAdapterPosition();
                    switch (positon){
                        case 0:{
                            intent.putExtra("branch","EEE");
                            intent.putExtra("activity_title",itmText);
                            break;
                        }
                        case 1:{
                            intent.putExtra("branch","ECE");
                            intent.putExtra("activity_title",itmText);
                            break;
                        }
                        case 2:{
                            intent.putExtra("branch","MCT");
                            intent.putExtra("activity_title",itmText);
                            break;
                        }
                        case 3:{
                            intent.putExtra("branch","IT");
                            intent.putExtra("activity_title",itmText);
                            break;
                        }
                        case 4:{
                            intent.putExtra("branch","CIVIL");
                            intent.putExtra("activity_title",itmText);
                            break;
                        }
                        case 5:{
                            intent.putExtra("branch","METALLURGY");
                            intent.putExtra("activity_title",itmText);
                            break;
                        }
                        case 6:{
                            intent.putExtra("branch","CSE");
                            intent.putExtra("activity_title",itmText);
                            break;
                        }
                        case 7:{
                            intent.putExtra("branch","MECH");
                            intent.putExtra("activity_title",itmText);
                            break;
                        }
                        case 8:{
                           intent.putExtra("branch","PhysioChemia");
                            intent.putExtra("activity_title",itmText);
                            break;
                        }
                        case 9:{
                            intent.putExtra("branch","AimHigh");
                            intent.putExtra("activity_title",itmText);
                            break;
                        }
                        case 10:{
                            intent.putExtra("branch","MAGISTECH");
                            intent.putExtra("activity_title",itmText);
                            break;
                        }

                    }
                    mContext.startActivity(intent);
                }
            });
        }

    }
}
