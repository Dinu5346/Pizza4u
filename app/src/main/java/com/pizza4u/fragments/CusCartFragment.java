package com.pizza4u.fragments;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pizza4u.R;
import com.pizza4u.adapters.CartRecycleAdapter;
import com.pizza4u.models.CartItemModel;
import com.pizza4u.models.UserModel;

import java.util.ArrayList;
import java.util.Objects;

public class CusCartFragment extends Fragment {

    private View view;
    private RecyclerView recyclerView;
    private CartRecycleAdapter cartRecycleAdapter;
    private TextView txttot;
    double tot=0.00;
    private Button btnOrder;
    UserModel userModel;
    ArrayList<CartItemModel> cartItemModelArrayList;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CusCartFragment() {
        // Required empty public constructor
    }

    public CusCartFragment(ArrayList<CartItemModel> cartItemModelArrayList) {
        this.cartItemModelArrayList=cartItemModelArrayList;
    }

    public static CusCartFragment newInstance(String param1, String param2) {
        CusCartFragment fragment = new CusCartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_cus_cart, container, false);
        return view;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        db.collection("cart")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            if(!task.getResult().isEmpty()) {
//                                for (QueryDocumentSnapshot document : task.getResult()) {
//                                    // Log.d(TAG, document.getId() + " => " + document.getData());
//                                    // String email = document.get("email").toString();
//                                    // Log.d("Email", email);
//
//                                    cartItemModelArrayList.add(document.toObject(CartItemModel.class));
//                                }
//
//                            }}
//                    }
//
//                });


        recyclerView = view.findViewById(R.id.recycler_cart);
        txttot = view.findViewById(R.id.textTotal);
        btnOrder=view.findViewById(R.id.btnPlaceOrder);

        cartItemModelArrayList=new ArrayList<>();
        cartRecycleAdapter=new CartRecycleAdapter(this.getContext(),cartItemModelArrayList);
        recyclerView.setAdapter(cartRecycleAdapter);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("cart-items")
                .whereEqualTo("userID",userModel.getUserID() )
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if(!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Log.d(TAG, document.getId() + " => " + document.getData());
                                    String documentid = document.getId();
                                    // Log.d("Email", email);

                                    tot+=parseDouble(Objects.requireNonNull(document.get("subTotal")).toString());

                                    CartItemModel cartItemModel =document.toObject(CartItemModel.class);
                                    cartItemModelArrayList.add(cartItemModel);
                                    cartRecycleAdapter.notifyDataSetChanged();

                                }
                            }}
                    }

                });

        txttot.setText(Double.toString(tot));

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

    }
}