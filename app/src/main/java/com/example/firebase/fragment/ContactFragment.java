package com.example.firebase.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.firebase.R;
import com.example.firebase.activity.BaseActivity;
import com.example.firebase.adapter.ContactAdapter;
import com.example.firebase.model.ContactModel;
import com.example.firebase.utils.Const;
import com.example.firebase.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContactFragment extends BaseFragment {
    private ProgressBar progressBar;
    private ListView lvContact;
    private ContactAdapter adapter;

    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        initUI(view);
        return view;
    }

    private void initUI(View view) {
        progressBar = view.findViewById(R.id.progressBar);
        lvContact = view.findViewById(R.id.lv_contact);

        adapter = new ContactAdapter(getActivity(), R.layout.item_list_contact);
        lvContact.setAdapter(adapter);
        listViewOnClick();

        //Truy xuất và lắng nghe sự thay đổi dữ liệu
        Utils.databaseReference(Const.Contact).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                //Vòng lặp để lấy dữ liệu khi có sự thay đổi trên Firebase
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    //Convert ra đối tượng ContactModel
                    ContactModel contact = data.getValue(ContactModel.class);
                    if (contact != null) {
                        contact.setContactId(data.getKey());
                        contact.setEmail((String) data.child("email").getValue());
                        contact.setName((String) data.child("name").getValue());
                        contact.setPhone((String) data.child("phone").getValue());
                        adapter.add(contact);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void listViewOnClick() {
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ContactModel contact = adapter.getItem(position);
                String key = contact.getContactId();
                ((BaseActivity) getActivity()).gotoFragment(new ContactDetailFragment(key), R.id.container);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().finish();
    }

}
