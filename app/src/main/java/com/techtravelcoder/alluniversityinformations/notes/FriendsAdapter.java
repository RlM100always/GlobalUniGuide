package com.techtravelcoder.alluniversityinformations.notes;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.FriendViewHolder> {

    private Context context;
    private ArrayList<FriendsModel>list;

    public FriendsAdapter(Context context, ArrayList<FriendsModel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FriendsAdapter.FriendViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.friends_design,parent,false);

        return new FriendViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.FriendViewHolder holder, int position) {
        FriendsModel friendsModel=list.get(position);

        holder.friendsDate.setText("last update : "+friendsModel.getDate());
        if (!TextUtils.isEmpty(friendsModel.getName())) {
            holder.friendsName.setText("Name : " + friendsModel.getName());
            holder.friendsName.setVisibility(View.VISIBLE);
        } else {
            holder.friendsName.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(friendsModel.getPhone())) {
            holder.friendsMobile.setText("Phone No : " + friendsModel.getPhone());
            holder.friendsMobile.setVisibility(View.VISIBLE);
        } else {
            holder.friendsMobile.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(friendsModel.getAddress())) {
            holder.friendsAddress.setText("Address : " + friendsModel.getAddress());
            holder.friendsAddress.setVisibility(View.VISIBLE);
        } else {
            holder.friendsAddress.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(friendsModel.getInstitution())) {
            holder.friendsInstitute.setText("Institute : " + friendsModel.getInstitution());
            holder.friendsInstitute.setVisibility(View.VISIBLE);
        } else {
            holder.friendsInstitute.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(friendsModel.getDepartment())) {
            holder.friendsDepartment.setText("Department : " + friendsModel.getDepartment());
            holder.friendsDepartment.setVisibility(View.VISIBLE);
        } else {
            holder.friendsDepartment.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(friendsModel.getNotes())) {
            holder.friendsNote.setText("Note : " + friendsModel.getNotes());
            holder.friendsNote.setVisibility(View.VISIBLE);
        } else {
            holder.friendsNote.setVisibility(View.GONE);
        }

        boolean isExpanded = friendsModel.isExpanded();
        if (friendsModel.isExpanded()) {
            holder.layout.setVisibility(View.VISIBLE);
            holder.upDown.setBackgroundResource(R.drawable.baseline_arrow_upward_24);
            holder.upDown.setTag(R.drawable.baseline_arrow_upward_24);
        } else {
            holder.layout.setVisibility(View.GONE);
            holder.upDown.setBackgroundResource(R.drawable.baseline_arrow_downward_24);
            holder.upDown.setTag(R.drawable.baseline_arrow_downward_24);
        }
        holder.upDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                friendsModel.setExpanded(!friendsModel.isExpanded());
                notifyItemChanged(position);
            }
        });

        holder.friendsCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text="MADE BY GLOBALUNIGUIDE \n\nName : "+friendsModel.getName()+"\n"+"Phone No : "+friendsModel.getPhone()+
                        "\n"+"Address : "+friendsModel.getAddress()+"\n"+"Institute : "+friendsModel.getInstitution()
                        +"\n"+"Department : "+friendsModel.getDepartment()+"\n"+"Facebook Id link : "+ friendsModel.getFacebook()
                        +"\n"+"Note : "+friendsModel.getNotes();
                copyToClipboard(text);
            }
        });
        holder.friendsShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text="MADE BY GLOBALUNIGUIDE \n\nName : "+friendsModel.getName()+"\n"+"Phone No : "+friendsModel.getPhone()+
                        "\n"+"Address : "+friendsModel.getAddress()+"\n"+"Institute : "+friendsModel.getInstitution()
                        +"\n"+"Department : "+friendsModel.getDepartment()+"\n"+"Facebook Id link : "+ friendsModel.getFacebook()
                        +"\n"+"Note : "+friendsModel.getNotes();

                shareNoteContent(text);
            }
        });
        holder.friendsCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                makeCall(friendsModel.getPhone());
            }
        });
        holder.friendsFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFacebookProfile(friendsModel.getFacebook());
            }
        });

        holder.friendsDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertObj = new AlertDialog.Builder(context);
                alertObj.setTitle(Html.fromHtml("<font color='#000000'>Confirm Delete...ℹ️</font>"));
                alertObj.setMessage(Html.fromHtml("<font color='#000000'>ℹ️ Do you want to delete this Friends Information??❓❓</font>"));

                alertObj.setPositiveButton(Html.fromHtml("<font color='#000000'>✅Yes</font>"), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Get the current adapter position
                        int adapterPosition = holder.getAdapterPosition();
                        if (adapterPosition == RecyclerView.NO_POSITION) {
                            return;
                        }

                        FriendsModel currentModel = list.get(adapterPosition);

                        FirebaseDatabase.getInstance().getReference("Friends")
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
                                            Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, "Failed to delete : " + e.getMessage(), Toast.LENGTH_SHORT).show();
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

        holder.friendsEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final View view = LayoutInflater.from(context).inflate(R.layout.add_friends_design, null);

                // Initialize UI components from the dialog layout
                final TextInputEditText nameEditText = view.findViewById(R.id.f_name_id);
                final TextInputEditText phoneEditText = view.findViewById(R.id.f_phone_no_id);
                final TextInputEditText addressEditText = view.findViewById(R.id.f_address_id);
                final TextInputEditText institutionEditText = view.findViewById(R.id.f_institute_id);
                final TextInputEditText departmentEditText = view.findViewById(R.id.f_department_id);
                final TextInputEditText facebookEditText = view.findViewById(R.id.f_facebook_id);
                final TextInputEditText notesEditText = view.findViewById(R.id.f_Notes_id);
                TextView saveButton = view.findViewById(R.id.f_save_id);
                TextView cancelButton = view.findViewById(R.id.f_cancel_id);
                TextView heading = view.findViewById(R.id.f_heading_id);

                heading.setText("Update Friends Information");
                nameEditText.setText(friendsModel.getName());
                phoneEditText.setText(friendsModel.getPhone());
                addressEditText.setText(friendsModel.getAddress());
                institutionEditText.setText(friendsModel.getInstitution());
                departmentEditText.setText(friendsModel.getDepartment());
                facebookEditText.setText(friendsModel.getFacebook());
                notesEditText.setText(friendsModel.getNotes());







                builder.setView(view);
                final AlertDialog alertDialog = builder.create();
                Drawable drawable = ContextCompat.getDrawable(context, R.drawable.note_back);
                if (alertDialog.getWindow() != null) {
                    alertDialog.getWindow().setBackgroundDrawable(drawable);
                }
                alertDialog.show();
                alertDialog.setCancelable(false);

                // Set click listener for the save button
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = nameEditText.getText().toString().trim();
                        String phone = phoneEditText.getText().toString().trim();
                        String address = addressEditText.getText().toString().trim();
                        String institution = institutionEditText.getText().toString().trim();
                        String department = departmentEditText.getText().toString().trim();
                        String facebook = facebookEditText.getText().toString().trim();
                        String notes = notesEditText.getText().toString().trim();

                        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phone)) {
                            Toast.makeText(context, "Name and Phone Number are required", Toast.LENGTH_SHORT).show();
                            return;
                        }


                        Calendar calendar = Calendar.getInstance();
                        Date times=calendar.getTime();
                        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy,EEEE", Locale.getDefault());
                        String date = sdf.format(times);

                        // Create a map to store data
                        Map<String, Object> friendData = new HashMap<>();
                        friendData.put("name", name);
                        friendData.put("phone", phone);
                        friendData.put("address", address);
                        friendData.put("institution", institution);
                        friendData.put("department", department);
                        friendData.put("facebook", facebook);
                        friendData.put("notes", notes);
                        friendData.put("date",date);
                        friendData.put("key",friendsModel.getKey());



                        // Save data to Firebase
                        FirebaseDatabase.getInstance().getReference("Friends").child(FirebaseAuth.getInstance().getUid()).child(friendsModel.getKey()).setValue(friendData).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(context, "Successfully Update.Refresh Page", Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "Failed to update data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                // Set click listener for the cancel button
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });
            }
        });



    }

    private void openFacebookProfile(String facebookUrl) {
        Intent facebookIntent = new Intent(Intent.ACTION_VIEW);
        facebookIntent.setData(Uri.parse(facebookUrl));
        try {
            context.startActivity(facebookIntent);
        } catch (Exception e) {
            Toast.makeText(context, "Unable to open Facebook profile", Toast.LENGTH_SHORT).show();
        }
    }

    private void makeCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        if (context.checkSelfPermission(android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            context.startActivity(callIntent);
        } else {
            Toast.makeText(context, "Please Go App Settings and Give Call Permission first", Toast.LENGTH_SHORT).show();
        }
    }

    private void copyToClipboard(String text) {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Note", text);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(context, "Information copied to clipboard", Toast.LENGTH_SHORT).show();
    }

    private void shareNoteContent(String text) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, text);
        context.startActivity(Intent.createChooser(shareIntent, "Share note via"));
    }





    @Override
    public int getItemCount() {
        return list.size();
    }

    public void searchLists(ArrayList<FriendsModel> filteredList) {
        list=filteredList;
    }

    public class FriendViewHolder extends RecyclerView.ViewHolder {
        TextView friendsDate;
        ImageView upDown;
        TextView friendsName;
        TextView friendsMobile;
        TextView friendsAddress;
        TextView friendsInstitute;
        TextView friendsDepartment;
        TextView friendsNote;
        ImageView friendsCall;
        ImageView friendsFacebook;
        ImageView friendsCopy;
        ImageView friendsShare;
        ImageView friendsEdit;
        ImageView friendsDelete;
        LinearLayout layout;
        public FriendViewHolder(@NonNull View itemView) {
            super(itemView);

            friendsDate = itemView.findViewById(R.id.friends_date_id);
            upDown = itemView.findViewById(R.id.up_down_id);
            friendsName = itemView.findViewById(R.id.friends_name_id);
            friendsMobile = itemView.findViewById(R.id.friends_mobile_id);
            friendsAddress = itemView.findViewById(R.id.friends_address_id);
            friendsInstitute = itemView.findViewById(R.id.friends_institute_id);
            friendsDepartment = itemView.findViewById(R.id.friends_department_id);
            friendsNote = itemView.findViewById(R.id.friends_note_id);
            friendsCall = itemView.findViewById(R.id.friends_call_id);
            friendsFacebook = itemView.findViewById(R.id.friends_facebook_id);
            friendsCopy = itemView.findViewById(R.id.friends_copy_id);
            friendsShare = itemView.findViewById(R.id.friends_share_id);
            friendsEdit = itemView.findViewById(R.id.friends_edit_id);
            friendsDelete = itemView.findViewById(R.id.friends_delete_id);
            layout=itemView.findViewById(R.id.friends_ll_id);
        }
    }
}
