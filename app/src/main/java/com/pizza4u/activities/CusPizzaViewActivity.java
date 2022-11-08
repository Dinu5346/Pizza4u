package com.pizza4u.activities;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.pizza4u.R;

public class CusPizzaViewActivity extends AppCompatActivity {

    private TextView txtName,txtPrice,txtDescription;
    private Spinner spinner_size;
    private Button btn_addCart;
    private String name,price,description;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_pizza_view);

        txtName = findViewById(R.id.txtPizzaName);
        txtDescription=findViewById(R.id.textDescription_pizzaView);
        txtPrice=findViewById(R.id.txtPizzaPrice);
        spinner_size=findViewById(R.id.spinner_p_size);
        btn_addCart=findViewById(R.id.btn_addtoCart);

        name=getIntent().getStringExtra("name");
        price=getIntent().getStringExtra("price");
        description=getIntent().getStringExtra("description");
        //photo

        txtName.setText(name);
        txtDescription.setText(description);

        if(spinner_size.isSelected()){
            if(spinner_size.getSelectedItem().toString().equals("Small")){
                txtPrice.setText(price);
            } else if(spinner_size.getSelectedItem().toString().equals("Medium")){
                double pricecal = parseDouble(price)*1.5;
                txtPrice.setText(Double.toString(pricecal));
            } else {
                double pricecal = parseDouble(price)*2;
                txtPrice.setText(Double.toString(pricecal));
            }
        }else {
            txtPrice.setText(price);
        }

        btn_addCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                newDB = new DatabaseHelper(CusPizzaViewActivity.this);
//                newDB.updateNote(Integer.valueOf(id),
//                        edttxtTitle.getText().toString().trim(),
//                        edttxtCategory.getText().toString().trim(),
//                        edttxtNote.getText().toString().trim());
            }
        });
    }

}