package com.policyreminder.policyreminder.adapter;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.policyreminder.policyreminder.Models.InsuranceTypeModel;
import com.policyreminder.policyreminder.R;
import com.policyreminder.policyreminder.Session.Session;
import com.policyreminder.policyreminder.databinding.InsuranceTypeLayoutBinding;

import java.util.List;

public class InsuranceTypeAdapter extends RecyclerView.Adapter<InsuranceTypeAdapter.ViewHolder>{

    Context context ;
    List<InsuranceTypeModel> models ;
    private FirebaseDatabase database ;
    private Session session ;

    public InsuranceTypeAdapter(Context context, List<InsuranceTypeModel> models) {
        this.context = context;
        this.models = models;
        session = new Session(context);
        database = FirebaseDatabase.getInstance();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(context).inflate(R.layout.insurance_type_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.binding.insuranceName.setText(models.get(position).getInsurance_name());

       holder.binding.deleteBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              delete(models.get(position).getInsurance_id());
              notifyItemChanged(position);
              notifyItemRangeChanged(0, models.size()-1);
              models.remove(position);
          }
      });

    }

    private  void showDialog(){
        final Dialog dialog = new Dialog(context);

       // dialog.setContentView(R.layout.no_internet_layout);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setDimAmount(0f);

        dialog.show();


    }

    private void delete(String insurance_id) {
        database.getReference().child("insurance").child(session.getUserId())
                .child(insurance_id)
                .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        Toast.makeText(context, "Deleted.!", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    public int getItemCount() {
        return models.size();
    }


    public  class ViewHolder extends RecyclerView.ViewHolder{
        InsuranceTypeLayoutBinding binding ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = InsuranceTypeLayoutBinding.bind(itemView);
        }
    }
}
