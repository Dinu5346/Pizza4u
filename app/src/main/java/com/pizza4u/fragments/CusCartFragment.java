package com.pizza4u.fragments;

import static java.lang.Double.parseDouble;
import static java.lang.Float.parseFloat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.ResultReceiver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.pizza4u.R;
import com.pizza4u.adapters.CartRecycleAdapter;
import com.pizza4u.models.CartItemModel;
import com.pizza4u.models.Constants;
import com.pizza4u.models.FetchAddressIntentService;
import com.pizza4u.models.OrderItemModel;
import com.pizza4u.models.OrderModel;
import com.pizza4u.models.UserModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

public class CusCartFragment extends Fragment implements LocationListener {

    private View view;
    private RecyclerView recyclerView;
    private CartRecycleAdapter cartRecycleAdapter;
    private TextView txttot, txtlocation, txtAdrress;
    private ProgressBar progressBar;
    double tot = 0.00;
    int orderID = 0;
    private Button btnOrder;
    private ImageButton btnGetLocation;
    ArrayList<OrderModel> orderModelArrayList;
    ArrayList<CartItemModel> cartItemModelArrayList;
    Double longitude, latitude;
    Calendar c = Calendar.getInstance();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat dateformat = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss aa");
    private ResultReceiver resultReceiver;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    public CusCartFragment() {
        // Required empty public constructor
    }

