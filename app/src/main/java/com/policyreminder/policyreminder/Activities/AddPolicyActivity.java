package com.policyreminder.policyreminder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.policyreminder.policyreminder.MainActivity;
import com.policyreminder.policyreminder.Models.CompanyModel;
import com.policyreminder.policyreminder.Models.InsuranceTypeModel;
import com.policyreminder.policyreminder.Models.ProgressDialog;
import com.policyreminder.policyreminder.R;
import com.policyreminder.policyreminder.Session.Session;
import com.policyreminder.policyreminder.databinding.ActivityAddPolicyBinding;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddPolicyActivity extends AppCompatActivity {

    ActivityAddPolicyBinding binding;
    private Session session;
    private AddPolicyActivity activity;
    private FirebaseDatabase database;
    private List<String> models = new ArrayList<>();
    private List<String> companyNames = new ArrayList<>();
    private List<String> paymentCycleList = new ArrayList<>();
    private String selectedInsuranceType = "";
    private String selectedCompanyName = "";
    private String selectedPaymentCycle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddPolicyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;
        session = new Session(activity);
        database = FirebaseDatabase.getInstance();

        getInsurance();
        getCompanyList();
        setPaymentCycleAdapter();

        binding.policyStartdate.setOnClickListener(view -> selectDate(binding.policyStartdate));

       binding.policyEnddate.setOnClickListener(view -> selectDate(binding.policyEnddate));

        binding.backBtn.setOnClickListener(view -> finish());

        binding.companyAddBtn.setOnClickListener(view -> {
            if (isValidate())
              addPolicy();
        });

    }

    private void addPolicy() {

        String string = binding.policyStartdate.getText().toString();
        Log.e("TAG", "addPolicy() called Start date "+binding.policyStartdate.getText().toString());
        DateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
        Date dt = null;
        try {
            dt = sdf .parse(string);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar c = Calendar.getInstance();
        c.setTime(dt);

        Log.e("TAG", "addPolicy() called Selected Cycle "+selectedPaymentCycle);

        if(selectedPaymentCycle.equalsIgnoreCase("Monthly")){
            c.add(Calendar.MONTH, 1);
        }else if(selectedPaymentCycle.equalsIgnoreCase("Quaterly")){
            c.add(Calendar.MONTH, 3);
        } else if(selectedPaymentCycle.equalsIgnoreCase("Half Yearly")){
            c.add(Calendar.MONTH, 6);
        }else if(selectedPaymentCycle.equalsIgnoreCase("Yearly")){
            c.add(Calendar.MONTH, 12);
        }

        String firstDate = sdf.format(c.getTime());
        System.out.println(firstDate);
        Log.e("TAG", "onCreate() called with: firstDate = [" + firstDate + "]");


        ProgressDialog progressDialog = new ProgressDialog(activity);
        progressDialog.show();

        String policy_id = database.getReference().push().getKey();

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", session.getUserId());
        map.put("insurance_type", selectedInsuranceType);
        map.put("company", selectedCompanyName);
        map.put("policy_name", binding.policyName.getText().toString());
        map.put("payment_cycle", selectedPaymentCycle);
        map.put("sum_insured", binding.policySunInsured.getText().toString());
        map.put("premium", binding.policyPremium.getText().toString());
        map.put("start_date", binding.policyStartdate.getText().toString());
        map.put("end_date", binding.policyEnddate.getText().toString());
        map.put("duration", binding.policyDuration.getText().toString());
        map.put("policy_status", "0");
        map.put("policy_id", policy_id);
        map.put("payment_status", "0");
        map.put("next_payment_date", firstDate);
        map.put("plan_ncb_number", binding.policyNcbVehicle.getText().toString());

        database.getReference().child("policy").child(session.getUserId())
                .child(policy_id)
                .setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(activity, "Policy Added..", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }).addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(activity, "Failed....!", Toast.LENGTH_SHORT).show();
                });


    }

    private boolean isValidate() {
        if (binding.policyStartdate.getText().toString().equalsIgnoreCase("Start Date")) {
            Snackbar.make(binding.coordinator, "Select Start Date..!", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (selectedCompanyName.equalsIgnoreCase("")) {
            Snackbar.make(binding.coordinator, "Select Company..!", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (selectedInsuranceType.equalsIgnoreCase("")) {
            Snackbar.make(binding.coordinator, "Select Insurance Type..!", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (selectedInsuranceType.equalsIgnoreCase("")) {
            Snackbar.make(binding.coordinator, "Select Payment Cycle..!", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (binding.policyDuration.getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(binding.coordinator, "Policy Duration Empty..!", Snackbar.LENGTH_LONG).show();
            return false;
        }else if (binding.policyName.getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(binding.coordinator, "Policy Name  Empty..!", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (binding.policySunInsured.getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(binding.coordinator, "Policy Sum Insured Empty..!", Snackbar.LENGTH_LONG).show();
            return false;
        } else if (binding.policyNcbVehicle.getText().toString().equalsIgnoreCase("")) {
            Snackbar.make(binding.coordinator, "Policy NCB Vehicle Number Empty..!", Snackbar.LENGTH_LONG).show();
            return false;
        } else {
            return true;
        }

    }

    private void getCompanyList() {
        database.getReference().child("company")
                .child(session.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        companyNames.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            CompanyModel companyModel = snapshot1.getValue(CompanyModel.class);
                            companyNames.add(companyModel.getCompany_name());
                        }

                        setCompanyAdapter();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void setCompanyAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, companyNames);
        binding.companyNameSpi.setAdapter(adapter);
        binding.companyNameSpi.setOnItemSelectedListener((view, position, id, item) -> {
            selectedCompanyName = companyNames.get(position);
        });
    }

    private void getInsurance() {
        database.getReference().child("insurance").child(session.getUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            InsuranceTypeModel m = snapshot1.getValue(InsuranceTypeModel.class);
                            models.add(m.getInsurance_name());
                        }
                        setAdapter();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }

    private void setAdapter() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, models);
        binding.insuranceTypeSpi.setAdapter(adapter);
        binding.insuranceTypeSpi.setOnItemSelectedListener((view, position, id, item) -> {
            selectedInsuranceType = models.get(position);
        });

    }

    private void setPaymentCycleAdapter() {

        paymentCycleList.clear();
        paymentCycleList.add("One Time");
        paymentCycleList.add("Monthly");
        paymentCycleList.add("Quaterly");
        paymentCycleList.add("Half Yearly");
        paymentCycleList.add("Yearly");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, android.R.layout.simple_spinner_item, paymentCycleList);
        binding.paymentCycleSpi.setAdapter(adapter);
        binding.paymentCycleSpi.setOnItemSelectedListener((view, position, id, item) -> {
            selectedPaymentCycle = paymentCycleList.get(position);
        });

    }

    private void selectDate(TextView textView) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                activity,
                (view, year1, monthOfYear, dayOfMonth) -> textView.setText((monthOfYear + 1) + "/" +dayOfMonth + "/" + year1), year, month, day);
        datePickerDialog.show();
    }
}