package com.policyreminder.policyreminder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.policyreminder.policyreminder.Models.HolderModel;
import com.policyreminder.policyreminder.R;
import com.policyreminder.policyreminder.databinding.PolicyHolderLayoutBinding;

import java.util.List;

public class HolderAdapter extends RecyclerView.Adapter<HolderAdapter.ViewHolder> {

    Context context;
    List<HolderModel> models;

    public HolderAdapter(Context context, List<HolderModel> models) {
        this.context = context;
        this.models = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.policy_holder_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.binding.holderAddress.setText(models.get(position).getHolder_address());
        holder.binding.holderName.setText(models.get(position).getHolder_name());

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        PolicyHolderLayoutBinding binding;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = PolicyHolderLayoutBinding.bind(itemView);
        }
    }
}
