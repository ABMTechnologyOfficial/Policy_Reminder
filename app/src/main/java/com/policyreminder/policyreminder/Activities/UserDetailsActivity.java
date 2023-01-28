package com.policyreminder.policyreminder.Activities;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import com.policyreminder.policyreminder.Models.ProgressDialog;
import com.policyreminder.policyreminder.Session.Session;
import com.policyreminder.policyreminder.databinding.ActivityUserDetailsBinding;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UserDetailsActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 200 ;
    private Uri filePath = null;
    FirebaseStorage storage;
    StorageReference storageReference;
    private ActivityUserDetailsBinding binding ;
    private  UserDetailsActivity activity ;
    private Session session ;
    private String imageUrl = "";
    private FirebaseDatabase database ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this ;
        session = new Session(activity);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        database = FirebaseDatabase.getInstance();

        binding.userImage.setOnClickListener(view -> SelectImage());

        binding.addBtn.setOnClickListener(view -> {
            if(binding.mobile.getText().toString().equalsIgnoreCase("")){
                Snackbar.make(binding.coordinator, "Your Mobile is Empty..!", Snackbar.LENGTH_LONG).show();
            }
            else if(binding.name.getText().toString().equalsIgnoreCase("")){
                Snackbar.make(binding.coordinator, "Your Name is Empty..!", Snackbar.LENGTH_LONG).show();
            }else {
                updateUserDetails();
            }

        });

        binding.skip.setOnClickListener(view -> {
            startActivity(new Intent(activity,HomeActivity.class));
            finish();
        });

    }

    private void updateUserDetails() {

        ProgressDialog pd = new ProgressDialog(activity);
        pd.show();

        Map<String, Object> map = new HashMap<>();
        map.put("profile_status" ,"1");
        map.put("name" ,binding.name.getText().toString());
        map.put("mobile" ,binding.mobile.getText().toString());
        map.put("profile_image" ,imageUrl);

        database.getReference().child("users")
                .child(session.getUserId())
                .updateChildren(map)
                .addOnSuccessListener(unused -> {
                    pd.dismiss();
                    Toast.makeText(activity, "Details Updated Success", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(activity,HomeActivity.class));
                    finish();
                }).addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(activity, "Failed..", Toast.LENGTH_SHORT).show();
                });

    }

    private void SelectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image from here..."), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                uploadImage(bitmap);
            }
            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(Bitmap bitmap)
    {
        if (filePath != null) {
            binding.profileProgress.setVisibility(View.VISIBLE);
            StorageReference ref = storageReference.child("images/" + session.getUserId());
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            taskSnapshot -> ref.getDownloadUrl().addOnSuccessListener(uri -> {
                                imageUrl = uri.toString();
                                binding.profileProgress.setVisibility(View.GONE);
                                binding.userImage.setImageBitmap(bitmap);
                            }))

                    .addOnFailureListener(e -> Toast.makeText(activity, "Failed..", Toast.LENGTH_SHORT).show());
        }
    }

}