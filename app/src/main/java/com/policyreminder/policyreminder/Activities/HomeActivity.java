package com.policyreminder.policyreminder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.policyreminder.policyreminder.Fragments.CustomersFragment;
import com.policyreminder.policyreminder.Fragments.HomeFragment;
import com.policyreminder.policyreminder.Fragments.InsuranceListFragment;
import com.policyreminder.policyreminder.Fragments.PolicysFragment;
import com.policyreminder.policyreminder.Fragments.ProfileFragment;
import com.policyreminder.policyreminder.R;
import com.policyreminder.policyreminder.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    boolean ishome = true;
    private HomeActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        activity = this;

        setUpNav();
        setUpNavigationView();
        loadFrag(new HomeFragment());


        binding.icMenu.setOnClickListener(view -> binding.drawerLayout.open());


    }

    private void setUpNav() {
        binding.bottomNavView.setOnNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home_main) {
                if (!ishome) {
                    loadFrag(new HomeFragment());
                    binding.textHomeTitle.setText(R.string.app_name);
                    ishome = true;
                }
            } else if (item.getItemId() == R.id.customer_main) {
                loadFrag(new CustomersFragment());
                binding.textHomeTitle.setText(R.string.app_name);
                ishome = false;
            } else if (item.getItemId() == R.id.policy_main) {
                loadFrag(new PolicysFragment());
                binding.textHomeTitle.setText(R.string.app_name);
                ishome = false;
            } else if (item.getItemId() == R.id.profile_main) {
                loadFrag(new ProfileFragment());
                ishome = false;
                binding.textHomeTitle.setText(R.string.app_name);
            }

            return true;
        });

    }

    private void setUpNavigationView() {
        binding.navCompanyLay.setOnClickListener(v -> {
            startActivity(new Intent(activity, CompanyListActivity.class));
            binding.drawerLayout.close();
        });


        binding.navInsuranceLay.setOnClickListener(v -> {
            loadFrag(new InsuranceListFragment());
            binding.drawerLayout.close();
        });

    }

    public void loadFrag(Fragment fragment) {
        FragmentTransaction ft_add1 = getSupportFragmentManager().beginTransaction();
        ft_add1.replace(R.id.container, fragment);
        ft_add1.commit();

    }

}