    public CusCartFragment(ArrayList<CartItemModel> cartItemModelArrayList) {
        this.cartItemModelArrayList = cartItemModelArrayList;
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
    public void onLocationChanged(@NonNull Location location) {
        longitude = location.getLatitude();
        latitude = location.getLongitude();
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

        UserModel userModel = (UserModel) requireArguments().getSerializable("userModel");

        FirebaseFirestore db = FirebaseFirestore.getInstance();


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
        btnOrder = view.findViewById(R.id.btnPlaceOrder);
        btnGetLocation = view.findViewById(R.id.btnChangeLocation);
        txtlocation = view.findViewById(R.id.maps_orderLocation);
        txtAdrress=view.findViewById(R.id.orderLocation_address);
        progressBar=view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        resultReceiver = new AddressResultReceiver(new Handler());

        cartItemModelArrayList = new ArrayList<>();
        recyclerView.setAdapter(cartRecycleAdapter);

        db.collection("cart-items")
                .whereEqualTo("userEmail", userModel.getEmail())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (!task.getResult().isEmpty()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    // Log.d(TAG, document.getId() + " => " + document.getData());
                                    String documentid = document.getId();
                                    // Log.d("Email", email);

                                    tot += parseDouble(Objects.requireNonNull(document.get("subTotal")).toString());

                                    CartItemModel cartItemModel = document.toObject(CartItemModel.class);
                                    cartItemModelArrayList.add(cartItemModel);
                                    cartRecycleAdapter = new CartRecycleAdapter(getContext(), cartItemModelArrayList, txttot);
                                    cartRecycleAdapter.notifyDataSetChanged();
                                }
                                if (!cartItemModelArrayList.isEmpty()) {
                                    recyclerView.setAdapter(cartRecycleAdapter);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                                    txttot.setText(String.valueOf(tot));
                                } else {
                                    recyclerView.setVisibility(View.GONE);
                                    btnOrder.setEnabled(false);
                                    // btnOrder.setClickable(false);
                                    //btnOrder.setBackgroundColor();
                                }
                            }
                        }
                    }

                });


        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // txtlocation.setText(String.valueOf(longitude)+","+String.valueOf(latitude));
                txtlocation.setText("longitude,latitude");
                Log.d("location", String.valueOf(longitude));
            }
        });

        txttot.setText(Double.toString(tot));

        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CollectionReference dbCartItems = db.collection("cart-items");
                CollectionReference dbOrderItems = db.collection("order-items");
                CollectionReference dbOrders = db.collection("orders");
                DocumentReference docOrder = dbOrders.document();

                orderModelArrayList = new ArrayList<>();
                dbOrders
                        .whereEqualTo("userEmail", userModel.getEmail())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (!task.getResult().isEmpty()) {
                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                            // Log.d(TAG, document.getId() + " => " + document.getData());
                                            String documentid = document.getId();
                                            // Log.d("Email", email);

                                            OrderModel orderModel = document.toObject(OrderModel.class);
                                            orderModelArrayList.add(orderModel);
                                        }
                                        Log.d("orderid", String.valueOf(orderModelArrayList.size()));
                                        if (orderModelArrayList.isEmpty()) {
                                            orderID = 1;
                                        } else {
                                            orderID = (int) orderModelArrayList.size() + 1;
                                        }

                                        //add to orders
                                        OrderModel orderModel = new OrderModel(userModel.getEmail(), Integer.toString(orderID), "Queued", parseDouble(txttot.getText().toString()), dateformat.format(c.getTime()), "longitude", "latitude", docOrder.getId());
                                        docOrder.set(orderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Your order has been added to Firebase Firestore Orders", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Failed to add your order to Firebase Firestore Orders", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        //add to order items and delete from cart
                                        for (CartItemModel item : cartItemModelArrayList) {
                                            OrderItemModel orderItemModel = new OrderItemModel(userModel.getEmail(), orderModel.getOrderId(), item.getPizzaName(), item.getCount(), item.getSubTotal(), item.getSize());
                                            dbOrderItems.add(orderItemModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(getContext(), "Your order item has been added to Firebase Firestore Order Items", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Failed to add your order item to Firebase Firestore Order Items", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            dbCartItems.document(item.getDocId()).delete();
//                    cartItemModelArrayList.remove(item);
                                        }

                                    } else {

                                        Log.d("orderid", String.valueOf(orderModelArrayList.size()));
                                        if (orderModelArrayList.isEmpty()) {
                                            orderID = 1;
                                        } else {
                                            orderID = (int) orderModelArrayList.size() + 1;
                                        }

                                        //add to orders
                                        OrderModel orderModel = new OrderModel(userModel.getEmail(), Integer.toString(orderID), "Queued", parseDouble(txttot.getText().toString()), dateformat.format(c.getTime()), String.valueOf(longitude), String.valueOf(latitude), docOrder.getId());
                                        docOrder.set(orderModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(getContext(), "Your order has been added to Firebase Firestore Orders", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(getContext(), "Failed to add your order to Firebase Firestore Orders", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        //add to order items and delete from cart
                                        for (CartItemModel item : cartItemModelArrayList) {
                                            OrderItemModel orderItemModel = new OrderItemModel(userModel.getEmail(), orderModel.getOrderId(), item.getPizzaName(), item.getCount(), item.getSubTotal(), item.getSize());
                                            dbOrderItems.add(orderItemModel).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    Toast.makeText(getContext(), "Your order item has been added to Firebase Firestore Order Items", Toast.LENGTH_SHORT).show();
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), "Failed to add your order item to Firebase Firestore Order Items", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                            dbCartItems.document(item.getDocId()).delete();
//                    cartItemModelArrayList.remove(item);
                                        }

                                    }
                                }
                            }

                        });

            }
        });

        btnGetLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(
                        requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    {
                        ActivityCompat.requestPermissions(
                                getActivity(),
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                REQUEST_CODE_LOCATION_PERMISSION
                        );
                    }
                } else {
                    getCurrentLocation();
                }
            }
        });

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(getContext(), "Permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        progressBar.setVisibility(View.VISIBLE);
        LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(getContext())
                .requestLocationUpdates(locationRequest, new LocationCallback() {
                    @Override
                    public void onLocationResult(@NonNull LocationResult locationResult) {
                        super.onLocationResult(locationResult);
                        LocationServices.getFusedLocationProviderClient(getContext())
                                .removeLocationUpdates(this);
                        if(locationResult.getLocations().size()>0){
                            int latestLocationIndex = locationResult.getLocations().size()-1;
                            latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                            longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                            txtlocation.setText(
                                    String.format(
                                            "Latitude: %s \t Longitude: %s",
                                            latitude,longitude
                                    )
                            );

                            Location location = new Location("providerNA");
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            fetchAddressFromLatLong(location);


                        }else {
                            progressBar.setVisibility(View.GONE);
                        }
                        progressBar.setVisibility(View.GONE);
                    }
                }, Looper.getMainLooper());
    }

    private void fetchAddressFromLatLong(Location location){
        Intent intent = new Intent(getContext(), FetchAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER,resultReceiver);
        intent.putExtra(Constants.LOCATION_DATA_EXTRA,location);
        getActivity().startService(intent);
    }

    private class AddressResultReceiver extends ResultReceiver{

        public AddressResultReceiver(Handler handler){
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData){
            super.onReceiveResult(resultCode, resultData);
            if(resultCode == Constants.SUCCESS_RESULT){
                txtAdrress.setText(resultData.getString(Constants.RESULT_DATA_KEY));
            }else{
                Toast.makeText(getContext(),resultData.getString(Constants.RESULT_DATA_KEY),Toast.LENGTH_SHORT).show();
            }
            progressBar.setVisibility(View.GONE);
        }
    }

}