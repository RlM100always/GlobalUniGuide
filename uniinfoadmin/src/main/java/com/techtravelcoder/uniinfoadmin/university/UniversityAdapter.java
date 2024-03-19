package com.techtravelcoder.uniinfoadmin.university;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.uniinfoadmin.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.UniViewHolder> {
    Context context;
    ArrayList<UniversityModel>list;

    public UniversityAdapter(Context context, ArrayList<UniversityModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UniversityAdapter.UniViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.university_design,parent,false);
        return new UniViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UniversityAdapter.UniViewHolder holder, int position) {
        UniversityModel universityModel=list.get(position);
        holder.uniName.setText(universityModel.getUniName());
        Glide.with(context).load(universityModel.getUniImageLink()).into(holder.uniImage);
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                LayoutInflater inflater = LayoutInflater.from(v.getContext());
                View view = inflater.inflate(R.layout.university_input, null);
                builder.setView(view);

                EditText uniName = view.findViewById(R.id.university_name_id);
                EditText uniImage = view.findViewById(R.id.university_image_id);
                EditText uniWebLink = view.findViewById(R.id.university_website_id);
                EditText top = view.findViewById(R.id.top_id);
                EditText publics = view.findViewById(R.id.public_id);
                EditText privates = view.findViewById(R.id.private_id);
                EditText suggest = view.findViewById(R.id.suggest_id);

                TextView update = view.findViewById(R.id.add_university_post_id);

                uniName.setText(universityModel.getUniName());
                uniImage.setText(universityModel.getUniImageLink());
                uniWebLink.setText(universityModel.getUniWebLink());
                top.setText(universityModel.getBest());
                publics.setText(universityModel.getPublics());
                privates.setText(universityModel.getPrivates());
                suggest.setText(universityModel.getSuggested());




                AlertDialog alertDialog = builder.create();

                alertDialog.show();

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("uniName",String.valueOf(uniName.getText()));
                        map.put("uniImageLink",String.valueOf(uniImage.getText()));
                        map.put("uniWebLink",String.valueOf(uniWebLink.getText()));
                        map.put("suggested",String.valueOf(suggest.getText()));
                        map.put("publics",String.valueOf(publics.getText()));
                        map.put("privates",String.valueOf(privates.getText()));
                        map.put("contryName",universityModel.getContryName());
                        map.put("best",String.valueOf(top.getText()));
                        map.put("key",universityModel.getKey());

                            FirebaseDatabase.getInstance().getReference("University").child(universityModel.getKey())
                                    .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Something wrong...", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        });

                    }
                });
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete post!!!");
                builder.setMessage("Do you want to delete this post..");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference("University").child(universityModel.getKey())
                                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(context, "Delete Successful", Toast.LENGTH_SHORT).show();
                                    }
                                });
                        dialog.cancel();


                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog=builder.create();
                alertDialog.show();

            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class UniViewHolder extends RecyclerView.ViewHolder {
        TextView uniName,update,delete;
        ImageView uniImage;
        public UniViewHolder(@NonNull View itemView) {
            super(itemView);
            uniImage=itemView.findViewById(R.id.uni_design_image_id);
            uniName=itemView.findViewById(R.id.uni_design_uniname);
            update=itemView.findViewById(R.id.uni_update_id);
            delete=itemView.findViewById(R.id.uni_delete_id);

        }
    }
}
