package com.example.hiram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class Fourth_flr extends AppCompatActivity {
    private Button back4, confirm_btn4;
    private ImageView next_4th, prev_4th;

    private ToggleButton selectedToggleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fourth_flr);

        back4= findViewById(R.id.back4);
        next_4th= findViewById(R.id.next_4th);
        prev_4th= findViewById(R.id.prev_4th);
        confirm_btn4= findViewById(R.id.confirm_btn4);

        back4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        next_4th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Fourth_flr.this, Fifth_flr.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        prev_4th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        confirm_btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transaction trans= new Transaction();
                trans.setRoom(selectedToggleButton.getText().toString());
                Intent intent = new Intent(Fourth_flr.this, Equipment.class);
                startActivity(intent);
            }
        });
    }
    public void onToggleButtonClicked(View view) {
        ToggleButton clickedButton = (ToggleButton) view;

        if (clickedButton.isChecked()) {
            confirm_btn4.setEnabled(true);
            // A button was selected
            if (selectedToggleButton != null && selectedToggleButton != clickedButton) {
                // Another button was previously selected, so uncheck it
                selectedToggleButton.setChecked(false);
            }
            // Update the selected button
            selectedToggleButton = clickedButton;
        } else {
            // The selected button was deselected
            confirm_btn4.setEnabled(false);
            selectedToggleButton = null;
        }
    }
}