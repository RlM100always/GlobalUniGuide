package com.techtravelcoder.alluniversityinformations.notes;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Html;
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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.techtravelcoder.alluniversityinformation.R;

import java.util.ArrayList;

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
                String text=notesModel.getTitle()+"\n"+notesModel.getNotes();
                copyToClipboard(text);
            }
        });
        holder.share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=notesModel.getTitle()+"\n"+notesModel.getNotes();

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
                        FirebaseDatabase.getInstance().getReference("MyNotes")
                                .child(FirebaseAuth.getInstance().getUid())
                                .child(notesModel.getKey())
                                .removeValue();

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
