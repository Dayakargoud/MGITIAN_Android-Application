package com.dayakar.mgitian.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dayakar.mgitian.R;

public class BunkManagerActivity extends AppCompatActivity {
    private ProgressBar progressBar;
    private TextView percentage,summary,num_attend,num_total;
    private Button calculate,btn_attend,btn_bunk;
    private EditText total_text,attended_text,require_perce_text;
    private TextView req_textView,card_pertage_text,card_total_classes;
    private CardView status_cardView,attendanec_card;
    private RelativeLayout relativeLayout;
    float actualPercentage;
    float card_attedance_perct_value,card_atnd_classes_value,card_total_classes_value;
    private int progress = 0;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bunk_manager);

        mToolbar= findViewById(R.id.bunk_toolbar);
        setSupportActionBar(mToolbar);
        progressBar = findViewById(R.id.progressBar);
        percentage = findViewById(R.id.text_percentage);
        calculate = findViewById(R.id.calculate);
        total_text= findViewById(R.id.total_classes);
        attended_text= findViewById(R.id.attended_classes);
        require_perce_text= findViewById(R.id.require_percentage);
        req_textView=findViewById(R.id.req_text);
        summary= findViewById(R.id.summary);
        num_attend= findViewById(R.id.attended_num);
        num_total= findViewById(R.id.total_num);
        status_cardView= findViewById(R.id.status_card);
        relativeLayout= findViewById(R.id.inner_relative_layout);
        attendanec_card=findViewById(R.id.percatge_card);
        card_pertage_text=findViewById(R.id.percatge_text1);
        card_total_classes=findViewById(R.id.percatge_text3);
        btn_attend=findViewById(R.id.btn_attnd);
        btn_bunk=findViewById(R.id.btn_bunk);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Bunk Manager");



        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String button_text = calculate.getText().toString();
                if (button_text.equals("Calculate")) {

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    float percent=get_percentage();
                                    int per = (int) percent;
                                    progressBar.setProgress(per);
                                    percentage.setText(String.format("%.2f", percent)+ "%");
                                }
                            });

                        }

                    }).start();

                }
                if(button_text.equals("Clear")){
                    total_text.setText("");
                    attended_text.setText("");
                    require_perce_text.setText("75");
                    total_text.setVisibility(View.VISIBLE);
                    attended_text.setVisibility(View.VISIBLE);
                    require_perce_text.setVisibility(View.VISIBLE);
                    req_textView.setVisibility(View.VISIBLE);
                    status_cardView.setVisibility(View.INVISIBLE);
                    relativeLayout.setVisibility(View.INVISIBLE);
                    attendanec_card.setVisibility(View.INVISIBLE);

                    progressBar.setProgress(0);
                    calculate.setText("Calculate");
                    percentage.setText("0.00%");



                }
            }
        });


    }
    private float get_percentage() {
        String cls_input = total_text.getText().toString().trim();
        String att_clases = attended_text.getText().toString().trim();
        String re_percent_text = require_perce_text.getText().toString().trim();

        if (cls_input.isEmpty() && att_clases.isEmpty() && re_percent_text.isEmpty()) {
            return 0;
        }


        try {
            float required_percent = Float.parseFloat(re_percent_text);
            float total_classes = Float.parseFloat(cls_input);
            float attended_classes = Float.parseFloat(att_clases);
            if (attended_classes > total_classes) {
                Toast.makeText(this, "Attended classes should not be more than Total clases", Toast.LENGTH_SHORT).show();
                return 0;
                        }
            else if(total_classes>5000){
                Toast.makeText(this, "Unable to calculate for "+total_classes+" classes", Toast.LENGTH_SHORT).show();
                return 0;
            }
            //calculatn
            actualPercentage = ((attended_classes / total_classes) * 100);

            float ac = attended_classes;
            float tc = total_classes;
            float result=0;
            float target_percentage = actualPercentage;
            if(required_percent>100){
                    summary.setText("You can't achive "+required_percent+"% in this semester");
                    calculationCompleted(total_classes, attended_classes, actualPercentage);
                return actualPercentage;

            }
            else if((target_percentage==required_percent)&&(required_percent==100)){
                summary.setText("On Track,You can't miss the next class.");
                calculationCompleted(total_classes, attended_classes, actualPercentage);
                return actualPercentage;

            }else if((target_percentage!=required_percent)&&(required_percent==100)){

                while (target_percentage <=99.9) {
                    ac++;
                    tc++;
                    target_percentage = (ac / tc) * 100;
                   // System.out.println(target_percentage);
                }
                result=ac-attended_classes;

                summary.setText("You can't achive "+required_percent+"% in this semester but you can achieve "+99.9+"% by attending "+ String.format("%.2f", result)+ " classes.");
                calculationCompleted(total_classes, attended_classes, actualPercentage);
                return actualPercentage;

            }

            if(actualPercentage<required_percent){
                    while (target_percentage <required_percent) {
                        ac=(float) (ac+0.1);
                        tc= (float) (tc+0.1);
                        target_percentage = (ac / tc) * 100;
                       // System.out.println(target_percentage);
                    }
                    result=ac-attended_classes;
                    calculationCompleted(total_classes, attended_classes, actualPercentage);

            } else if(target_percentage==required_percent){
                result=0;
                calculationCompleted(total_classes, attended_classes, actualPercentage);


            }
            else{
                      while (target_percentage >required_percent) {

                            tc=(float) (tc+0.1);
                            target_percentage = (ac / tc) * 100;
                           // System.out.println(target_percentage);

                      }
                //float req = ac - attended_classes;
                result=total_classes-tc;
                calculationCompleted(total_classes, attended_classes, actualPercentage);
                }
            showAttendanceInfo(result,required_percent);


        } catch (Exception e) {
            Toast.makeText(this, "Enter correct input", Toast.LENGTH_SHORT).show();
            return 0;
        }

       return actualPercentage;


    }
    private void showAttendanceInfo(float result,float required_percent){
        if (result == 0) {
            summary.setText("On Track,You can't miss the next class.");
        } else if (result> 0) {
            float days=result/6;
            summary.setText("You need to attend next " +  String.format("%.2f", result)+ " classes to get " +
                    String.format("%.2f",required_percent)+" percentage"
                    +" i.e "+String.format("%.2f",days)+" working days."

            );
        } else {
            float days=Math.abs(result)/6;
            summary.setText("You may leave upto " + String.format("%.2f",Math.abs(result)) + " classes"
                    +" i.e "+String.format("%.2f",days)+" working days."
            );
        }
    }

    private   void calculationCompleted(float total_classes,float attended_classes,float perct){
            total_text.setVisibility(View.INVISIBLE);
            attended_text.setVisibility(View.INVISIBLE);
            require_perce_text.setVisibility(View.INVISIBLE);
            req_textView.setVisibility(View.INVISIBLE);
            num_total.setText(String.format("%.2f", total_classes));
            num_total.setVisibility(View.VISIBLE);
            num_attend.setText(String.format("%.2f", attended_classes));
            num_attend.setVisibility(View.VISIBLE);
            calculate.setText("Clear");
            relativeLayout.setVisibility(View.VISIBLE);
            status_cardView.setVisibility(View.VISIBLE);
            attendanec_card.setVisibility(View.VISIBLE);
          showAttendaceCard(total_classes,attended_classes,perct);

        }
        private void showAttendaceCard(float total_classes,float attended_classes,float perct){
        card_total_classes_value=total_classes;
        card_atnd_classes_value=attended_classes;
        card_attedance_perct_value=perct;
            card_pertage_text.setText(String.format("%.2f", card_attedance_perct_value)+"%");
            card_total_classes.setText(card_atnd_classes_value+"/" +card_total_classes_value);

            btn_attend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    card_atnd_classes_value++;
                    card_total_classes_value++;
                    card_attedance_perct_value=(card_atnd_classes_value/card_total_classes_value)*100;
                    card_pertage_text.setText(String.format("%.2f", card_attedance_perct_value)+"%");
                    card_total_classes.setText(card_atnd_classes_value+"/" +card_total_classes_value);
                }
            });

            btn_bunk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    card_total_classes_value++;
                    card_attedance_perct_value=(card_atnd_classes_value/card_total_classes_value)*100;
                    card_pertage_text.setText(String.format("%.2f", card_attedance_perct_value)+"%");
                    card_total_classes.setText(card_atnd_classes_value+"/" +card_total_classes_value);


                }
            });
        }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }


    }