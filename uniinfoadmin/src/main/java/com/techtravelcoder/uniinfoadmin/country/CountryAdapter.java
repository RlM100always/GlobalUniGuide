package com.techtravelcoder.uniinfoadmin.country;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.uniinfoadmin.R;
import com.techtravelcoder.uniinfoadmin.university.UniversityActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CountryAdapter extends RecyclerView.Adapter<CountryAdapter.MyViewHolder> {

    Context context;
    ArrayList<CountryModel>list;

    public CountryAdapter(Context context, ArrayList<CountryModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public CountryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.country_design,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryAdapter.MyViewHolder holder, int position) {
        CountryModel countryModel=list.get(position);

        holder.name.setText(countryModel.getName());
        Glide.with(context).load(countryModel.getImageLink()).into(holder.img);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, UniversityActivity.class);
                intent.putExtra("name",countryModel.getName());
                context.startActivity(intent);
            }
        });
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(context);
                final View view = inflater.inflate(R.layout.task_update_design, null);
                EditText name = view.findViewById(R.id.enter_task_title1);
                EditText link = view.findViewById(R.id.enter_task_link1);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(view);


                name.setText(countryModel.getName());
                link.setText(countryModel.getImageLink());

                //String nameText = String.valueOf(name.getText());
                //String linkText = String.valueOf(link.getText());


                TextView update = view.findViewById(R.id.add_post_id1);

                AlertDialog alertDialog = builder.create();
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.back);
                alertDialog.getWindow().setBackgroundDrawable(drawable);
                alertDialog.show();

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Map<String,Object>map=new HashMap<>();
                        map.put("name",String.valueOf(name.getText()));
                        map.put("imageLink",String.valueOf(link.getText()));
                        map.put("key",countryModel.getKey());
                        String countryKey = countryModel.getKey();
                        if (countryKey != null) {
                            FirebaseDatabase.getInstance().getReference("Country").child(countryKey)
                                    .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        alertDialog.dismiss();
                    }
                });
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView name,update;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            img=itemView.findViewById(R.id.country_image_id);
            name=itemView.findViewById(R.id.country_name_id);
            update=itemView.findViewById(R.id.country_update_id);
        }
    }
}
