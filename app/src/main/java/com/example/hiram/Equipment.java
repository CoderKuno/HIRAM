package com.example.hiram;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Equipment extends AppCompatActivity {

    private Button back_equipment, confirm_equipment, cancel_equipment, hist_btn;
    private ToggleButton tv_remote_btn, ac_remote_btn, key_btn;
    private int counter= 0;
    private Transaction trans= new Transaction();
    private TextView note;
    private boolean checker;

    private ArrayList<String> array = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        back_equipment=findViewById(R.id.back_equipment);
        confirm_equipment= findViewById(R.id.confirm_equipment);
        cancel_equipment= findViewById(R.id.cancel_equipment);
        tv_remote_btn= findViewById(R.id.tv_remote_btn);
        ac_remote_btn= findViewById(R.id.ac_remote_btn);
        key_btn= findViewById(R.id.key_btn);
        note=findViewById(R.id.note);
        hist_btn= findViewById(R.id.history_room);
        check();

        hist_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Equipment.this, RoomHistory.class);
                startActivity(intent);
            }
        });
        back_equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm_equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checker) {
                    setItems();
                    setTransaction();
                    showBorrowSuccessful();
                    array.clear();
                }
            }
        });

        cancel_equipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelTransaction();
            }
        });

    }
    private void check() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("rooms");
        Query query= databaseReference.child("room"+trans.getRoom());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String acRemote = snapshot.child("acRemote").getValue(String.class);
                String tvRemote = snapshot.child("tvRemote").getValue(String.class);
                String roomKey = snapshot.child("roomKey").getValue(String.class);

                if ("returned".equals(acRemote) && "returned".equals(tvRemote) && "returned".equals(roomKey)) {
                    checker = true;
                } else {
                    checker = false;
                    confirm_equipment.setEnabled(false);
                    note.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setTransaction() {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("transactions");
        DatabaseReference userReference = FirebaseDatabase.getInstance().getReference("users");
        Query query= userReference.child(trans.getQr());
        DatabaseReference newTransactionRef = databaseReference.push();
        String transactionId = newTransactionRef.getKey();
        // Get the current date and time
        Date currentDate = new Date();
        // Define the desired date and time format
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Format the current date and time as per the desired format
        String formattedDateTime = dateFormat.format(currentDate);
        boolean acRemote= false;
        boolean tvRemote= false;
        boolean roomKey= false;
        for (String item: array) {
            if (item.equals("acRemote")) acRemote= true;
            else if (item.equals("tvRemote")) tvRemote= true;
            else roomKey=true;
        }

        boolean finalAcRemote = acRemote;
        boolean finalTvRemote = tvRemote;
        boolean finalRoomKey = roomKey;
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username= snapshot.child("username").getValue().toString();
                newTransactionRef.child("acRemote").setValue(finalAcRemote);
                newTransactionRef.child("tvRemote").setValue(finalTvRemote);
                newTransactionRef.child("roomKey").setValue(finalRoomKey);
                newTransactionRef.child("username").setValue(username);
                newTransactionRef.child("user").setValue(trans.getQr());
                newTransactionRef.child("roomNo").setValue(trans.getRoom());
                newTransactionRef.child("borrowedTime").setValue(formattedDateTime);
                newTransactionRef.child("returnedTime").setValue("");
                newTransactionRef.child("status").setValue("not returned");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void setItems() {
        DatabaseReference databaseReference = databaseReference = FirebaseDatabase.getInstance().getReference().child("rooms");

        for (String arr: array) {
            databaseReference.child("room"+trans.getRoom()).child(arr).setValue("borrowed");
        }
    }

    private void showCancelTransaction() {
        RelativeLayout layout= findViewById(R.id.cancel_transaction_layout);
        View view= LayoutInflater.from(Equipment.this).inflate(R.layout.cancel_transanction, layout);


        AlertDialog.Builder builder= new AlertDialog.Builder(Equipment.this);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog alertDialog= builder.create();


        Button cancel_yes= view.findViewById(R.id.cancel_yes);
        Button cancel_no= view.findViewById(R.id.cancel_no);

        cancel_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trans.clearTransaction();
                Intent intent = new Intent(Equipment.this, Scan.class);
                startActivity(intent);
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        cancel_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showBorrowAgain() {
        RelativeLayout layout= findViewById(R.id.borrow_again_layout);
        View view= LayoutInflater.from(Equipment.this).inflate(R.layout.borrow_again, layout);


        AlertDialog.Builder builder= new AlertDialog.Builder(Equipment.this);
        builder.setView(view);
        builder.setCancelable(false);
        final AlertDialog alertDialog= builder.create();


        Button borrow_yes= view.findViewById(R.id.borrow_yes);
        Button borrow_no= view.findViewById(R.id.borrow_no);
        borrow_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trans.clearData();
                Intent intent = new Intent(Equipment.this, Second_flr.class);
                startActivity(intent);
            }
        });
        if (alertDialog.getWindow() != null) {
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }

        borrow_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                trans.clearTransaction();
                Intent intent = new Intent(Equipment.this, Scan.class);
                startActivity(intent);
            }
        });
        alertDialog.show();
    }
    private void showBorrowSuccessful() {
        RelativeLayout layout= findViewById(R.id.borrow_successful_layout);
        View view= LayoutInflater.from(Equipment.this).inflate(R.layout.borrow_successful, layout);

        AlertDialog.Builder builder= new AlertDialog.Builder(Equipment.this);
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
    public void onToggleButtonClicked(View view) {
        ToggleButton clickedButton = (ToggleButton) view;
        if (clickedButton.isChecked()) {
            if (clickedButton.getText().toString().equals("TV Remote")) {
                array.add("tvRemote");
            } else if (clickedButton.getText().toString().equals("AC Remote")) {
                array.add("acRemote");
            } else {
                array.add("roomKey");
            }
            counter++;
        } else {
            if (clickedButton.getText().toString().equals("TV Remote")) {
                array.remove("tvRemote");
            } else if (clickedButton.getText().toString().equals("AC Remote")) {
                array.remove("acRemote");
            } else {
                array.remove("roomKey");
            }
            counter-=1;
        }

        if (counter > 0 && checker) {
            confirm_equipment.setEnabled(true);
        } else {
            confirm_equipment.setEnabled(false);
        }
    }
}