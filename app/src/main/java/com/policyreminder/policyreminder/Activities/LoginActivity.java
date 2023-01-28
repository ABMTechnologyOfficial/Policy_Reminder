package com.policyreminder.policyreminder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.policyreminder.policyreminder.Models.ProgressDialog;
import com.policyreminder.policyreminder.R;
import com.policyreminder.policyreminder.Session.Session;
import com.policyreminder.policyreminder.databinding.ActivityLoginBinding;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding ;
    LoginActivity activity ;
    FirebaseAuth auth ;
    Session session;
    FirebaseDatabase firebaseDatabase ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;

        session = new Session(activity);

        auth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();

        binding.loginBtn.setOnClickListener(view -> {
            if(binding.edtEmail.getText().toString().equalsIgnoreCase("")){
                binding.edtEmail.setError("Enter email");
                binding.edtEmail.requestFocus();
            }else if(binding.edtPassword.getText().toString().equalsIgnoreCase("")){
                binding.edtPassword.setError("Enter Password");
                binding.edtPassword.requestFocus();
            }else{
                loginUser(binding.edtEmail.getText().toString(), binding.edtPassword.getText().toString());
            }
        });
    }

    private  void loginUser(String email , String password ){

        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.show();

        auth.signInWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(activity, "Login Success", Toast.LENGTH_SHORT).show();
                session.setUserId(authResult.getUser().getUid());
                startActivity(new Intent(activity, HomeActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                progressDialog.dismiss();
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
              if(e.getLocalizedMessage().contains("no user record")){
                    auth.createUserWithEmailAndPassword(email,password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            session.setUserId(authResult.getUser().getUid());

                            Map<String, Object> map = new HashMap<>();
                            map.put("user_id",authResult.getUser().getUid());
                            map.put("user_name","");
                            map.put("email",email);
                            map.put("password",password);

                            firebaseDatabase.getReference().child("users").child(authResult.getUser().getUid()).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    startActivity(new Intent(activity, HomeActivity.class)
                                            .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                            .setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                                    );
                                    Toast.makeText(activity, "Sign Up Success", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    finish();
                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                        }
                    });
              }else {
                  progressDialog.dismiss();
                  Toast.makeText(activity, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
              }
            }
        });


    }
}