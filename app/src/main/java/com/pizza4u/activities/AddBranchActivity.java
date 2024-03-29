package com.pizza4u.activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.pizza4u.R;
import com.pizza4u.models.BranchModel;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddBranchActivity extends AppCompatActivity {

    EditText editTextBranchName;
    EditText editTextLocationName;
    EditText editTextCoordinates;
    EditText editTextBranchId;
    Button buttonOpenMap;
    Button buttonOk;
    Button buttonInstructions;
    String longitude;
    String latitude;
    String coordinates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_branch);

        editTextBranchName = findViewById(R.id.editTextBranchName);
        editTextLocationName = findViewById(R.id.editTextLocationName);
        editTextCoordinates = findViewById(R.id.editTextCoordinates);
        editTextBranchId = findViewById(R.id.editTextBranchId);
        buttonOpenMap = findViewById(R.id.buttonOpenMap);
        buttonOk = findViewById(R.id.buttonOk);
        buttonInstructions = findViewById(R.id.buttonInstructions);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        buttonInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddBranchActivity.this , AddBranchLocationInstructionsActivity.class);
                startActivity(intent);
            }
        });

        buttonOpenMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String uri = String.format(Locale.ENGLISH, "geo:0,0");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });


        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                coordinates = editTextCoordinates.getText().toString().trim();

                if(coordinates != null) {
                    String[] res = coordinates.split("[,]", 0);
                    latitude = res[0].trim();
                    longitude = res[1].trim();
                }


                CollectionReference dbBranch= db.collection("branch");
                DocumentReference documentReference = dbBranch.document();

                BranchModel branchModel = new BranchModel(
                        editTextBranchName.getText().toString().trim(),
                        editTextBranchId.getText().toString().trim(),
                        editTextLocationName.getText().toString().trim(),
                        longitude,
                        latitude,
                        documentReference.getId()
                );



                documentReference.set(branchModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                    public void onSuccess(Void unused) {
                        Log.d(TAG, "DocumentSnapshot written with ID: " + documentReference.getId());
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        Intent intent = new Intent(AddBranchActivity.this, BranchListActivity.class);
                                        startActivity(intent);
                                        finish();
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setMessage("Branch added successfully.").setPositiveButton("Ok", dialogClickListener)
                                .show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        break;
                                }
                            }
                        };

                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setTitle("Failed to add branch.").setMessage("Error: " + String.valueOf(e)).setPositiveButton("Ok", dialogClickListener)
                                .show();
                    }
                });
            }
        });
    }
}