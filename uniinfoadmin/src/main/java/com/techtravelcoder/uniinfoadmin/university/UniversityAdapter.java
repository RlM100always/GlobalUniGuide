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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UniversityAdapter extends RecyclerView.Adapter<UniversityAdapter.UniViewHolder> {
    Context context;
    ArrayList<UniversityModel>list;
    String contryName;
    private String checkedTextTop,checkedTextPublic,checkedTextPrivates,checkedTextSuggest;


    public UniversityAdapter(Context context, ArrayList<UniversityModel> list,String contryName) {
        this.context = context;
        this.list = list;
        this.contryName=contryName;
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

                RadioGroup top,publics,privates,suggest;
                RadioButton topTrue,topFalse,publicsTrue,publicsFalse,privatesTrue,privatesFalse,suggestTrue,suggestFalse;
                top=view.findViewById(R.id.group_radio_top);
                publics=view.findViewById(R.id.radio_group_public);
                privates=view.findViewById(R.id.radio_group_private);
                suggest=view.findViewById(R.id.radio_group_suggest);

                topTrue=view.findViewById(R.id.top_true_id);
                topFalse=view.findViewById(R.id.top_false_id);
                publicsTrue=view.findViewById(R.id.public_true_id);
                publicsFalse=view.findViewById(R.id.public_false_id);
                privatesTrue=view.findViewById(R.id.private_true_id);
                privatesFalse=view.findViewById(R.id.private_false_id);
                suggestTrue=view.findViewById(R.id.suggest_true_id);
                suggestFalse=view.findViewById(R.id.suggest_false_id);





//                EditText top = view.findViewById(R.id.top_id);
//                EditText publics = view.findViewById(R.id.public_id);
//                EditText privates = view.findViewById(R.id.private_id);
//                EditText suggest = view.findViewById(R.id.suggest_id);

                TextView update = view.findViewById(R.id.add_university_post_id);

                uniName.setText(universityModel.getUniName());
                uniImage.setText(universityModel.getUniImageLink());
                uniWebLink.setText(universityModel.getUniWebLink());

//                top.setText(universityModel.getBest());
//                publics.setText(universityModel.getPublics());
//                privates.setText(universityModel.getPrivates());
//                suggest.setText(universityModel.getSuggested());

                if(universityModel.getBest().equals("true") || universityModel.getBest().equals("true ")){
                    topTrue.setChecked(true);
                    checkedTextTop="true";
                }else if(universityModel.getBest().equals("false") || universityModel.getBest().equals("false ")){
                    topFalse.setChecked(true);
                    checkedTextTop="false";
                }

                if(universityModel.getPrivates().equals("true") || universityModel.getPrivates().equals("true ")){
                    privatesTrue.setChecked(true);
                    checkedTextPrivates="true";
                }else if(universityModel.getPrivates().equals("false") || universityModel.getPrivates().equals("false ")){
                    privatesFalse.setChecked(true);
                    checkedTextPrivates="false";

                }

                if(universityModel.getPublics().equals("true") || universityModel.getPublics().equals("true ")){
                    publicsTrue.setChecked(true);
                    checkedTextPublic="true";
                }else if(universityModel.getPublics().equals("false") || universityModel.getPublics().equals("false ")){
                    publicsFalse.setChecked(true);
                    checkedTextPublic="false";

                }

                if(universityModel.getSuggested().equals("true") || universityModel.getSuggested().equals("true ")){
                    suggestTrue.setChecked(true);
                    checkedTextSuggest="true";
                }else if(universityModel.getSuggested().equals("false") || universityModel.getSuggested().equals("false ")){
                    suggestFalse.setChecked(true);
                    checkedTextSuggest="false";

                }


                top.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton checkedRadioButton = view.findViewById(checkedId);
                        checkedTextTop = checkedRadioButton.getText().toString();

                    }
                });
                top.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton checkedRadioButton = view.findViewById(checkedId);
                        checkedTextTop = checkedRadioButton.getText().toString();

                    }
                });
                publics.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton checkedRadioButton = view.findViewById(checkedId);
                        checkedTextPublic = checkedRadioButton.getText().toString();

                    }
                });
                privates.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton checkedRadioButton = view.findViewById(checkedId);
                        checkedTextPrivates = checkedRadioButton.getText().toString();

                    }
                });
                suggest.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton checkedRadioButton = view.findViewById(checkedId);
                        checkedTextSuggest = checkedRadioButton.getText().toString();

                    }
                });







                AlertDialog alertDialog = builder.create();

                alertDialog.show();

                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Calendar calendar = Calendar.getInstance();
                        Date times=calendar.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
                        String date = sdf.format(times);

                        Map<String,Object> map=new HashMap<>();
                        map.put("uniName",String.valueOf(uniName.getText()));
                        map.put("uniImageLink",String.valueOf(uniImage.getText()));
                        map.put("uniWebLink",String.valueOf(uniWebLink.getText()));

                        map.put("suggested",checkedTextSuggest);
                        map.put("publics",checkedTextPublic);
                        map.put("privates",checkedTextPrivates);
                        map.put("best",checkedTextTop);


                        map.put("contryName",universityModel.getContryName());
                        map.put("key",universityModel.getKey());
                        map.put("date",date);
                       // Toast.makeText(context, ""+checkedTextTop+" "+checkedTextPublic+" "+checkedTextPrivates+" "+checkedTextSuggest, Toast.LENGTH_SHORT).show();

                        FirebaseDatabase.getInstance().getReference("University").child(universityModel.getKey()).child("uniName").setValue(String.valueOf(uniName.getText()));
                        FirebaseDatabase.getInstance().getReference("University").child(universityModel.getKey()).child("uniImageLink").setValue(String.valueOf(uniImage.getText()));
                        FirebaseDatabase.getInstance().getReference("University").child(universityModel.getKey()).child("uniWebLink").setValue(String.valueOf(uniWebLink.getText()));
                        FirebaseDatabase.getInstance().getReference("University").child(universityModel.getKey()).child("suggested").setValue(checkedTextSuggest);
                        FirebaseDatabase.getInstance().getReference("University").child(universityModel.getKey()).child("publics").setValue(checkedTextPublic);
                        FirebaseDatabase.getInstance().getReference("University").child(universityModel.getKey()).child("privates").setValue(checkedTextPrivates);
                        FirebaseDatabase.getInstance().getReference("University").child(universityModel.getKey()).child("best").setValue(checkedTextTop);
                        FirebaseDatabase.getInstance().getReference("University").child(universityModel.getKey()).child("contryName").setValue(contryName);
                        FirebaseDatabase.getInstance().getReference("University").child(universityModel.getKey()).child("date").setValue(date);
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();



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

    public void searchListsFunc(ArrayList<UniversityModel> fList) {
        list=fList;
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
