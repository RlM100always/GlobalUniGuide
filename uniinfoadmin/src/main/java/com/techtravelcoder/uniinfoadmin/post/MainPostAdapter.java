package com.techtravelcoder.uniinfoadmin.post;

import android.content.Context;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainPostAdapter extends RecyclerView.Adapter<MainPostAdapter.MainPostViewHolder> {
    Context context;
    ArrayList<MainPostModel>list;
    String label,category;

    public MainPostAdapter(Context context, ArrayList<MainPostModel> list,String label,String category ) {
        this.context = context;
        this.list = list;
        this.label=label;
        this.category=category;
    }

    @NonNull
    @Override
    public MainPostAdapter.MainPostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.main_post_view_design,parent,false);
        return new MainPostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainPostAdapter.MainPostViewHolder holder, int position) {
            MainPostModel mainPostModel=list.get(position);
            holder.id.setText(mainPostModel.getPostId());
            holder.title.setText(mainPostModel.getTitle());
            Glide.with(context).load(mainPostModel.getImage()).into(holder.img);

            holder.update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                    final View view=LayoutInflater.from(context).inflate(R.layout.main_post_design,null);
                     EditText postId,imageLink,title;

                    Toast.makeText(context, ""+label+"   "+category, Toast.LENGTH_SHORT).show();
                    title=view.findViewById(R.id.post_title_id);
                    imageLink=view.findViewById(R.id.post_image_link_id);
                    postId=view.findViewById(R.id.post_post_id);

                    title.setText(mainPostModel.getTitle());
                    postId.setText(mainPostModel.getPostId());
                    imageLink.setText(mainPostModel.getImage());

                    TextView posttv=view.findViewById(R.id.add_main_post_id);


                    builder.setView(view);
                    AlertDialog alertDialog= builder.create();
                    Drawable drawable= ContextCompat.getDrawable(context,R.drawable.back);
                    alertDialog.getWindow().setBackgroundDrawable(drawable);
                    alertDialog.show();
                    posttv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!TextUtils.isEmpty(title.getText().toString()) && !TextUtils.isEmpty(imageLink.getText().toString()) && !TextUtils.isEmpty(postId.getText().toString())){

                                Calendar calendar = Calendar.getInstance();
                                Date times=calendar.getTime();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
                                String date = sdf.format(times);

                                String s_title=title.getText().toString();
                                String s_postid=postId.getText().toString();
                                String s_link=imageLink.getText().toString();



                                Map<String,Object> map=new HashMap<>();
                                map.put("title",s_title);
                                map.put("postId",s_postid);
                                map.put("image",s_link);
                                map.put("key",mainPostModel.getKey());
                                map.put("label",label);
                                map.put("uniqueNum",mainPostModel.getUniqueNum());
                                map.put("category",category);
                                map.put("date",date);
                                map.put("views",mainPostModel.getViews());


                                //Toast.makeText(this, ""+s_title+" "+s_label+" "+entryKey+" "+s_link, Toast.LENGTH_SHORT).show();
                                FirebaseDatabase.getInstance().getReference("Post").child(mainPostModel.getKey()).child("title").setValue(s_title);
                                FirebaseDatabase.getInstance().getReference("Post").child(mainPostModel.getKey()).child("postId").setValue(s_postid);
                                FirebaseDatabase.getInstance().getReference("Post").child(mainPostModel.getKey()).child("image").setValue(s_link);
                                FirebaseDatabase.getInstance().getReference("Post").child(mainPostModel.getKey()).child("category").setValue(category);
                                FirebaseDatabase.getInstance().getReference("Post").child(mainPostModel.getKey()).child("date").setValue(date);
                                FirebaseDatabase.getInstance().getReference("Post").child(mainPostModel.getKey()).child("label").setValue(label);
                                Toast.makeText(context, "Successful", Toast.LENGTH_SHORT).show();

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

    public class MainPostViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView id,title,update;
        public MainPostViewHolder(@NonNull View itemView) {
            super(itemView);

            img=itemView.findViewById(R.id.view_image_id);
            id=itemView.findViewById(R.id.view_unique_id);
            title=itemView.findViewById(R.id.view_title_id);
            update=itemView.findViewById(R.id.view_update_id);


        }
    }
}
