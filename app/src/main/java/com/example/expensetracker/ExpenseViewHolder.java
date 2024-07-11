package com.example.expensetracker;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExpenseViewHolder extends RecyclerView.ViewHolder {
    TextView expenseDate;
    TextView expenseType;
    TextView expenseNote;
    TextView expenseAmount;

    public ExpenseViewHolder(@NonNull View itemView) {
        super(itemView);

        expenseType = itemView.findViewById(R.id.type_txt_expense);
        expenseAmount = itemView.findViewById(R.id.amount_txt_expense);
        expenseNote = itemView.findViewById(R.id.note_txt_expense);
        expenseDate = itemView.findViewById(R.id.date_txt_expense);

    }
}
