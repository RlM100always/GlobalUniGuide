package com.techtravelcoder.alluniversityinformations.notes;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.alluniversityinformation.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NoteviewHolder> {
    private ArrayList<NotesModel> list;
    private Context context;

    public NotesAdapter(ArrayList<NotesModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public NotesAdapter.NoteviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_lay_design, parent, false);
        return new NoteviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesAdapter.NoteviewHolder holder, int position) {
        NotesModel notesModel = list.get(position);
        holder.title.setText(notesModel.getTitle());
        holder.desc.setText(notesModel.getNotes());
        holder.noteDate.setText(notesModel.getDate());

        // Handle expanding and collapsing
        boolean isExpanded = notesModel.isExpanded();
        if (notesModel.isExpanded()) {
            holder.desc.setVisibility(View.VISIBLE);
            holder.updown.setBackgroundResource(R.drawable.baseline_arrow_upward_24);
            holder.updown.setTag(R.drawable.baseline_arrow_upward_24);
        } else {
            holder.desc.setVisibility(View.GONE);
            holder.updown.setBackgroundResource(R.drawable.baseline_arrow_downward_24);
            holder.updown.setTag(R.drawable.baseline_arrow_downward_24);
        }
        holder.updown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                notesModel.setExpanded(!notesModel.isExpanded());
                notifyItemChanged(position);
            }
        });
        holder.copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text="MADE BY GLOBALUNIGUIDE \n\n"+"Title : "+notesModel.getTitle()+"\n"+"Note : "+notesModel.getNotes();
                copyToClipboard(text);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text="MADE BY GLOBALUNIGUIDE \n\n"+"Title : "+notesModel.getTitle()+"\n"+"Note : "+notesModel.getNotes();

                shareNoteContent(text);
            }
        });
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertObj = new AlertDialog.Builder(context);
                alertObj.setTitle(Html.fromHtml("<font color='#000000'>Confirm Delete...ℹ️</font>"));
                alertObj.setMessage(Html.fromHtml("<font color='#000000'>ℹ️ Do you want to delete this Note??❓❓</font>"));

                alertObj.setPositiveButton(Html.fromHtml("<font color='#000000'>✅Yes</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get the current adapter position
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition == RecyclerView.NO_POSITION) {
                            return;
                        }

                        NotesModel currentModel = list.get(adapterPosition);

                        FirebaseDatabase.getInstance().getReference("MyNotes")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(currentModel.getKey())
                                .removeValue()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        // Ensure the position is still valid before removing
                                        int newPosition = holder.getAdapterPosition();
                                        if (newPosition != RecyclerView.NO_POSITION && newPosition < list.size()) {
                                            list.remove(newPosition);
                                            notifyItemRemoved(newPosition);
                                            notifyItemRangeChanged(newPosition, list.size());
                                            Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failed to delete note: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.alert_back);
                dialog.getWindow().setBackgroundDrawable(drawable);
            }
        });

        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final View view = LayoutInflater.from(context).inflate(R.layout.notes_design, null);
                builder.setView(view);

                final TextInputEditText title = view.findViewById(R.id.notes_title_id);
                final TextInputEditText writeNotes = view.findViewById(R.id.notes_write_id);
                TextView saveButton = view.findViewById(R.id.notes_save_id);
                TextView cancelButton = view.findViewById(R.id.notes_cancel_id);
                TextView heading = view.findViewById(R.id.notes_heading_id);

                heading.setText("Update Notes");
                title.setText(notesModel.getTitle());
                writeNotes.setText(notesModel.getNotes());


                final AlertDialog alertDialog = builder.create();
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.note_back);
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(drawable);
                }
                alertDialog.show();
                alertDialog.setCancelable(false);

                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!TextUtils.isEmpty(writeNotes.getText().toString())) {
                            Calendar calendar = Calendar.getInstance();
                            Date times = calendar.getTime();
                            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy, EEEE", Locale.getDefault());
                            String date = sdf.format(times);

                            Map<String, Object> map = new HashMap<>();
                            map.put("title", title.getText().toString());
                            map.put("notes", writeNotes.getText().toString());
                            map.put("date", date);
                            map.put("key",notesModel.getKey());


                            FirebaseDatabase.getInstance().getReference("MyNotes")
                                    .child(FirebaseAuth.getInstance().getUid())
                                    .child(notesModel.getKey())
                                    .setValue(map)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            notesModel.setTitle(title.getText().toString());
                                            notesModel.setNotes(writeNotes.getText().toString());
                                            notesModel.setDate(date);

                                            // Find the position of the updated note in the list
                                            int position = list.indexOf(notesModel);
                                            if (position != -1) {
                                                // Notify the adapter about the change
                                                notifyItemChanged(position);
                                            }

                                            Toast.makeText(context, "Successfully Note updated", Toast.LENGTH_SHORT).show();
                                            alertDialog.dismiss();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(context, "Failed to update note: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        } else {
                            Toast.makeText(context, "Notes is Empty", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });



    }

    private void shareNoteContent(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(shareIntent, "Share note via"));
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Note", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Note copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void searchLists(ArrayList<NotesModel> filteredList) {
        list=filteredList;
    }

    public class NoteviewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView desc,noteDate;
        private ImageView updown,copy,share,update,delete;

        public NoteviewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_note_id);
            desc = itemView.findViewById(R.id.note_description_id);
            updown = itemView.findViewById(R.id.up_down_id);
            noteDate = itemView.findViewById(R.id.note_date_id);
            copy = itemView.findViewById(R.id.notes_copy_id);
            share = itemView.findViewById(R.id.notes_share_id);
            update = itemView.findViewById(R.id.notes_edit_id);
            delete = itemView.findViewById(R.id.notes_delete_id);



        }
    }
}
