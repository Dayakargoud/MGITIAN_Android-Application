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

public class Faculty_Adapter extends RecyclerView.Adapter<Faculty_Adapter.ViewHolder> {
   private ArrayList<String> faculty_names,qualifications,designations=new ArrayList<>();
   private Context mContext;

    public Faculty_Adapter(ArrayList<String> faculty_names, ArrayList<String> qualifications, ArrayList<String> designations, Context mContext) {
        this.faculty_names = faculty_names;
        this.qualifications = qualifications;
        this.designations = designations;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(mContext).inflate(R.layout.faculty_list,parent,false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.faculty_name.setText(faculty_names.get(position));
           holder.qualification.setText(qualifications.get(position));
           holder.designation.setText(designations.get(position));
    }

    @Override
    public int getItemCount() {
        return faculty_names.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

            TextView faculty_name,designation,qualification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            faculty_name= itemView.findViewById(R.id.faculty_name);
            designation= itemView.findViewById(R.id.designation_name);
            qualification= itemView.findViewById(R.id.qualification);

        }
    }

}
