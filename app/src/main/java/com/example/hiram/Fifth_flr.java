package com.example.hiram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class Fifth_flr extends AppCompatActivity {
    private Button back5, confirm_btn5;
    private ImageView prev_5th;
    private ToggleButton selectedToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fifth_flr);

        back5= findViewById(R.id.back5);
        prev_5th= findViewById(R.id.prev_5th);
        confirm_btn5= findViewById(R.id.confirm_btn5);

        back5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        prev_5th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                overridePendingTransition(0, 0);
            }
        });
        confirm_btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transaction trans= new Transaction();
                trans.setRoom(selectedToggleButton.getText().toString());
                Intent intent = new Intent(Fifth_flr.this, Equipment.class);
                startActivity(intent);
            }
        });
    }
    public void onToggleButtonClicked(View view) {
        ToggleButton clickedButton = (ToggleButton) view;

        if (clickedButton.isChecked()) {
            confirm_btn5.setEnabled(true);
            // A button was selected
            if (selectedToggleButton != null && selectedToggleButton != clickedButton) {
                // Another button was previously selected, so uncheck it
                selectedToggleButton.setChecked(false);
            }
            // Update the selected button
            selectedToggleButton = clickedButton;
        } else {
            // The selected button was deselected
            confirm_btn5.setEnabled(false);
            selectedToggleButton = null;
        }
    }
}