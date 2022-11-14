package com.pizza4u.fragments;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

import static java.lang.Integer.parseInt;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.pizza4u.MainActivity;
import com.pizza4u.R;
import com.pizza4u.models.UserModel;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;


public class CusProfileFragment extends Fragment {

    private View view;
    private EditText edt_fname,edt_lname,edt_phone,edt_email,edt_password;
    private Button btnSave;
    private ImageButton btn_changePP;
    private ImageView imgPP;
    Bitmap image;
    Uri selectedImage;
    String profilepicUri;
    public UserModel userModelGlobal;
    private static final String USERMODEL_KEY = "usermodel_key";

    public CusProfileFragment() {
        // Required empty public constructor
    }

    @NonNull
    public static CusProfileFragment newInstance (UserModel userModel) {
        CusProfileFragment fragment = new CusProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(USERMODEL_KEY, userModel);
        fragment.setArguments(bundle);
        return fragment;
    }

//    public static CusProfileFragment newInstance(String param1, String param2) {
//        CusProfileFragment fragment = new CusProfileFragment();
//        Bundle args = new Bundle();
//
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Log.d("User model from customer", userModel.getFname());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cus_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        edt_fname=view.findViewById(R.id.txtProfileFName);
        edt_lname=view.findViewById(R.id.txtProfileLName);
        edt_email=view.findViewById(R.id.txtCusMail);
        edt_phone=view.findViewById(R.id.txtCusPhone);
        imgPP=view.findViewById(R.id.imgProfilePic);
        edt_password=view.findViewById(R.id.txtPassword);
        btnSave=view.findViewById(R.id.btnSave);
        btn_changePP=view.findViewById(R.id.btnEditProPic);

        assert getArguments() != null;
        userModelGlobal = (UserModel) getArguments().getSerializable(USERMODEL_KEY);
        Log.d("User model from customer", userModelGlobal.getFname());

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        edt_fname.setText(userModelGlobal.getFname());
        edt_lname.setText(userModelGlobal.getLname());
        edt_email.setText(userModelGlobal.getEmail());
        edt_phone.setText(userModelGlobal.getPhone());

        Picasso.get().load(userModelGlobal.getProfilepic()).into(imgPP);


        btn_changePP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(pickPhoto , 1);
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                startActivityForResult(takePicture, 0);
                                break;

                            case DialogInterface.BUTTON_NEUTRAL:
                                dialog.cancel();
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Select image from gallery or capture from camera?").setPositiveButton("Gallery", dialogClickListener)
                        .setNegativeButton("Camera", dialogClickListener).setNeutralButton("Cancel",dialogClickListener).show();
            }
        });



        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(image != null || selectedImage != null) {
                    StorageReference storageRef = storage.getReference();
                    StorageReference profilepicRef = storageRef.child(edt_email.getText().toString().trim() + "/profilepic.jpg");

                    if(image != null) {
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] imageData = baos.toByteArray();

                        UploadTask uploadTask = profilepicRef.putBytes(imageData);
                        Task<Uri> urlTask = uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                long size = taskSnapshot.getMetadata().getSizeBytes();
                                Log.d(TAG, "Image uploaded to Firebase Storage: " + size);
                            }
                        }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return profilepicRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    Log.d("Profile picture download uri: ", downloadUri.toString());

                                    updateCustomer(db,view,downloadUri.toString());

                                } else {
                                    Log.d(TAG, "Failed to get image download URL");
                                }
                            }
                        });
                    } else if(selectedImage != null) {
                        UploadTask uploadTask = profilepicRef.putFile(selectedImage);
                        Task<Uri> urlTask = uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                e.printStackTrace();
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                long size = taskSnapshot.getMetadata().getSizeBytes();
                                Log.d(TAG, "Image uploaded to Firebase Storage: " + size);
                            }
                        }).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                return profilepicRef.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    Uri downloadUri = task.getResult();
                                    Log.d("Profile picture download uri: ", downloadUri.toString());

                                    updateCustomer(db,view,downloadUri.toString());

                                } else {
                                    Log.d(TAG, "Failed to get image download URL");
                                }
                            }
                        });
                    }
                }
                //If image is not changed
                else {
                    updateCustomer(db,view,userModelGlobal.getProfilepic());
                }


//                Map<String, Object> data = new HashMap<>();
//                data.put("fname", edt_fname.getText().toString().trim());
//                data.put("lname", edt_lname.getText().toString().trim());
//                data.put("email", edt_email.getText().toString().trim());
//                data.put("phone", edt_phone.getText().toString().trim());
//                data.put("profilepic", profilepicUri);
//
//                db.collection("users").document(userModel.getEmail()).set(data)
//                        .addOnSuccessListener(new OnSuccessListener<Void>() {
//                            @Override
//                            public void onSuccess(Void unused) {
//                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                switch (which){
//                                    case DialogInterface.BUTTON_POSITIVE:
//                                        //Yes button clicked
//                                        Intent intent = new Intent(getContext(), MainActivity.class);
//                                        startActivity(intent);
//                                        break;
//                                }
//                            }
//                        };
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                        builder.setMessage("Account updated successfully.").setPositiveButton("Ok", dialogClickListener)
//                                .show();
//                            }
//                        }).addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception e) {
//                                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                switch (which){
//                                    case DialogInterface.BUTTON_POSITIVE:
//                                        break;
//                                }
//                            }
//                        };
//
//                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
//                        builder.setTitle("Failed to update account.").setMessage("Error: " + String.valueOf(e)).setPositiveButton("Ok", dialogClickListener)
//                                .show();
//                            }
//                        });
//

            }

        });

    }

    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
//                    selectedImage = imageReturnedIntent.getData();
//                    profilepicManager.setImageURI(selectedImage);

                    image = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    imgPP.setImageBitmap(image);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    selectedImage = imageReturnedIntent.getData();
                    imgPP.setImageURI(selectedImage);

//                    image = (Bitmap) imageReturnedIntent.getExtras().get("data");
//                    profilepicManager.setImageBitmap(image);
                }
                break;
        }
     }


    public void updateCustomer(FirebaseFirestore db,View view,String downloadUri){
        CollectionReference dbUsers = db.collection("users");
        DocumentReference documentReference = dbUsers.document(userModelGlobal.getDocID());

        userModelGlobal.setEmail(edt_email.getText().toString().trim());
        userModelGlobal.setFname(edt_fname.getText().toString().trim());
        userModelGlobal.setLname(edt_lname.getText().toString().trim());
        userModelGlobal.setPhone(parseInt(edt_phone.getText().toString()));
        userModelGlobal.setPassword(edt_password.getText().toString().trim());
        userModelGlobal.setProfilepic(downloadUri);

        documentReference.set(userModelGlobal).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Intent intent = new Intent(getContext(),MainActivity.class);
                                startActivity(intent);
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage("Account Updated successfully.").setPositiveButton("Ok", dialogClickListener)
                        .show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
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
                builder.setTitle("Failed to update account.").setMessage("Error: " + String.valueOf(e)).setPositiveButton("Ok", dialogClickListener)
                        .show();
            }
        });

    }
}