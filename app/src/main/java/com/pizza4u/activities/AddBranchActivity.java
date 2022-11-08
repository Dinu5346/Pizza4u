package com.pizza4u.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.pizza4u.R;

public class AddBranchActivity extends AppCompatActivity {

    EditText editTextBranchName;
    EditText editTextLocationName;
    Button buttonOpenMap;
    Button buttonOk;
    Button buttonExit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_branch);

        editTextBranchName = findViewById(R.id.editTextBranchName);
        editTextLocationName = findViewById(R.id.editTextLocationName);
        buttonOpenMap = findViewById(R.id.buttonOpenMap);
        buttonOk = findViewById(R.id.buttonOk);
        buttonExit = findViewById(R.id.buttonExit);
    }
}