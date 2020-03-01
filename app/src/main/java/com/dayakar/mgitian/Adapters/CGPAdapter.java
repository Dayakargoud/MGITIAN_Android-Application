package com.dayakar.mgitian.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dayakar.mgitian.R;

import java.util.ArrayList;

public class CGPAdapter extends RecyclerView.Adapter<CGPAdapter.CGPAViewHolder> {
    private ArrayList<String> subjects=new ArrayList<>();
    private ArrayList<Integer> credits=new ArrayList<>();
    private Context mContext;
    private ArrayList<String> grades =new ArrayList<>();


    public CGPAdapter(ArrayList<String> subjects, ArrayList<Integer> credits, Context context) {
        this.subjects = subjects;
        this.credits = credits;
        mContext = context;
        initialiseGrades();
    }

    @NonNull
    @Override
    public CGPAViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(mContext).inflate(R.layout.cgpa_item,parent,false);
        return new CGPAViewHolder(v);    }

    @Override
    public void onBindViewHolder(@NonNull CGPAViewHolder holder, int position) {
        holder.subject.setText(subjects.get(position));
        holder.credit.setText(String.valueOf(credits.get(position)));
        int p=position;

        holder.gradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String grd = parent.getItemAtPosition(position ).toString();
                    if(!(grd.equalsIgnoreCase("Select Grade"))) {

                      //  Toast.makeText(mContext, "selected " + grd, Toast.LENGTH_SHORT).show();
                         grades.set(p,grd);
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return subjects.size();
    }

    public class CGPAViewHolder extends RecyclerView.ViewHolder{
        TextView subject,credit;
        Spinner gradeSpinner;

        public CGPAViewHolder(@NonNull View itemView) {
            super(itemView);
            subject=itemView.findViewById(R.id.cgpa_sub);
            credit=itemView.findViewById(R.id.cgpa_sub_credits);
            gradeSpinner=itemView.findViewById(R.id.cgpa_grade);

        }
    }
    public double calculate(String year){
        int totalCredits=0;
        if(year.contains("4")||year.contains("3")){
            totalCredits=24;
        }else {
            totalCredits=21;
        }

        double sum=0;
       // Toast.makeText(mContext, ""+grades, Toast.LENGTH_SHORT).show();;
        System.out.println(grades);

        for(int i=0;i<subjects.size();i++){
            String grade=grades.get(i);
            if(grade.equalsIgnoreCase("O")){
                sum=sum+(10*credits.get(i));
                System.out.println(subjects.get(i)+"="+grade+" ="+(10*credits.get(i)));
            }else if(grade.equalsIgnoreCase("A+")){
                sum=sum+(9*credits.get(i));
                System.out.println(subjects.get(i)+"="+grade+" ="+(9*credits.get(i)));
            }else if(grade.equalsIgnoreCase("A")){
                sum=sum+(8*credits.get(i));
                System.out.println(subjects.get(i)+"="+grade+" ="+(8*credits.get(i)));

            }else if(grade.equalsIgnoreCase("B+")){
                sum=sum+(7*credits.get(i));
                System.out.println(subjects.get(i)+"="+grade+" ="+(7*credits.get(i)));

            }else if(grade.equalsIgnoreCase("B")){
                sum=sum+(6*credits.get(i));
                System.out.println(subjects.get(i)+"="+grade+" ="+(6*credits.get(i)));

            }else if(grade.equalsIgnoreCase("C")){
                sum=sum+(5*credits.get(i));
                System.out.println(subjects.get(i)+"="+grade+" ="+(5*credits.get(i)));

            }else if(grade.equalsIgnoreCase("F")){
                sum=sum+(0*credits.get(i));
                System.out.println(subjects.get(i)+"="+grade+" ="+(0*credits.get(i)));

            }else if(grade.equalsIgnoreCase("Ab")) {
                sum=sum+(0*credits.get(i));
            }


        }
        System.out.println("sum= "+sum);
        //double sgpa=sum/totalCredits;
      //  System.out.println("CGPA= "+sgpa);
       // Toast.makeText(mContext, " CGPA"+cgpa, Toast.LENGTH_SHORT).show();
        System.out.println(grades);
        return sum;

    }
    public void initialiseGrades(){
        for(int i=0;i<subjects.size();i++){
            grades.add(null);

        }
    }
}
