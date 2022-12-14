package com.pizza4u.fragments;

import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.pizza4u.R;
import com.pizza4u.adapters.CusOrdersRecycleAdapter;
import com.pizza4u.models.OrderModel;
import com.pizza4u.models.UserModel;

import java.util.ArrayList;


public class CusOrdersFragment extends Fragment {

    private RecyclerView recyclerView;
    ArrayList<OrderModel> orderModelArrayList;
    CusOrdersRecycleAdapter ordersRecycleAdapter;

    public CusOrdersFragment() {
        // Required empty public constructor
    }

    public static CusOrdersFragment newInstance(String param1, String param2) {
        CusOrdersFragment fragment = new CusOrdersFragment();
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
        return inflater.inflate(R.layout.fragment_cus_orders, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        UserModel userModel = (UserModel) requireArguments().getSerializable("userModel");

        FirebaseFirestore db =FirebaseFirestore.getInstance();

        recyclerView = view.findViewById(R.id.recycler_orders);

        orderModelArrayList=new ArrayList<>();

        db.collection("orders")
                .whereEqualTo("userEmail",userModel.getEmail())
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

                                    OrderModel orderModel = document.toObject(OrderModel.class);
                                    orderModelArrayList.add(orderModel);
                                    ordersRecycleAdapter = new CusOrdersRecycleAdapter(getContext(), orderModelArrayList,userModel.getEmail());
                                    ordersRecycleAdapter.notifyDataSetChanged();

                                }
                                if(!orderModelArrayList.isEmpty()){
                                    recyclerView.setAdapter(ordersRecycleAdapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                }else{
                                    recyclerView.setVisibility(View.GONE);
                                }
                             }

                        }
                    }

                });

    }

}
