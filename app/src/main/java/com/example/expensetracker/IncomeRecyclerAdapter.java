package com.example.expensetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.model.Data;

import java.util.List;

public class IncomeRecyclerAdapter extends RecyclerView.Adapter<IncomeViewHolder>{
    private final List<Data> incomeList;

    public IncomeRecyclerAdapter(List<Data> incomeList) {
        this.incomeList = incomeList;
    }

    @NonNull
    @Override
    public IncomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.income_recycler_data, parent, false);
        return new IncomeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeViewHolder holder, int position) {
        Data data = incomeList.get(position);

        holder.incomeType.setText(data.getType());
        holder.incomeNote.setText(data.getNote());
        holder.incomeAmount.setText(String.valueOf(data.getAmount()));
        holder.incomeDate.setText(data.getDate());
    }

    @Override
    public int getItemCount() {
        return incomeList.size();
    }
}
