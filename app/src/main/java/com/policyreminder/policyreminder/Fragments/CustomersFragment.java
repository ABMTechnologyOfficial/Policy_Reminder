package com.policyreminder.policyreminder.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.policyreminder.policyreminder.Activities.AddPolicyHolderActivity;
import com.policyreminder.policyreminder.Models.HolderModel;
import com.policyreminder.policyreminder.R;
import com.policyreminder.policyreminder.Session.Session;
import com.policyreminder.policyreminder.adapter.HolderAdapter;
import com.policyreminder.policyreminder.databinding.FragmentCustomersBinding;

import java.util.ArrayList;
import java.util.List;

public class CustomersFragment extends Fragment {

    private FragmentCustomersBinding binding;
    private DatabaseReference reference;
    private List<HolderModel> holderList = new ArrayList<>();
    private Activity activity;
    private Session session ;

    public CustomersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCustomersBinding.inflate(getLayoutInflater());

        activity = requireActivity();
        session = new Session(activity);

        reference = FirebaseDatabase.getInstance().getReference().child("holder");

        getAllHolders();

        binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(activity, AddPolicyHolderActivity.class));
            }
        });

        return binding.getRoot();
    }

    private void getAllHolders() {

        reference.child(session.getUserId()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                holderList.clear();
                for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                    HolderModel model = snapshot1.getValue(HolderModel.class);
                    holderList.add(model);
                }

                if (holderList.size() != 0) {
                    binding.companyRecycler.setLayoutManager(new LinearLayoutManager(activity));
                    binding.companyRecycler.setAdapter(new HolderAdapter(activity, holderList));
                    binding.companyRecycler.setVisibility(View.VISIBLE);
                    binding.progressBar.setVisibility(View.GONE);
                    binding.noDataLinear.setVisibility(View.GONE);
                } else {
                    binding.progressBar.setVisibility(View.VISIBLE);
                    binding.companyRecycler.setVisibility(View.GONE);
                    binding.noDataLinear.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("TAG", "onCancelled() called with: error = [" + error.getMessage() + "]");
            }
        });


    }
}