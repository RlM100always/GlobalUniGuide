package com.techtravelcoder.alluniversityinformations.mcq;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.techtravelcoder.alluniversityinformation.R;

import java.util.ArrayList;

public class QuestionTitleAdapter extends RecyclerView.Adapter<QuestionTitleAdapter.QuestionTitleViewHolder> {
    private Context context;
    private ArrayList<QuestionTitleModel>list;

    public QuestionTitleAdapter(Context context, ArrayList<QuestionTitleModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public QuestionTitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.mcq_set_design,parent,false);
        return new QuestionTitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionTitleViewHolder holder, int position) {
        QuestionTitleModel questionTitleModel=list.get(position);
        holder.title.setText(questionTitleModel.getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, DoExamActivity.class);
                intent.putExtra("key",questionTitleModel.getKey());
                intent.putExtra("category",questionTitleModel.getCategory());
                intent.putExtra("label",questionTitleModel.getLabel());
                intent.putExtra("title",questionTitleModel.getTitle());
                intent.putExtra("uniqueNum",questionTitleModel.getUniqueNum());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuestionTitleViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        public QuestionTitleViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.question_set_title_id);

        }
    }
}
