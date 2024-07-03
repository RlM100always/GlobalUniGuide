package com.techtravelcoder.uniinfoadmin.mcq;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.uniinfoadmin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DetailsExamHisAdapter extends RecyclerView.Adapter<DetailsExamHisAdapter.ExamHisViewHolder> {
    private Context context;
    private ArrayList<DetailsExamHisModel>list;
    private String radioButton;
    private String questionSetKey;

    public DetailsExamHisAdapter(Context context, ArrayList<DetailsExamHisModel> list,String questionSetKey) {
        this.context = context;
        this.list = list;
        this.questionSetKey=questionSetKey;
    }

    @NonNull
    @Override
    public ExamHisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.exam_result_design_admin,parent,false);

        return new ExamHisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamHisViewHolder holder, int position) {
        DetailsExamHisModel detailsExamHisModel=list.get(position);



        holder.title.setText(detailsExamHisModel.getTitle());
        holder.sNo.setText(""+(position+1));
        holder.optionA.setText(detailsExamHisModel.getOptionA());
        holder.optionB.setText(detailsExamHisModel.getOptionB());
        holder.optionC.setText(detailsExamHisModel.getOptionC());
        holder.optionD.setText(detailsExamHisModel.getOptionD());
        holder.explanation.setText("Explanation : "+detailsExamHisModel.getExplanation());
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                LayoutInflater inflater = LayoutInflater.from(context);
                final View view = inflater.inflate(R.layout.question_type, null);

                EditText title=view.findViewById(R.id.ed_question_set_title);
                EditText a=view.findViewById(R.id.ed_option_a);
                EditText b=view.findViewById(R.id.ed_option_b);
                EditText c=view.findViewById(R.id.ed_option_c);
                EditText d=view.findViewById(R.id.ed_option_d);
                EditText explain=view.findViewById(R.id.ed_explanation_id);
                RadioGroup radioGroup=view.findViewById(R.id.group_radio_mcq);
                TextView submit=view.findViewById(R.id.question_submit_id);


                a.setText(detailsExamHisModel.getOptionA());
                b.setText(detailsExamHisModel.getOptionB());
                c.setText(detailsExamHisModel.getOptionC());
                d.setText(detailsExamHisModel.getOptionD());
                explain.setText(detailsExamHisModel.getExplanation());
                title.setText(detailsExamHisModel.getTitle());

                RadioButton r1=view.findViewById(R.id.rb_a_id);
                RadioButton r2=view.findViewById(R.id.rb_b_id);
                RadioButton r3=view.findViewById(R.id.rb_c_id);
                RadioButton r4=view.findViewById(R.id.rb_d_id);

                if(detailsExamHisModel.getRightAnswer().equals(detailsExamHisModel.getOptionA())){
                    r1.setChecked(true);
                }
                if(detailsExamHisModel.getRightAnswer().equals(detailsExamHisModel.getOptionB())){
                    r2.setChecked(true);
                }
                if(detailsExamHisModel.getRightAnswer().equals(detailsExamHisModel.getOptionC())){
                    r3.setChecked(true);
                }
                if(detailsExamHisModel.getRightAnswer().equals(detailsExamHisModel.getOptionD())){
                    r4.setChecked(true);
                }

                radioButton=detailsExamHisModel.getRightAnswer();


                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton checkedRadioButton = view.findViewById(checkedId);
                        radioButton = checkedRadioButton.getText().toString();

                        if(!TextUtils.isEmpty(radioButton)){
                            if(radioButton.equals("Option a")){
                                radioButton=a.getText().toString();
                            }
                            if(radioButton.equals("Option b")){
                                radioButton=b.getText().toString();
                            }
                            if(radioButton.equals("Option c")){
                                radioButton=c.getText().toString();
                            }
                            if(radioButton.equals("Option d")){
                                radioButton=d.getText().toString();
                            }
                        }



                    }
                });


                builder.setView(view);
                AlertDialog alertDialog= builder.create();
                Drawable drawable= ContextCompat.getDrawable(context,R.drawable.back);
                alertDialog.getWindow().setBackgroundDrawable(drawable);
                alertDialog.show();
                alertDialog.setCancelable(false);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(a.getText().toString()) && !TextUtils.isEmpty(b.getText().toString())&& !TextUtils.isEmpty(c.getText().toString() ) ){


                            Map<String,Object> map=new HashMap<>();
                            map.put("title",title.getText().toString());
                            map.put("optionA",a.getText().toString());
                            map.put("optionB",b.getText().toString());
                            map.put("optionC",c.getText().toString());
                            map.put("optionD",d.getText().toString());
                            map.put("explanation",explain.getText().toString());
                            map.put("rightAnswer",radioButton);
                            map.put("mcqKey",detailsExamHisModel.getMcqKey());
                             Toast.makeText(context, ""+radioButton, Toast.LENGTH_SHORT).show();

                            FirebaseDatabase.getInstance().getReference("McqQuestion").child("QuestionSet").child(questionSetKey).child("AllQuestion").child(detailsExamHisModel.getMcqKey()).setValue(map).
                                    addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    });



                            alertDialog.dismiss();

                        }

                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ExamHisViewHolder extends RecyclerView.ViewHolder {

        private TextView title,sNo,explanation;
        private TextView optionA,optionB,optionC,optionD,update;
        private LinearLayout l1,l2,l3,l4;
        public ExamHisViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.exam_result_title_id);
            sNo=itemView.findViewById(R.id.exam_result_serial_no_id);
            explanation=itemView.findViewById(R.id.exam_result_explanation_id);
            optionA=itemView.findViewById(R.id.t1);
            optionB=itemView.findViewById(R.id.t2);
            optionC=itemView.findViewById(R.id.t3);
            optionD=itemView.findViewById(R.id.t4);
            l1=itemView.findViewById(R.id.l1);
            l2=itemView.findViewById(R.id.l2);
            l3=itemView.findViewById(R.id.l3);
            l4=itemView.findViewById(R.id.l4);
            update=itemView.findViewById(R.id.question_update_id);







        }
    }
}
