package com.example.hiram;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {

    private Spinner spinner;
    private EditText username, email, contact;
    private Button register;
    private String[] colleges= {"Select College","COE", "CIC", "CAEC", "CED"};
    private boolean isItemSelected= false;
    private String selectedCollege;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        spinner= findViewById(R.id.college_dropdown);
        username= findViewById(R.id.username);
        email=findViewById(R.id.email);
        contact=findViewById(R.id.contact);
        register=findViewById(R.id.register);


        ArrayAdapter<String> adapter= new ArrayAdapter<String>(Register.this, R.layout.college_options, colleges) {
            @Override
            public boolean isEnabled(int position) {
                // Disable the click for the hint item (position 0)
                return position != 0;
            }
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                if (position == 0) {
                    // Set the hint text color to gray
                    textView.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    // Set the normal item text color
                    textView.setTextColor(getResources().getColor(R.color.white));
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(R.layout.college_options);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String value= parent.getItemAtPosition(position).toString();
                Toast.makeText(Register.this, value, Toast.LENGTH_SHORT).show();
                if (position == 0) {
                    isItemSelected = false;
                } else {
                    isItemSelected = true;
                    selectedCollege = colleges[position];
                }

                boolean allFieldsFilled = username.getText().length() > 0
                        && email.getText().length() > 0 && contact.getText().length() > 0 && isItemSelected;

                // Enable or disable the button based on the condition
                register.setEnabled(allFieldsFilled);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String input = s.toString().trim();

                if (!isValidEmail(input)) {
                    email.setError("Please enter a valid email address.");
                } else {
                    email.setError(null);
                }
            }

            private boolean isValidEmail(String email) {
                return email.contains("@");
            }
        });
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Check if all EditText fields are not empty
                boolean allFieldsFilled = username.getText().length() > 0
                        && email.getText().length() > 0 && contact.getText().length() > 0;

                // Enable or disable the button based on the condition
                register.setEnabled(isItemSelected && allFieldsFilled);
            }
        };
        username.addTextChangedListener(textWatcher);
        email.addTextChangedListener(textWatcher);
        contact.addTextChangedListener(textWatcher);
        register.setOnClickListener(v -> {
            databaseReference= databaseReference= FirebaseDatabase.getInstance().getReference();
            //String qr=getIntent().getStringExtra("id");
            Transaction trans= new Transaction();
            databaseReference.child("users").child(trans.getQr()).child("username").setValue(username.getText().toString());
            databaseReference.child("users").child(trans.getQr()).child("email").setValue(email.getText().toString());
            databaseReference.child("users").child(trans.getQr()).child("contact").setValue(contact.getText().toString());
            databaseReference.child("users").child(trans.getQr()).child("college").setValue(selectedCollege);
            Intent intent = new Intent(Register.this, Borrow_return.class);
            startActivity(intent);

        });
    }
}