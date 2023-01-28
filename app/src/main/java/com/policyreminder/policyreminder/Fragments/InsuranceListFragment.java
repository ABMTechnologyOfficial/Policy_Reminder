package com.policyreminder.policyreminder.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.policyreminder.policyreminder.Activities.AddInsuranceTypeActivity;
import com.policyreminder.policyreminder.Models.CompanyModel;
import com.policyreminder.policyreminder.Models.InsuranceTypeModel;
import com.policyreminder.policyreminder.R;
import com.policyreminder.policyreminder.Session.Session;
import com.policyreminder.policyreminder.adapter.CompanyAdapter;
import com.policyreminder.policyreminder.adapter.InsuranceTypeAdapter;
import com.policyreminder.policyreminder.databinding.FragmentInsuranceListBinding;

import java.util.ArrayList;
import java.util.List;

public class InsuranceListFragment extends Fragment {

    private Session session;
    private FirebaseDatabase database;
    private FragmentInsuranceListBinding binding;
    private Activity activity ;
    private List<InsuranceTypeModel> models = new ArrayList<>();

    public InsuranceListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentInsuranceListBinding.inflate(getLayoutInflater());

        activity = requireActivity();
        session = new Session(activity);
        database = FirebaseDatabase.getInstance();

        binding.addBtn.setOnClickListener(v -> startActivity(new Intent(activity, AddInsuranceTypeActivity.class)));


        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        getInsuranceTypeList();
    }

    private  void getInsuranceTypeList(){

        database.getReference().child("insurance")
                .child(session.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        models.clear();
                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                            InsuranceTypeModel c = snapshot1.getValue(InsuranceTypeModel.class);
                            models.add(c);
                        }

                        if(models.size() != 0){
                            binding.companyRecycler.setLayoutManager(new LinearLayoutManager(activity));
                            binding.companyRecycler.setAdapter(new InsuranceTypeAdapter(activity, models));
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