package com.techtravelcoder.alluniversityinformations.mcq;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.techtravelcoder.alluniversityinformation.R;

import java.util.ArrayList;

public class DetailsExamHisAdapter extends RecyclerView.Adapter<DetailsExamHisAdapter.ExamHisViewHolder> {
    private Context context;
    private ArrayList<DetailsExamHisModel>list;

    public DetailsExamHisAdapter(Context context, ArrayList<DetailsExamHisModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public DetailsExamHisAdapter.ExamHisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.exam_result_design,parent,false);

        return new ExamHisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsExamHisAdapter.ExamHisViewHolder holder, int position) {
        DetailsExamHisModel detailsExamHisModel=list.get(position);


        // Reset views to default state
        holder.sNo.setBackgroundResource(R.drawable.searchbar_background); // Replace with your default background
        holder.l1.setBackgroundResource(R.drawable.searchbar_background);
        holder.l2.setBackgroundResource(R.drawable.searchbar_background);
        holder.l3.setBackgroundResource(R.drawable.searchbar_background);
        holder.l4.setBackgroundResource(R.drawable.searchbar_background);
        holder.optionA.setTextColor(ContextCompat.getColor(context, R.color.whiteTextColor1)); // Replace with your default text color
        holder.optionB.setTextColor(ContextCompat.getColor(context, R.color.whiteTextColor1));
        holder.optionC.setTextColor(ContextCompat.getColor(context, R.color.whiteTextColor1));
        holder.optionD.setTextColor(ContextCompat.getColor(context, R.color.whiteTextColor1));
        holder.i1.setImageResource(0); // Set to 0 or your default image resource
        holder.i2.setImageResource(0);
        holder.i3.setImageResource(0);
        holder.i4.setImageResource(0);



        holder.title.setText(detailsExamHisModel.getTitle());
        holder.sNo.setText(""+(position+1));
        holder.optionA.setText(detailsExamHisModel.getOptionA());
        holder.optionB.setText(detailsExamHisModel.getOptionB());
        holder.optionC.setText(detailsExamHisModel.getOptionC());
        holder.optionD.setText(detailsExamHisModel.getOptionD());
        holder.explanation.setText("Explanation : "+detailsExamHisModel.getExplanation());


        if (detailsExamHisModel.getAnsStatus().equals("true"))
        {
            holder.sNo.setBackgroundResource(R.drawable.mcq_back_green);

            if(detailsExamHisModel.getRightAnswer().equals(detailsExamHisModel.getOptionA())){
                holder.l1.setBackgroundResource(R.drawable.green_back);
                holder.optionA.setTextColor(ContextCompat.getColor(context,R.color.back));
                holder.i1.setImageResource(R.drawable.mcorrect);
            }
            else if(detailsExamHisModel.getRightAnswer().equals(detailsExamHisModel.getOptionB())){
                holder.l2.setBackgroundResource(R.drawable.green_back);
                holder.optionB.setTextColor(ContextCompat.getColor(context,R.color.back));
                holder.i2.setImageResource(R.drawable.mcorrect);



            }
            else if(detailsExamHisModel.getRightAnswer().equals(detailsExamHisModel.getOptionC())){
                holder.l3.setBackgroundResource(R.drawable.green_back);
                holder.optionC.setTextColor(ContextCompat.getColor(context,R.color.back));
                holder.i3.setImageResource(R.drawable.mcorrect);



            }
            else if(detailsExamHisModel.getRightAnswer().equals(detailsExamHisModel.getOptionD())){
                holder.l4.setBackgroundResource(R.drawable.green_back);
                holder.optionD.setTextColor(ContextCompat.getColor(context,R.color.back));
                holder.i4.setImageResource(R.drawable.mcorrect);



            }
        }
        else {
            holder.sNo.setBackgroundResource(R.drawable.mcq_back_red);

            if(detailsExamHisModel.getRightAnswer().equals(detailsExamHisModel.getOptionA())){
                holder.l1.setBackgroundResource(R.drawable.green_back);
                holder.optionA.setTextColor(ContextCompat.getColor(context,R.color.back));
                holder.i1.setImageResource(R.drawable.mcorrect);


            }
            if(detailsExamHisModel.getRightAnswer().equals(detailsExamHisModel.getOptionB())){
                holder.l2.setBackgroundResource(R.drawable.green_back);
                holder.optionB.setTextColor(ContextCompat.getColor(context,R.color.back));
                holder.i2.setImageResource(R.drawable.mcorrect);

            }
            if(detailsExamHisModel.getRightAnswer().equals(detailsExamHisModel.getOptionC())){
                holder.l3.setBackgroundResource(R.drawable.green_back);
                holder.optionC.setTextColor(ContextCompat.getColor(context,R.color.back));
                holder.i3.setImageResource(R.drawable.mcorrect);



            }
            if(detailsExamHisModel.getRightAnswer().equals(detailsExamHisModel.getOptionD())){
                holder.l4.setBackgroundResource(R.drawable.green_back);
                holder.optionD.setTextColor(ContextCompat.getColor(context,R.color.back));
                holder.i4.setImageResource(R.drawable.mcorrect);

            }



            if(detailsExamHisModel.getStudentAns().equals(detailsExamHisModel.getOptionA())){
                holder.l1.setBackgroundResource(R.drawable.red_back);
                holder.optionA.setTextColor(ContextCompat.getColor(context,R.color.back));
                holder.i1.setImageResource(R.drawable.mcross);


            }
            if(detailsExamHisModel.getStudentAns().equals(detailsExamHisModel.getOptionB())){
                holder.l2.setBackgroundResource(R.drawable.red_back);
                holder.optionB.setTextColor(ContextCompat.getColor(context,R.color.back));
                holder.i2.setImageResource(R.drawable.mcross);



            }
            if(detailsExamHisModel.getStudentAns().equals(detailsExamHisModel.getOptionC())){
                holder.l3.setBackgroundResource(R.drawable.red_back);
                holder.optionC.setTextColor(ContextCompat.getColor(context,R.color.back));
                holder.i3.setImageResource(R.drawable.mcross);



            }
            if(detailsExamHisModel.getStudentAns().equals(detailsExamHisModel.getOptionD())){
                holder.l4.setBackgroundResource(R.drawable.red_back);
                holder.optionD.setTextColor(ContextCompat.getColor(context,R.color.back));
                holder.i4.setImageResource(R.drawable.mcross);



            }
        }




    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ExamHisViewHolder extends RecyclerView.ViewHolder {

        private TextView title,sNo,explanation;
        private TextView optionA,optionB,optionC,optionD;
        private LinearLayout l1,l2,l3,l4;
        private ImageView i1,i2,i3,i4;
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
            i1=itemView.findViewById(R.id.i1);
            i2=itemView.findViewById(R.id.i2);
            i3=itemView.findViewById(R.id.i3);
            i4=itemView.findViewById(R.id.i4);






        }
    }
}
