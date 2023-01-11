package com.policyreminder.policyreminder.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.policyreminder.policyreminder.Fragments.CustomersFragment;
import com.policyreminder.policyreminder.Fragments.HomeFragment;
import com.policyreminder.policyreminder.Fragments.PolicysFragment;
import com.policyreminder.policyreminder.Fragments.ProfileFragment;
import com.policyreminder.policyreminder.R;
import com.policyreminder.policyreminder.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    boolean ishome = true;
    String banner_path;
    String post_path;
    /// Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        loadFrag(new HomeFragment());
        binding.bottomNavView.setOnNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.home_main) {
                if (!ishome) {
                    loadFrag(new HomeFragment());
                    ishome = true;
                }
            } else if (item.getItemId() == R.id.customer_main) {
                loadFrag(new CustomersFragment());
                ishome = false;
            } else if (item.getItemId() == R.id.policy_main) {
                loadFrag(new PolicysFragment());
                ishome = false;
            } else if (item.getItemId() == R.id.profile_main) {
                loadFrag(new ProfileFragment());
                ishome = false;
            }

            return true;
        });


    }


    public void loadFrag(Fragment fragment) {
        FragmentTransaction ft_add1 = getSupportFragmentManager().beginTransaction();
        ft_add1.replace(R.id.container, fragment);
        ft_add1.commit();

    }

}