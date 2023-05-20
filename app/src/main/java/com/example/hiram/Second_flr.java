package com.example.hiram;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ToggleButton;

public class Second_flr extends AppCompatActivity {
    private Button back2, confirm_btn;
    private ImageView next_2nd;
    private ToggleButton selectedToggleButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_flr);

        back2= findViewById(R.id.back2);
        confirm_btn= findViewById(R.id.confirm_btn2);
        next_2nd= findViewById(R.id.next_2nd);

        back2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
        confirm_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Transaction trans= new Transaction();
                trans.setRoom(selectedToggleButton.getText().toString());
                Intent intent = new Intent(Second_flr.this, Equipment.class);
                startActivity(intent);
            }
        });
        next_2nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Second_flr.this, Third_flr.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }
    public void onToggleButtonClicked(View view) {
        ToggleButton clickedButton = (ToggleButton) view;

        if (clickedButton.isChecked()) {
            confirm_btn.setEnabled(true);
            // A button was selected
            if (selectedToggleButton != null && selectedToggleButton != clickedButton) {
                // Another button was previously selected, so uncheck it
                selectedToggleButton.setChecked(false);
            }
            // Update the selected button
            selectedToggleButton = clickedButton;
        } else {
            // The selected button was deselected
            confirm_btn.setEnabled(false);
            selectedToggleButton = null;
        }
    }
}