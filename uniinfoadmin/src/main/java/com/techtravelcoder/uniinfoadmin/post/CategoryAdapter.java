package com.techtravelcoder.uniinfoadmin.post;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {
    ArrayList<CategoryModel>list;
    Context context;
    Boolean check;

    public CategoryAdapter(ArrayList<CategoryModel>list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public CategoryAdapter.CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(context).inflate(R.layout.category_design,parent,false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.CategoryViewHolder holder, int position) {


        CategoryModel categoryModel=list.get(position);
        holder.title.setText(categoryModel.getName());
        holder.label.setText(categoryModel.getLabel());
        holder.id.setText(categoryModel.getId());
        Glide.with(context).load(categoryModel.getImageLink()).into(holder.img);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,MainPostActivity.class);
                intent.putExtra("category",categoryModel.getName());
                intent.putExtra("label",categoryModel.getLabel());
                intent.putExtra("num",categoryModel.getId());
                context.startActivity(intent);


            }
        });
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(context);
                final View view = inflater.inflate(R.layout.post_category_design, null);

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                EditText title,label,link,uniId;
                RadioGroup radioGroup;
                RadioButton lesson,random;

                title=view.findViewById(R.id.category_title_id);
                label=view.findViewById(R.id.categoty_label_id);
                link=view.findViewById(R.id.category_image_link_id);
                uniId=view.findViewById(R.id.unique_id);
                radioGroup=view.findViewById(R.id.group_radio_category);
                lesson=view.findViewById(R.id.lesson_id);
                random=view.findViewById(R.id.random_id);

                uniId.setVisibility(View.GONE);

                title.setText(categoryModel.getName());
                label.setText(categoryModel.getLabel());
                link.setText(categoryModel.getImageLink());
                if(categoryModel.getCategoryType()!=null)
                {
                    if(categoryModel.getCategoryType()==true)lesson.setChecked(true);
                    if(categoryModel.getCategoryType()==false)random.setChecked(true);
                }

                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        // checkedId is the RadioButton selected
                        if (checkedId == R.id.lesson_id) {
                            check=true;

                        } else if (checkedId == R.id.random_id) {
                            check=false;
                        }
                    }
                });

                TextView posttv=view.findViewById(R.id.add_news_category_id);


                builder.setView(view);
                AlertDialog alertDialog= builder.create();
                Drawable drawable= ContextCompat.getDrawable(context,R.drawable.back);
                alertDialog.getWindow().setBackgroundDrawable(drawable);
                alertDialog.show();
                posttv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(link.getText().toString()) && !TextUtils.isEmpty(label.getText().toString()) && check!=null){
                            String entryKey = categoryModel.getKey();
                            String s_title=title.getText().toString();
                            String s_link=link.getText().toString();
                            String s_label=label.getText().toString();


                            Map<String,Object> map=new HashMap<>();
                            map.put("name",s_title);
                            map.put("imageLink",s_link);
                            map.put("label",s_label);
                            map.put("key",entryKey);
                            map.put("id",categoryModel.getId());
                            map.put("categoryType",check);

                           // Toast.makeText(context, ""+s_title+" "+s_label+" "+entryKey+" "+s_link, Toast.LENGTH_SHORT).show();


                            FirebaseDatabase.getInstance().getReference("Category").child(entryKey).setValue(map).
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

    public void searchListsFunc(ArrayList<CategoryModel> fList) {
        list=fList;
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView title,label,update,id;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.category_design_image_id);
            title=itemView.findViewById(R.id.category_design_title_id);
            label=itemView.findViewById(R.id.category_design_label_id);
            update=itemView.findViewById(R.id.category_design_update_id);
            id=itemView.findViewById(R.id.category_unique_id);

        }
    }
}
