package com.techtravelcoder.alluniversityinformations.mcq;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.alluniversityinformation.R;

import java.util.ArrayList;

public class ExamHisAdapter extends RecyclerView.Adapter<ExamHisAdapter.ExamHisViewHolder> {
    private Context context;
    private ArrayList<ExamDetails>list;

    public ExamHisAdapter(Context context, ArrayList<ExamDetails> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ExamHisAdapter.ExamHisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.exam_his_design,parent,false);
        return new ExamHisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamHisAdapter.ExamHisViewHolder holder, int position) {
        ExamDetails examHisModel=list.get(position);
        holder.title.setText("Set : "+examHisModel.getTitle());
        holder.topic.setText("Topic : "+examHisModel.getCategory());
        holder.date.setText(examHisModel.getDate());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DetailsExamHisActivity.class);
                intent.putExtra("title",examHisModel.getTitle());
                intent.putExtra("key",examHisModel.getKey());
                intent.putExtra("set",examHisModel.getCategory());

                context.startActivity(intent);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("ExamHis").child(FirebaseAuth.getInstance().getUid());
                databaseReference.child(examHisModel.getKey()).child("status").setValue("false");
                Toast.makeText(context, "Successfully Delete", Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ExamHisViewHolder extends RecyclerView.ViewHolder {
        private TextView topic,title,date,delete;
        public ExamHisViewHolder(@NonNull View itemView) {
            super(itemView);
            topic=itemView.findViewById(R.id.exam_his_topic_id);
            title=itemView.findViewById(R.id.exam_his_question_set_id);
            date=itemView.findViewById(R.id.exam_his_date_id);
            delete=itemView.findViewById(R.id.his_delete_id);

        }
    }
}
