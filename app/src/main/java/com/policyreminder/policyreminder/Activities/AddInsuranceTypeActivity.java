package com.policyreminder.policyreminder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.policyreminder.policyreminder.Models.ProgressDialog;
import com.policyreminder.policyreminder.R;
import com.policyreminder.policyreminder.Session.Session;
import com.policyreminder.policyreminder.databinding.ActivityAddInsuranceTypeBinding;

import java.util.HashMap;
import java.util.Map;

public class AddInsuranceTypeActivity extends AppCompatActivity {

    private ActivityAddInsuranceTypeBinding binding;
    private Session session ;
    private  AddInsuranceTypeActivity activity ;
    private FirebaseDatabase database ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddInsuranceTypeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity  = this ;
        session = new Session(activity);
        database = FirebaseDatabase.getInstance();

        binding.addBtn.setOnClickListener(v -> {
            if(binding.policyCompanyName.getText().toString().equalsIgnoreCase("")){
                Snackbar.make(binding.coordinator, "Add Insurance Name..!", Snackbar.LENGTH_LONG).show();
            }else {
                addInsuranceType();
            }
        });

    }

    private void addInsuranceType(){
        ProgressDialog pd  = new ProgressDialog(activity);
        pd.show();
        String insuranceId = database.getReference().push().getKey();
        Map<String,String> map = new HashMap<>();
        map.put("insurance_name",binding.policyCompanyName.getText().toString());
        map.put("insurance_id",insuranceId);
        map.put("user_id",session.getUserId());

        database.getReference().child("insurance").child(session.getUserId())
                .child(insuranceId)
                .setValue(map)
                .addOnSuccessListener(unused -> {
                    pd.dismiss();
                    Toast.makeText(activity, "Insurance Added..!", Toast.LENGTH_SHORT).show();
                    binding.policyCompanyName.setText("");
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(activity, "Failed......", Toast.LENGTH_SHORT).show();
                    }
                });
    }


}