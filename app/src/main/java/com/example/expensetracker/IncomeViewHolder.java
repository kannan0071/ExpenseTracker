package com.example.expensetracker;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class IncomeViewHolder extends RecyclerView.ViewHolder {
     TextView incomeDate;
     TextView incomeType;
     TextView incomeNote;
     TextView incomeAmount;
    public IncomeViewHolder(@NonNull View itemView) {
        super(itemView);
        incomeType = itemView.findViewById(R.id.type_txt_income);
        incomeAmount = itemView.findViewById(R.id.amount_txt_income);
        incomeNote = itemView.findViewById(R.id.note_txt_income);
        incomeDate = itemView.findViewById(R.id.date_txt_income);

    }
}
