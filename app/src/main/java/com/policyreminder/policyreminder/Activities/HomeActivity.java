package com.policyreminder.policyreminder.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.policyreminder.policyreminder.Fragments.CustomersFragment;
import com.policyreminder.policyreminder.Fragments.HomeFragment;
import com.policyreminder.policyreminder.Fragments.InsuranceListFragment;
import com.policyreminder.policyreminder.Fragments.PolicysFragment;
import com.policyreminder.policyreminder.Fragments.ProfileFragment;
import com.policyreminder.policyreminder.Models.UsersModel;
import com.policyreminder.policyreminder.R;
import com.policyreminder.policyreminder.Session.Session;
import com.policyreminder.policyreminder.databinding.ActivityHomeBinding;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ActivityHomeBinding binding;
    boolean ishome = true;
    private HomeActivity activity;
    private FirebaseDatabase database ;
    private Session session ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        activity = this;
        session = new Session(activity);
        database = FirebaseDatabase.getInstance();

        setUpNav();
        setUpNavigationView();
        loadFrag(new HomeFragment());

        getProfileStatus();
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

    private  void getProfileStatus(){
        database.getReference().child("users").child(session.getUserId())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        UsersModel usersModel = snapshot.getValue(UsersModel.class);
                        session.setProfile_status(usersModel.getProfile_status());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
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


       binding.navPolicyrLay.setOnClickListener(v -> {
           startActivity(new Intent(activity, AddPolicyActivity.class));
            binding.drawerLayout.close();
        });

    }

    public void loadFrag(Fragment fragment) {
        FragmentTransaction ft_add1 = getSupportFragmentManager().beginTransaction();
        ft_add1.replace(R.id.container, fragment);
        ft_add1.commit();

    }

    private  void loadProfileDialog(){

        final Dialog dialog = new Dialog(activity);

        dialog.setContentView(R.layout.compete_profile_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setDimAmount(0f);

        ImageView cancel_button = dialog.findViewById(R.id.cancel_button);
        TextView complete_now = dialog.findViewById(R.id.complete_now);

        complete_now.setOnClickListener(view -> startActivity(new Intent(activity,UserDetailsActivity.class)));

        cancel_button.setOnClickListener(view -> dialog.dismiss());


        dialog.show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpNav();
        setUpNavigationView();
        loadFrag(new HomeFragment());

        getProfileStatus();
        Log.e("TAG", "onResume() called User Status "+session.getProfile_status());
        if(session.getProfile_status().equalsIgnoreCase("0")){
            loadProfileDialog();
        }
    }
}