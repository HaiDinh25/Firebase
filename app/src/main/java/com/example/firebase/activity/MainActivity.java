package com.example.firebase.activity;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.firebase.R;
import com.example.firebase.adapter.ContactAdapter;
import com.example.firebase.model.ContactModel;
import com.example.firebase.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends BaseActivity {
    private ListView lvContact;
    private ContactAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        lvContact = findViewById(R.id.lv_contact);
        adapter = new ContactAdapter(this, R.layout.item_list_contact);
        lvContact.setAdapter(adapter);
        listViewOnClick();

        //Truy xuất và lắng nghe sự thay đổi dữ liệu
        Utils.databaseReferenceContact().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                //Vòng lặp để lấy dữ liệu khi có sự thay đổi trên Firebase
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    //Convert ra đối tượng ContactModel
                    ContactModel contact = data.getValue(ContactModel.class);
                    if (contact != null) {
                        contact.setContactId(data.getKey());
                        contact.setContactName(data.child("name").getValue().toString());
                        contact.setContactEmail(data.child("email").getValue().toString());
                        contact.setContactPhone(data.child("phone").getValue().toString());
                        adapter.add(contact);
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
                gotoActivity(UpdateActivity.class, "KEY", key);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_contact) {
            gotoActivity(AddContactActivity.class);
        }
        return super.onOptionsItemSelected(item);
    }
}
