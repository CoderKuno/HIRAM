package com.example.hiram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoomHistory extends AppCompatActivity {

    private Button back_hist, all_hist;
    private TableLayout tableLayout;
    private TextView roomNo;
    private Transaction trans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_history);

        back_hist= findViewById(R.id.back_hist);
        all_hist= findViewById(R.id.room_hist);
        tableLayout= findViewById(R.id.tableLayout);
        roomNo= findViewById(R.id.roomNo);
        trans= new Transaction();

        roomNo.setText("Room "+trans.getRoom());

        back_hist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        addRow();
    }

    private void addRow() {
        TableRow newRow= new TableRow(this);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("transactions");
        Query query = databaseReference.orderByChild("roomNo").equalTo(trans.getRoom()).limitToLast(10);

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<DataSnapshot> dataSnapshotList = new ArrayList<>();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    dataSnapshotList.add(dataSnapshot);
                }

                // Reverse the list to get descending order
                Collections.reverse(dataSnapshotList);
                for (DataSnapshot dataSnapshot:dataSnapshotList) {
                    TableRow newRow= new TableRow(RoomHistory.this);

                    String userId= dataSnapshot.child("user").getValue().toString();
                    String username= dataSnapshot.child("username").getValue().toString();
                    String borrowedTime= dataSnapshot.child("borrowedTime").getValue().toString();
                    String returnedTime= dataSnapshot.child("returnedTime").getValue().toString();
                    String status= dataSnapshot.child("status").getValue().toString();

                    String[] array= new String[]{userId, username, borrowedTime, returnedTime, status};

                    for (String item:array) {
                        TextView data= new TextView(RoomHistory.this);
                        data.setText(item);
                        data.setTextColor(Color.WHITE);
                        data.setGravity(Gravity.CENTER);
                        data.setTextSize(18);
                        Typeface typeface = ResourcesCompat.getFont(RoomHistory.this, R.font.poppins_regular);
                        newRow.addView(data);
                    }
                    tableLayout.addView(newRow);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}