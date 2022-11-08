package com.pizza4u;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pizza4u.models.CartItemModel;
import com.pizza4u.models.OrderItemModel;
import com.pizza4u.models.OrderModel;
import com.pizza4u.models.PizzaModel;
import com.pizza4u.models.PizzaTypeModel;
import com.pizza4u.models.UserModel;

import java.util.ArrayList;

public class CustomerMainActivity extends AppCompatActivity {

    BottomNavigationView nav;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    UserModel userModel;
    PizzaTypeModel pizzaTypeModel;
    PizzaModel pizzaModel;
    OrderModel orderModel;
    OrderItemModel orderItemModel;
    ArrayList<CartItemModel> cartItemModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main);
        replaceFragment(new CusHomeFragment());

        cartItemModelArrayList = new ArrayList<>();

        Intent intent = getIntent();
        userModel=(UserModel) intent.getSerializableExtra("userData");

        db.collection("pizza-types")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Log.d(TAG, document.getId() + " => " + document.getData());
                                    // String email = document.get("email").toString();
                                    // Log.d("Email", email);

                                    pizzaTypeModel=document.toObject(PizzaTypeModel.class);

                                    }
                                }}
                        }

                });

        db.collection("pizzas")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Log.d(TAG, document.getId() + " => " + document.getData());
                                    // String email = document.get("email").toString();
                                    // Log.d("Email", email);

                                   pizzaModel=document.toObject(PizzaModel.class);

                                }
                            }}
                    }

                });


        db.collection("orders")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Log.d(TAG, document.getId() + " => " + document.getData());
                                    // String email = document.get("email").toString();
                                    // Log.d("Email", email);

                                    orderModel=document.toObject(OrderModel.class);

                                }
                            }}
                    }

                });

        db.collection("order-items")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Log.d(TAG, document.getId() + " => " + document.getData());
                                    // String email = document.get("email").toString();
                                    // Log.d("Email", email);

                                    orderItemModel=document.toObject(OrderItemModel.class);

                                }
                            }}
                    }

                });

        UserModel userModel = (UserModel) getIntent().getSerializableExtra("userData");
        Log.d("UserData from Customer Home", userModel.getEmail() + " " + userModel.getFname());

        nav = findViewById(R.id.nav_bar);
        nav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        replaceFragment(new CusHomeFragment());
                        break;
                    case R.id.cart:
                        replaceFragment(new CusCartFragment());
                        break;
                    case R.id.orders:
                        replaceFragment(new CusOrdersFragment());
                        break;
                    case R.id.profile:
                        replaceFragment(new CusProfileFragment(userModel));
                        break;

                }
                return true;
            }
        });
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_frame_container,fragment);
        fragmentTransaction.commit();
    }
}