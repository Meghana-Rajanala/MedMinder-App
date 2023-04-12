package com.example.myapplication.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.example.myapplication.ContactsDatabaseHelper;
import com.example.myapplication.R;
import com.example.myapplication.call;

import java.util.ArrayList;
import java.util.List;

public class ContactListAdapter extends BaseAdapter {

    private ArrayList<call.Contact> contacts;

    private Context mContext;
    private List<call.Contact> mContacts;

    public ContactListAdapter(Context context, List<call.Contact> contacts) {
        super();
        mContext = context;
        mContacts = contacts;
    }

    @Override
    public int getCount() {
        return mContacts.size();
    }

    @Override
    public Object getItem(int position) {
        return mContacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.list_item_contact, parent, false);

            holder = new ViewHolder();
            holder.nameTextView = convertView.findViewById(R.id.name_text_view);
            holder.phoneTextView = convertView.findViewById(R.id.phone_text_view);
            holder.editButton = convertView.findViewById(R.id.edit_button);
            holder.deleteButton = convertView.findViewById(R.id.delete_button);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final call.Contact contact = mContacts.get(position);
        holder.nameTextView.setText(contact.getName());
        holder.phoneTextView.setText(contact.getPhone());

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext, R.style.AlertDialogStyle);
                    builder.setTitle("Edit Contact");

                    LayoutInflater inflater = LayoutInflater.from(mContext);
                    View view = inflater.inflate(R.layout.dialog_add_contact, null);

                    final EditText nameEditText = view.findViewById(R.id.name_edittext);
                    final EditText phoneEditText = view.findViewById(R.id.phone_edittext);

                    nameEditText.setText(contact.getName());
                    phoneEditText.setText(contact.getPhone());

                    builder.setView(view);

                    builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String name = nameEditText.getText().toString().trim();
                            String phone = phoneEditText.getText().toString().trim();

                            if (TextUtils.isEmpty(name)) {
                                nameEditText.setError("Name is required");
                                return;
                            }

                            if (TextUtils.isEmpty(phone)) {
                                phoneEditText.setError("Phone number is required");
                                return;
                            }

                            // Update contact in the database
                            contact.setName(name);
                            contact.setPhone(phone);
                            ContactsDatabaseHelper dbHelper = new ContactsDatabaseHelper(mContext);
                            dbHelper.updateContact(contact);

                            // Update the contact in the ListView
                            notifyDataSetChanged();
                        }
                    });

                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContacts.remove(contact);
                ContactsDatabaseHelper dbHelper = new ContactsDatabaseHelper(mContext);
                dbHelper.deleteContact(contact);
                // Update the list view
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView nameTextView;
        TextView phoneTextView;
        Button editButton;
        Button deleteButton;
    }
}