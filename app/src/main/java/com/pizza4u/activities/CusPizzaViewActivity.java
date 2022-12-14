package com.pizza4u.activities;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pizza4u.R;
import com.pizza4u.models.CartItemModel;
import com.pizza4u.models.OrderItemModel;
import com.pizza4u.models.OrderModel;
import com.pizza4u.models.UserModel;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CusPizzaViewActivity extends AppCompatActivity {

    private TextView txtName,txtPrice,txtDescription;
    private Spinner spinner_size;
    private ImageView image;
    private Button btn_addCart;
    private String name,price,description,size,userEmail,url;
    private FirebaseFirestore db;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cus_pizza_view);

        db = FirebaseFirestore.getInstance();

        txtName = findViewById(R.id.txtPizzaName);
        txtDescription=findViewById(R.id.textDescription_pizzaView);
        txtPrice=findViewById(R.id.txtPizzaPrice);
        spinner_size=findViewById(R.id.spinner_p_size);
        image=findViewById(R.id.imgPizza);
        btn_addCart=findViewById(R.id.btn_addtoCart);

        name=getIntent().getStringExtra("name");
        description=getIntent().getStringExtra("description");
        price=getIntent().getStringExtra("price");
        Log.d("price",price.toString());
        url=getIntent().getStringExtra("photo");
        Picasso.get().load(url).into(image);
        userEmail=getIntent().getStringExtra("userEmail");

        txtName.setText(name);
        txtDescription.setText(description);
        txtPrice.setText(price);

        spinner_size.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(spinner_size.getSelectedItem().toString().equals("Small")){
                    txtPrice.setText(price);
                    size="Small";
                    Log.d("size","SMALL");
                } else if(spinner_size.getSelectedItem().toString().equals("Medium")){
                    double pricecal = parseDouble(price)*1.5;
                    txtPrice.setText(Double.toString(pricecal));
                    size="Medium";
                } else {
                    double pricecal = parseDouble(price)*2;
                    txtPrice.setText(Double.toString(pricecal));
                    size="Large";
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                txtPrice.setText(price);
            }
        });


                btn_addCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CollectionReference dbCartItems = db.collection("cart-items");
                        DocumentReference documentReference = dbCartItems.document();

                        CartItemModel cartItemModel = new CartItemModel(userEmail,txtName.getText().toString(),size,parseFloat(txtPrice.getText().toString()),parseFloat(price),1,documentReference.getId(),url);

                        documentReference.set(cartItemModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(CusPizzaViewActivity.this, "Your item has been added to Firebase Firestore Cart", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CusPizzaViewActivity.this, "Fail to add Your item to Firebase Firestore cart", Toast.LENGTH_SHORT).show();
                            }
                        });
//                        dbCartItems.add(cartItemModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                            @Override
//                            public void onSuccess(DocumentReference documentReference) {
//                                }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                               }
//                        });
                    }
        });
    }

}