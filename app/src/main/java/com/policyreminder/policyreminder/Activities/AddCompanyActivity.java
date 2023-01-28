package com.policyreminder.policyreminder.Activities;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.FirebaseDatabase;
import com.policyreminder.policyreminder.Models.ProgressDialog;
import com.policyreminder.policyreminder.Session.Session;
import com.policyreminder.policyreminder.databinding.ActivityAddCompanyBinding;

import java.util.HashMap;
import java.util.Map;

public class AddCompanyActivity extends AppCompatActivity {

    private ActivityAddCompanyBinding binding;
    private AddCompanyActivity activity;
    private Session session;
    private FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddCompanyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        session = new Session(activity);

        database = FirebaseDatabase.getInstance();

        binding.backBtn.setOnClickListener(v -> onBackPressed());

        binding.companyAddBtn.setOnClickListener(v -> {
            if (validate())
                addCompany();
        });

    }

    private void addCompany() {

        ProgressDialog pd = new ProgressDialog(activity);
        pd.show();

        String comId = database.getReference().push().getKey();


        Map<String, Object> map = new HashMap<>();
        map.put("user_id", session.getUserId());
        map.put("company_name", binding.companyName.getText().toString().trim());
        map.put("company_id", comId);
        map.put("company_payment_link", binding.companyPaymentLink.getText().toString().trim());
        map.put("company_notes", binding.companyNotes.getText().toString().trim());

        database.getReference().child("company")
                .child(session.getUserId())
                .child(comId)
                .setValue(map)
                .addOnSuccessListener(unused -> {
                    pd.dismiss();
                    Toast.makeText(activity, "Company Added..!", Toast.LENGTH_SHORT).show();
                    binding.companyNotes.setText("");
                    binding.companyName.setText("");
                    binding.companyPaymentLink.setText("");
                }).addOnFailureListener(e -> {
                    pd.dismiss();
                    Toast.makeText(activity, "Failed..!", Toast.LENGTH_SHORT).show();
                });
    }

    private boolean validate() {
        if (binding.companyName.getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(binding.coordinator, "Add Company Name..!", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (binding.companyPaymentLink.getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(binding.coordinator, "Paste Payment Website Link..!", Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

}