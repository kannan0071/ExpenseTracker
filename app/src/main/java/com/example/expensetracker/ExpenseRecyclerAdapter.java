package com.example.expensetracker;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expensetracker.model.Data;

import java.util.List;

public class ExpenseRecyclerAdapter extends RecyclerView.Adapter<ExpenseViewHolder> {

    private final List<Data> expenseList;

    public ExpenseRecyclerAdapter(List<Data>expenseList){
        this.expenseList = expenseList;
    }

    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_recycler_data, parent, false); // Replace with your item layout
        return new ExpenseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Data data = expenseList.get(position);

        holder.expenseType.setText(data.getType());
        holder.expenseNote.setText(data.getNote());
        holder.expenseAmount.setText(String.valueOf(data.getAmount()));
        holder.expenseDate.setText(data.getDate());

    }
    @Override
    public int getItemCount() {
        return expenseList.size();
    }
}
