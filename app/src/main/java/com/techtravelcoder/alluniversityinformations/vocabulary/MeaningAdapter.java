package com.techtravelcoder.alluniversityinformations.vocabulary;

import static android.content.Context.CLIPBOARD_SERVICE;

import static androidx.core.content.ContextCompat.getSystemService;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;
import com.techtravelcoder.alluniversityinformation.R;
import com.techtravelcoder.alluniversityinformations.postDetails.PostWebViewActivity;

import java.security.acl.Group;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MeaningAdapter extends RecyclerView.Adapter<MeaningAdapter.MeaningViewHolder> {

    private List<Meaning> meaningList;
    private Context context;
    static TextToSpeech textToSpeech;
    Boolean bool=true;
    String check;
    String result="";
    String currentSpinItem="";

    FirebaseTranslator englishBanglaTranslator,englishArabicTranslator,englishUrduTranslator,englishAfrikaansTranslator,englishBelarusianTranslator,englishBulgarianTranslator
            ,englishCatalanTranslator,englishCzechTranslator,englishWelshTranslator,englishDanishTranslator,englishGermanTranslator,englishGreekTranslator,englishEnglishTranslator
            ,englishEsperantoTranslator,englishSpanishTranslator,englishEstonianTranslator,englishPersianTranslator,englishFinnishTranslator,englishIrishTranslator,englishGalicianTranslator
           ,englishGujaratiTranslator,englishHebrewTranslator,englishHindiTranslator,englishHaitianTranslator,englishHungarianTranslator,englishIndonesianTranslator,englishIcelandicTranslator
            ,englishItalianTranslator,englishJapaneseTranslator,englishGeorgianTranslator,englishKannadaTranslator,englishKoreanTranslator,englishLithuanianTranslator,
            englishLatvianTranslator,englishMacedonianTranslator,englishMarathiTranslator,englishMalteseTranslator,englishDutchTranslator,englishNorwegianTranslator,englishPolishTranslator,
            englishPortugueseTranslator,englishRomanianTranslator,englishRussianTranslator,englishSlovakTranslator,englishSlovenianTranslator,englishAlbanianTranslator,englishSwedishTranslator,
            englishSwahiliTranslator,englishTamilTranslator,englishTeluguTranslator,englishThaiTranslator,englishTagalogTranslator,englishTurkishTranslator,englishUkrainianTranslator,englishVietnameseTranslator,englishFrenchTranslator;



    public MeaningAdapter(List<Meaning> meaningList,Context context) {
        this.meaningList = meaningList;
        this.context=context;
        textToSpeech = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    int result = textToSpeech.setLanguage(Locale.getDefault());

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(context, "Language not supported", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Initialization failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String utteranceId) {
                // Speech started
            }

            @Override
            public void onDone(String utteranceId) {
                // Speech completed
            }

            @Override
            public void onError(String utteranceId) {
                // Speech error
            }
        });
    }

    public void updateNewData(List<Meaning> newMeaningList) {
        meaningList = newMeaningList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MeaningViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View itemView = LayoutInflater.from(context).inflate(R.layout.vocabulary_design, parent, false);
        return new MeaningViewHolder(itemView);
    }

    private void copyToClipboard(String text) {
        // Get system clipboard service
        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        if (clipboardManager != null) {
            ClipData clipData = ClipData.newPlainText("Copied Text", text);
            clipboardManager.setPrimaryClip(clipData);
            Toast.makeText(context, "Text copied Successfully", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onBindViewHolder(@NonNull MeaningViewHolder holder, int position) {
        Meaning meaning = meaningList.get(position);
        holder.partOfSpeechTextview.setText(meaning.getPartOfSpeech());
        holder.definitionsTextview.setText(joinDefinitions(meaning.getDefinitions()));

        holder.notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialogView = LayoutInflater.from(context).inflate(R.layout.notice_design, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView); // Set custom view
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                TextView ok=dialogView.findViewById(R.id.ok_button_id);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.alert_back);
                alertDialog.getWindow().setBackgroundDrawable(drawable);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=meaning.getPartOfSpeech()+"Definitions"+joinDefinitions(meaning.getDefinitions())+"Synonyms"+joinStrings(meaning.getSynonyms())+"Antonyms"+joinStrings(meaning.getAntonyms());
                if (textToSpeech != null && !text.isEmpty())
                {
                    if (bool==true) {
                        holder.textAudio.setText("Audio On");
                        bool=false;
                        holder.imageView.setImageResource(R.drawable.speaker_on);


                        textToSpeech.setSpeechRate(0.75f); // Adjust speech rate (0.5f is slower than normal)
                        textToSpeech.setPitch(1.0f); // Adjust pitch (1.0f is normal)
                        String[] sentences = text.split("[.!?]");

                            for (String sentence : sentences) {
                                // Trim whitespace and skip empty sentences
                                sentence = sentence.trim();
                                if (!sentence.isEmpty()) {
                                    speakSentence(sentence);
                                }
                            }

                        // Start speaking
                       // speakSentence(text);

                    }
                    else if(bool==false){
                        bool=true;
                        holder.textAudio.setText("Audio Off");
                        holder.imageView.setImageResource(R.drawable.speaker_off);
                        textToSpeech.stop();
                        // Toggle to OFF state
                    }


                }

            }
        });
        holder.copyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=holder.partOfSpeechTextview.getText().toString()+"Definitions"+holder.definitionsTextview.getText().toString()+"Synonyms"+holder.synonymsTextview.getText().toString()+"Antonyms"+holder.antonymsTextview.getText().toString();

                copyToClipboard(text);
            }
        });
        holder.shareText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    String text=holder.partOfSpeechTextview.getText().toString()+"\n\n"+"Definitions"+"\n\n"+holder.definitionsTextview.getText().toString()+"\n\n"+"Synonyms"+"\n\n"+holder.synonymsTextview.getText().toString()+"\n\n"+"Antonyms"+"\n\n"+holder.antonymsTextview.getText().toString();

                    Intent intent= new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        String subject = text;
                        String appLink = "Download Now : "+"\uD83D\uDC49 \uD83D\uDC49"+"https://play.google.com/store/apps/details?id=" + context.getPackageName();
                        String message = subject + "\n" + appLink;
                        intent.putExtra(Intent.EXTRA_TEXT, message);

                       context.startActivity(Intent.createChooser(intent,"Share With"));

                }catch (Exception e){
                    Toast.makeText(context, "Unable to Share!!!"+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        holder.translate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                View dialogView = LayoutInflater.from(context).inflate(R.layout.translate_design, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setView(dialogView); // Set custom view

                AppCompatSpinner spinner=dialogView.findViewById(R.id.translate_app_compat_id);
                List<String> dataList = Arrays.asList(
                        "Choose Language", "Bangla", "Arabic", "Urdu",
                        "Afrikaans", "Belarusian", "Bulgarian", "Catalan", "Czech",
                        "Welsh", "Danish", "German", "Greek", "English",
                        "Esperanto", "Spanish", "Estonian", "Persian", "Finnish",
                        "French", "Irish", "Galician", "Gujarati", "Hebrew",
                        "Hindi", "Croatian", "Haitian", "Hungarian", "Indonesian",
                        "Icelandic", "Italian", "Japanese", "Georgian", "Kannada",
                        "Korean", "Lithuanian", "Latvian", "Macedonian", "Marathi",
                        "Malay", "Maltese", "Dutch", "Norwegian", "Polish",
                        "Portuguese", "Romanian", "Russian", "Slovak", "Slovenian",
                        "Albanian", "Swedish", "Swahili", "Tamil", "Telugu",
                        "Thai", "Tagalog", "Turkish", "Ukrainian", "Vietnamese"
                );
                ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(context, android.R.layout.simple_spinner_item ,dataList);
                arrayAdapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                spinner.setAdapter(arrayAdapter);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        // Handle item selection
                        currentSpinItem = parentView.getItemAtPosition(position).toString();
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                        // Do nothing here
                    }
                });

                TextView convert=dialogView.findViewById(R.id.covert_id);
                TextView afterTr=dialogView.findViewById(R.id.after_tr_id);


                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.alert_back);
                alertDialog.getWindow().setBackgroundDrawable(drawable);
                result="";



                convert.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        //String text=meaning.getPartOfSpeech()+"   "+"Definitions"+"  "+joinDefinitions(meaning.getDefinitions())+"\n\n"+"Synonyms"+"\n\n"+joinStrings(meaning.getSynonyms())+"\n\n"+"Antonyms"+"\n\n"+joinStrings(meaning.getAntonyms());

                        if (currentSpinItem.equals("Bangla")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(FirebaseTranslateLanguage.EN).setTargetLanguage(FirebaseTranslateLanguage.BN).build();
                            englishBanglaTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishBanglaTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishBanglaTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    }).
                                                    addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishBanglaTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    }).
                                                    addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishBanglaTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    }).
                                                    addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishBanglaTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    }).
                                                    addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                        if (currentSpinItem.equals("Arabic")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder().setSourceLanguage(FirebaseTranslateLanguage.EN).setTargetLanguage(FirebaseTranslateLanguage.AR).build();
                            englishArabicTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishArabicTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishArabicTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    }).
                                                    addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishArabicTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    }).
                                                    addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishArabicTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    }).
                                                    addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishArabicTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    }).
                                                    addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });


                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                        }
                        if (currentSpinItem.equals("Urdu")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.UR)
                                    .build();
                            englishUrduTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishUrduTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishUrduTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishUrduTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishUrduTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishUrduTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Afrikaans")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.AF)
                                    .build();
                            englishAfrikaansTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishAfrikaansTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishAfrikaansTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishAfrikaansTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishAfrikaansTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishAfrikaansTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Belarusian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.BE)
                                    .build();
                            englishBelarusianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishBelarusianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishBelarusianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishBelarusianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishBelarusianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishBelarusianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Bulgarian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.BG)
                                    .build();
                            englishBulgarianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishBulgarianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishBulgarianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishBulgarianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishBulgarianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishBulgarianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Catalan")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.CA)
                                    .build();
                            englishCatalanTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishCatalanTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishCatalanTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishCatalanTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishCatalanTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishCatalanTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Czech")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.CS)
                                    .build();
                            englishCzechTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishCzechTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishCzechTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishCzechTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishCzechTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishCzechTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Welsh")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.CY)
                                    .build();
                            englishWelshTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishWelshTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishWelshTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishWelshTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishWelshTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishWelshTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Danish")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.DA)
                                    .build();
                            englishDanishTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishDanishTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishDanishTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishDanishTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishDanishTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishDanishTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("German")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.DE)
                                    .build();
                            englishGermanTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishGermanTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishGermanTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishGermanTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishGermanTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishGermanTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Greek")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.EL)
                                    .build();
                            englishGreekTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishGreekTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishGreekTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishGreekTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishGreekTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishGreekTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("English")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.EN)
                                    .build();
                            englishEnglishTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishEnglishTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishEnglishTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishEnglishTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishEnglishTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishEnglishTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Esperanto")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.EO)
                                    .build();
                            englishEsperantoTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishEsperantoTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishEsperantoTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishEsperantoTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishEsperantoTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishEsperantoTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Spanish")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.ES)
                                    .build();
                            englishSpanishTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishSpanishTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishSpanishTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishSpanishTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishSpanishTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishSpanishTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Estonian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.ET)
                                    .build();
                            englishEstonianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishEstonianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishEstonianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishEstonianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishEstonianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishEstonianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Persian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.FA)
                                    .build();
                            englishPersianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishPersianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishPersianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishPersianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishPersianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishPersianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Finnish")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.FI)
                                    .build();
                            englishFinnishTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishFinnishTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishFinnishTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishFinnishTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishFinnishTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishFinnishTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("French")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.FR) // Setting target language to French
                                    .build();
                            englishFrenchTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishFrenchTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishFrenchTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishFrenchTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishFrenchTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishFrenchTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        if (currentSpinItem.equals("Irish")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.GA)
                                    .build();
                            englishIrishTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishIrishTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishIrishTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishIrishTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishIrishTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishIrishTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Galician")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.GL)
                                    .build();
                            englishGalicianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishGalicianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishGalicianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishGalicianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishGalicianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishGalicianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Gujarati")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.GU)
                                    .build();
                            englishGujaratiTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishGujaratiTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishGujaratiTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishGujaratiTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishGujaratiTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishGujaratiTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Hebrew")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.HE)
                                    .build();
                            englishHebrewTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishHebrewTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishHebrewTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishHebrewTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishHebrewTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishHebrewTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if(currentSpinItem.equals("Hindi")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.HI)
                                    .build();
                            englishHindiTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishHindiTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishHindiTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishHindiTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishHindiTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishHindiTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if(currentSpinItem.equals("Haitian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.HT)
                                    .build();
                            englishHaitianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishHaitianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishHaitianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishHaitianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishHaitianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishHaitianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if(currentSpinItem.equals("Hungarian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.HU)
                                    .build();
                            englishHungarianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishHungarianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishHungarianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishHungarianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishHungarianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishHungarianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if(currentSpinItem.equals("Indonesian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.ID)
                                    .build();
                            englishIndonesianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishIndonesianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishIndonesianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishIndonesianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishIndonesianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishIndonesianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if(currentSpinItem.equals("Icelandic")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.IS)
                                    .build();
                            englishIcelandicTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishIcelandicTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishIcelandicTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishIcelandicTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishIcelandicTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishIcelandicTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if(currentSpinItem.equals("Italian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.IT)
                                    .build();
                            englishItalianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishItalianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            englishItalianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.definitionsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishItalianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.partOfSpeechTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishItalianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.synonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            englishItalianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                                        @Override
                                                        public void onSuccess(String s) {
                                                            //result=result+s+"\n "+"--------------------"+"\n ";
                                                            holder.antonymsTextview.setText(s);

                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to download modal", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                        if (currentSpinItem.equals("Japanese")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.JA) // Setting target language to Japanese
                                    .build();
                            englishJapaneseTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishJapaneseTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishJapaneseTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishJapaneseTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishJapaneseTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishJapaneseTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Georgian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.KA) // Setting target language to Georgian
                                    .build();
                            englishGeorgianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishGeorgianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishGeorgianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishGeorgianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishGeorgianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishGeorgianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Kannada")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.KN) // Setting target language to Kannada
                                    .build();
                            englishKannadaTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishKannadaTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishKannadaTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishKannadaTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishKannadaTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishKannadaTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Korean")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.KO) // Setting target language to Korean
                                    .build();
                            englishKoreanTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishKoreanTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishKoreanTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishKoreanTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishKoreanTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishKoreanTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Lithuanian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.LT) // Setting target language to Lithuanian
                                    .build();
                            englishLithuanianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishLithuanianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishLithuanianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishLithuanianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishLithuanianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishLithuanianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Latvian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.LV) // Setting target language to Latvian
                                    .build();
                            englishLatvianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishLatvianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishLatvianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishLatvianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishLatvianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishLatvianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Macedonian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.MK) // Setting target language to Macedonian
                                    .build();
                            englishMacedonianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishMacedonianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishMacedonianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishMacedonianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishMacedonianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishMacedonianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Marathi")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.MR) // Setting target language to Marathi
                                    .build();
                            englishMarathiTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishMarathiTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishMarathiTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishMarathiTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishMarathiTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishMarathiTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Maltese")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.MT) // Setting target language to Maltese
                                    .build();
                            englishMalteseTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishMalteseTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishMalteseTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishMalteseTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishMalteseTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishMalteseTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Dutch")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.NL) // Setting target language to Dutch
                                    .build();
                            englishDutchTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishDutchTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishDutchTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishDutchTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishDutchTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishDutchTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Norwegian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.NO) // Setting target language to Norwegian
                                    .build();
                            englishNorwegianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishNorwegianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishNorwegianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishNorwegianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishNorwegianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishNorwegianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Polish")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.PL) // Setting target language to Polish
                                    .build();
                            englishPolishTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishPolishTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishPolishTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishPolishTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishPolishTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishPolishTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Portuguese")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.PT) // Setting target language to Portuguese
                                    .build();
                            englishPortugueseTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishPortugueseTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishPortugueseTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishPortugueseTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishPortugueseTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishPortugueseTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Romanian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.RO) // Setting target language to Romanian
                                    .build();
                            englishRomanianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishRomanianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishRomanianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishRomanianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishRomanianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishRomanianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Russian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.RU) // Setting target language to Russian
                                    .build();
                            englishRussianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishRussianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishRussianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishRussianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishRussianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishRussianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Slovak")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.SK) // Setting target language to Slovak
                                    .build();
                            englishSlovakTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishSlovakTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishSlovakTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishSlovakTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishSlovakTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishSlovakTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Slovenian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.SL) // Setting target language to Slovenian
                                    .build();
                            englishSlovenianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishSlovenianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishSlovenianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishSlovenianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishSlovenianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishSlovenianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Albanian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.SQ) // Setting target language to Albanian
                                    .build();
                            englishAlbanianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishAlbanianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishAlbanianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishAlbanianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishAlbanianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishAlbanianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Swedish")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.SV) // Setting target language to Swedish
                                    .build();
                            englishSwedishTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishSwedishTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishSwedishTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishSwedishTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishSwedishTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishSwedishTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Swahili")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.SW) // Setting target language to Swahili
                                    .build();
                            englishSwahiliTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishSwahiliTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishSwahiliTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishSwahiliTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishSwahiliTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishSwahiliTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Tamil")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.TA) // Setting target language to Tamil
                                    .build();
                            englishTamilTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishTamilTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishTamilTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishTamilTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishTamilTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishTamilTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Telugu")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.TE) // Setting target language to Telugu
                                    .build();
                            englishTeluguTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishTeluguTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishTeluguTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishTeluguTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishTeluguTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishTeluguTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Thai")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.TH) // Setting target language to Thai
                                    .build();
                            englishThaiTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishThaiTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishThaiTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishThaiTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishThaiTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishThaiTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Tagalog")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.TL) // Setting target language to Tagalog
                                    .build();
                            englishTagalogTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishTagalogTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishTagalogTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishTagalogTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishTagalogTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishTagalogTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Turkish")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.TR) // Setting target language to Turkish
                                    .build();
                            englishTurkishTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishTurkishTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishTurkishTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishTurkishTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishTurkishTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishTurkishTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Ukrainian")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.UK) // Setting target language to Ukrainian
                                    .build();
                            englishUkrainianTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishUkrainianTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishUkrainianTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishUkrainianTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishUkrainianTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishUkrainianTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        if (currentSpinItem.equals("Vietnamese")) {
                            FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                                    .setSourceLanguage(FirebaseTranslateLanguage.EN)
                                    .setTargetLanguage(FirebaseTranslateLanguage.VI) // Setting target language to Vietnamese
                                    .build();
                            englishVietnameseTranslator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
                            FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
                            englishVietnameseTranslator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    englishVietnameseTranslator.translate(joinDefinitions(meaning.getDefinitions())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.definitionsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishVietnameseTranslator.translate(meaning.getPartOfSpeech()).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.partOfSpeechTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishVietnameseTranslator.translate(joinStrings(meaning.getSynonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.synonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    englishVietnameseTranslator.translate(joinStrings(meaning.getAntonyms())).addOnSuccessListener(new OnSuccessListener<String>() {
                                        @Override
                                        public void onSuccess(String s) {
                                            holder.antonymsTextview.setText(s);
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Fail to translate", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, "Fail to download model", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }





                        alertDialog.dismiss();

                    }
                });


            }
        });




        if (meaning.getSynonyms().isEmpty()) {
            holder.synonymsTitleTextview.setVisibility(View.GONE);
            holder.synonymsTextview.setVisibility(View.GONE);
        }
        else {
            holder.synonymsTitleTextview.setVisibility(View.VISIBLE);
            holder.synonymsTextview.setVisibility(View.VISIBLE);
            holder.synonymsTextview.setText(joinStrings(meaning.getSynonyms()));
        }
        if (meaning.getAntonyms().isEmpty()) {
            holder.antonymsTitleTextview.setVisibility(View.GONE);
            holder.antonymsTextview.setVisibility(View.GONE);
        }
        else {
            holder.antonymsTitleTextview.setVisibility(View.VISIBLE);
            holder.antonymsTextview.setVisibility(View.VISIBLE);
            holder.antonymsTextview.setText(joinStrings(meaning.getAntonyms()));
        }

    }

    @Override
    public int getItemCount() {
        return meaningList.size();
    }

    static class MeaningViewHolder extends RecyclerView.ViewHolder {

        TextView partOfSpeechTextview;
        private TextView definitionsTextview;
        private TextView synonymsTitleTextview;
        private TextView synonymsTextview;
        private TextView antonymsTitleTextview;
        private TextView antonymsTextview,textAudio,translate,notice;
        private ImageView imageView,copyText,shareText;

        MeaningViewHolder(@NonNull View itemView) {
            super(itemView);
            partOfSpeechTextview = itemView.findViewById(R.id.part_of_speech_textview);
            definitionsTextview = itemView.findViewById(R.id.definitions_textview);
            synonymsTitleTextview = itemView.findViewById(R.id.synonyms_title_textview);
            synonymsTextview = itemView.findViewById(R.id.synonyms_textview);
            antonymsTitleTextview = itemView.findViewById(R.id.antonyms_title_textview);
            antonymsTextview = itemView.findViewById(R.id.antonyms_textview);
            imageView=itemView.findViewById(R.id.speaker_off_id);
            textAudio=itemView.findViewById(R.id.audio_off_id);
            copyText=itemView.findViewById(R.id.copy_text_id);
            translate=itemView.findViewById(R.id.translate_id);
            shareText=itemView.findViewById(R.id.copy_share_id);
            notice=itemView.findViewById(R.id.voca_notice_id);
        }




    }
    private void speakSentence(String sentence) {
        if (textToSpeech != null && !sentence.isEmpty()) {

            // Speak the sentence
            HashMap<String, String> params = new HashMap<>();
            params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "text");
            textToSpeech.speak(sentence, TextToSpeech.QUEUE_ADD, params);
        }
    }

    private String joinDefinitions(List<Definition> definitions) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < definitions.size(); i++) {
            Definition definition = definitions.get(i);
            builder.append((i + 1)).append(". ").append(definition.getDefinition());
            if (i < definitions.size() - 1) {
                builder.append("\n\n");

            }
        }
        return builder.toString();
    }

    private String joinStrings(List<String> strings) {
        return String.join(", ", strings);
    }
}
