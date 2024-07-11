package com.example.expensetracker;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
//import android.app.usage.NetworkStats;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;

import com.example.expensetracker.model.Data;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.Query;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ExpenseFragment extends Fragment {

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference mExpenseDatabase;

    //Recyclerview
    private RecyclerView recyclerView;

    //private FirebaseRecyclerAdapter adapter;
    //Calender view
    private CalendarView calendarView;

    private ExpenseRecyclerAdapter expenseRecyclerAdapter;

    private List<Data> expenseList;
    private TextView expenseSumResult;

    //Update editText..
    private EditText edtAmount;
    private EditText edtType;
    private EditText edtNote;

    //Update and Delete button..
    private Button btnUpdate;
    private Button btnDelete;

    //Data item value..
    private String type;
    private String note;
    private int amount;
    private String post_key;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_expense, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            String uid = mUser.getUid();
            mExpenseDatabase = FirebaseDatabase.getInstance("https://expense-tracker-a0331-default-rtdb.asia-southeast1.firebasedatabase.app").getReference().child("ExpenseData").child(uid);
        }
        mExpenseDatabase.keepSynced(true);
        expenseSumResult = myView.findViewById(R.id.expense_txt_result);

        recyclerView = myView.findViewById(R.id.recycler_id_expense);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                int expenseSum = 0;

                for (DataSnapshot mySnapshot : snapshot.getChildren()) {
                    Data data = mySnapshot.getValue(Data.class);
                    assert data != null;
                    expenseSum += data.getAmount();
                    String strexpenseSum = String.valueOf(expenseSum);
                    expenseSumResult.setText(strexpenseSum + ".00");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        expenseList = new ArrayList<>();
        expenseRecyclerAdapter = new ExpenseRecyclerAdapter(expenseList);
        recyclerView.setAdapter(expenseRecyclerAdapter);

        mExpenseDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Data data = dataSnapshot.getValue(Data.class);

                    expenseList.add(data);
                }
                expenseRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Calender view
        calendarView = myView.findViewById(R.id.calendarView1);
        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            String selectedDate = getFormattedDate(year, month, dayOfMonth);
            fetchExpenseDataForDate(selectedDate);
        });

        return myView;
    }

    private String getFormattedDate(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, dayOfMonth);

        SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }
    private void fetchExpenseDataForDate(String selectedDate) {

        Query query = mExpenseDatabase.orderByChild("date").equalTo(selectedDate);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<Data> expenseList = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Data data = snapshot.getValue(Data.class);
                    expenseList.add(data);
                }
                populateRecyclerView(expenseList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle potential errors or canceled database queries
            }
        });
    }

    private void populateRecyclerView(List<Data> expenseList) {
        ExpenseRecyclerAdapter adapter = new ExpenseRecyclerAdapter(expenseList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}