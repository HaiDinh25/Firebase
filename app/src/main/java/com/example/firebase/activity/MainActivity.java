package com.example.firebase.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.firebase.R;
import com.example.firebase.adapter.ContactAdapter;
import com.example.firebase.model.Contact;
import com.example.firebase.utils.Utils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
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
        adapter.clear();

        //Truy xuất và lắng nghe sự thay đổi dữ liệu
        Utils.databaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Vòng lặp để lấy dữ liệu khi có sự thay đổi trên Firebase
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    //Convert ra đối tượng Contact
                    Contact contact = data.getValue(Contact.class);
                    String key = data.getKey();
                    String name = String.valueOf(data.child("name").getValue());
                    String email = String.valueOf(data.child("email").getValue());
                    String phone = String.valueOf(data.child("phone").getValue());
                    Log.d("TAG", "onDataChange: " + name);
                    contact.setContactId(key);
                    contact.setContactName(name);
                    contact.setContactEmail(email);
                    contact.setContactPhone(phone);
                    adapter.add(contact);
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
                Contact contact = adapter.getItem(position);
                String key = contact.getContactId();
                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                intent.putExtra("KEY", key);
                startActivity(intent);
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
            Intent intent = new Intent(this, AddContactActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
