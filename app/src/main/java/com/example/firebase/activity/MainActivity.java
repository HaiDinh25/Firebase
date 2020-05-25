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
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.firebase.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private ListView lvContact;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initUI();
    }

    private void initUI() {
        lvContact = findViewById(R.id.lv_contact);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        lvContact.setAdapter(adapter);
        listViewOnClick();

        //Lấy đối tượng FirebaseDatabase
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        //Kết nối tới node có tên là contacts
        DatabaseReference databaseReference = firebaseDatabase.getReference("contacts");

        //Truy xuất và lắng nghe sự thay đổi dữ liệu
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
                //Vòng lặp để lấy dữ liệu khi có sự thay đổi trên Firebase
                for (DataSnapshot data: dataSnapshot.getChildren()) {
                    //Lấy key và giá trị data
                    String key = data.getKey();
                    String value = data.getValue().toString();
                    adapter.add((key + "\n" + value));
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
                String data = adapter.getItem(position);
                String key = data.split("\n")[0];
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
