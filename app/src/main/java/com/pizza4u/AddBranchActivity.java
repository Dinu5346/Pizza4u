package com.pizza4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;

public class AddBranchActivity extends AppCompatActivity {

    EditText editTextBranchName;
    EditText editTextLocationName;
    Button buttonOpenMap;
    Button buttonOk;
    Button buttonExit;
    Button buttonInstructions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_branch);

        editTextBranchName = findViewById(R.id.editTextBranchName);
        editTextLocationName = findViewById(R.id.editTextLocationName);
        buttonOpenMap = findViewById(R.id.buttonOpenMap);
        buttonOk = findViewById(R.id.buttonOk);
        buttonExit = findViewById(R.id.buttonExit);
        buttonInstructions = findViewById(R.id.buttonInstructions);

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

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}