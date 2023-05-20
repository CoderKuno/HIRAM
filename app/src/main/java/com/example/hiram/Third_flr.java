package com.example.hiram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class Third_flr extends AppCompatActivity {
    private Button back3, confirm_btn3;
    private ImageView next_3rd, prev_3rd;
    private ToggleButton selectedToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third_flr);

        back3= findViewById(R.id.back3);
        next_3rd= findViewById(R.id.next_3rd);
        prev_3rd= findViewById(R.id.prev_3rd);
        confirm_btn3= findViewById(R.id.confirm_btn3);

        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        prev_3rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        next_3rd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Third_flr.this, Fourth_flr.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        confirm_btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transaction trans= new Transaction();
                trans.setRoom(selectedToggleButton.getText().toString());
                Intent intent = new Intent(Third_flr.this, Equipment.class);
                startActivity(intent);
            }
        });
    }
    public void onToggleButtonClicked(View view) {
        ToggleButton clickedButton = (ToggleButton) view;

        if (clickedButton.isChecked()) {
            confirm_btn3.setEnabled(true);
            // A button was selected
            if (selectedToggleButton != null && selectedToggleButton != clickedButton) {
                // Another button was previously selected, so uncheck it
                selectedToggleButton.setChecked(false);
            }
            // Update the selected button
            selectedToggleButton = clickedButton;
        } else {
            // The selected button was deselected
            confirm_btn3.setEnabled(false);
            selectedToggleButton = null;
        }
    }
}