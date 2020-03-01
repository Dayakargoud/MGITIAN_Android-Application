package com.dayakar.mgitian.Adapters;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.dayakar.mgitian.Interfaces.OnDownloadListener;
import com.dayakar.mgitian.R;

import java.io.File;
import java.util.ArrayList;

public class File_Adapter extends RecyclerView.Adapter<File_Adapter.ViewHolder> {
    private ArrayList<String> file_names=new ArrayList<>();
    private ArrayList<String> file_links=new ArrayList<>();
    private Context mContext;
    private String fragment_name;
    private OnDownloadListener mOnDownloadListener;
    private String file_path;
    private String icon;


    public File_Adapter(OnDownloadListener listener, Context context, ArrayList<String> file_names, ArrayList<String> file_links,
                        String fragment_name) {
        this.file_names = file_names;
        this.file_links = file_links;
        mContext = context;
        this.fragment_name=fragment_name;
        this.mOnDownloadListener=listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v=LayoutInflater.from(mContext).inflate(R.layout.file_layout_item,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
         holder.file_name.setText(file_names.get(position));

        if (fragment_name.equals("Saved files")){
            holder.download_icon.setVisibility(View.INVISIBLE);
            holder.download_icon.setEnabled(false);
        }
        if(isFile_Exist(file_names.get(position))){
            holder.download_icon.setBackgroundResource(R.drawable.completed);
            holder.download_icon.setEnabled(false);
        }



    }

    @Override
    public int getItemCount() {

        return file_names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView file_name;
        ImageView pdf_icon;
        ImageButton download_icon;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            file_name = itemView.findViewById(R.id.pdf_title_textview);
            pdf_icon = itemView.findViewById(R.id.pdf_icon);
            download_icon = itemView.findViewById(R.id.file_download_button);
            download_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnDownloadListener.onDownload(file_links.get(getAdapterPosition()),file_names.get(getAdapterPosition()));
                    download_icon.setEnabled(false);

                }
            });
           itemView.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   if(fragment_name.equalsIgnoreCase("Saved files")){
                       mOnDownloadListener.onFileOpen(file_links.get(getAdapterPosition()
                       ),file_names.get(getAdapterPosition()));
                   }else{

                   if(isFile_Exist(file_names.get(getAdapterPosition()))){
                       mOnDownloadListener.onFileOpen(file_path,file_names.get(getAdapterPosition()));
                   }else{
                       Toast.makeText(mContext, "Please download file first", Toast.LENGTH_SHORT).show();
                   }}

               }
           });
         itemView.setOnLongClickListener(new View.OnLongClickListener() {
             @Override
             public boolean onLongClick(View v) {
                 itemView.setBackgroundResource(R.color.colorLightPrimary);

                 if(fragment_name.equalsIgnoreCase("Saved files")){
                     mOnDownloadListener.onItemSelected(file_links.get(getAdapterPosition()
                     ),file_links.get(getAdapterPosition()));
                 }else{
                     if(isFile_Exist(file_names.get(getAdapterPosition()))){
                         mOnDownloadListener.onItemSelected(file_path,file_names.get(getAdapterPosition()));
                     }else{
                         Toast.makeText(mContext, "Please download file first", Toast.LENGTH_SHORT).show();
                     }}
                 return true;
             }
         });
        }

    }

    public boolean isFile_Exist(String file_name){
        boolean found=false;
        String new_file=file_name+".pdf";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
            String root=mContext.getExternalFilesDir(null).getAbsolutePath();
            File dir=new File(root+"/Documents");
            if(dir.exists()){
            for(File single:dir.listFiles()) {
                if (single.getName().equals(new_file)) {
                    file_path=single.getPath();
                    found = true;

                }
            }}

        }else{
            File root = Environment.getExternalStorageDirectory();
            File path = new File(root.getAbsolutePath() + "/MGIT/Documents");
            if(path.exists()){
                for(File single:path.listFiles()){
                    if(single.getName().equals(new_file)){
                        file_path=single.getPath();
                        found=true;

                    }
                }

            }}


        return found;
    }

}
