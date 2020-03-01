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
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.browser.customtabs.CustomTabsServiceConnection;
import androidx.browser.customtabs.CustomTabsSession;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.dayakar.mgitian.Activities.BunkManagerActivity;
import com.dayakar.mgitian.Activities.Syllabus_Activity;
import com.dayakar.mgitian.Activities.WinnouActivity;
import com.dayakar.mgitian.Activities.ExamsActivity;
import com.dayakar.mgitian.Activities.NotificationsActivity;
import com.dayakar.mgitian.Activities.CGPAActivity;
import com.dayakar.mgitian.R;
import com.dayakar.mgitian.Activities.TransportActivity;

import java.util.ArrayList;
import java.util.List;

public class Category_Adapter extends RecyclerView.Adapter<Category_Adapter.ViewHolder> {
    private Context mContext;
    private List<Integer> icons = new ArrayList<Integer>();
    private List<String> titles = new ArrayList<String>();



    public Category_Adapter(Context mContext, List<Integer> icons, List<String> titles) {
        this.mContext = mContext;
        this.icons = icons;
        this.titles = titles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.category_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(titles.get(position));
        holder.icon.setImageResource(icons.get(position));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.category_text);
            icon = itemView.findViewById(R.id.category_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    switch (getAdapterPosition()) {
//                        case 0:{
//                            Intent intent=new Intent(mContext, ImageActivity.class);
//                          //  intent.putExtra("feature","day");
//                           // intent.putExtra("activity_title","Schedule");
//                            mContext.startActivity(intent);
//                            break;
//                        }
                        case 0: {
                            Intent intent = new Intent(mContext, Syllabus_Activity.class);
                            intent.putExtra("feature", "syllabus");
                            intent.putExtra("activity_title", "Syllabus");
                            mContext.startActivity(intent);
                            break;

                        }
                        case 1: {
                            mContext.startActivity(new Intent(mContext, TransportActivity.class));
//                            Intent intent=new Intent(mContext, WinnouActivity.class);
//                            intent.putExtra("page","Results");
//                            intent.putExtra("pageURL",mContext.getResources().getString(R.string.resultsJntuH));
//                            mContext.startActivity(intent);
                            break;

                        }
                        case 2: {

                            Intent intent = new Intent(mContext, WinnouActivity.class);
                            intent.putExtra("page", "Winnou");
                            intent.putExtra("pageURL", mContext.getResources().getString(R.string.winnoutUrl));
                            mContext.startActivity(intent);
                            break;

                        }
                        case 3: {

                            mContext.startActivity(new Intent(mContext, BunkManagerActivity.class));
                            break;

                        }
                        case 4: {
                            Intent intent = new Intent(mContext, ExamsActivity.class);
                            intent.putExtra("key", "ebooks");
                            mContext.startActivity(intent);
                            break;
                        }

                        case 5: {
                            Intent intent = new Intent(mContext, WinnouActivity.class);
                            intent.putExtra("page", "MGIT");
                            intent.putExtra("pageURL", mContext.getResources().getString(R.string.mgitUrl));
                            mContext.startActivity(intent);
                            break;
                        }
                        case 6: {
                            Intent intent = new Intent(mContext, ExamsActivity.class);
                            intent.putExtra("key", "exams");
                            mContext.startActivity(intent);
                            break;
                        }
                        case 7: {
                            Intent intent = new Intent(mContext, NotificationsActivity.class);
                            intent.putExtra("activity", "Announcements");
                            mContext.startActivity(intent);
                            break;
                        }
                        case 8: {
                            Intent intent = new Intent(mContext, CGPAActivity.class);
                            mContext.startActivity(intent);
                            break;
                        }
                    }
                }
            });
        }

    }



}
