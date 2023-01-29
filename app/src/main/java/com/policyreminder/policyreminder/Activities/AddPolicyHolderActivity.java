package com.policyreminder.policyreminder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.policyreminder.policyreminder.Models.PolicyModel;
import com.policyreminder.policyreminder.Models.ProgressDialog;
import com.policyreminder.policyreminder.R;
import com.policyreminder.policyreminder.Session.Session;
import com.policyreminder.policyreminder.databinding.ActivityAddPolicyHolderBinding;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPolicyHolderActivity extends AppCompatActivity {

    private ActivityAddPolicyHolderBinding binding;
    private Session session;
    private AddPolicyHolderActivity activity;
    private List<String> policyNamesList = new ArrayList<>();
    private List<String> policyIdsList = new ArrayList<>();
    private String selectedPolicyName = "", selectedPolicyId = "";
    private FirebaseDatabase database;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPolicyHolderBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;
        session = new Session(activity);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("policy");
        getPolicyList();

        binding.companyAddBtn.setOnClickListener(view -> {
            if (isValidate())
                addHolder();
        });

        binding.backBtn.setOnClickListener(view -> finish());
        binding.holderDob.setOnClickListener(view -> selectDate(binding.holderDob));

    }

    private void addHolder() {

        ProgressDialog pd = new ProgressDialog(activity);
        pd.show();

        String holder_id = reference.push().getKey();
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", session.getUserId());
        map.put("holder_name", binding.holderName.getText().toString());
        map.put("holder_age", binding.holderAge.getText().toString());
        map.put("holder_address", binding.holderAddress.getText().toString());
        map.put("holder_mobile", binding.holderNumber.getText().toString());
        map.put("holder_dob", binding.holderDob.getText().toString());
        map.put("policy", selectedPolicyId);
        map.put("policy_name", selectedPolicyName);
        map.put("holder_id", holder_id);

        database.getReference().child("holder").child(session.getUserId())
                .child(holder_id)
                .setValue(map)
                .addOnSuccessListener(unused -> {
                    pd.dismiss();
                    Toast.makeText(activity, "Holder Added", Toast.LENGTH_SHORT).show();
                    finish();
                });

    }

    private boolean isValidate() {
        if (binding.holderAddress.getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(binding.coordinator, "Holder Address is required...!", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (binding.holderName.getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(binding.coordinator, "Holder Name is required...!", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (binding.holderNumber.getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(binding.coordinator, "Holder Mobile Number is required...!", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (binding.holderAge.getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(binding.coordinator, "Holder Age is required...!", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (selectedPolicyId.equalsIgnoreCase("")) {
            Snackbar.make(binding.coordinator, "Select Policy", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (binding.holderDob.getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(binding.coordinator, "Select date Of Birth.!", Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }
    }

    private void selectDate(TextView textView) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                activity,
                (view, year1, monthOfYear, dayOfMonth) -> textView.setText((monthOfYear + 1) + "/" + dayOfMonth + "/" + year1), year, month, day);
        datePickerDialog.show();
    }

    private void getPolicyList() {
        reference.child(session.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                policyNamesList.clear();
                policyIdsList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    PolicyModel policyModel = snapshot1.getValue(PolicyModel.class);
                    policyIdsList.add(policyModel.getPolicy_id());
                    policyNamesList.add(policyModel.getPolicy_name());
                }
                setPolicyAdapter();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void setPolicyAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, policyNamesList);
        binding.policySpi.setAdapter(adapter);
        binding.policySpi.setOnItemSelectedListener((view, position, id, item) -> {
            selectedPolicyId = policyIdsList.get(position);
            selectedPolicyName = policyNamesList.get(position);
        });
    }
}