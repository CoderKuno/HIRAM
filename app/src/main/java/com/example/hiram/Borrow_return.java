package com.example.hiram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Borrow_return extends AppCompatActivity {

    private Button borrow, return_;
    private Transaction trans= new Transaction();
    private ArrayList<String> transactionKeys= new ArrayList<>();
    private ArrayList<String> rooms= new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_return);
        check();

        borrow= findViewById(R.id.borrow);
        return_= findViewById(R.id.return_);

        borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Borrow_return.this, Second_flr.class);
                startActivity(intent);
            }
        });

        return_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showReturnSuccessful();
                returnItems();
                transactionKeys.clear();
                rooms.clear();
            }
        });
    }

    private void returnItems() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("transactions");
        DatabaseReference roomsReference = FirebaseDatabase.getInstance().getReference("rooms");
        for (String transaction: transactionKeys) {
            Date currentDate = new Date();
            // Define the desired date and time format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            // Format the current date and time as per the desired format
            String formattedDateTime = dateFormat.format(currentDate);
            databaseReference.child(transaction).child("status").setValue("returned");
            databaseReference.child(transaction).child("returnedTime").setValue(formattedDateTime);
        }
        for (String roomNo: rooms) {
            roomsReference.child("room"+roomNo).child("acRemote").setValue("returned");
            roomsReference.child("room"+roomNo).child("tvRemote").setValue("returned");
            roomsReference.child("room"+roomNo).child("roomKey").setValue("returned");
        }
    }
    private  void check() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("transactions");
        Query query = databaseReference.orderByChild("status").equalTo("not returned");

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int matchingCount = (int) dataSnapshot.getChildrenCount();
                Log.d("Check", "Number of matching transactions: " + matchingCount);
                for (DataSnapshot transactionSnapshot : dataSnapshot.getChildren()) {
                    if (transactionSnapshot.child("user").getValue(String.class).equals(trans.getQr())) {
                        transactionKeys.add(transactionSnapshot.getKey().toString());
                        rooms.add(transactionSnapshot.child("roomNo").getValue(String.class));
                    }

                }

                if (transactionKeys.size()!= 0) {
                    return_.setEnabled(true);
                } else {
                    return_.setEnabled(false);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DataSnapshot", "DatabaseError: " + error.getMessage());
            }
        });

    }
    private void showBorrowAgain() {
        RelativeLayout layout= findViewById(R.id.borrow_again_layout);
        View view= LayoutInflater.from(Borrow_return.this).inflate(R.layout.borrow_again, layout);


        AlertDialog.Builder builder= new AlertDialog.Builder(Borrow_return.this);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog alertDialog= builder.create();


        Button borrow_yes= view.findViewById(R.id.borrow_yes);
        Button borrow_no= view.findViewById(R.id.borrow_no);
        borrow_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Borrow_return.this, Second_flr.class);
                startActivity(intent);
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        borrow_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Borrow_return.this, Scan.class);
                startActivity(intent);
            }
        });
        alertDialog.show();
    }

    private void showReturnSuccessful() {
        RelativeLayout layout= findViewById(R.id.return_successful_layout);
        View view= LayoutInflater.from(Borrow_return.this).inflate(R.layout.return_successful, layout);

        AlertDialog.Builder builder= new AlertDialog.Builder(Borrow_return.this);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog alertDialog= builder.create();

        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (alertDialog != null && alertDialog.isShowing()) {
                    alertDialog.dismiss();
                    showBorrowAgain();
                }
            }
        }, 1500); // 1 seconds delay
    }
}