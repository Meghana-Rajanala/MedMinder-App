package com.example.myapplication;

import android.content.Context;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class ContactManager {
    private ArrayList<call.Contact> contacts;
    private ArrayAdapter<call.Contact> adapter;

    public ContactManager(Context context) {
        contacts = new ArrayList<call.Contact>();
        adapter = new ArrayAdapter<call.Contact>(context, android.R.layout.simple_list_item_1, contacts);
    }

    public ArrayAdapter<call.Contact> getAdapter() {
        return adapter;
    }

    public void addContact(call.Contact contact) {
        contacts.add(contact);
        adapter.notifyDataSetChanged();
    }

    public void updateContact(call.Contact oldContact, call.Contact newContact) {
        int index = contacts.indexOf(oldContact);
        if (index != -1) {
            contacts.set(index, newContact);
            adapter.notifyDataSetChanged();
        }
    }

    public void deleteContact(call.Contact contact) {
        contacts.remove(contact);
        adapter.notifyDataSetChanged();
    }

    public call.Contact getContact(int index) {
        return contacts.get(index);
    }

    public int getContactCount() {
        return contacts.size();
    }
}