package com.techtravelcoder.alluniversityinformations.mcq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.alluniversityinformation.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DoExamAdapter extends RecyclerView.Adapter<DoExamAdapter.ExamViewHolder> {
    private Context context;
    private List<DoEaxmModel> mcqList;
    private String quesSetKey;

    public DoExamAdapter(Context context, List<DoEaxmModel> mcqList,String quesSetKey) {
        this.context = context;
        this.mcqList = mcqList;
        this.quesSetKey=quesSetKey;
    }

    @NonNull
    @Override
    public DoExamAdapter.ExamViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.do_exam_design,parent,false);
        return new ExamViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoExamAdapter.ExamViewHolder holder, int position) {

        DoEaxmModel doEaxmModel=mcqList.get(position);
        holder.serialNo.setText(""+(position+1));
        holder.mcqTitle.setText(doEaxmModel.getTitle());
        holder.optionA.setText(doEaxmModel.getOptionA());
        holder.optionB.setText(doEaxmModel.getOptionB());
        holder.optionC.setText(doEaxmModel.getOptionC());
        holder.optionD.setText(doEaxmModel.getOptionD());
        holder.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton selectedRadioButton = holder.itemView.findViewById(checkedId);
                if (selectedRadioButton != null) {
                    String ansStat;
                    String selectedText = selectedRadioButton.getText().toString();
                    Toast.makeText(context, ""+selectedText, Toast.LENGTH_SHORT).show();
                    if(selectedText.equals(doEaxmModel.getRightAnswer())){
                        ansStat="true";
                    }
                    else {
                        ansStat="false";
                    }

                    Map<String,Object> map=new HashMap<>();
                    map.put("title",doEaxmModel.getTitle());
                    map.put("optionA",doEaxmModel.getOptionA());
                    map.put("optionB",doEaxmModel.getOptionB());
                    map.put("optionC",doEaxmModel.getOptionC());
                    map.put("optionD",doEaxmModel.getOptionD());
                    map.put("explanation",doEaxmModel.getExplanation());
                    map.put("rightAnswer",doEaxmModel.getRightAnswer());
                    map.put("mcqKey",doEaxmModel.getMcqKey());
                    map.put("ansStatus",ansStat);
                    map.put("studentAns",selectedText);
                    //  Toast.makeText(QuestionAddActivity.this, ""+radioButton, Toast.LENGTH_SHORT).show();

                    FirebaseDatabase.getInstance().getReference("ExamHis").child(FirebaseAuth.getInstance().getUid())
                            .child(quesSetKey)
                            .child(doEaxmModel.getMcqKey()).setValue(map).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }
                            });
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return mcqList.size();
    }

    public class ExamViewHolder extends RecyclerView.ViewHolder {
        private TextView serialNo,mcqTitle;
        private RadioButton optionA,optionB,optionC,optionD;
        private RadioGroup radioGroup;


        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            serialNo=itemView.findViewById(R.id.mcq_serial_no_id);
            mcqTitle=itemView.findViewById(R.id.mcq_title_id);
            optionA=itemView.findViewById(R.id.mcq_option_a_id);
            optionB=itemView.findViewById(R.id.mcq_option_b_id);
            optionC=itemView.findViewById(R.id.mcq_option_c_id);
            optionD=itemView.findViewById(R.id.mcq_option_d_id);
            radioGroup=itemView.findViewById(R.id.mcq_radio_group_id);



        }
    }
}
