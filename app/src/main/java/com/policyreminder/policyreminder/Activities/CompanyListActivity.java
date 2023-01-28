package com.policyreminder.policyreminder.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.policyreminder.policyreminder.Models.CompanyModel;
import com.policyreminder.policyreminder.Session.Session;
import com.policyreminder.policyreminder.adapter.CompanyAdapter;
import com.policyreminder.policyreminder.databinding.ActivityCompanyListBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class CompanyListActivity extends AppCompatActivity {

    private ActivityCompanyListBinding binding;
    private CompanyListActivity activity;
    private Session session;
    private FirebaseDatabase database;
    private List<CompanyModel> models = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCompanyListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;
        session = new Session(activity);
        database = FirebaseDatabase.getInstance();

        getCompanyList();

        binding.addBtn.setOnClickListener(v -> startActivity(new Intent(activity, AddCompanyActivity.class)));

    }


    private void getCompanyList() {

        database.getReference().child("company").child(session.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        models.clear();

                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            CompanyModel c = snapshot1.getValue(CompanyModel.class);
                            models.add(c);
                        }

                        if(models.size() != 0){
                            binding.companyRecycler.setLayoutManager(new LinearLayoutManager(activity));
                            binding.companyRecycler.setAdapter(new CompanyAdapter(activity, models));
                            binding.progressBar.setVisibility(View.GONE);
                            binding.noDataLinear.setVisibility(View.GONE);
                            binding.companyRecycler.setVisibility(View.VISIBLE);
                        }else {
                            binding.progressBar.setVisibility(View.GONE);
                            binding.companyRecycler.setVisibility(View.GONE);
                            binding.noDataLinear.setVisibility(View.VISIBLE);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }


}