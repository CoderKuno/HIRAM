package com.example.hiram;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

public class Scan extends AppCompatActivity {

    private Button scan;
    TextView textView;
    private String qr;
    private DatabaseReference databaseReference;
    private View loadingScreen;
    private ConstraintLayout rootLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_scan);
        rootLayout = findViewById(R.id.rootLayout);

        scan= findViewById(R.id.scan);

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                scanCode();
            }
        });

    }
    private void scanCode() {
        ScanOptions options= new ScanOptions();
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLauncher= registerForActivityResult(new ScanContract(), result->
    {
        if (result != null && result.getContents() != null) {
            qr= result.getContents();
            new FetchDataAsyncTask().execute();
            Transaction trans= new Transaction();
            trans.setQr(qr);
        }

    });
    public void showAlertDialog(String qrCodeContent) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New QR code scanned.");
        builder.setMessage("Click okay to register.");

        builder.setPositiveButton("Okay", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Handle "Okay" button click event
                dialog.dismiss(); // Dismiss the dialog
                Intent intent= new Intent(Scan.this, Register.class);
                //intent.putExtra("id", qr);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private class FetchDataAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // Display the loading screen
            showLoadingScreen();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Perform the data fetching operation here
            // This method runs on a background thread

            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
            // Fetch data from the Firebase Realtime Database
            Query query = databaseReference.child(qr);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        // QR code (user ID) exists in the database
                        // Proceed with the transaction
                        Intent intent = new Intent(Scan.this, Borrow_return.class);
                        startActivity(intent);
                    } else {
                        // QR code (user ID) does not exist in the database
                        // User needs to register first
                        showAlertDialog(qr);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    // This method will be called if there is an error retrieving the data
                    Log.e("Firebase", "Error fetching data: " + databaseError.getMessage());
                }
            });

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            // Data fetching is complete
            // Hide the loading screen
            hideLoadingScreen();
        }
    }
    private void showLoadingScreen() {
        // Inflate the loading screen layout
        LayoutInflater inflater = LayoutInflater.from(this);
        loadingScreen = inflater.inflate(R.layout.loading_screen, null);

        // Add the loading screen to the root layout of your activity
        Log.d("LoadingScreen", "Adding loading screen to root layout");
        rootLayout.addView(loadingScreen);
        Log.d("LoadingScreen", "Loading screen added to root layout");
    }

    // Method to hide the loading screen
    private void hideLoadingScreen() {
        // Remove the loading screen from the root layout of your activity
        rootLayout.removeViewAt(rootLayout.getChildCount() - 1);
    }
}