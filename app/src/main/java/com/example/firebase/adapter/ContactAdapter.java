package com.example.firebase.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.firebase.R;
import com.example.firebase.model.ContactModel;

public class ContactAdapter extends ArrayAdapter<ContactModel> {
    private Activity context;
    private int resource;

    private TextView tvContactId;
    private TextView tvName;
    private TextView tvEmail;
    private TextView tvPhone;

    public ContactAdapter(@NonNull Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = context.getLayoutInflater().inflate(resource, null);
        tvContactId = view.findViewById(R.id.tv_contactId);
        tvName = view.findViewById(R.id.tv_name);
        tvEmail = view.findViewById(R.id.tv_email);
        tvPhone = view.findViewById(R.id.tv_phone);

        ContactModel contact = getItem(position);
        tvContactId.setText(contact.getContactId());
        tvName.setText(contact.getName());
        tvEmail.setText(contact.getEmail());
        tvPhone.setText(contact.getPhone());

        return view;
    }
}
