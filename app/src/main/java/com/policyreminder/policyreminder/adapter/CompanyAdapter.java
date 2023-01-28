package com.policyreminder.policyreminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.policyreminder.policyreminder.Models.CompanyModel;
import com.policyreminder.policyreminder.R;
import com.policyreminder.policyreminder.databinding.CompanyLayoutBinding;

import java.util.List;

public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder>{

    Context context ;
    List<CompanyModel> models ;

    public CompanyAdapter(Context context, List<CompanyModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(context).inflate(R.layout.company_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.binding.companyName.setText(models.get(position).getCompany_name());
        holder.binding.companyWebsite.setText(models.get(position).getCompany_payment_link());

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public  class  ViewHolder extends RecyclerView.ViewHolder{
        CompanyLayoutBinding binding ;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = CompanyLayoutBinding.bind(itemView);
        }
    }
}
