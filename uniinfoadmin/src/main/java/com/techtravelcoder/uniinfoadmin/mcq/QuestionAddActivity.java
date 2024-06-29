package com.techtravelcoder.uniinfoadmin.mcq;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.uniinfoadmin.R;
import com.techtravelcoder.uniinfoadmin.post.MainPostActivity;

import java.util.HashMap;
import java.util.Map;

public class QuestionAddActivity extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private String key;
    private String radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_add);

        key=getIntent().getStringExtra("key");

        floatingActionButton=findViewById(R.id.add_question_float_id);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMcq();
            }
        });
    }


    private void addMcq() {
        AlertDialog.Builder builder=new AlertDialog.Builder(QuestionAddActivity.this);
        final View view=getLayoutInflater().inflate(R.layout.question_type,null);

        EditText title=view.findViewById(R.id.ed_question_set_title);
        EditText a=view.findViewById(R.id.ed_option_a);
        EditText b=view.findViewById(R.id.ed_option_b);
        EditText c=view.findViewById(R.id.ed_option_c);
        EditText d=view.findViewById(R.id.ed_option_d);
        EditText explain=view.findViewById(R.id.ed_explanation_id);
        RadioGroup radioGroup=view.findViewById(R.id.group_radio_mcq);
        TextView submit=view.findViewById(R.id.question_submit_id);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton checkedRadioButton = view.findViewById(checkedId);
                radioButton = checkedRadioButton.getText().toString();

                if(!TextUtils.isEmpty(radioButton)){
                    if(radioButton.equals("Option a")){
                        radioButton=a.getText().toString();
                    }
                    if(radioButton.equals("Option b")){
                        radioButton=b.getText().toString();
                    }
                    if(radioButton.equals("Option c")){
                        radioButton=c.getText().toString();
                    }
                    if(radioButton.equals("Option d")){
                        radioButton=d.getText().toString();
                    }
                }



            }
        });




        builder.setView(view);
        AlertDialog alertDialog= builder.create();
        Drawable drawable= ContextCompat.getDrawable(getApplicationContext(),R.drawable.back);
        alertDialog.getWindow().setBackgroundDrawable(drawable);
        alertDialog.show();
        alertDialog.setCancelable(false);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(a.getText().toString()) && !TextUtils.isEmpty(b.getText().toString())&& !TextUtils.isEmpty(c.getText().toString() ) ){

                    String entryKey = FirebaseDatabase.getInstance().getReference("McqQuestion").child("QuestionSet").child(key).child("AllQuestion").push().getKey();

                    Map<String,Object> map=new HashMap<>();
                    map.put("title",title.getText().toString());
                    map.put("optionA",a.getText().toString());
                    map.put("optionB",b.getText().toString());
                    map.put("optionC",c.getText().toString());
                    map.put("optionD",d.getText().toString());
                    map.put("explanation",explain.getText().toString());
                    map.put("rightAnswer",radioButton);
                    map.put("mcqKey",entryKey);
                  //  Toast.makeText(QuestionAddActivity.this, ""+radioButton, Toast.LENGTH_SHORT).show();

                    FirebaseDatabase.getInstance().getReference("McqQuestion").child("QuestionSet").child(key).child("AllQuestion").child(entryKey).setValue(map).
                            addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(QuestionAddActivity.this, "Successful", Toast.LENGTH_SHORT).show();
                                }
                            });



                    alertDialog.dismiss();

                }

            }
        });
    }

}