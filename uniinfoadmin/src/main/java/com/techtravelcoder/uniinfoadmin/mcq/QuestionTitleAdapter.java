package com.techtravelcoder.uniinfoadmin.mcq;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.uniinfoadmin.R;
import com.techtravelcoder.uniinfoadmin.post.MainPostActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestionTitleAdapter extends RecyclerView.Adapter<QuestionTitleAdapter.QuestionTitleViewHolder> {
    private Context context;
    private ArrayList<QuestionTitleModel>list;

    public QuestionTitleAdapter(Context context, ArrayList<QuestionTitleModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public QuestionTitleAdapter.QuestionTitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.question_set_name_design,parent,false);
        return new QuestionTitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionTitleAdapter.QuestionTitleViewHolder holder, int position) {
        QuestionTitleModel questionTitleModel=list.get(position);
        holder.title.setText(questionTitleModel.getTitle());
        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, QuestionAddActivity.class);
                intent.putExtra("key",questionTitleModel.getKey());
                context.startActivity(intent);
            }
        });




        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create AlertDialog.Builder instance
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                // Inflate the custom layout for the dialog
                final View view = LayoutInflater.from(context).inflate(R.layout.question_title_input, null);

                // Find EditText and TextView from the custom layout
                final EditText edQuestionSet = view.findViewById(R.id.ed_question_set_name);
                TextView addQuestionSet = view.findViewById(R.id.add_question_set_id);

                edQuestionSet.setText(questionTitleModel.getTitle());

                // Set the custom view to the dialog builder
                builder.setView(view);
                // Create and show the dialog
                AlertDialog alertDialog = builder.create();
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.back);
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(drawable);
                }
                alertDialog.show();
                alertDialog.setCancelable(false);

                // Set click listener for the addQuestionSet TextView
                addQuestionSet.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Check if the EditText is not empty
                        if (!TextUtils.isEmpty(edQuestionSet.getText().toString())) {

                            // Add the entry to the Firebase database
                            FirebaseDatabase.getInstance().getReference("McqQuestion").child("QuestionSet").child(questionTitleModel.getKey()).child("title").setValue(edQuestionSet.getText().toString())
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            // Show a success message
                                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                            // Dismiss the dialog
                            alertDialog.dismiss();
                        }
                    }
                });
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertObj = new AlertDialog.Builder(context);
                alertObj.setTitle(Html.fromHtml("<font color='#000000'>Confirm Delete...ℹ️</font>"));
                alertObj.setMessage(Html.fromHtml("<font color='#000000'>ℹ️ Do you want to delete this question set??❓❓</font>"));

                alertObj.setPositiveButton(Html.fromHtml("<font color='#000000'>✅Yes</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FirebaseDatabase.getInstance().getReference("McqQuestion").child("QuestionSet")
                                .child(questionTitleModel.getKey()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Successfully Delete", Toast.LENGTH_SHORT).show();
                                    }
                                });

                    }
                });

                alertObj.setNegativeButton(Html.fromHtml("<font color='#000000'>❌No</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = alertObj.create();
                dialog.show();

            }
        });







    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class QuestionTitleViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView update,delete;
        public QuestionTitleViewHolder(@NonNull View itemView) {
            super(itemView);

            title=itemView.findViewById(R.id.questino_set_title_name_id);
            update=itemView.findViewById(R.id.question_set_update_id);
            delete=itemView.findViewById(R.id.question_set_delete_id);

        }
    }
}
